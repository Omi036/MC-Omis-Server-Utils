package omi.serverutils.osutils.events;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.config.GeneralConfig;
import omi.serverutils.osutils.modules.RegionsModule;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DetonateEventHandler {

    @SubscribeEvent
    public void onDetonate(ExplosionEvent.Detonate event) {

        // If regions are not used, ignore the event
        if(!GeneralConfig.regionsEnabled) return;

        String dimension = event.getLevel().dimension().location().toString();

        if(!RegionsModule.dimensionIsRegistered(dimension)) return;

        // If explosion is inside of the region
        Vec3 explosionPos = event.getExplosion().getPosition();
        if (RegionsModule.getRegionsInDimensionAsArray(dimension).stream()
                .filter(region -> region.enabled)
                .filter(region -> !region.properties.get("allowExplosions"))
                .anyMatch(region -> region.isPositionInside(explosionPos))) {
            event.getExplosion().clearToBlow();
            return;
        }

        event.getAffectedBlocks().removeIf(blockPos ->
            RegionsModule.getRegionsInDimensionAsArray(dimension).stream()
                .filter(region -> region.enabled)
                .filter(region -> !region.properties.get("allowExplosions"))
                .anyMatch(region ->
                        region.isPositionInside(new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                )
        );
    }

}
