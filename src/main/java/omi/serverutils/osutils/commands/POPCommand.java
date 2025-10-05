package omi.serverutils.osutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.config.POPConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class POPCommand {
    public static final String COMMAND_NAME = "pop";
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal(COMMAND_NAME)
                .then(Commands.argument("password", StringArgumentType.string())
                    .executes(POPCommand::execute)
                )
        );
    }

    public static int execute(CommandContext<CommandSourceStack> context) {
        String password = StringArgumentType.getString(context, "password");
        ServerPlayer player = context.getSource().getPlayer();

        // See if its executed by command block or server console
        if(player == null){
            context.getSource().sendFailure(Component.literal("Only players can execute this command"));
            return 0;
        }

        String playerIP = player.getIpAddress();
        String playerName = player.getDisplayName().getString();

        boolean namelistEnabled = !POPConfig.popAllowedNames.isEmpty();
        boolean iplistEnabled = !POPConfig.popAllowedIps.isEmpty();

        // See if player is already op
        if(OSUtils.Server.getPlayerList().isOp(player.getGameProfile())) {
            context.getSource().sendFailure(Component.literal("You are already OP"));
            return 0;
        }

        // See if player name is in the Name whitelist
        if(namelistEnabled && !POPConfig.popAllowedNames.contains(playerName)){
            context.getSource().sendFailure(Component.literal("Forbidden, you are not in the OP whitelist"));
            return 0;
        }

        // See if player ip is in the IP whitelist
        if(iplistEnabled && !POPConfig.popAllowedIps.contains(playerIP)) {
            context.getSource().sendFailure(Component.literal("Forbidden, you are not in the OP whitelist"));
            return 0;
        }

        // See if the password is correct
        if(!POPConfig.popPassword.equals(password)){
            context.getSource().sendFailure(Component.literal("Forbidden, invalid password"));
            return 0;
        }

        // Make user op
        OSUtils.Server.getPlayerList().op(player.getGameProfile());

        context.getSource().sendSuccess(() ->
            Component.literal("Success, you are now OP"), false);
        return 1;
    }
}
