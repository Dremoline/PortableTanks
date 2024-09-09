package com.dremoline.portabletanks;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.item.BaseBlockItem;
import com.supermartijn642.core.item.ItemProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nonnull;
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
            fluidStack = FluidStack.parseOptional(CommonUtils.getRegistryAccess(), tag.getCompound("fluid"));
        Component capacity = TextComponents.string(Integer.toString(this.type.tankCapacity.get())).color(ChatFormatting.GOLD).get();
        if (fluidStack.isEmpty())
            info.accept(TextComponents.translation("portabletanks.portable_tank.info.capacity", capacity).color(ChatFormatting.GRAY).get());
        else {
            Component fluidName = TextComponents.fromTextComponent(fluidStack.getHoverName()).color(ChatFormatting.GOLD).get();
            Component amount = TextComponents.string(Integer.toString(fluidStack.getAmount())).color(ChatFormatting.GOLD).get();
            info.accept(TextComponents.translation("portabletanks.portable_tank.info.stored", fluidName, amount, capacity).color(ChatFormatting.GRAY).get());
        }
        super.appendItemInformation(stack, info, advanced);
    }

    public static class ItemFluidHandler implements IFluidHandlerItem {
        private final ItemStack stack;
        private final PortableTankType type;

        public ItemFluidHandler(ItemStack stack, PortableTankType type) {
            this.stack = stack;
            this.type = type;
        }

        @Override
        public int getTanks() {
            return 1;
        }

        @Nonnull
        @Override
        public FluidStack getFluidInTank(int tank) {
            return this.getFluid().copy();
        }

        @Override
        public int getTankCapacity(int tank) {
            return this.type.tankCapacity.get();
        }

        @Override
        public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
            return true;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource == null || resource.isEmpty())
                return 0;
            FluidStack current = this.getFluid();
            if (!current.isEmpty() && !FluidStack.isSameFluidSameComponents(current, resource))
                return 0;
            int amount = Math.min(resource.getAmount(), this.getTankCapacity(0) - current.getAmount());
            if (action.execute()) {
                FluidStack newStack = resource.copy();
                newStack.setAmount(current.getAmount() + amount);
                this.setFluid(newStack);
            }
            return amount;
        }

        @Nonnull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (resource == null || resource.isEmpty())
                return FluidStack.EMPTY;
            FluidStack current = this.getFluid();
            if (current.isEmpty() || !FluidStack.isSameFluidSameComponents(current, resource))
                return FluidStack.EMPTY;
            int amount = Math.min(current.getAmount(), resource.getAmount());
            if (action.execute()) {
                FluidStack newStack = current.copy();
                newStack.shrink(amount);
                this.setFluid(newStack);
            }
            current.setAmount(amount);
            return current;
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            if (maxDrain == 0)
                return FluidStack.EMPTY;
            FluidStack current = this.getFluid();
            if (current.isEmpty())
                return FluidStack.EMPTY;
            int amount = Math.min(current.getAmount(), maxDrain);
            if (action.execute()) {
                FluidStack newStack = current.copy();
                newStack.shrink(amount);
                this.setFluid(newStack);
            }
            current.setAmount(amount);
            return current;
        }

        @Nonnull
        @Override
        public ItemStack getContainer() {
            return this.stack;
        }

        private FluidStack getFluid() {
            CompoundTag compound = this.stack.get(BaseBlock.TILE_DATA);
            return compound.contains("fluid") ? FluidStack.parseOptional(CommonUtils.getRegistryAccess(), compound.getCompound("fluid")) : FluidStack.EMPTY;
        }

        private void setFluid(FluidStack fluid) {
            CompoundTag tileData = this.stack.get(BaseBlock.TILE_DATA);
            if (tileData == null)
                tileData = new CompoundTag();
            tileData.put("fluid", fluid.saveOptional(CommonUtils.getRegistryAccess()));
            this.stack.set(BaseBlock.TILE_DATA, tileData);
        }
    }
}
