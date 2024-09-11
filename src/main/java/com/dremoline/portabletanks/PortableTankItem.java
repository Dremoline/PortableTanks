package com.dremoline.portabletanks;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.item.BaseBlockItem;
import com.supermartijn642.core.item.ItemProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

/**
 * Created 7/19/2021 by SuperMartijn642
 */
public class PortableTankItem extends BaseBlockItem {

    private final PortableTankType type;

    public PortableTankItem(PortableTankType type) {
        super(type.getBlock(), ItemProperties.create().group(PortableTanks.GROUP));
        this.type = type;
    }

    @Override
    protected void appendItemInformation(ItemStack stack, Consumer<Component> info, boolean advanced) {
        FluidStack fluidStack = FluidStack.EMPTY;
        CompoundTag tag = stack.get(BaseBlock.TILE_DATA);
        if (tag != null)
            fluidStack = FluidStack.loadFluidStackFromNBT(tag.getCompound("fluid"));
        Component capacity = TextComponents.string(Integer.toString(this.type.tankCapacity.get())).color(ChatFormatting.GOLD).get();
        if (fluidStack.isEmpty())
            info.accept(TextComponents.translation("portabletanks.portable_tank.info.capacty", capacity).color(ChatFormatting.GRAY).get());
        else {
            Component fluidName = TextComponents.fromTextComponent(fluidStack.getDisplayName()).color(ChatFormatting.GOLD).get();
            Component amount = TextComponents.string(Integer.toString(fluidStack.getAmount())).color(ChatFormatting.GOLD).get();
            info.accept(TextComponents.translation("portabletanks.portable_tank.info.stored", fluidName, amount, capacity).color(ChatFormatting.GRAY).get());
        }
        super.appendItemInformation(stack, info, advanced);
    }
}
