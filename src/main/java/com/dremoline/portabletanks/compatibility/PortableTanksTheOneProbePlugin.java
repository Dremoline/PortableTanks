package com.dremoline.portabletanks.compatibility;

import com.dremoline.portabletanks.PortableTankBlockEntity;
import com.supermartijn642.core.TextComponents;
import mcjty.theoneprobe.api.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

import java.util.function.Function;

public class PortableTanksTheOneProbePlugin {
    public static void interModEnqueue(InterModEnqueueEvent e) {
        InterModComms.sendTo("theoneprobe", "getTheOneProbe", () -> new ProbeInfoProvider());
    }

    public static class ProbeInfoProvider implements IProbeInfoProvider, Function<ITheOneProbe, Void> {

        @Override
        public Void apply(ITheOneProbe theOneProbe) {
            theOneProbe.registerProvider(new ProbeInfoProvider());
            return null;
        }

        @Override
        public ResourceLocation getID() {
            return new ResourceLocation("portabletanks", "portable_tank_component");
        }

        @Override
        public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, BlockState blockState, IProbeHitData probeHitData) {
            BlockEntity entity = level.getBlockEntity(probeHitData.getPos());
            if (entity instanceof PortableTankBlockEntity) {
                int capacity = ((PortableTankBlockEntity) entity).getTankType().tankCapacity.get();
                FluidStack contents = ((PortableTankBlockEntity) entity).getTankContent();
                Component capacityText = TextComponents.number(capacity).color(ChatFormatting.GOLD).get();
                if (contents.isEmpty()) {
                    Component text = TextComponents.translation("portabletanks.portable_tank.info.capacity", capacityText).get();
                    probeInfo.vertical().text(text);
                } else {
                    Component amountText = TextComponents.number(contents.getAmount()).color(ChatFormatting.GOLD).get();
                    Component fluidName = TextComponents.fluidStack(contents).color(ChatFormatting.GOLD).get();
                    Component text = TextComponents.translation("portabletanks.portable_tank.info.stored", fluidName, amountText, capacityText).get();
                    probeInfo.vertical().text(text);
                }
            }
        }
    }
}
