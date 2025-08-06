package com.cwg.cobblemon.jei.data;

/**
 * 纯数据类 - 表示物品信息
 * 不包含任何平台特定的依赖
 */
public class ItemData {
    
    private final String itemId;
    private final int quantity;
    private final float dropChance;
    
    public ItemData(String itemId, int quantity, float dropChance) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.dropChance = dropChance;
    }
    
    public String getItemId() {
        return itemId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public float getDropChance() {
        return dropChance;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ItemData itemData = (ItemData) obj;
        return quantity == itemData.quantity &&
                Float.compare(itemData.dropChance, dropChance) == 0 &&
                itemId != null ? itemId.equals(itemData.itemId) : itemData.itemId == null;
    }
    
    @Override
    public int hashCode() {
        int result = itemId != null ? itemId.hashCode() : 0;
        result = 31 * result + quantity;
        result = 31 * result + (dropChance != +0.0f ? Float.floatToIntBits(dropChance) : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "ItemData{" +
                "itemId='" + itemId + '\'' +
                ", quantity=" + quantity +
                ", dropChance=" + dropChance +
                '}';
    }
}
