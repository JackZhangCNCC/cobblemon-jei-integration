package com.cwg.cobblemon.jei.ingredient;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

/**
 * 宝可梦成分助手类，处理宝可梦成分的各种操作
 */
public class PokemonIngredientHelper implements IIngredientHelper<PokemonIngredient> {

    @Override
    public IIngredientType<PokemonIngredient> getIngredientType() {
        return () -> PokemonIngredient.class;
    }

    @Override
    public String getDisplayName(PokemonIngredient ingredient) {
        if (ingredient.getSpecies() == null) {
            return "Unknown Pokemon";
        }
        return ingredient.getSpecies().getTranslatedName().getString();
    }

    @Override
    public String getUniqueId(PokemonIngredient ingredient, UidContext context) {
        if (ingredient.getSpecies() == null) {
            return "unknown";
        }
        return ingredient.getSpecies().getName();
    }

    @Override
    public ResourceLocation getResourceLocation(PokemonIngredient ingredient) {
        if (ingredient.getSpecies() == null) {
            return ResourceLocation.fromNamespaceAndPath("cobblemon", "unknown");
        }
        return ingredient.getSpecies().getResourceIdentifier();
    }

    @Override
    public PokemonIngredient copyIngredient(PokemonIngredient ingredient) {
        return new PokemonIngredient(ingredient.getSpecies());
    }

    @Override
    public String getErrorInfo(@Nullable PokemonIngredient ingredient) {
        if (ingredient == null) {
            return "null pokemon ingredient";
        }
        if (ingredient.getSpecies() == null) {
            return "pokemon ingredient with null species";
        }
        return "pokemon ingredient: " + ingredient.getSpecies().getName();
    }

    @Override
    public boolean isValidIngredient(PokemonIngredient ingredient) {
        return ingredient != null && ingredient.getSpecies() != null;
    }

    @Override
    public boolean isIngredientOnServer(PokemonIngredient ingredient) {
        // 宝可梦数据在服务器和客户端都存在
        return true;
    }

    // 移除过时的 getWildcardId 方法

    public String getModId(PokemonIngredient ingredient) {
        return "cobblemon";
    }

    public Iterable<Integer> getColors(PokemonIngredient ingredient) {
        // 可以根据宝可梦的类型返回对应的颜色
        // 暂时返回空，后续可以实现
        return java.util.Collections.emptyList();
    }
}
