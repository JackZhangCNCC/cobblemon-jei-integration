package com.cwg.cobblemon.jei.data;

import java.util.List;

/**
 * 纯数据类 - 表示宝可梦掉落物信息
 * 不包含任何平台特定的依赖
 */
public class DropData {
    
    private final String pokemonName;
    private final List<ItemData> dropItems;
    
    public DropData(String pokemonName, List<ItemData> dropItems) {
        this.pokemonName = pokemonName;
        this.dropItems = dropItems;
    }
    
    public String getPokemonName() {
        return pokemonName;
    }
    
    public List<ItemData> getDropItems() {
        return dropItems;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        DropData dropData = (DropData) obj;
        return pokemonName != null ? pokemonName.equals(dropData.pokemonName) : dropData.pokemonName == null;
    }
    
    @Override
    public int hashCode() {
        return pokemonName != null ? pokemonName.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "DropData{" +
                "pokemonName='" + pokemonName + '\'' +
                ", dropItems=" + dropItems.size() +
                '}';
    }
}
