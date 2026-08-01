package dev.qwe664.bbc.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

public final class ReflectionUtil {

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

            ConsoleLogger.reflection("Public Methods：");

            for (Method method : methods) {

                ConsoleLogger.reflection(
                        method.getReturnType().getSimpleName()
                                + " "
                                + method.getName()
                                + "()"
                );
            }

            ConsoleLogger.reflection("");
            ConsoleLogger.reflection("方法數量：" + methods.length);

        } catch (ClassNotFoundException e) {

            ConsoleLogger.error("找不到類別：" + className);

        } catch (Exception e) {

            ConsoleLogger.error("Reflection 發生錯誤：" + e.getMessage());

        }

        ConsoleLogger.reflection("========================================");
    }
}
