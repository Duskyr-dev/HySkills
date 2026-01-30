package io.github.duskyrdev.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.duskyrdev.components.PlayerRPGComponent;
import io.github.duskyrdev.level.XPTable;

import javax.annotation.Nonnull;

public class RPGStatsCommand extends AbstractPlayerCommand {

    public RPGStatsCommand() {
        super("stats", "Show your RPG stats");
    }

    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world
    ) {
        var rpg = store.getComponent(ref, PlayerRPGComponent.getComponentType());
        if (rpg == null) {
            playerRef.sendMessage(Message.raw("No RPG data found"));
            return;
        }

        var level = rpg.getLevel();
        var totalXp = rpg.getTotalExperience();
        var currentXp = rpg.getCurrentLevelXP();
        var toNext = rpg.getXPToNextLevel();
        var progress = (int) (rpg.getProgress() * 100);

        playerRef.sendMessage(Message.raw("=== RPG Stats ==="));
        playerRef.sendMessage(Message.raw("Level: %d%s".formatted(
                level,
                rpg.isMaxLevel() ? " (MAX)" : ""
        )));
        playerRef.sendMessage(Message.raw("Total XP: %d".formatted(totalXp)));

        if (!rpg.isMaxLevel()) {
            playerRef.sendMessage(Message.raw("Progress: %d/%d (%d%%)".formatted(
                    currentXp,
                    currentXp + toNext,
                    progress
            )));
            playerRef.sendMessage(Message.raw("To next level: %d XP".formatted(toNext)));
        }
    }
}
