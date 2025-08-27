package dev.dong4j.zeka.kernel.spi.compiler.support;


import dev.dong4j.zeka.kernel.spi.utils.SpiClassUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.8.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.8.0
 */
@SuppressWarnings("all")
public class JdkCompiler extends AbstractCompiler {

    /** Compiler */
    private final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    /** Diagnostic collector */
    private final DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();

    /** Class loader */
    private final ClassLoaderImpl classLoader;

    /** Java file manager */
    private final JavaFileManagerImpl javaFileManager;

    /** Options */
    private volatile List<String> options;

    /**
     * Jdk compiler
     *
     * @since 1.8.0
     */
    public JdkCompiler() {
        options = new ArrayList<String>();
        options.add("-source");
        options.add("1.6");
        options.add("-target");
        options.add("1.6");
        StandardJavaFileManager manager = compiler.getStandardFileManager(diagnosticCollector, null, null);
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader instanceof URLClassLoader
            && (!"sun.misc.Launcher$AppClassLoader".equals(loader.getClass().getName()))) {
            try {
                URLClassLoader urlClassLoader = (URLClassLoader) loader;
                List<File> files = new ArrayList<File>();
                for (URL url : urlClassLoader.getURLs()) {
                    files.add(new File(url.getFile()));
                }
                manager.setLocation(StandardLocation.CLASS_PATH, files);
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        classLoader = new ClassLoaderImpl(loader);
        javaFileManager = new JavaFileManagerImpl(manager, classLoader);
    }

    /**
     * Do compile
     *
     * @param name       name
     * @param sourceCode source code
     * @return the class
     * @throws Throwable throwable
     * @since 1.8.0
     */
    @Override
    public Class<?> doCompile(String name, String sourceCode) throws Throwable {
        int i = name.lastIndexOf('.');
        String packageName = i < 0 ? "" : name.substring(0, i);
        String className = i < 0 ? name : name.substring(i + 1);
        JavaFileObjectImpl javaFileObject = new JavaFileObjectImpl(className, sourceCode);
        javaFileManager.putFileForInput(StandardLocation.SOURCE_PATH, packageName,
            className + CompilerClassUtils.JAVA_EXTENSION, javaFileObject);
        Boolean result = compiler.getTask(null, javaFileManager, diagnosticCollector, options,
            null, Arrays.asList(javaFileObject)).call();
        if (result == null || !result) {
            throw new IllegalStateException("Compilation failed. class: " + name + ", diagnostics: " + diagnosticCollector);
        }
        return classLoader.loadClass(name);
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.8.0
     * @email "mailto:dong4j@gmaidl.com"
     * @date 2021.02.26 17:47
     * @since 1.8.0
     */
    private static final class JavaFileObjectImpl extends SimpleJavaFileObject {

        /** Source */
        private final CharSequence source;
        /** Bytecode */
        private ByteArrayOutputStream bytecode;

        /**
         * Java file object
         *
         * @param baseName base name
         * @param source   source
         * @since 1.8.0
         */
        public JavaFileObjectImpl(final String baseName, final CharSequence source) {
            super(CompilerClassUtils.toURI(baseName + CompilerClassUtils.JAVA_EXTENSION), Kind.SOURCE);
            this.source = source;
        }

        /**
         * Java file object
         *
         * @param name name
         * @param kind kind
         * @since 1.8.0
         */
        JavaFileObjectImpl(final String name, final Kind kind) {
            super(CompilerClassUtils.toURI(name), kind);
            source = null;
        }

        /**
         * Java file object
         *
         * @param uri  uri
         * @param kind kind
         * @since 1.8.0
         */
        public JavaFileObjectImpl(URI uri, Kind kind) {
            super(uri, kind);
            source = null;
        }

        /**
         * Gets char content *
         *
         * @param ignoreEncodingErrors ignore encoding errors
         * @return the char content
         * @throws UnsupportedOperationException unsupported operation exception
         * @since 1.8.0
         */
        @Override
        public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws UnsupportedOperationException {
            if (source == null) {
                throw new UnsupportedOperationException("source == null");
            }
            return source;
        }

        /**
         * Open input stream
         *
         * @return the input stream
         * @since 1.8.0
         */
        @Override
        public InputStream openInputStream() {
            return new ByteArrayInputStream(getByteCode());
        }

        /**
         * Open output stream
         *
         * @return the output stream
         * @since 1.8.0
         */
        @Override
        public OutputStream openOutputStream() {
            return bytecode = new ByteArrayOutputStream();
        }

        /**
         * Get byte code
         *
         * @return the byte [ ]
         * @since 1.8.0
         */
        public byte[] getByteCode() {
            return bytecode.toByteArray();
        }
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.8.0
     * @email "mailto:dong4j@gmaidl.com"
     * @date 2021.02.26 17:47
     * @since 1.8.0
     */
    private static final class JavaFileManagerImpl extends ForwardingJavaFileManager<JavaFileManager> {

        /** Class loader */
        private final ClassLoaderImpl classLoader;

        /** File objects */
        private final Map<URI, JavaFileObject> fileObjects = new HashMap<URI, JavaFileObject>();

        /**
         * Java file manager
         *
         * @param fileManager file manager
         * @param classLoader class loader
         * @since 1.8.0
         */
        public JavaFileManagerImpl(JavaFileManager fileManager, ClassLoaderImpl classLoader) {
            super(fileManager);
            this.classLoader = classLoader;
        }

        /**
         * Gets file for input *
         *
         * @param location     location
         * @param packageName  package name
         * @param relativeName relative name
         * @return the file for input
         * @throws IOException io exception
         * @since 1.8.0
         */
        @Override
        public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
            FileObject o = fileObjects.get(uri(location, packageName, relativeName));
            if (o != null) {
                return o;
            }
            return super.getFileForInput(location, packageName, relativeName);
        }

        /**
         * Put file for input
         *
         * @param location     location
         * @param packageName  package name
         * @param relativeName relative name
         * @param file         file
         * @since 1.8.0
         */
        public void putFileForInput(StandardLocation location, String packageName, String relativeName, JavaFileObject file) {
            fileObjects.put(uri(location, packageName, relativeName), file);
        }

        /**
         * Uri
         *
         * @param location     location
         * @param packageName  package name
         * @param relativeName relative name
         * @return the uri
         * @since 1.8.0
         */
        private URI uri(Location location, String packageName, String relativeName) {
            return CompilerClassUtils.toURI(location.getName() + '/' + packageName + '/' + relativeName);
        }

        /**
         * Gets java file for output *
         *
         * @param location      location
         * @param qualifiedName qualified name
         * @param kind          kind
         * @param outputFile    output file
         * @return the java file for output
         * @throws IOException io exception
         * @since 1.8.0
         */
        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String qualifiedName, Kind kind, FileObject outputFile)
            throws IOException {
            JavaFileObject file = new JavaFileObjectImpl(qualifiedName, kind);
            classLoader.add(qualifiedName, file);
            return file;
        }

        /**
         * Gets class loader *
         *
         * @param location location
         * @return the class loader
         * @since 1.8.0
         */
        @Override
        public ClassLoader getClassLoader(Location location) {
            return classLoader;
        }

        /**
         * Infer binary name
         *
         * @param loc  loc
         * @param file file
         * @return the string
         * @since 1.8.0
         */
        @Override
        public String inferBinaryName(Location loc, JavaFileObject file) {
            if (file instanceof JavaFileObjectImpl) {
                return file.getName();
            }
            return super.inferBinaryName(loc, file);
        }

        /**
         * List
         *
         * @param location    location
         * @param packageName package name
         * @param kinds       kinds
         * @param recurse     recurse
         * @return the iterable
         * @throws IOException io exception
         * @since 1.8.0
         */
        @Override
        public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse)
            throws IOException {
            Iterable<JavaFileObject> result = super.list(location, packageName, kinds, recurse);

            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            List<URL> urlList = new ArrayList<URL>();
            Enumeration<URL> e = contextClassLoader.getResources("com");
            while (e.hasMoreElements()) {
                urlList.add(e.nextElement());
            }

            ArrayList<JavaFileObject> files = new ArrayList<JavaFileObject>();

            if (location == StandardLocation.CLASS_PATH && kinds.contains(Kind.CLASS)) {
                for (JavaFileObject file : fileObjects.values()) {
                    if (file.getKind() == Kind.CLASS && file.getName().startsWith(packageName)) {
                        files.add(file);
                    }
                }

                files.addAll(classLoader.files());
            } else if (location == StandardLocation.SOURCE_PATH && kinds.contains(Kind.SOURCE)) {
                for (JavaFileObject file : fileObjects.values()) {
                    if (file.getKind() == Kind.SOURCE && file.getName().startsWith(packageName)) {
                        files.add(file);
                    }
                }
            }

            for (JavaFileObject file : result) {
                files.add(file);
            }

            return files;
        }
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.8.0
     * @email "mailto:dong4j@gmaidl.com"
     * @date 2021.02.26 17:47
     * @since 1.8.0
     */
    private final class ClassLoaderImpl extends ClassLoader {

        /** Classes */
        private final Map<String, JavaFileObject> classes = new HashMap<String, JavaFileObject>();

        /**
         * Class loader
         *
         * @param parentClassLoader parent class loader
         * @since 1.8.0
         */
        ClassLoaderImpl(final ClassLoader parentClassLoader) {
            super(parentClassLoader);
        }

        /**
         * Files
         *
         * @return the collection
         * @since 1.8.0
         */
        Collection<JavaFileObject> files() {
            return Collections.unmodifiableCollection(classes.values());
        }

        /**
         * Find class
         *
         * @param qualifiedClassName qualified class name
         * @return the class
         * @throws ClassNotFoundException class not found exception
         * @since 1.8.0
         */
        @Override
        protected Class<?> findClass(final String qualifiedClassName) throws ClassNotFoundException {
            JavaFileObject file = classes.get(qualifiedClassName);
            if (file != null) {
                byte[] bytes = ((JavaFileObjectImpl) file).getByteCode();
                return defineClass(qualifiedClassName, bytes, 0, bytes.length);
            }
            try {
                return SpiClassUtils.forNameWithCallerClassLoader(qualifiedClassName, getClass());
            } catch (ClassNotFoundException nf) {
                return super.findClass(qualifiedClassName);
            }
        }

        /**
         * Add
         *
         * @param qualifiedClassName qualified class name
         * @param javaFile           java file
         * @since 1.8.0
         */
        void add(final String qualifiedClassName, final JavaFileObject javaFile) {
            classes.put(qualifiedClassName, javaFile);
        }

        /**
         * Load class
         *
         * @param name    name
         * @param resolve resolve
         * @return the class
         * @throws ClassNotFoundException class not found exception
         * @since 1.8.0
         */
        @Override
        protected synchronized Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
            return super.loadClass(name, resolve);
        }

        /**
         * Gets resource as stream *
         *
         * @param name name
         * @return the resource as stream
         * @since 1.8.0
         */
        @Override
        public InputStream getResourceAsStream(final String name) {
            if (name.endsWith(CompilerClassUtils.CLASS_EXTENSION)) {
                String qualifiedClassName = name.substring(0, name.length() - CompilerClassUtils.CLASS_EXTENSION.length()).replace('/',
                    '.');
                JavaFileObjectImpl file = (JavaFileObjectImpl) classes.get(qualifiedClassName);
                if (file != null) {
                    return new ByteArrayInputStream(file.getByteCode());
                }
            }
            return super.getResourceAsStream(name);
        }
    }


}
