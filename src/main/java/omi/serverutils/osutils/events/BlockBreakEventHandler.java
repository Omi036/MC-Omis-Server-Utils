package omi.serverutils.osutils.events;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.config.GeneralConfig;
import omi.serverutils.osutils.modules.RegionsModule;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BlockBreakEventHandler {

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {

        // If regions are not used, ignore the event
        if(!GeneralConfig.regionsEnabled) return;

        ServerPlayer player = (ServerPlayer) event.getPlayer();

        if(OSUtils.Server.getPlayerList().isOp(player.getGameProfile())) return;

        String dimension = player.level().dimension().location().toString();


        if(!RegionsModule.dimensionIsRegistered(dimension)) return;

        ArrayList<RegionsModule.Region> enabledRegions = RegionsModule.getRegionsInDimensionAsArray(dimension).stream()
            .filter(region -> region.enabled)
            .filter(region -> !region.userIsOwner(player.getName().getString()))
            .filter(region -> !region.properties.get("allowBreak"))
            .filter(region -> region.isPositionInside( new Vec3(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ())))
            .collect(Collectors.toCollection(ArrayList::new));

        if(enabledRegions.isEmpty()) return;

        player.sendSystemMessage(
                Component.literal("You are not allowed to break blocks in this region.")
                        .withStyle(ChatFormatting.RED)
        );

        event.setCanceled(true);
    }

}
