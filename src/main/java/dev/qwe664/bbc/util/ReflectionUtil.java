package dev.qwe664.bbc.util;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;

public final class ReflectionUtil {

    private static final Set<String> IGNORED_METHODS = Set.of(
            "equals",
            "hashCode",
            "toString",
            "getClass",
            "wait",
            "notify",
            "notifyAll",
            "compareTo"
    );

    private ReflectionUtil() {
    }

    public static void printPublicMethods(String className) {

        ConsoleLogger.reflection("========================================");

        try {
            Class<?> clazz = Class.forName(className);

            ConsoleLogger.reflection("類別：" + clazz.getName());
            ConsoleLogger.reflection("");

            Method[] methods = clazz.getMethods();

            Arrays.sort(methods, Comparator.comparing(Method::getName));

            ConsoleLogger.reflection("Public API Methods：");

            int shown = 0;
            int filtered = 0;

            for (Method method : methods) {

                if (IGNORED_METHODS.contains(method.getName())) {
                    filtered++;
                    continue;
                }

                StringBuilder builder = new StringBuilder();

                builder.append(method.getReturnType().getSimpleName())
                        .append(" ")
                        .append(method.getName())
                        .append("(");

                Parameter[] parameters = method.getParameters();

                for (int i = 0; i < parameters.length; i++) {

                    builder.append(parameters[i].getType().getSimpleName());

                    if (i < parameters.length - 1) {
                        builder.append(", ");
                    }
                }

                builder.append(")");

                ConsoleLogger.reflection(builder.toString());

                shown++;
            }

            ConsoleLogger.reflection("");
            ConsoleLogger.reflection("Public API 方法：" + shown);
            ConsoleLogger.reflection("已過濾方法：" + filtered);

        } catch (ClassNotFoundException e) {

            ConsoleLogger.error("找不到類別：" + className);

        } catch (Exception e) {

            ConsoleLogger.error("Reflection 發生錯誤：" + e.getMessage());

        }

        ConsoleLogger.reflection("========================================");
    }
}
