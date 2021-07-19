package com.dremoline.portabletanks;

import com.supermartijn642.core.TextComponents;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created 7/19/2021 by SuperMartijn642
 */
public class PortableTankItem extends BlockItem {

    private final PortableTankType type;

    public PortableTankItem(PortableTankType type){
        super(type.getBlock(), new Properties().tab(PortableTanks.GROUP).setISTER(() -> () -> PortableTankItemStackRenderer.INSTANCE));
        this.setRegistryName(type.getRegistryName());
        this.type = type;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag advanced){
        FluidStack fluidStack = FluidStack.EMPTY;
        if(stack.getOrCreateTag().contains("tileData"))
            fluidStack = FluidStack.loadFluidStackFromNBT(stack.getOrCreateTag().getCompound("tileData").getCompound("fluid"));
        ITextComponent capacity = TextComponents.string(Integer.toString(this.type.tankCapacity.get())).color(TextFormatting.GOLD).get();
        if(fluidStack.isEmpty())
            list.add(TextComponents.translation("portabletanks.portable_tank.info.capacity", capacity).color(TextFormatting.GRAY).get());
        else{
            ITextComponent fluidName = TextComponents.fromTextComponent(fluidStack.getDisplayName()).color(TextFormatting.GOLD).get();
            ITextComponent amount = TextComponents.string(Integer.toString(fluidStack.getAmount())).color(TextFormatting.GOLD).get();
            list.add(TextComponents.translation("portabletanks.portable_tank.info.stored", fluidName, amount, capacity).color(TextFormatting.GRAY).get());
        }
        super.appendHoverText(stack, world, list, advanced);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt){
        return new ItemFluidHandler(stack, this.type);
    }

    public static class ItemFluidHandler implements ICapabilityProvider, IFluidHandlerItem {

        private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);

        private final ItemStack stack;
        private final PortableTankType type;

        public ItemFluidHandler(ItemStack stack, PortableTankType type){
            this.stack = stack;
            this.type = type;
        }

        @Override
        public int getTanks(){
            return 1;
        }

        @Nonnull
        @Override
        public FluidStack getFluidInTank(int tank){
            return this.getFluid().copy();
        }

        @Override
        public int getTankCapacity(int tank){
            return this.type.tankCapacity.get();
        }

        @Override
        public boolean isFluidValid(int tank, @Nonnull FluidStack stack){
            return true;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action){
            if(resource == null || resource.isEmpty())
                return 0;
            FluidStack current = this.getFluid();
            if(!current.isEmpty() && !current.isFluidEqual(resource))
                return 0;
            int amount = Math.min(resource.getAmount(), this.getTankCapacity(0) - current.getAmount());
            if(action.execute()){
                FluidStack newStack = resource.copy();
                newStack.setAmount(current.getAmount() + amount);
                this.setFluid(newStack);
            }
            return amount;
        }

        @Nonnull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action){
            if(resource == null || resource.isEmpty())
                return FluidStack.EMPTY;
            FluidStack current = this.getFluid();
            if(!current.isFluidEqual(resource))
                return FluidStack.EMPTY;
            int amount = Math.min(current.getAmount(), resource.getAmount());
            if(action.execute()){
                FluidStack newStack = current.copy();
                newStack.shrink(amount);
                this.setFluid(newStack);
            }
            current.setAmount(amount);
            return current;
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action){
            if(maxDrain == 0)
                return FluidStack.EMPTY;
            FluidStack current = this.getFluid();
            int amount = Math.min(current.getAmount(), maxDrain);
            if(action.execute()){
                FluidStack newStack = current.copy();
                newStack.shrink(amount);
                this.setFluid(newStack);
            }
            current.setAmount(amount);
            return current;
        }

        @Nonnull
        @Override
        public ItemStack getContainer(){
            return this.stack;
        }

        @Override
        @Nonnull
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing){
            return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.orEmpty(capability, this.holder);
        }

        private FluidStack getFluid(){
            CompoundNBT compound = this.stack.getOrCreateTag().getCompound("tileData");
            return compound.contains("fluid") ? FluidStack.loadFluidStackFromNBT(compound.getCompound("fluid")) : FluidStack.EMPTY;
        }

        private void setFluid(FluidStack fluid){
            CompoundNBT tileData = this.stack.getOrCreateTag().getCompound("tileData");
            tileData.put("fluid", fluid.writeToNBT(new CompoundNBT()));
            this.stack.getOrCreateTag().put("tileData", tileData);
        }
    }
}
