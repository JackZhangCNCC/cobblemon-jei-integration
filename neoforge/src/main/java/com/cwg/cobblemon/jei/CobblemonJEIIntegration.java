package com.cwg.cobblemon.jei;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;

import com.cwg.cobblemon.jei.category.PokemonDropsRecipeCategory;
import com.cwg.cobblemon.jei.ingredient.PokemonIngredient;
import com.cwg.cobblemon.jei.ingredient.PokemonIngredientHelper;
import com.cwg.cobblemon.jei.ingredient.PokemonIngredientRenderer;

import com.cwg.cobblemon.jei.recipe.PokemonDropsRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.*;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Cobblemon JEI Integration Plugin - NeoForge 实现
 * 为 Cobblemon 提供 JEI 集成，显示宝可梦掉落物信息，并提供球果自动掉落功能
 */
@JeiPlugin
public class CobblemonJEIIntegration implements IModPlugin {

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(
        CobblemonJEIConstants.MOD_ID, 
        CobblemonJEIConstants.JEI_PLUGIN_ID
    );
    
    // 自定义成分类型
    public static final IIngredientType<PokemonIngredient> POKEMON_INGREDIENT = () -> PokemonIngredient.class;
    


    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        try {
            CobblemonJEILogger.info("Starting to register Pokemon ingredient type...");

            // 注册宝可梦成分类型
            registration.register(
                POKEMON_INGREDIENT,
                new ArrayList<>(), // 初始为空，稍后添加
                new PokemonIngredientHelper(),
                new PokemonIngredientRenderer(),
                PokemonIngredient.CODEC
            );

            CobblemonJEILogger.info("Successfully registered Pokemon ingredient type");
        } catch (Exception e) {
            CobblemonJEILogger.error("Failed to register Pokemon ingredient type: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        try {
            CobblemonJEILogger.info("Starting to register Pokemon drops recipe category...");
            // 注册宝可梦掉落物配方类别
            PokemonDropsRecipeCategory category = new PokemonDropsRecipeCategory(registration.getJeiHelpers().getGuiHelper());
            registration.addRecipeCategories(category);
            CobblemonJEILogger.info("Successfully registered Pokemon drops recipe category");
        } catch (Exception e) {
            CobblemonJEILogger.error("Failed to register Pokemon drops recipe category: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        try {
            CobblemonJEILogger.info("Starting to collect Pokemon drop recipes...");
            // 收集并注册宝可梦掉落配方
            List<PokemonDropsRecipe> dropRecipes = collectPokemonDrops();
            CobblemonJEILogger.info("Collected {} Pokemon drop recipes", dropRecipes.size());

            if (!dropRecipes.isEmpty()) {
                registration.addRecipes(PokemonDropsRecipeCategory.TYPE, dropRecipes);
                CobblemonJEILogger.info("Successfully registered {} Pokemon drop recipes", dropRecipes.size());
            } else {
                CobblemonJEILogger.warn("No Pokemon drop recipes found to register");
            }
        } catch (Exception e) {
            CobblemonJEILogger.error("Failed to register Pokemon drop recipes: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 收集所有宝可梦的掉落物信息
     */
    private List<PokemonDropsRecipe> collectPokemonDrops() {
        List<PokemonDropsRecipe> recipes = new ArrayList<>();
        
        try {
            // 遍历所有宝可梦种类
            for (Species species : PokemonSpecies.INSTANCE.getSpecies()) {
                if (species.getDrops() != null && !species.getDrops().getEntries().isEmpty()) {
                    // 为每个有掉落物的宝可梦创建配方
                    PokemonDropsRecipe recipe = new PokemonDropsRecipe(species, species.getDrops().getEntries());
                    recipes.add(recipe);
                }
            }
        } catch (Exception e) {
            CobblemonJEILogger.error("Error collecting Pokemon drops: {}", e.getMessage());
        }
        
        return recipes;
    }



    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        // 可以在这里添加 GUI 处理器，比如为 Cobblemon 的机器添加配方点击区域
        // 目前暂时留空，后续可以扩展
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        // 可以在这里添加配方传输处理器
        // 目前暂时留空，后续可以扩展
    }
}
