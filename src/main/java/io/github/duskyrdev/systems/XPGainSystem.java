package io.github.duskyrdev.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.duskyrdev.components.PlayerRPGComponent;
import io.github.duskyrdev.events.GiveXPEvent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class XPGainSystem extends DeathSystems.OnDeathSystem {

    private static final long XP_PER_KILL = 50L;

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.of(DeathComponent.getComponentType());
    }

    @Override
    public void onComponentAdded(
            @NonNullDecl Ref<EntityStore> ref,
            @NonNullDecl DeathComponent deathComponent,
            @NonNullDecl Store<EntityStore> store,
            @NonNullDecl CommandBuffer<EntityStore> commandBuffer
    ) {
        var deathInfo = deathComponent.getDeathInfo();
        if (deathInfo == null) return;

        if (!(deathInfo.getSource() instanceof Damage.EntitySource source)) return;

        var killerRef = source.getRef();
        if (!killerRef.isValid()) return;

        var killer = store.getComponent(killerRef, PlayerRef.getComponentType());
        if (killer == null) return;

        var playerRpgComponent = store.getComponent(killerRef, PlayerRPGComponent.getComponentType());
        if (playerRpgComponent == null) return;

        killer.sendMessage(Message.raw("+%d XP".formatted(XP_PER_KILL)));
        GiveXPEvent.dispatch(killerRef, XP_PER_KILL);
    }
}