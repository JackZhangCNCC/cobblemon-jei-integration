package com.cwg.cobblemon.jei;

/**
 * 简化的日志工具类 - 不依赖任何外部库
 * 实际的日志实现在平台特定模块中
 */
public class CobblemonJEILogger {

    public static void info(String message, Object... args) {
        System.out.println("[CobblemonJEI/INFO] " + String.format(message, args));
    }

    public static void warn(String message, Object... args) {
        System.out.println("[CobblemonJEI/WARN] " + String.format(message, args));
    }

    public static void error(String message, Object... args) {
        System.err.println("[CobblemonJEI/ERROR] " + String.format(message, args));
    }

    public static void debug(String message, Object... args) {
        System.out.println("[CobblemonJEI/DEBUG] " + String.format(message, args));
    }
}
