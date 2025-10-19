package omi.serverutils.osutils.commands.region;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.modules.RegionsModule;

import java.util.ArrayList;
import java.util.HashMap;

public class ListRegionsSubCommand {
    public static int listRegions(CommandContext<CommandSourceStack> context, ServerLevel dimension) {
        String dimensionName = dimension != null ? dimension.dimension().location().toString() : "all dimensions";
        ArrayList<String> regions;
        ArrayList<RegionsModule.Region> fullRegions;

        if (dimension != null) {
            regions = RegionsModule.getRegionsNamesInDimension(dimensionName);
            fullRegions = RegionsModule.getRegionsInDimensionAsArray(dimensionName);

        } else {
            regions = new ArrayList<>();
            fullRegions = RegionsModule.getAllRegions();

            // Get regions from all dimensions
            for (String dim : RegionsModule.dimensionRegions.keySet()) {
                regions.addAll(RegionsModule.getRegionsNamesInDimension(dim));
            }
        }

        if (regions.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("No regions found in " + dimensionName), false);
            return 0;
        }

        context.getSource().sendSuccess(() ->
                RegionOutputCommandUtility.getRegionList(fullRegions, dimensionName),
                false
        );
        return 1;
    }
}
