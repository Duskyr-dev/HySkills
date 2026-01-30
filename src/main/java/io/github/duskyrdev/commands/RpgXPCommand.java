package io.github.duskyrdev.commands;

import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.duskyrdev.components.PlayerRPGComponent;
import io.github.duskyrdev.events.GiveXPEvent;

import javax.annotation.Nonnull;

public class RpgXPCommand extends AbstractPlayerCommand {

    private static final int DEFAULT_AMOUNT = 50;

    private final OptionalArg<Integer> amountArg;

    public RpgXPCommand() {
        super("xp", "Give yourself XP");
        this.amountArg = withOptionalArg("amount", "XP amount (>0)", ArgTypes.INTEGER)
                .addValidator(Validators.greaterThan(0));
    }

    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world
    ) {
        var amount = amountArg.get(context);
        if (amount == null) amount = DEFAULT_AMOUNT;

        // Check if player has RPG component
        if (store.getComponent(ref, PlayerRPGComponent.getComponentType()) == null) {
            playerRef.sendMessage(Message.raw("No RPG data found"));
            return;
        }

        playerRef.sendMessage(Message.raw("+%d XP".formatted(amount)));
        GiveXPEvent.dispatch(ref, amount);
    }
}
