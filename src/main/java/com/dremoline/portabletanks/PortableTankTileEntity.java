package com.dremoline.portabletanks;

import com.supermartijn642.core.block.BaseTileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public class PortableTankTileEntity extends BaseTileEntity implements IFluidHandler {
    private final PortableTankType type;
    private FluidStack fluidStack = FluidStack.EMPTY;
    private boolean output = false;
    public PortableTankTileEntity(PortableTankType type){
        super(PortableTanks.portable_tank_tile);
        this.type = type;
    }
    @Override
    protected CompoundNBT writeData() {
        CompoundNBT compound = new CompoundNBT();
        compound.putBoolean("output",this.output);
        compound.put("fluid",this.fluidStack.writeToNBT(new CompoundNBT()));
        return compound;
    }

    @Override
    protected void readData(CompoundNBT compound) {
        this.output = compound.getBoolean("output");
        this.fluidStack = FluidStack.loadFluidStackFromNBT(compound.getCompound("fluid"));
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return this.fluidStack.copy();
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.type.tankCapacity;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return null;
    }
}
