package omi.serverutils.osutils.commands.region;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import omi.serverutils.osutils.modules.RegionsModule;

public class RegionReloadSubCommand {
    public static int reloadRegions(CommandContext<CommandSourceStack> context) {
        RegionsModule.loadRegions();
        context.getSource().sendSuccess(() -> Component.literal("Regions reloaded"), true);
        return 1;
    }
}
