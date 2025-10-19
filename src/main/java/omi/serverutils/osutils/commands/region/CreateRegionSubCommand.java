package omi.serverutils.osutils.commands.region;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.modules.RegionsModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateRegionSubCommand {
    public static int createRegion(CommandContext<CommandSourceStack> context) {
        try {
            String name = StringArgumentType.getString(context, "name");
            BlockPos startPos = BlockPosArgument.getLoadedBlockPos(context, "startPos");
            BlockPos endPos = BlockPosArgument.getLoadedBlockPos(context, "endPos");
            ServerLevel level = context.getSource().getLevel();
            String dimension = level.dimension().location().toString();

            if(!name.matches("^[a-zA-Z0-9_]*$")){
                context.getSource().sendFailure(Component.literal("Invalid region name, only letters, numbers and underscores are allowed"));
                return 0;
            }

            // Create default properties
            Map<String, Boolean> properties = new HashMap<>();
            RegionCommand.PROPERTY_NAMES.keySet().forEach(property -> properties.put(property, false));

            // Create region with current player as owner
            ArrayList<String> owners = new ArrayList<>();
            if (context.getSource().getEntity() instanceof ServerPlayer) {
                owners.add(context.getSource().getPlayerOrException().getScoreboardName());
            }

            RegionsModule.Region region = new RegionsModule.Region(
                    name,
                    dimension,
                    new Vec3(startPos.getX(), startPos.getY(), startPos.getZ()),
                    new Vec3(endPos.getX(), endPos.getY(), endPos.getZ()),
                    properties,
                    owners,
                    true
            );

            RegionsModule.Store.uploadRegion(region);
            context.getSource().sendSuccess(() ->
                            Component.literal(String.format("Region '%s' created in %s", name, dimension)),
                    true
            );
            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Failed to create region"));
            OSUtils.LOGGER.error(e.getMessage());
            return 0;
        }
    }
}
