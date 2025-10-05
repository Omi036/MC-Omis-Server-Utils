package omi.serverutils.osutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.modules.SequenceModule;

public class SequenceCommand {
    public static final String COMMAND_NAME = "sequence";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal(COMMAND_NAME)
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("run")
                    .then(Commands.argument("sequence_name", StringArgumentType.string())
                        .suggests((context, builder) -> {
                            SequenceModule.sequences.keySet().forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(SequenceCommand::executeRun)
                    )
                )
                .then(Commands.literal("list")
                    .executes(SequenceCommand::executeList)
                )
                .then(Commands.literal("reload")
                    .requires(source -> source.hasPermission(2)) // Requires OP level 2
                    .executes(SequenceCommand::executeReload)
                )
        );
    }

    private static int executeRun(CommandContext<CommandSourceStack> context) {
        String sequenceName = StringArgumentType.getString(context, "sequence_name");
        
        if (SequenceModule.sequences.containsKey(sequenceName)) {
            SequenceModule.runSequence(sequenceName);
            context.getSource().sendSuccess(() -> Component.literal("Started sequence: " + sequenceName), true);
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("Sequence not found: " + sequenceName));
            return 0;
        }
    }

    private static int executeList(CommandContext<CommandSourceStack> context) {
        if (SequenceModule.sequences.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("No sequences available"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.literal("Available sequences:"), false);
            SequenceModule.sequences.keySet().forEach(sequence -> 
                context.getSource().sendSuccess(() -> Component.literal("- " + sequence), false)
            );
        }
        return 1;
    }

    private static int executeReload(CommandContext<CommandSourceStack> context) {
        boolean success = SequenceModule.loadSequences(OSUtils.sequencesDirectory);
        if (success) {
            context.getSource().sendSuccess(() -> Component.literal("Reloaded " + SequenceModule.sequences.size() + " sequences"), true);
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("Failed to reload sequences. Check server logs for details."));
            return 0;
        }
    }
}
