package omi.serverutils.osutils;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import omi.serverutils.events.*;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(OSUtils.MODID)
public class OSUtils {

    public static final String MODID = "osutils";
    public static int Clock = 0;
    private static final Logger LOGGER = LogUtils.getLogger();

    public OSUtils() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("Registering events");
        MinecraftForge.EVENT_BUS.register(new PlayerJoinEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerLeftEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerDeathEventHandler());
        MinecraftForge.EVENT_BUS.register(new ServerChatEventHandler());
        MinecraftForge.EVENT_BUS.register(new BlockBreakEventHandler());
        MinecraftForge.EVENT_BUS.register(new BlockPlaceEventHandler());
        MinecraftForge.EVENT_BUS.register(new DetonateEventHandler());
        MinecraftForge.EVENT_BUS.register(new ClockTickEventHandler());
        MinecraftForge.EVENT_BUS.register(new CommandRegisterEventHandler());
        LOGGER.info("Events registered successfully");

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
