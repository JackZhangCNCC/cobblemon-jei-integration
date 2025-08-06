package com.cwg.cobblemon.jei.category;

import com.cobblemon.mod.common.CobblemonItems;
import com.cwg.cobblemon.jei.CobblemonJEIIntegration;
import com.cwg.cobblemon.jei.ingredient.PokemonIngredientRenderer;
import com.cwg.cobblemon.jei.recipe.PokemonDropsRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Cobblemon 宝可梦掉落物配方类别
 * 基于 PixelTweaks 的 DropsRecipeCategory 实现
 */
public class PokemonDropsRecipeCategory implements IRecipeCategory<PokemonDropsRecipe> {

    public static final RecipeType<PokemonDropsRecipe> TYPE = RecipeType.create("cobblemon_jei_integration", "pokemon_drops", PokemonDropsRecipe.class);
    private static final PokemonIngredientRenderer renderer = new PokemonIngredientRenderer(3F);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic slotDrawable;

    public PokemonDropsRecipeCategory(IGuiHelper guiHelper) {
        // 使用 Cobblemon 的物品作为图标
        ItemStack iconStack = new ItemStack(CobblemonItems.POKE_BALL);
        this.icon = guiHelper.createDrawableItemStack(iconStack);
        this.slotDrawable = guiHelper.getSlotDrawable();
        
        // 创建背景 - 使用简单的纯色背景，尺寸与 PixelTweaks 相同
        this.background = guiHelper.createBlankDrawable(112, 100);
    }

    @Override
    public RecipeType<PokemonDropsRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.cobblemon_jei_integration.pokemon_drops.title");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return 112;
    }

    @Override
    public int getHeight() {
        return 100;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PokemonDropsRecipe recipe, IFocusGroup focuses) {
        // 添加宝可梦输入槽位 - 参考 PixelTweaks 的实现
        builder.addSlot(RecipeIngredientRole.INPUT, 14 - 8, 6)
                .setCustomRenderer(CobblemonJEIIntegration.POKEMON_INGREDIENT, renderer)
                .addIngredient(CobblemonJEIIntegration.POKEMON_INGREDIENT,
                    new com.cwg.cobblemon.jei.ingredient.PokemonIngredient(recipe.getPokemon()));

        // 添加掉落物输出槽位
        for (int i = 0; i < recipe.getDropItems().size() && i < 4; i++) { // 最多显示4个掉落物
            builder.addSlot(RecipeIngredientRole.OUTPUT, 8 + (26 * i), 65)
                    .addItemStack(recipe.getDropItems().get(i));
        }
    }

    @Override
    public void draw(PokemonDropsRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        // 绘制掉落概率 - 与 PixelTweaks 相同的逻辑
        for (int i = 0; i < recipe.getDropItems().size() && i < 4; i++) {
            float chance = recipe.getDropChances().get(i);
            String chanceString = String.format("%.0f", chance) + "%";
            int width = Minecraft.getInstance().font.width(chanceString);
            
            // 在物品下方显示概率
            guiGraphics.drawString(
                Minecraft.getInstance().font, 
                chanceString, 
                8 + (26 * i) + 10 - (width / 2), 
                66 + 24, 
                0xFFFFFF, 
                true
            );
        }
        
        // 绘制宝可梦名称
        String pokemonName = recipe.getPokemon().getTranslatedName().getString();
        int nameWidth = Minecraft.getInstance().font.width(pokemonName);
        guiGraphics.drawString(
            Minecraft.getInstance().font,
            pokemonName,
            56 - (nameWidth / 2), // 居中显示
            58,
            0xFFFFFF,
            true
        );
    }
}
