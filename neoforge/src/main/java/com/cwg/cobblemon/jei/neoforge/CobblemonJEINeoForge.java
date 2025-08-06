package com.cwg.cobblemon.jei.neoforge;

import com.cwg.cobblemon.jei.CobblemonJEILogger;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Cobblemon JEI Integration NeoForge 主类
 *
 * 提供 JEI 和 EMI 集成支持，显示宝可梦掉落物信息
 */
@Mod("cobblemon_jei_integration")
public class CobblemonJEINeoForge {

    public CobblemonJEINeoForge(IEventBus modEventBus) {
        CobblemonJEILogger.info("Cobblemon JEI Integration NeoForge initializing...");

        // 注册初始化事件
        modEventBus.addListener(this::onCommonSetup);

        CobblemonJEILogger.info("Cobblemon JEI Integration NeoForge initialized");
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        CobblemonJEILogger.info("Cobblemon JEI Integration common setup starting...");

        // JEI 和 EMI 插件通过注解自动加载：
        // - @JeiPlugin 注解的 CobblemonJEIIntegration 类
        // - @EmiEntrypoint 注解的 CobblemonEmiPlugin 类

        CobblemonJEILogger.info("Cobblemon JEI Integration common setup completed");
    }
}
