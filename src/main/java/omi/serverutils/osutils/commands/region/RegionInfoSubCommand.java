package omi.serverutils.osutils.commands.region;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import omi.serverutils.osutils.modules.RegionsModule;

import java.util.ArrayList;

public class RegionInfoSubCommand {
    public static int infoRegion(CommandContext<CommandSourceStack> context, String dimension, String regionName) {

        RegionsModule.Region selectedRegion = RegionsModule.getRegionByName(regionName, dimension);

        if (selectedRegion == null) {
            context.getSource().sendSuccess(() -> Component.literal("No region found with name " + regionName), false);
            return 0;
        }

        context.getSource().sendSuccess(() ->
                RegionOutputCommandUtility.getRegionInfo(selectedRegion),
                false
        );
        return 1;
    }
}
