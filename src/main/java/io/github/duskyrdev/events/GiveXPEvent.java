package io.github.duskyrdev.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.event.IEventDispatcher;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public record GiveXPEvent(
        @Nonnull Ref<EntityStore> playerRef,
        long amount
) implements IEvent<Void> {

    // Static helper method.
    // Callers don't need EventBus knowledge.
    public static void dispatch(Ref<EntityStore> playerRef, long amount) {
        // Get typed dispatcher from EventBus.
        // Handles listener lookup internally.
        IEventDispatcher<GiveXPEvent, GiveXPEvent> dispatcher =
                HytaleServer.get().getEventBus().dispatchFor(GiveXPEvent.class);

        // Skip if no listeners registered.
        // Avoid creating objects for nothing.
        if (dispatcher.hasListener()) {
            dispatcher.dispatch(new GiveXPEvent(playerRef, amount));
        }
    }
}
