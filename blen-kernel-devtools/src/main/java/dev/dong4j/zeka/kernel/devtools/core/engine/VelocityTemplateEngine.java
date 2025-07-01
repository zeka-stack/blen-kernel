package dev.dong4j.zeka.kernel.devtools.core.engine;

import dev.dong4j.zeka.kernel.common.util.StringPool;
import dev.dong4j.zeka.kernel.devtools.core.config.ConstVal;
import dev.dong4j.zeka.kernel.devtools.core.config.builder.ConfigBuilder;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Properties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

/**
 * Velocity 模板引擎实现文件输出
 *
 * @author hubin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2018 -01-10
 */
public class VelocityTemplateEngine extends AbstractTemplateEngine {

    /** DOT_VM */
    private static final String DOT_VM = ".vm";
    /** Velocity engine */
    protected VelocityEngine velocityEngine;

    /**
     * Init
     *
     * @param configBuilder config builder
     * @return the velocity template engine
     * @since 2024.2.0
     */
    @Override
    public VelocityTemplateEngine init(ConfigBuilder configBuilder) {
        super.init(configBuilder);
        if (null == velocityEngine) {
            Properties p = new Properties();
            p.setProperty(ConstVal.VM_LOAD_PATH_KEY, ConstVal.VM_LOAD_PATH_VALUE);
            p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, StringPool.EMPTY);
            p.setProperty(Velocity.ENCODING_DEFAULT, ConstVal.UTF8);
            p.setProperty(Velocity.INPUT_ENCODING, ConstVal.UTF8);
            p.setProperty("resource.loader.file.unicode", StringPool.TRUE);
            velocityEngine = new VelocityEngine(p);
        }
        return this;
    }

    /**
     * Writer
     *
     * @param objectMap    object map
     * @param templatePath template path
     * @param outputFile   output file
     * @throws Exception exception
     * @since 2024.2.0
     */
    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        Template template = velocityEngine.getTemplate(templatePath, ConstVal.UTF8);
        try (FileOutputStream fos = new FileOutputStream(outputFile);
             OutputStreamWriter ow = new OutputStreamWriter(fos, ConstVal.UTF8);
             BufferedWriter writer = new BufferedWriter(ow)) {
            template.merge(new VelocityContext(objectMap), writer);
        }
        logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }


    /**
     * Template file path
     *
     * @param filePath file path
     * @return the string
     * @since 2024.2.0
     */
    @Override
    public String templateFilePath(String filePath) {
        if (null == filePath || filePath.contains(DOT_VM)) {
            return filePath;
        }
        return filePath + DOT_VM;
    }
}
