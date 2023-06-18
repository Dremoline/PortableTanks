package com.dremoline.portabletanks.compatibility;

import com.dremoline.portabletanks.PortableTankBlockEntity;
import com.supermartijn642.core.TextComponents;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
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
        public String getID() {
            return new ResourceLocation("portabletanks", "portable_tank_component").toString();
        }

        @Override
        public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World level, BlockState blockState, IProbeHitData probeHitData) {
            TileEntity entity = level.getBlockEntity(probeHitData.getPos());
            if (entity instanceof PortableTankBlockEntity) {
                int capacity = ((PortableTankBlockEntity) entity).getTankType().tankCapacity.get();
                FluidStack contents = ((PortableTankBlockEntity) entity).getTankContent();
                ITextComponent capacityText = TextComponents.number(capacity).color(TextFormatting.GOLD).get();
                if (contents.isEmpty()) {
                    ITextComponent text = TextComponents.translation("portabletanks.portable_tank.info.capacity", capacityText).get();
                    probeInfo.vertical().text(text);
                } else {
                    ITextComponent amountText = TextComponents.number(contents.getAmount()).color(TextFormatting.GOLD).get();
                    ITextComponent fluidName = TextComponents.fluidStack(contents).color(TextFormatting.GOLD).get();
                    ITextComponent text = TextComponents.translation("portabletanks.portable_tank.info.stored", fluidName, amountText, capacityText).get();
                    probeInfo.vertical().text(text);
                }
            }
        }
    }
}
