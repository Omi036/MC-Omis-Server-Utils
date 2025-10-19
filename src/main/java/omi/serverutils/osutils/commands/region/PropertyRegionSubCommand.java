package omi.serverutils.osutils.commands.region;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.network.chat.Component;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.modules.RegionsModule;

public class PropertyRegionSubCommand {
    public static int manageProperty(CommandContext<CommandSourceStack> context, String property, boolean setMode) {
        try {
            String dimension = DimensionArgument.getDimension(context, "dimension").dimension().location().toString();
            String name = StringArgumentType.getString(context, "name");

            if (!RegionCommand.PROPERTY_NAMES.containsKey(property)) {
                context.getSource().sendFailure(Component.literal("Invalid property. Valid properties: " + String.join(", ", RegionCommand.PROPERTY_NAMES.keySet())));
                return 0;
            }

            RegionsModule.Region region = RegionsModule.getRegionByName(name, dimension);
            if (region == null) {
                context.getSource().sendFailure(Component.literal("Region not found"));
                return 0;
            }

            if(setMode) {
                boolean value = BoolArgumentType.getBool(context, "value");

                region.properties.put(property, value);

                OSUtils.LOGGER.debug("Saving");
                RegionsModule.Store.replaceRegionAndSave(name, dimension, region);
                context.getSource().sendSuccess(() ->
                    Component.literal(String.format("Set property '%s' to '%s' for region '%s'", property, value, name)),
                    true
                );

            } else {
                // Get property value
                String value = String.valueOf(region.properties.getOrDefault(property, false));


                context.getSource().sendSuccess(() ->
                    Component.literal(String.format("%s.%s = %s", name, property, value)),
                    false
                );
            }
            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error: " + e.getMessage()));
            return 0;
        }
    }
}
