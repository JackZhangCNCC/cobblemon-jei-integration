package com.cwg.cobblemon.jei.integration.emi;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.cwg.cobblemon.jei.CobblemonJEIConstants;
import com.cwg.cobblemon.jei.CobblemonJEILogger;
import com.cobblemon.mod.common.CobblemonItems;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Cobblemon EMI 插件
 * 为 EMI 提供宝可梦掉落物信息显示
 */
@EmiEntrypoint
public class CobblemonEmiPlugin implements EmiPlugin {
    
    // 宝可梦掉落物配方类别
    public static final EmiRecipeCategory POKEMON_DROPS = new EmiRecipeCategory(
        ResourceLocation.fromNamespaceAndPath(CobblemonJEIConstants.MOD_ID, "pokemon_drops"),
        EmiStack.of(CobblemonItems.POKE_BALL), // 使用精灵球作为图标
        EmiStack.of(CobblemonItems.POKE_BALL)  // 简化图标也使用精灵球
    );
    
    @Override
    public void register(EmiRegistry registry) {
        try {
            CobblemonJEILogger.info("Starting EMI plugin registration...");
            
            // 注册配方类别
            registry.addCategory(POKEMON_DROPS);
            CobblemonJEILogger.info("Registered Pokemon drops recipe category for EMI");
            
            // 添加工作台（可选）
            registry.addWorkstation(POKEMON_DROPS, EmiStack.of(CobblemonItems.POKE_BALL));
            
            // 注册宝可梦掉落物配方
            registerPokemonDropsRecipes(registry);
            
            CobblemonJEILogger.info("EMI plugin registration completed successfully");
            
        } catch (Exception e) {
            CobblemonJEILogger.error("Failed to register EMI plugin: {}", e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 注册宝可梦掉落物配方
     */
    private void registerPokemonDropsRecipes(EmiRegistry registry) {
        try {
            CobblemonJEILogger.info("Starting to register Pokemon drops recipes for EMI...");
            
            // 获取所有宝可梦种类
            List<Species> allSpecies = new ArrayList<>();
            try {
                allSpecies.addAll(PokemonSpecies.INSTANCE.getSpecies());
                CobblemonJEILogger.info("Found {} Pokemon species for EMI integration", allSpecies.size());
            } catch (Exception e) {
                CobblemonJEILogger.error("Failed to get Pokemon species: {}", e.getMessage());
                return;
            }
            
            int recipeCount = 0;
            
            // 为每个宝可梦创建掉落物配方
            for (Species species : allSpecies) {
                try {
                    // 创建 EMI 配方
                    PokemonDropsEmiRecipe recipe = new PokemonDropsEmiRecipe(species);
                    
                    // 只注册有掉落物的宝可梦
                    if (!recipe.getOutputs().isEmpty()) {
                        registry.addRecipe(recipe);
                        recipeCount++;
                    }
                    
                } catch (Exception e) {
                    CobblemonJEILogger.debug("Failed to create EMI recipe for {}: {}", 
                        species.getName(), e.getMessage());
                }
            }
            
            CobblemonJEILogger.info("Successfully registered {} Pokemon drops recipes for EMI", recipeCount);
            
        } catch (Exception e) {
            CobblemonJEILogger.error("Failed to register Pokemon drops recipes for EMI: {}", e.getMessage());
        }
    }
}
