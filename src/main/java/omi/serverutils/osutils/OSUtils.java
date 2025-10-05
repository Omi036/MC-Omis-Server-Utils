package omi.serverutils.osutils;

import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import omi.serverutils.osutils.events.*;
import omi.serverutils.osutils.modules.ConfigModule;
import omi.serverutils.osutils.modules.SequenceModule;
import org.slf4j.Logger;

import java.io.File;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(OSUtils.MODID)
public class OSUtils {

    public static final String MODID = "osutils";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static MinecraftServer Server;
    public static int Clock = 0;

    public static File sequencesDirectory;
    public static File dataDirectory;

    public OSUtils() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::serverSetup);

        ConfigModule.registerConfigFiles();
    }

    private void setup(final FMLCommonSetupEvent event) {

        LOGGER.info("Registering runtime events...");

        MinecraftForge.EVENT_BUS.register(new PlayerJoinEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerLeftEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerDeathEventHandler());
        MinecraftForge.EVENT_BUS.register(new ServerChatEventHandler());
        MinecraftForge.EVENT_BUS.register(new BlockBreakEventHandler());
        MinecraftForge.EVENT_BUS.register(new BlockPlaceEventHandler());
        MinecraftForge.EVENT_BUS.register(new DetonateEventHandler());
        MinecraftForge.EVENT_BUS.register(new CommandRegisterEventHandler());
        MinecraftForge.EVENT_BUS.register(new ClockTickEventHandler());

        LOGGER.info("Events registered successfully");
    }

    private void serverSetup(ServerStartingEvent event) {
        LOGGER.info("Server starting");
        OSUtils.Server = event.getServer();

        // Create mod folders
        File serverDirectory = OSUtils.Server.getServerDirectory();
        sequencesDirectory = new File(serverDirectory, "osutils/sequences/");
        dataDirectory = new File(serverDirectory, "osutils/data/");

        if(!sequencesDirectory.exists()) sequencesDirectory.mkdirs();
        if(!dataDirectory.exists()) dataDirectory.mkdirs();


        SequenceModule.loadSequences(sequencesDirectory);
    }
}
