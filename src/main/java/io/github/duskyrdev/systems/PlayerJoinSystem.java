package io.github.duskyrdev.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.duskyrdev.components.PlayerRPGComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class PlayerJoinSystem extends RefSystem<EntityStore> {
    @Override
    public void onEntityAdded(
            @NonNullDecl Ref<EntityStore> ref,
            @NonNullDecl AddReason addReason,
            @NonNullDecl Store<EntityStore> store,
            @NonNullDecl CommandBuffer<EntityStore> commandBuffer
    ) {
        if (addReason != AddReason.LOAD) return;

        var playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null) return;

        var rpgType = PlayerRPGComponent.getComponentType();
        var rpg = store.getComponent(ref, rpgType);

        if (rpg != null) {
            playerRef.sendMessage(Message.raw(
                    "Welcome back! Level %d (%d xp)".formatted(rpg.getLevel(), rpg.getTotalExperience())
            ));
        } else {
            // commandBuffer.addComponent()
            // Deferred write - safe during iteration!
            commandBuffer.addComponent(ref, rpgType, new PlayerRPGComponent());
            playerRef.sendMessage(Message.raw("Welcome! Your adventure begins at level 1."));
        }
    }

    @Override
    public void onEntityRemove(
            @NonNullDecl Ref<EntityStore> ref,
            @NonNullDecl RemoveReason removeReason,
            @NonNullDecl Store<EntityStore> store,
            @NonNullDecl CommandBuffer<EntityStore> commandBuffer
    ) {
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        // Only entites WITH PLayerRef trigger us.
        // Filters out NPCs, items, projectiles.
        return Archetype.of(PlayerRef.getComponentType());
    }
}
