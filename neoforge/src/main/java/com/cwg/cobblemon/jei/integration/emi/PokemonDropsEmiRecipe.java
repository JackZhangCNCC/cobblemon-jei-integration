package com.cwg.cobblemon.jei.integration.emi;

import com.cobblemon.mod.common.api.drop.DropEntry;
import com.cobblemon.mod.common.api.drop.ItemDropEntry;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.cwg.cobblemon.jei.CobblemonJEIConstants;
import com.cwg.cobblemon.jei.CobblemonJEILogger;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.api.widget.TextWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;


import java.util.ArrayList;
import java.util.List;

/**
 * EMI 宝可梦掉落物配方
 * 显示特定宝可梦的掉落物信息
 */
public class PokemonDropsEmiRecipe implements EmiRecipe {

    private final Species pokemon;
    private final ResourceLocation id;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;
    private final List<Float> dropChances; // 存储掉落概率

    public PokemonDropsEmiRecipe(Species pokemon) {
        this.pokemon = pokemon;
        this.id = ResourceLocation.fromNamespaceAndPath(
            CobblemonJEIConstants.MOD_ID,
            "pokemon_drops/" + pokemon.getName().toLowerCase()
        );
        this.inputs = createInputs();
        this.dropChances = new ArrayList<>();
        this.outputs = createOutputs();
    }

    /**
     * 创建输入（宝可梦本身）
     */
    private List<EmiIngredient> createInputs() {
        List<EmiIngredient> inputs = new ArrayList<>();

        try {
            // 创建一个 Pokemon 实例
            Pokemon pokemonInstance = new Pokemon();
            pokemonInstance.setSpecies(pokemon);

            // 使用 Cobblemon 的 PokemonItem 创建物品堆栈
            ItemStack pokemonItemStack = com.cobblemon.mod.common.item.PokemonItem.from(pokemonInstance);

            if (!pokemonItemStack.isEmpty()) {
                inputs.add(EmiStack.of(pokemonItemStack));
                CobblemonJEILogger.debug("Created Pokemon input for: {}", pokemon.getName());
            } else {
                // 如果无法创建 PokemonItem，使用文本表示
                CobblemonJEILogger.debug("Failed to create Pokemon item for: {}, using text representation", pokemon.getName());
            }
        } catch (Exception e) {
            CobblemonJEILogger.error("Failed to create Pokemon input for {}: {}", pokemon.getName(), e.getMessage());
        }

        return inputs;
    }
    
    /**
     * 创建输出物品列表
     */
    private List<EmiStack> createOutputs() {
        List<EmiStack> outputs = new ArrayList<>();

        try {
            // 获取宝可梦的掉落物
            var drops = pokemon.getDrops();
            if (drops != null) {
                for (DropEntry drop : drops.getEntries()) {
                    try {
                        // 只处理物品掉落
                        if (drop instanceof ItemDropEntry itemDrop) {
                            // 创建物品堆栈
                            ItemStack dropItem = new ItemStack(
                                net.minecraft.core.registries.BuiltInRegistries.ITEM.get(itemDrop.getItem()),
                                itemDrop.getQuantity()
                            );
                            if (!dropItem.isEmpty()) {
                                outputs.add(EmiStack.of(dropItem));
                                // 同时收集掉落概率
                                dropChances.add(itemDrop.getPercentage());
                            }
                        }
                    } catch (Exception e) {
                        CobblemonJEILogger.debug("Failed to process drop for {}: {}",
                            pokemon.getName(), e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            CobblemonJEILogger.debug("Failed to get drops for {}: {}",
                pokemon.getName(), e.getMessage());
        }

        return outputs;
    }
    
    @Override
    public EmiRecipeCategory getCategory() {
        return CobblemonEmiPlugin.POKEMON_DROPS;
    }
    
    @Override
    public ResourceLocation getId() {
        return id;
    }
    
    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }
    
    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }
    
    @Override
    public int getDisplayWidth() {
        // 计算需要的宽度：左侧宝可梦 + 箭头 + 右侧掉落物
        int baseWidth = 60; // 左侧基础宽度
        int itemsPerRow = Math.min(4, outputs.size());
        int itemsWidth = itemsPerRow * 24; // 每个物品24像素间距
        return Math.max(150, baseWidth + itemsWidth + 20); // 最小150像素
    }
    
    @Override
    public int getDisplayHeight() {
        // 计算需要的高度：宝可梦名称 + 槽位 + 概率文本
        int baseHeight = 75; // 基础高度，为概率文本留出更多空间
        int rows = Math.max(1, (outputs.size() + 3) / 4); // 计算需要的行数
        return baseHeight + (rows - 1) * 24; // 每行额外增加24像素（增加行间距）
    }
    
    @Override
    public void addWidgets(WidgetHolder widgets) {
        try {
            // 添加宝可梦名称（顶部居中显示）
            Component pokemonName = pokemon.getTranslatedName();
            widgets.addText(pokemonName, 56, 5, 0xFFFFFF, false);

            // 添加宝可梦输入槽位（左侧）
            if (!inputs.isEmpty()) {
                widgets.addSlot(inputs.get(0), 5, 20);
            }

            // 添加箭头（指向右侧）- 使用简单的文本箭头
            widgets.addText(Component.literal("→"), 35, 28, 0x404040, false);

            // 添加输出物品槽位（右侧）
            int x = 60;
            int y = 20; // 调整 y 位置为宝可梦名称留出空间
            int slotIndex = 0;
            int slotSpacing = 24; // 增加槽位间距，避免文字重叠

            for (EmiStack output : outputs) {
                if (slotIndex >= 8) break; // 最多显示8个物品

                int slotX = x + (slotIndex % 4 * slotSpacing);
                int slotY = y + (slotIndex / 4 * 24); // 增加行间距

                widgets.addSlot(output, slotX, slotY);

                // 添加掉落概率文本（在物品下方）
                if (slotIndex < dropChances.size()) {
                    float chance = dropChances.get(slotIndex);
                    String chanceText = String.format("%.0f%%", chance);

                    // 计算文本真正居中位置
                    // 槽位宽度是16像素，我们需要将文本居中在槽位下方
                    int textX = slotX + 8; // 槽位中心（16/2 = 8）
                    int textY = slotY + 20; // 槽位下方

                    // 使用居中对齐的文本渲染
                    widgets.addText(Component.literal(chanceText), textX, textY, 0xFFFFFF, true)
                           .horizontalAlign(TextWidget.Alignment.CENTER);
                }

                slotIndex++;
            }

            // 如果没有掉落物，显示提示
            if (outputs.isEmpty()) {
                widgets.addText(Component.literal("No drops"), 60, 35, 0x808080, false);
            }

        } catch (Exception e) {
            CobblemonJEILogger.error("Failed to add widgets for Pokemon drops recipe: {}", e.getMessage());

            // 添加错误提示
            widgets.addText(Component.literal("Error loading drops"), 5, 5, 0xFF0000, false);
        }
    }
    
    /**
     * 获取宝可梦种类
     */
    public Species getPokemon() {
        return pokemon;
    }

    /**
     * 获取掉落概率列表
     */
    public List<Float> getDropChances() {
        return dropChances;
    }
}
