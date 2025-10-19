package omi.serverutils.osutils.events;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.config.GeneralConfig;
import omi.serverutils.osutils.modules.RegionsModule;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BlockInteractEventHandler {

    @SubscribeEvent
    public void onBlockInteractEventHandler(PlayerInteractEvent.RightClickBlock event){
        // If regions are not used, ignore the event
        if(!GeneralConfig.regionsEnabled) return;

        // if entity is not a player, skip
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // If player is op, skip
        if(OSUtils.Server.getPlayerList().isOp(player.getGameProfile())) return;

        String dimension = player.level().dimension().location().toString();

        // If there isnt any region on the dimension, skip
        if(!RegionsModule.dimensionIsRegistered(dimension)) return;

        ArrayList<RegionsModule.Region> enabledRegions = RegionsModule.getRegionsInDimensionAsArray(dimension).stream()
                .filter(region -> region.enabled)
                .filter(region -> !region.userIsOwner(player.getName().getString()))
                .filter(region -> !region.properties.get("allowInteract"))
                .filter(region -> region.isPositionInside( new Vec3(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ())))
                .collect(Collectors.toCollection(ArrayList::new));

        // If no region affected is found, skip
        if(enabledRegions.isEmpty()) return;

        BlockPos blockpos = event.getPos();
        Block targetBlock = event.getLevel().getBlockState(blockpos).getBlock();

        if(targetBlock instanceof DoorBlock ||
            targetBlock instanceof ChestBlock ||
            targetBlock instanceof SignBlock ||
            targetBlock instanceof TrapDoorBlock ||
            targetBlock instanceof LeverBlock ||
            targetBlock instanceof ButtonBlock ||
            targetBlock instanceof PressurePlateBlock ||
            targetBlock instanceof BellBlock ||
            targetBlock instanceof CauldronBlock ||
            targetBlock instanceof BeaconBlock ||
            targetBlock instanceof FlowerPotBlock ||
            targetBlock instanceof LecternBlock ||
            targetBlock instanceof ShulkerBoxBlock ||
            targetBlock instanceof EnderChestBlock ||
            targetBlock instanceof BarrelBlock ||
            targetBlock instanceof RespawnAnchorBlock ||
            targetBlock instanceof BedBlock ||
            targetBlock instanceof EndGatewayBlock ||
            targetBlock instanceof CandleBlock ||
            targetBlock instanceof CampfireBlock ||
            targetBlock instanceof FurnaceBlock ||
            targetBlock instanceof BrewingStandBlock ||
            targetBlock instanceof SmokerBlock ||
            targetBlock instanceof BlastFurnaceBlock
        ) {
            player.sendSystemMessage(
                    Component.literal("You are not allowed to interact with blocks in this region.")
                            .withStyle(ChatFormatting.RED)
            );
            event.setCanceled(true);
        }
    }
}
