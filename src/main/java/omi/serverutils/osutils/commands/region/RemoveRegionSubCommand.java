package omi.serverutils.osutils.commands.region;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.network.chat.Component;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.modules.RegionsModule;

public class RemoveRegionSubCommand {
    public static int removeRegion(CommandContext<CommandSourceStack> context) {
        try {
            String dimension = DimensionArgument.getDimension(context, "dimension").dimension().location().toString();
            String name = StringArgumentType.getString(context, "name");

            if(RegionsModule.getRegionByName(name, dimension) == null){
                context.getSource().sendFailure(Component.literal("Region does not exist"));
                return 0;
            }

            RegionsModule.Store.removeRegion(name, dimension);
            context.getSource().sendSuccess(() ->
                            Component.literal(String.format("Region '%s' removed from %s", name, dimension)),
                    true
            );
            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Failed to remove region"));
            OSUtils.LOGGER.error(e.getMessage());
            return 0;
        }
    }
}
