package com.dremoline.portabletanks.compatibility;

import com.dremoline.portabletanks.PortableTankBlock;
import com.dremoline.portabletanks.PortableTankTileEntity;
import com.supermartijn642.core.TextComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin("portabletanks")
public class PortableTanksWailaPlugin implements IWailaPlugin, IBlockComponentProvider {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(this, PortableTankBlock.class);
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BlockEntity entity = blockAccessor.getBlockEntity();
        if (entity instanceof PortableTankTileEntity) {
            int capacity = ((PortableTankTileEntity) entity).getTankType().tankCapacity.get();
            FluidStack contents = ((PortableTankTileEntity) entity).getTankContent();
            Component capacityText = TextComponents.number(capacity).color(ChatFormatting.GOLD).get();
            if (contents.isEmpty()) {
                Component text = TextComponents.translation("portabletanks.portable_tank.info.capacity", capacityText).get();
                tooltip.add(text);
            } else {
                Component amountText = TextComponents.number(contents.getAmount()).color(ChatFormatting.GOLD).get();
                Component fluidName = TextComponents.fluidStack(contents).color(ChatFormatting.GOLD).get();
                Component text = TextComponents.translation("portabletanks.portable_tank.info.stored", fluidName, amountText, capacityText).get();
                tooltip.add(text);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation("portabletanks", "portable_tank_component");
    }
}
