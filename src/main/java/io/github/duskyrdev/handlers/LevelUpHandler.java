package io.github.duskyrdev.handlers;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.github.duskyrdev.events.LevelUpEvent;

import java.util.function.Consumer;

public class LevelUpHandler implements Consumer<LevelUpEvent> {

    @Override
    public void accept(LevelUpEvent event) {
        if (!event.playerRef().isValid()) return;

        var store = event.playerRef().getStore();
        var playerRef = store.getComponent(event.playerRef(), PlayerRef.getComponentType());
        if (playerRef == null) return;

        var message = switch (event.levelsGained()) {
            case 1 -> "LEVEL UP! You are now level %d!".formatted(event.newLevel());
            default -> "LEVEL UP! +%d levels! Now level %d!".formatted(event.levelsGained(), event.newLevel());
        };

        playerRef.sendMessage(Message.raw(message));
    }
}
