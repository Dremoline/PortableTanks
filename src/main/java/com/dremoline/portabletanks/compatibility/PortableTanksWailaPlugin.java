package com.dremoline.portabletanks.compatibility;

import com.dremoline.portabletanks.PortableTankBlock;
import com.dremoline.portabletanks.PortableTankBlockEntity;
import com.supermartijn642.core.TextComponents;
import mcp.mobius.waila.api.*;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

@WailaPlugin("portabletanks")
public class PortableTanksWailaPlugin implements IWailaPlugin, IComponentProvider {
    @Override
    public void register(IRegistrar registration) {
        registration.registerComponentProvider(this, TooltipPosition.BODY, PortableTankBlock.class);
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BlockEntity entity = blockAccessor.getBlockEntity();
        if (entity instanceof PortableTankBlockEntity) {
            int capacity = ((PortableTankBlockEntity) entity).getTankType().tankCapacity.get();
            FluidStack contents = ((PortableTankBlockEntity) entity).getTankContent();
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
}
