package io.github.duskyrdev.handlers;

import io.github.duskyrdev.components.PlayerRPGComponent;
import io.github.duskyrdev.events.GiveXPEvent;
import io.github.duskyrdev.events.LevelUpEvent;

import java.util.function.Consumer;

public class GiveXPHandler implements Consumer<GiveXPEvent> {

    @Override
    public void accept(GiveXPEvent event) {
        if (!event.playerRef().isValid()) return;

        var store = event.playerRef().getStore();

        var rpg = store.getComponent(event.playerRef(), PlayerRPGComponent.getComponentType());
        if (rpg == null) return;

        var oldLevel = rpg.getLevel();
        var leveledUp = rpg.addExperience(event.amount());

        if (leveledUp) {
            LevelUpEvent.dispatch(event.playerRef(), oldLevel, rpg.getLevel());
        }
    }
}
