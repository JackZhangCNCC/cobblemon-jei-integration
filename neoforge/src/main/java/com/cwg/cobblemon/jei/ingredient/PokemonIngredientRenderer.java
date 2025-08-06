package com.cwg.cobblemon.jei.ingredient;

import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cwg.cobblemon.jei.CobblemonJEILogger;
import com.mojang.blaze3d.platform.Lighting;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

/**
 * 宝可梦成分渲染器，负责在 JEI 中渲染宝可梦图标和信息
 */
public class PokemonIngredientRenderer implements IIngredientRenderer<PokemonIngredient> {

    private final float scale;

    public PokemonIngredientRenderer() {
        this(1.0f);
    }

    public PokemonIngredientRenderer(float scale) {
        this.scale = scale;
    }

    @Override
    public void render(GuiGraphics guiGraphics, PokemonIngredient ingredient) {
        if (ingredient == null || ingredient.getSpecies() == null) {
            // 渲染一个默认的占位符
            renderPlaceholder(guiGraphics);
            return;
        }

        try {
            // 渲染宝可梦图标 - 使用 Cobblemon 的 PokemonItem
            renderPokemonIcon(guiGraphics, ingredient);
        } catch (Exception e) {
            CobblemonJEILogger.error("Error rendering pokemon ingredient: {}", e.getMessage());
            renderPlaceholder(guiGraphics);
        }
    }

    private void renderPokemonIcon(GuiGraphics guiGraphics, PokemonIngredient ingredient) {
        try {
            // 创建一个 Pokemon 实例，然后使用 PokemonItem.from() 方法
            Pokemon pokemon = new Pokemon();
            pokemon.setSpecies(ingredient.getSpecies());

            ItemStack pokemonItemStack = PokemonItem.from(pokemon);

            if (!pokemonItemStack.isEmpty()) {
                // 使用简化的渲染方法，参考 PixelTweaks 的实现
                renderItem(guiGraphics, pokemonItemStack, 0, 0, this.scale);

                CobblemonJEILogger.debug("Successfully rendered Pokemon item for: {}", ingredient.getSpecies().getName());
            } else {
                // 如果无法创建 PokemonItem，回退到文本渲染
                renderPokemonText(guiGraphics, ingredient);
            }

        } catch (Exception e) {
            CobblemonJEILogger.error("Error rendering Pokemon icon: {}", e.getMessage());
            renderPokemonText(guiGraphics, ingredient);
        }
    }

    private void renderPokemonText(GuiGraphics guiGraphics, PokemonIngredient ingredient) {
        Font font = Minecraft.getInstance().font;
        String name = ingredient.getSpecies().getName();
        String displayText = name.length() > 3 ? name.substring(0, 3) : name;

        int x = 8 - font.width(displayText) / 2;
        int y = 8 - font.lineHeight / 2;

        guiGraphics.drawString(font, displayText, x, y, 0xFFFFFF, true);
    }

    private void renderPlaceholder(GuiGraphics guiGraphics) {
        // 渲染一个简单的占位符
        Font font = Minecraft.getInstance().font;
        String text = "?";
        
        int x = 8 - font.width(text) / 2;
        int y = 8 - font.lineHeight / 2;
        
        guiGraphics.drawString(font, text, x, y, 0xFF0000, true);
    }

    // 实现 JEI API 方法 - 保留两个版本以兼容不同的 JEI 版本
    @Override
    public List<Component> getTooltip(PokemonIngredient ingredient, TooltipFlag tooltipFlag) {
        List<Component> tooltip = new ArrayList<>();

        if (ingredient == null || ingredient.getSpecies() == null) {
            tooltip.add(Component.literal("Unknown Pokemon"));
            return tooltip;
        }

        try {
            // 添加宝可梦名称
            tooltip.add(ingredient.getSpecies().getTranslatedName());

            // 简化类型信息显示，避免复杂的集合操作
            tooltip.add(Component.literal("Pokemon Species"));

        } catch (Exception e) {
            CobblemonJEILogger.error("Error creating pokemon tooltip: {}", e.getMessage());
            tooltip.add(Component.literal("Error loading pokemon info"));
        }

        return tooltip;
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, PokemonIngredient ingredient, TooltipFlag tooltipFlag) {
        List<Component> tooltipComponents = getTooltip(ingredient, tooltipFlag);
        tooltip.addAll(tooltipComponents);
    }

    @Override
    public Font getFontRenderer(Minecraft minecraft, PokemonIngredient ingredient) {
        return minecraft.font;
    }

    @Override
    public int getWidth() {
        return (int) (16 * scale);
    }

    @Override
    public int getHeight() {
        return (int) (16 * scale);
    }

    // 从 PixelTweaks 复制的渲染方法
    public static void renderItem(GuiGraphics matrixStack, ItemStack stack, int x, int y, float scale) {
        matrixStack.pose().pushPose();

        BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack);
        bakedmodel = bakedmodel.getOverrides().resolve(bakedmodel, stack, null, null, 0);

        float half = 8.0F * scale;
        float full = 16.0F * scale;

        matrixStack.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pose().translate((float)x, (float)y, 100.0F);
        matrixStack.pose().translate(half, half, 0.0F);
        matrixStack.pose().scale(1.0F, -1.0F, 1.0F);
        matrixStack.pose().scale(full, full, full);
        boolean flag = !bakedmodel.usesBlockLight();
        if (flag) {
            Lighting.setupForFlatItems();
        }

        Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.GUI, false, matrixStack.pose(), matrixStack.bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
        matrixStack.flush();

        if (flag) {
            Lighting.setupFor3DItems();
        }
        matrixStack.pose().popPose();
    }
}
