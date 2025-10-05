package omi.serverutils.osutils.modules;

import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import omi.serverutils.osutils.OSUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class SequenceModule {

    public static HashMap<String, List<String>> sequences = new HashMap<>();
    private static final List<SequenceExecutor> activeSequences = new ArrayList<>();


    // Loads all the sequences and statements into the module memory
    public static boolean loadSequences(File sequenceFolder) {
        sequences.clear();

        // Load each sequence
        for(File sequence : Objects.requireNonNull(sequenceFolder.listFiles())){

            // Check if file is valid
            if(sequence.isFile() && sequence.getName().endsWith(".seq")) {
                try{

                    // Add statements to the sequences map
                    List<String> statements = Files.readAllLines(Path.of(sequence.getAbsolutePath()));
                    sequences.put(sequence.getName().replace(".seq", ""), statements);

                } catch (Exception e) {
                    OSUtils.LOGGER.error("Error while reading sequence: " + sequence.getAbsolutePath());
                    OSUtils.LOGGER.error(e.getMessage());
                }
            }
        }
        return true;
    }


    // Retrieves each statement from the sequence
    public static List<String> getSequenceStatements(String sequenceName) {
        return sequences.get(sequenceName);
    }



    // Gets all the statements and creates an executor for it
    public static void runSequence(String sequenceName) {
        Queue<ScheduledCommand> sequenceQueue = prepareSequence(sequenceName);
        activeSequences.add(new SequenceExecutor(sequenceQueue));
    }


    // Load each statement into a ScheduledCommand queue with delays
    public static Queue<ScheduledCommand> prepareSequence(String sequenceName){
        List<String> statements = getSequenceStatements(sequenceName);
        Queue<ScheduledCommand> sequenceQueue = new LinkedBlockingQueue<>();
        int currentDelay = 0;

        for(String statement: statements) {
            statement = statement.trim();

            // wait directive
            if(statement.startsWith("wait")) {
                try {
                    int delay = Integer.parseInt(statement.split(" ")[1]);
                    currentDelay += delay;
                } catch (Exception e) {
                    OSUtils.LOGGER.error("Invalid wait delay: {}", statement);
                    break;
                }
            } else {
                sequenceQueue.add(new ScheduledCommand(currentDelay, statement));
                currentDelay = 0;
            }
        }

        return sequenceQueue;
    }


    // A scheduled command is a command that waits for x tick to execute
    public static class ScheduledCommand{
        private final int executionTick;
        private final String command;

        public ScheduledCommand(int delayTicks, String command){
            this.executionTick = delayTicks + OSUtils.Clock;
            this.command = command;
        }

        public boolean shouldExecute(){
            return OSUtils.Clock >= this.executionTick;
        }

        public void execute(){
            CommandSourceStack source = OSUtils.Server.createCommandSourceStack();
            OSUtils.Server.getCommands().performPrefixedCommand(source, this.command);
        }
    }



    // The sequence executor is created once per sequence run.
    // It listens for tickEvent (so it does not block the main thread)
    private static class SequenceExecutor {
        private final Queue<ScheduledCommand> queue;

        public SequenceExecutor(Queue<ScheduledCommand> queue) {
            this.queue = queue;
            MinecraftForge.EVENT_BUS.register(this);
        }

        @SubscribeEvent
        public void onServerTick(TickEvent.ServerTickEvent event) {
            if (event.phase == TickEvent.Phase.END && !queue.isEmpty()) {
                // See if it should wait for the next command (delay)
                if (queue.peek().shouldExecute()) {
                    ScheduledCommand command = queue.poll();
                    command.execute();

                    // See if it was the last command
                    if (queue.isEmpty()) {
                        MinecraftForge.EVENT_BUS.unregister(this);
                        activeSequences.remove(this);
                    }
                }
            }
        }
    }
}