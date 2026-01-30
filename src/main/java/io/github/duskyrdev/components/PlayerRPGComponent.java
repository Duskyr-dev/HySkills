package io.github.duskyrdev.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.duskyrdev.level.XPTable;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class PlayerRPGComponent implements Component<EntityStore> {
        private static ComponentType<EntityStore, PlayerRPGComponent> TYPE;

        public static void setComponentType(ComponentType<EntityStore, PlayerRPGComponent> type) {
            TYPE = type;
        }

        public static ComponentType<EntityStore, PlayerRPGComponent> getComponentType() {
            return TYPE;
        }

        public static final BuilderCodec<PlayerRPGComponent> CODEC = BuilderCodec
                .builder(PlayerRPGComponent.class, PlayerRPGComponent::new)
                .append(
                        new KeyedCodec<>("TotalExperience", Codec.LONG),
                        (component, value) -> component.totalExperience = value,
                        component -> component.totalExperience
                ).add()
                .build();

    private long totalExperience = 0;

    public PlayerRPGComponent() {}

    // NEW:
    // Constructor with param. Used by clone() below.
    public PlayerRPGComponent (long totalExperience) {
        this.totalExperience = Math.max(0L, totalExperience);
    }

    public long getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(long xp) {
        this.totalExperience = Math.max(0L, xp);
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

    @NullableDecl
    @Override
    public PlayerRPGComponent clone() {
        return new PlayerRPGComponent(this.totalExperience);
    }

    @Override
    public String toString() {
        return "PlayerRPGComponent{level=" + getLevel() +
                ", totalXP=" + totalExperience +
                ", toNext=" + getXPToNextLevel() + "}";
    }

}
