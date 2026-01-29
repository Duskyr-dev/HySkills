package io.github.duskyrdev.components;

import io.github.duskyrdev.level.XPTable;

public class PlayerRPGComponent {
    private long totalExperience = 0;

    public PlayerRPGComponent() {}

    public long getTotalExperience() {
        return totalExperience;
    }

    // No stored 'level' field!
    // Computed via XPTable lookup.
    public int getLevel() {
        return XPTable.getLevelForXP(totalExperience);
    }

    public long getCurrentLevelXP() {
        return XPTable.getXPInCurrentLevel(totalExperience);
    }

    public long getXPToNextLevel() {
        return XPTable.getXPToNextLevel(totalExperience);
    }

    public float getProgress() {
        return XPTable.getProgressToNextLevel(totalExperience);
    }

    // All these getters compute from totalExperience.
    // No redundant fields!
    public boolean isMaxLevel() {
        return getLevel() >= XPTable.MAX_LEVEL;
    }

    public boolean addExperience(long amount) {
        if (amount <= 0) return false;

        // Capture level before & after.
        // Returns true if leveled up!
        int oldLevel = getLevel();
        totalExperience += amount;
        int newLevel = getLevel();

        return newLevel > oldLevel;
    }

}
