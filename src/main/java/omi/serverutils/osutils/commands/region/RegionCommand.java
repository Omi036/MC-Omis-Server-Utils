package omi.serverutils.osutils.commands.region;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import org.checkerframework.checker.units.qual.C;

import java.util.Map;

public class RegionCommand {
    public static final String COMMAND_NAME = "region";
    public static final Map<String, String> PROPERTY_NAMES = Map.of(
            "allowBreak", "allowBreak",
            "allowPlace", "allowPlace",
            "allowInteract", "allowInteract",
            "allowExplosions", "allowExplosions"
    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // /region list [dimension]
        LiteralArgumentBuilder<CommandSourceStack> listCommand = Commands.literal("list")
                .executes(context -> ListRegionsSubCommand.listRegions(context, null))
                .then(Commands.argument("dimension", DimensionArgument.dimension())
                        .executes(context -> ListRegionsSubCommand.listRegions(context, DimensionArgument.getDimension(context, "dimension"))));

        // /region create <name> <startPos> <endPos>
        LiteralArgumentBuilder<CommandSourceStack> createCommand = Commands.literal("create")
                .then(Commands.argument("name", StringArgumentType.string())
                        .then(Commands.argument("startPos", BlockPosArgument.blockPos())
                                .then(Commands.argument("endPos", BlockPosArgument.blockPos())
                                        .executes(CreateRegionSubCommand::createRegion))));

        // /region remove <dimension> <name>
        LiteralArgumentBuilder<CommandSourceStack> removeCommand = Commands.literal("remove")
                .then(Commands.argument("dimension", DimensionArgument.dimension())
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(RemoveRegionSubCommand::removeRegion)));

        // /region modify <dimension> <name> <property> <value>
        LiteralArgumentBuilder<CommandSourceStack> modifyCommand = Commands.literal("modify")
                .then(Commands.argument("dimension", DimensionArgument.dimension())
                        .then(Commands.argument("name", StringArgumentType.string())
                                .then(Commands.literal("name")
                                        .then(Commands.argument("newName", StringArgumentType.string())
                                                .executes(context -> ModifyRegionSubCommand.modifyRegion(context, "name", StringArgumentType.getString(context, "newName")))))
                                .then(Commands.literal("enabled")
                                        .then(Commands.argument("state", BoolArgumentType.bool())
                                                .executes(context -> ModifyRegionSubCommand.modifyRegion(context, "enabled", BoolArgumentType.getBool(context, "state")))))
                                .then(Commands.literal("dimension")
                                        .then(Commands.argument("newDimension", DimensionArgument.dimension())
                                                .executes(context -> ModifyRegionSubCommand.modifyRegion(context, "dimension", DimensionArgument.getDimension(context, "newDimension").dimension().location().toString()))))
                                .then(Commands.literal("startpos")
                                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                                .executes(context -> ModifyRegionSubCommand.modifyRegion(context, "startpos", BlockPosArgument.getLoadedBlockPos(context, "pos")))))
                                .then(Commands.literal("endpos")
                                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                                .executes(context -> ModifyRegionSubCommand.modifyRegion(context, "endpos", BlockPosArgument.getLoadedBlockPos(context, "pos")))))));

        // /region reload
        LiteralArgumentBuilder<CommandSourceStack> reloadCommand = Commands.literal("reload")
                .executes(RegionReloadSubCommand::reloadRegions);

        // /region info <dimension> <name>
        LiteralArgumentBuilder<CommandSourceStack> infoCommand = Commands.literal("info")
                .then(Commands.argument("dimension", DimensionArgument.dimension())
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes((context) -> {
                                    String dimensionName = DimensionArgument.getDimension(context, "dimension").dimension().location().toString();
                                    String regionName = StringArgumentType.getString(context, "name");
                                    return RegionInfoSubCommand.infoRegion(context, dimensionName, regionName);
                                })));

        // /region property <set|get> <dimension> <name> <property> [value]
        LiteralArgumentBuilder<CommandSourceStack> propertyCommand = Commands.literal("property")
                .then(Commands.literal("set")
                        .then(Commands.argument("dimension", DimensionArgument.dimension())
                                .then(Commands.argument("name", StringArgumentType.string())
                                        .then(Commands.literal("owners")
                                                .then(Commands.literal("add")
                                                        .then(Commands.argument("playerName", StringArgumentType.string())
                                                                .executes(context -> PropertyOwnerRegionSubCommand.addOwner(context, StringArgumentType.getString(context, "playerName")))))
                                                .then(Commands.literal("remove")
                                                        .then(Commands.argument("playerName", StringArgumentType.string())
                                                                .executes(context -> PropertyOwnerRegionSubCommand.removeOwner(context,  StringArgumentType.getString(context, "playerName"))))))

                                        .then(Commands.argument("property", StringArgumentType.string())
                                                .suggests((context, builder) -> {
                                                    PROPERTY_NAMES.keySet().forEach(builder::suggest);
                                                    return builder.buildFuture();
                                                })
                                                .then(Commands.argument("value", BoolArgumentType.bool())
                                                        .executes(context -> PropertyRegionSubCommand.manageProperty(context, StringArgumentType.getString(context, "property"), true)))))))

                .then(Commands.literal("get")
                        .then(Commands.argument("dimension", DimensionArgument.dimension())
                                .then(Commands.argument("name", StringArgumentType.string())
                                        .then(Commands.literal("owners")
                                                .executes(context -> PropertyOwnerRegionSubCommand.getOwners(context)))
                                        .then(Commands.argument("property", StringArgumentType.string())
                                                .suggests((context, builder) -> {
                                                    PROPERTY_NAMES.keySet().forEach(builder::suggest);
                                                    return builder.buildFuture();
                                                })
                                                .executes(context -> PropertyRegionSubCommand.manageProperty(context, StringArgumentType.getString(context, "property"), false))))));

        // Main command registration
        dispatcher.register(
                Commands.literal(COMMAND_NAME)
                        .requires(player -> player.hasPermission(2))
                        .then(infoCommand)
                        .then(reloadCommand)
                        .then(listCommand)
                        .then(createCommand)
                        .then(removeCommand)
                        .then(modifyCommand)
                        .then(propertyCommand)
        );
    }
}