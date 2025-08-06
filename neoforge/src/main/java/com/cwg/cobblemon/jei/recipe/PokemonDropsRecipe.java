package com.cwg.cobblemon.jei.recipe;

import com.cobblemon.mod.common.api.drop.DropEntry;
import com.cobblemon.mod.common.api.drop.ItemDropEntry;
import com.cobblemon.mod.common.pokemon.Species;
import com.cwg.cobblemon.jei.CobblemonJEILogger;
import com.cwg.cobblemon.jei.ingredient.PokemonIngredient;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

/**
 * 宝可梦掉落物配方类，表示一个宝可梦及其掉落物
 */
public class PokemonDropsRecipe {

    private final Species pokemon;
    private final List<DropEntry> dropEntries;
    private final List<ItemStack> dropItems;
    private final List<Float> dropChances;
    private final PokemonIngredient pokemonIngredient;

    public PokemonDropsRecipe(Species pokemon, List<DropEntry> dropEntries) {
        this.pokemon = pokemon;
        this.dropEntries = new ArrayList<>(dropEntries);
        this.dropItems = new ArrayList<>();
        this.dropChances = new ArrayList<>();
        this.pokemonIngredient = new PokemonIngredient(pokemon);

        // 将 DropEntry 转换为 ItemStack
        for (DropEntry entry : dropEntries) {
            try {
                if (entry instanceof ItemDropEntry itemDrop) {
                    // 创建 ItemStack - 基于 Cobblemon 的 ItemDropEntry 实现
                    ItemStack stack = createItemStackFromDropEntry(itemDrop);
                    if (stack != null && !stack.isEmpty()) {
                        dropItems.add(stack);
                        dropChances.add(itemDrop.getPercentage());
                    }
                }
            } catch (Exception e) {
                CobblemonJEILogger.error("Error processing drop entry for {}: {}", pokemon.getName(), e.getMessage());
            }
        }
    }

    /**
     * 从 ItemDropEntry 创建 ItemStack
     * 基于 Cobblemon 的 ItemDropEntry.drop() 方法实现
     */
    private ItemStack createItemStackFromDropEntry(ItemDropEntry dropEntry) {
        try {
            // 这里模拟 ItemDropEntry.drop() 方法中的物品创建逻辑
            // 由于我们在客户端环境中，无法直接访问 ServerLevel 的注册表
            // 所以使用默认的物品注册表
            var item = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(dropEntry.getItem());
            if (item != null && item != Items.AIR) {
                // 使用基础数量，避免 Kotlin IntRange 问题
                int quantity = dropEntry.getQuantity();
                return new ItemStack(item, quantity);
            }
        } catch (Exception e) {
            CobblemonJEILogger.error("Error creating ItemStack from drop entry: {}", e.getMessage());
        }
        return ItemStack.EMPTY;
    }
    
    public Species getPokemon() {
        return pokemon;
    }
    
    public List<DropEntry> getDropEntries() {
        return new ArrayList<>(dropEntries);
    }
    
    public List<ItemStack> getDropItems() {
        return new ArrayList<>(dropItems);
    }

    /**
     * 获取掉落物的掉落概率
     */
    public List<Float> getDropChances() {
        return new ArrayList<>(dropChances);
    }

    /**
     * 获取宝可梦成分对象
     */
    public PokemonIngredient getPokemonIngredient() {
        return pokemonIngredient;
    }
    
    /**
     * 检查这个配方是否有效（有宝可梦和掉落物）
     */
    public boolean isValid() {
        return pokemon != null && !dropEntries.isEmpty();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        PokemonDropsRecipe that = (PokemonDropsRecipe) obj;
        return pokemon != null ? pokemon.equals(that.pokemon) : that.pokemon == null;
    }
    
    @Override
    public int hashCode() {
        return pokemon != null ? pokemon.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "PokemonDropsRecipe{" +
                "pokemon=" + (pokemon != null ? pokemon.getName() : "null") +
                ", dropCount=" + dropEntries.size() +
                '}';
    }
}
