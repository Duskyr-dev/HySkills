package io.github.duskyrdev;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import io.github.duskyrdev.commands.RPGCommand;
import io.github.duskyrdev.components.PlayerRPGComponent;
import io.github.duskyrdev.events.GiveXPEvent;
import io.github.duskyrdev.events.LevelUpEvent;
import io.github.duskyrdev.handlers.GiveXPHandler;
import io.github.duskyrdev.handlers.LevelUpHandler;
import io.github.duskyrdev.systems.PlayerJoinSystem;
import io.github.duskyrdev.systems.XPGainSystem;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class HySkills extends JavaPlugin{
    public HySkills(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    // Called at server boot.
    // Done before the worlds load, before players join.
    protected void setup() {
        var registry = getEntityStoreRegistry();

        // Takes 3 args: class, persistence ID, codec
        // ID is saved to disk - never change this!!
        var rpgType = registry.registerComponent(
                PlayerRPGComponent.class,
                // "HySkills_PlayerData" = key in save files.
                // Change it = orphaning all existing data!
                "HySkills_PlayerData",
                PlayerRPGComponent.CODEC
        );
        // Wire it back to the component class.
        // Now getComponentType() works everywhere.
        PlayerRPGComponent.setComponentType(rpgType);

        registry.registerSystem(new XPGainSystem());
        registry.registerSystem(new PlayerJoinSystem());

        getEventRegistry().register(GiveXPEvent.class, new GiveXPHandler());
        getEventRegistry().register(LevelUpEvent.class, new LevelUpHandler());

        getCommandRegistry().registerCommand(new RPGCommand());
    }
}
