package io.github.duskyrdev.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class RPGCommand extends AbstractCommandCollection {

    public RPGCommand() {
        super("RPG", "RPG Debug Commands");
        addSubCommand(new RPGSpawnCommand());
        addSubCommand(new RpgXPCommand());
        addSubCommand(new RPGStatsCommand());
    }
}

