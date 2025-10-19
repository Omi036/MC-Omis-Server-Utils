package omi.serverutils.osutils.commands.region;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.network.chat.Component;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.modules.RegionsModule;

public class PropertyOwnerRegionSubCommand {
    public static int addOwner(CommandContext<CommandSourceStack> context, String username){
        try {
            // Gets the parameters
            String dimension = DimensionArgument.getDimension(context, "dimension").dimension().location().toString();
            String name = StringArgumentType.getString(context, "name");

            // Finds the region
            RegionsModule.Region region = RegionsModule.getRegionByName(name, dimension);

            if(region == null) {
                context.getSource().sendFailure(Component.literal("Region not found"));
                return 0;
            }


            // Check if owner is already in the list
            if (region.owners.contains(username)) {
                context.getSource().sendFailure(Component.literal(String.format("Owner '%s' was already in the list.", username)));
                return 0;
            }

            // Adds owner to the region
            region.owners.add(username);


            // And replaces the region with the new one
            RegionsModule.Store.replaceRegionAndSave(name, dimension, region);
            context.getSource().sendSuccess(() ->
                            Component.literal(String.format("Owner '%s' added.", username)),
                    true
            );
            return 1;
        } catch (CommandSyntaxException e) {
            context.getSource().sendFailure(Component.literal("Error adding owner"));
            OSUtils.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    public static int removeOwner(CommandContext<CommandSourceStack> context, String username){
        try {
            // Gets the parameters
            String dimension = DimensionArgument.getDimension(context, "dimension").dimension().location().toString();
            String name = StringArgumentType.getString(context, "name");

            // Finds the region
            RegionsModule.Region region = RegionsModule.getRegionByName(name, dimension);

            if(region == null) {
                context.getSource().sendFailure(Component.literal("Region not found"));
                return 0;
            }

            OSUtils.LOGGER.debug(username);
            OSUtils.LOGGER.debug(region.owners.toString());

            // Check if owner is already out of the list
            if (!region.owners.contains(username)) {
                context.getSource().sendFailure(Component.literal(String.format("Owner '%s' was not in the list.", username)));
                return 0;
            }

            // Remove region from the array
            region.owners.remove(username);

            // And replaces the region with the new one
            RegionsModule.Store.replaceRegionAndSave(name, dimension, region);
            context.getSource().sendSuccess(() ->
                            Component.literal(String.format("Owner '%s' removed.", username)),
                    true
            );
            return 1;
        } catch (CommandSyntaxException e) {
            context.getSource().sendFailure(Component.literal("Error removing owner"));
            OSUtils.LOGGER.error(e.getMessage());
            return 0;
        }
    }

    public static int getOwners(CommandContext<CommandSourceStack> context){
        try {
            // Gets the parameters
            String dimension = DimensionArgument.getDimension(context, "dimension").dimension().location().toString();
            String name = StringArgumentType.getString(context, "name");

            // Finds the region
            RegionsModule.Region region = RegionsModule.getRegionByName(name, dimension);

            if(region == null) {
                context.getSource().sendFailure(Component.literal("Region not found"));
                return 0;
            }

            context.getSource().sendSuccess(() ->
                Component.literal(String.format("Owners of region '%s': %s", name, String.join(", ", region.owners))),
                false
            );
            return 1;

        } catch (CommandSyntaxException e) {
            context.getSource().sendFailure(Component.literal("Error getting owners"));
            OSUtils.LOGGER.error(e.getMessage());
            return 0;
        }
    }
}
