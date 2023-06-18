package com.dremoline.portabletanks.compatibility;

import com.dremoline.portabletanks.PortableTankBlock;
import com.dremoline.portabletanks.PortableTankBlockEntity;
import com.supermartijn642.core.TextComponents;
import mcp.mobius.waila.api.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

@WailaPlugin("portabletanks")
public class PortableTanksWailaPlugin implements IWailaPlugin, IComponentProvider {
    @Override
    public void register(IRegistrar registration) {
        registration.registerComponentProvider(this, TooltipPosition.BODY, PortableTankBlock.class);
    }

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig iPluginConfig) {
        TileEntity entity = accessor.getTileEntity();
        if (entity instanceof PortableTankBlockEntity) {
            int capacity = ((PortableTankBlockEntity) entity).getTankType().tankCapacity.get();
            FluidStack contents = ((PortableTankBlockEntity) entity).getTankContent();
            ITextComponent capacityText = TextComponents.number(capacity).color(TextFormatting.GOLD).get();
            if (contents.isEmpty()) {
                ITextComponent text = TextComponents.translation("portabletanks.portable_tank.info.capacity", capacityText).get();
                tooltip.add(text);
            } else {
                ITextComponent amountText = TextComponents.number(contents.getAmount()).color(TextFormatting.GOLD).get();
                ITextComponent fluidName = TextComponents.fluidStack(contents).color(TextFormatting.GOLD).get();
                ITextComponent text = TextComponents.translation("portabletanks.portable_tank.info.stored", fluidName, amountText, capacityText).get();
                tooltip.add(text);
            }
        }
    }
}
