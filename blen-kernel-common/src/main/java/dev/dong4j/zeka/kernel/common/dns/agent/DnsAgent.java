package dev.dong4j.zeka.kernel.common.dns.agent;

import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.dns.DnsCache;
import dev.dong4j.zeka.kernel.common.dns.DnsCacheEntry;
import dev.dong4j.zeka.kernel.common.dns.DnsCacheManipulator;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.09 19:52
 * @since 1.5.0
 */
@SuppressWarnings("all")
public class DnsAgent {
    /** File */
    static final String FILE = "file";
    /** Dcm agent success mark line */
    static final String DCM_AGENT_SUCCESS_MARK_LINE = "!!SUCCESS!!";

    /**
     * Agentmain
     *
     * @param agentArgument agent argument
     * @throws Exception exception
     * @since 1.5.0
     */
    public static void agentmain(String agentArgument) throws Exception {
        System.out.printf("%s: attached with agent argument: %s.\n", DnsAgent.class.getName(), agentArgument);

        agentArgument = agentArgument.trim();
        if (agentArgument.isEmpty()) {
            System.out.println(DnsAgent.class.getName() + ": agent argument is blank, do nothing!");
            return;
        }

        initAction2Method();

        FileOutputStream fileOutputStream = null;
        try {
            Map<String, List<String>> action2Arguments = parseAgentArgument(agentArgument);

            PrintWriter filePrinter = null;

            // Extract file argument, set file printer if needed
            if (action2Arguments.containsKey(FILE)) {
                fileOutputStream = new FileOutputStream(action2Arguments.get(FILE).get(0), false);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");

                filePrinter = new PrintWriter(outputStreamWriter, true);
                action2Arguments.remove(FILE);
            }

            if (action2Arguments.isEmpty()) {
                System.out.println(DnsAgent.class.getName() + ": No action in agent argument, do nothing!");
                if (filePrinter != null) {
                    filePrinter.printf("No action in agent argument, do nothing! agent argument: %s.\n", agentArgument);
                }
                return;
            }

            boolean allSuccess = true;

            for (Map.Entry<String, List<String>> entry : action2Arguments.entrySet()) {
                String action = entry.getKey();
                List<String> arguments = entry.getValue();
                String argumentString = join(arguments);

                if (!action2Method.containsKey(action)) {
                    System.out.printf("%s: Unknown action %s, ignore! action: %<s %s!\n", DnsAgent.class.getName(), action, argumentString);
                    if (filePrinter != null) {
                        filePrinter.printf("Unknown action %s, ignore! action: %<s %s !\n", action, argumentString);
                    }
                    continue;
                }

                try {
                    Object result = doAction(action, arguments.toArray(new String[0]));
                    printResult(action, result, filePrinter);
                } catch (Exception e) {
                    allSuccess = false;
                    String exString = throwable2StackString(e);

                    System.out.printf("%s: Error to do action %s %s, cause: %s\n", DnsAgent.class.getName(), action, argumentString,
                        exString);
                    if (filePrinter != null) {
                        filePrinter.printf("Error to do action %s %s, cause: %s\n", action, argumentString, exString);
                    }
                }
            }

            if (allSuccess && filePrinter != null) {
                filePrinter.println(DCM_AGENT_SUCCESS_MARK_LINE);
            }
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Throwable e) {
                    // do nothing!
                }
            }
        }
    }

    /**
     * Parse agent argument
     *
     * @param argument argument
     * @return the map
     * @since 1.5.0
     */
    static @NotNull Map<String, List<String>> parseAgentArgument(@NotNull String argument) {
        String[] split = argument.split("\\s+");

        int idx = 0;
        Map<String, List<String>> action2Arguments = new HashMap<>(16);
        while (idx < split.length) {
            String action = split[idx++];
            if (!action2Method.containsKey(action)) {
                continue; // TODO error message
            }

            List<String> arguments = new ArrayList<String>();
            while (idx < split.length) {
                if (action2Method.containsKey(split[idx])) {
                    break;
                }
                arguments.add(split[idx++]);
            }
            action2Arguments.put(action, arguments);
        }

        return action2Arguments;
    }

    /**
     * Join
     *
     * @param list list
     * @return the string
     * @since 1.5.0
     */
    static @NotNull String join(List<String> list) {
        return join(list, " ");
    }

    /**
     * Join
     *
     * @param list      list
     * @param separator separator
     * @return the string
     * @since 1.5.0
     */
    static @NotNull String join(@NotNull List<String> list, String separator) {
        StringBuilder ret = new StringBuilder();
        for (String argument : list) {
            if (ret.length() > 0) {
                ret.append(separator);
            }
            ret.append(argument);
        }
        return ret.toString();
    }

    /**
     * Do action
     *
     * @param action    action
     * @param arguments arguments
     * @return the object
     * @throws Exception exception
     * @since 1.5.0
     */
    static Object doAction(String action, String[] arguments) throws Exception {
        Method method = action2Method.get(action);

        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] methodArgs = convertStringArray2Arguments(action, arguments, parameterTypes);
        return method.invoke(null, methodArgs);
    }

    /**
     * Convert string array 2 arguments
     *
     * @param action         action
     * @param arguments      arguments
     * @param parameterTypes parameter types
     * @return the object [ ]
     * @since 1.5.0
     */
    static Object @NotNull [] convertStringArray2Arguments(String action,
                                                           String @NotNull [] arguments,
                                                           Class<?> @NotNull [] parameterTypes) {
        if (arguments.length < parameterTypes.length) {
            String message = String.format("action %s need more argument! arguments: %s", action, Arrays.toString(arguments));
            throw new IllegalStateException(message);
        }
        if (parameterTypes.length == 0) {
            return new Object[0];
        }

        Object[] methodArgs = new Object[parameterTypes.length];

        int lastArgumentIdx = parameterTypes.length - 1;
        if (parameterTypes[(lastArgumentIdx)] == String[].class) {
            // set all tail method argument of type String[]
            String[] varArgs = new String[arguments.length - lastArgumentIdx];
            System.arraycopy(arguments, lastArgumentIdx, varArgs, 0, varArgs.length);
            methodArgs[(lastArgumentIdx)] = varArgs;
        } else if (arguments.length > parameterTypes.length) {
            String message = String.format("Too more arguments for Action %s! arguments: %s", action, Arrays.toString(arguments));
            throw new IllegalStateException(message);
        }

        for (int i = 0; i < parameterTypes.length; i++) {
            if (methodArgs[i] != null) { // already set
                continue;
            }

            Class<?> parameterType = parameterTypes[i];
            String argument = arguments[i];
            if (parameterType.equals(String.class)) {
                methodArgs[i] = argument;
            } else if (parameterType.equals(int.class)) {
                methodArgs[i] = Integer.parseInt(argument);
            } else {
                String message = String.format("Unexpected method type %s! Bug!!", parameterType.getName());
                throw new IllegalStateException(message);
            }
        }

        return methodArgs;
    }

    /**
     * Print result
     *
     * @param action action
     * @param result result
     * @param writer writer
     * @since 1.5.0
     */
    static void printResult(String action, Object result, PrintWriter writer) {
        if (writer == null) {
            return;
        }

        Method method = action2Method.get(action);
        if (method.getReturnType() == void.class) {
            return;
        }
        if (result == null) {
            writer.println((Object) null);
        } else if (result instanceof DnsCacheEntry) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            DnsCacheEntry entry = (DnsCacheEntry) result;
            writer.printf("%s %s %s\n", entry.getHost(), join(Arrays.asList(entry.getIps()), ","),
                dateFormat.format(entry.getExpiration()));
        } else if (result instanceof DnsCache) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            DnsCache dnsCache = (DnsCache) result;

            writer.println("Dns cache:");
            for (DnsCacheEntry entry : dnsCache.getCache()) {
                writer.printf("    %s %s %s\n", entry.getHost(), join(Arrays.asList(entry.getIps()), ","),
                    dateFormat.format(entry.getExpiration()));
            }
            writer.println("Dns negative cache: ");
            for (DnsCacheEntry entry : dnsCache.getNegativeCache()) {
                writer.printf("    %s %s %s\n", entry.getHost(), join(Arrays.asList(entry.getIps()), ","),
                    dateFormat.format(entry.getExpiration()));
            }
        } else {
            writer.println(result.toString());
        }
    }

    /**
     * Throwable 2 stack string
     *
     * @param e e
     * @return the string
     * @since 1.5.0
     */
    static String throwable2StackString(@NotNull Throwable e) {
        StringWriter w = new StringWriter();
        e.printStackTrace(new PrintWriter(w, true));
        return w.toString();
    }

    /** Action 2 method */
    static volatile Map<String, Method> action2Method;

    /**
     * Init action 2 method
     *
     * @throws Exception exception
     * @since 1.5.0
     */
    static synchronized void initAction2Method() throws Exception {
        if (action2Method != null) {
            return;
        }

        Map<String, Method> map = Maps.newHashMapWithExpectedSize(9);
        map.put("set", DnsCacheManipulator.class.getMethod("setDnsCache", String.class, String[].class));
        map.put("get", DnsCacheManipulator.class.getMethod("getDnsCache", String.class));
        map.put("rm", DnsCacheManipulator.class.getMethod("removeDnsCache", String.class));

        map.put("list", DnsCacheManipulator.class.getMethod("getWholeDnsCache"));
        map.put("clear", DnsCacheManipulator.class.getMethod("clearDnsCache"));

        map.put("setPolicy", DnsCacheManipulator.class.getMethod("setDnsCachePolicy", int.class));
        map.put("getPolicy", DnsCacheManipulator.class.getMethod("getDnsCachePolicy"));
        map.put("setNegativePolicy", DnsCacheManipulator.class.getMethod("setDnsNegativeCachePolicy", int.class));
        map.put("getNegativePolicy", DnsCacheManipulator.class.getMethod("getDnsNegativeCachePolicy"));

        map.put(FILE, null);

        action2Method = map;
    }
}
