package io.github.duskyrdev.commands;

import com.hypixel.hytale.codec.validation.Validators;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadLocalRandom;

/**
 * /rpg spawn - Spawns NPCs around the player for testing.
 *
 * Usage:
 *   /rpg spawn                     -> 5 Skeletons
 *   /rpg spawn --type Zombie       -> 5 Zombies
 *   /rpg spawn --count 10          -> 10 Skeletons
 *   /rpg spawn --type Kweebec --count 3
 */
public class RPGSpawnCommand extends AbstractPlayerCommand {

    private static final String DEFAULT_TYPE = "Skeleton";
    private static final int DEFAULT_COUNT = 5;
    private static final int MAX_COUNT = 50;

    private final OptionalArg<String> typeArg;
    private final OptionalArg<Integer> countArg;

    public RPGSpawnCommand() {
        super("spawn", "Spawn NPCs around you");
        this.typeArg = withOptionalArg("type", "NPC type", ArgTypes.STRING);
        this.countArg = withOptionalArg("count", "Amount (1-50)", ArgTypes.INTEGER)
                .addValidator(Validators.greaterThanOrEqual(1))
                .addValidator(Validators.lessThan(MAX_COUNT + 1));
    }

    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world
    ) {
        var npcType = typeArg.get(context);
        if (npcType == null) npcType = DEFAULT_TYPE;

        var count = countArg.get(context);
        if (count == null) count = DEFAULT_COUNT;

        var transform = store.getComponent(ref, TransformComponent.getComponentType());
        if (transform == null) {
            playerRef.sendMessage(Message.raw("Error: Could not get position"));
            return;
        }

        var roleIndex = NPCPlugin.get().getIndex(npcType);
        if (roleIndex < 0) {
            playerRef.sendMessage(Message.raw("Unknown NPC: %s".formatted(npcType)));
            return;
        }

        var playerPos = transform.getPosition();
        var worldStore = world.getEntityStore().getStore();
        var random = ThreadLocalRandom.current();
        var spawned = 0;

        // Simple spawn: random positions around the player
        for (int i = 0; i < count; i++) {
            var spawnPos = new Vector3d(
                    playerPos.getX() + random.nextDouble() * 6 - 3,  // -3 to +3
                    playerPos.getY() + 0.5,
                    playerPos.getZ() + random.nextDouble() * 6 - 3   // -3 to +3
            );

            var result = NPCPlugin.get().spawnNPC(
                    worldStore, npcType, null, spawnPos, new Vector3f()
            );

            if (result != null && result.first() != null) spawned++;
        }

        playerRef.sendMessage(Message.raw("Spawned %d %s".formatted(spawned, npcType)));
    }
}
