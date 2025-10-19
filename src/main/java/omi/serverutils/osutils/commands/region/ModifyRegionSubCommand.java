package omi.serverutils.osutils.commands.region;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import omi.serverutils.osutils.modules.RegionsModule;

public class ModifyRegionSubCommand {
    public static int modifyRegion(CommandContext<CommandSourceStack> context, String property, Object value) {
        try {
            String dimension = DimensionArgument.getDimension(context, "dimension").dimension().location().toString();
            String name = StringArgumentType.getString(context, "name");

            RegionsModule.Region region = RegionsModule.getRegionByName(name, dimension);
            if (region.empty) {
                context.getSource().sendFailure(Component.literal("Region not found"));
                return 0;
            }

            switch (property.toLowerCase()) {
                case "name":
                    String stringValue = (String) value;
                    if(!stringValue.matches("^[a-zA-Z0-9_]*$")){
                        context.getSource().sendFailure(Component.literal("Invalid region name, only letters, numbers and underscores are allowed"));
                        return 0;
                    }

                    region.name = stringValue;
                    break;
                case "enabled":
                    region.enabled = (Boolean) value;
                    break;
                case "dimension":
                    region.dimension = (String) value;
                    break;
                case "startpos":
                    BlockPos startPos = (BlockPos) value;
                    region.startPos = new net.minecraft.world.phys.Vec3(startPos.getX(), startPos.getY(), startPos.getZ());
                    break;
                case "endpos":
                    BlockPos endPos = (BlockPos) value;
                    region.endPos = new net.minecraft.world.phys.Vec3(endPos.getX(), endPos.getY(), endPos.getZ());
                    break;
                default:
                    context.getSource().sendFailure(Component.literal("Invalid property. Use: name, dimension, startPos, or endPos"));
                    return 0;
            }

            if(property.toLowerCase().equals("dimension")){
                RegionsModule.Store.removeRegion(name, dimension);
                RegionsModule.Store.uploadRegion(region);
            } else {
                RegionsModule.Store.replaceRegionAndSave(name, dimension, region);
            }


            context.getSource().sendSuccess(() ->
                            Component.literal(String.format("Updated %s for region '%s'", property, name)),
                    true
            );
            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Failed to modify region: " + e.getMessage()));
            return 0;
        }
    }
}
