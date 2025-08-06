package com.cwg.cobblemon.jei.autodrop;

/**
 * 自动掉落功能配置类
 */
public class AutoDropConfig {
    
    // 是否启用自动掉落功能
    private boolean enableAutoDrop = true;
    
    // 自动掉落检查间隔（tick）
    private int checkInterval = 20; // 1秒
    
    // 是否只在玩家附近掉落
    private boolean requirePlayerNearby = false;
    
    // 玩家附近的距离（方块）
    private double playerNearbyDistance = 16.0;
    
    // 是否在掉落时播放音效
    private boolean playDropSound = true;
    
    // 是否在掉落时显示粒子效果
    private boolean showDropParticles = true;
    
    // 构造函数
    public AutoDropConfig() {
    }
    
    // Getter 和 Setter 方法
    public boolean isEnableAutoDrop() {
        return enableAutoDrop;
    }
    
    public void setEnableAutoDrop(boolean enableAutoDrop) {
        this.enableAutoDrop = enableAutoDrop;
    }
    
    public int getCheckInterval() {
        return checkInterval;
    }
    
    public void setCheckInterval(int checkInterval) {
        this.checkInterval = Math.max(1, checkInterval); // 最小1tick
    }
    
    public boolean isRequirePlayerNearby() {
        return requirePlayerNearby;
    }
    
    public void setRequirePlayerNearby(boolean requirePlayerNearby) {
        this.requirePlayerNearby = requirePlayerNearby;
    }
    
    public double getPlayerNearbyDistance() {
        return playerNearbyDistance;
    }
    
    public void setPlayerNearbyDistance(double playerNearbyDistance) {
        this.playerNearbyDistance = Math.max(1.0, playerNearbyDistance);
    }
    
    public boolean isPlayDropSound() {
        return playDropSound;
    }
    
    public void setPlayDropSound(boolean playDropSound) {
        this.playDropSound = playDropSound;
    }
    
    public boolean isShowDropParticles() {
        return showDropParticles;
    }
    
    public void setShowDropParticles(boolean showDropParticles) {
        this.showDropParticles = showDropParticles;
    }
    
    /**
     * 重置为默认配置
     */
    public void resetToDefaults() {
        this.enableAutoDrop = true;
        this.checkInterval = 20;
        this.requirePlayerNearby = false;
        this.playerNearbyDistance = 16.0;
        this.playDropSound = true;
        this.showDropParticles = true;
    }
    
    /**
     * 验证配置的有效性
     */
    public boolean isValid() {
        return checkInterval > 0 && playerNearbyDistance > 0;
    }
    
    @Override
    public String toString() {
        return "AutoDropConfig{" +
                "enableAutoDrop=" + enableAutoDrop +
                ", checkInterval=" + checkInterval +
                ", requirePlayerNearby=" + requirePlayerNearby +
                ", playerNearbyDistance=" + playerNearbyDistance +
                ", playDropSound=" + playDropSound +
                ", showDropParticles=" + showDropParticles +
                '}';
    }
}
