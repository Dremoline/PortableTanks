package com.dremoline.portabletanks;

import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.block.BaseBlockEntity;
import com.supermartijn642.core.block.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nonnull;

public class PortableTankBlockEntity extends BaseBlockEntity implements IFluidHandler, TickableBlockEntity {

    private final PortableTankType type;
    private FluidStack fluidStack = FluidStack.EMPTY;
    private boolean output = false;

    public PortableTankBlockEntity(PortableTankType type, BlockPos pos, BlockState state) {
        super(type.getBlockEntityType(), pos, state);
        this.type = type;
    }

    public PortableTankType getTankType() {
        return this.type;
    }

    public FluidStack getTankContent() {
        return this.fluidStack;
    }

    @Override
    protected CompoundTag writeData() {
        CompoundTag compound = new CompoundTag();
        compound.putBoolean("output", this.output);
        compound.put("fluid", this.fluidStack.saveOptional(this.level.registryAccess()));
        return compound;
    }

    @Override
    protected void readData(CompoundTag compound) {
        this.output = compound.getBoolean("output");
        this.fluidStack = FluidStack.parseOptional(CommonUtils.getRegistryAccess(), compound.getCompound("fluid"));
    }

    @Override
    public void update() {
        if (this.output && !this.fluidStack.isEmpty()) {
            IFluidHandler fluidHandler = Capabilities.FluidHandler.BLOCK.getCapability(this.level, this.worldPosition.below(), null, null, Direction.UP);
            if (fluidHandler != null)
                FluidUtil.tryFluidTransfer(fluidHandler, this, 1000, true);
        }
    }

    public boolean toggleOutput() {
        this.output = !this.output;
        BlockState state = this.getBlockState();
        this.level.setBlock(this.worldPosition, state.setValue(PortableTankBlock.OUTPUT, this.output), 6);
        this.dataChanged();
        return this.output;
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
        return this.type.tankCapacity.get();
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return true;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !(this.fluidStack.isEmpty() || FluidStack.isSameFluidSameComponents(this.fluidStack, resource)))
            return 0;
        int amount = Math.min(resource.getAmount(), this.getTankCapacity(0) - this.fluidStack.getAmount());
        if (action.execute()) {
            FluidStack newStack = resource.copy();
            newStack.setAmount(this.fluidStack.getAmount() + amount);
            this.fluidStack = newStack;
            this.dataChanged();
        }
        return amount;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || this.fluidStack.isEmpty() || !this.fluidStack.getFluid().isSame(resource.getFluid()))
            return FluidStack.EMPTY;
        int amount = Math.min(resource.getAmount(), this.fluidStack.getAmount());
        FluidStack returnStack = this.fluidStack.copy();
        returnStack.setAmount(amount);
        if (action.execute()) {
            this.fluidStack.shrink(amount);
            this.dataChanged();
        }
        return returnStack;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (maxDrain <= 0 || this.fluidStack.isEmpty())
            return FluidStack.EMPTY;
        int amount = Math.min(maxDrain, this.fluidStack.getAmount());
        FluidStack returnStack = this.fluidStack.copy();
        returnStack.setAmount(amount);
        if (action.execute()) {
            this.fluidStack.shrink(amount);
            this.dataChanged();
        }
        return returnStack;
    }

    public boolean interactWithItemFluidHandler(IFluidHandlerItem fluidHandler, Player player) {
        // just only consider tank 0 from items, as that's probably fine in most cases
        if (fluidHandler.getTanks() == 0)
            return false;
        FluidStack tankFluid = fluidHandler.getFluidInTank(0);
        if (tankFluid.isEmpty()) {
            if (!this.fluidStack.isEmpty() && fluidHandler.isFluidValid(0, this.fluidStack)) {
                int amount = fluidHandler.fill(this.fluidStack.copy(), FluidAction.EXECUTE);
                if (amount > 0) {
                    this.fluidStack.getFluid().getPickupSound().ifPresent(player::playSound);
                    this.fluidStack.shrink(amount);
                    this.dataChanged();
                    return true;
                }
            }
        } else if (this.fluidStack.isEmpty() || FluidStack.isSameFluidSameComponents(this.fluidStack, tankFluid)) {
            tankFluid = tankFluid.copy();
            tankFluid.setAmount(this.getTankCapacity(0) - this.fluidStack.getAmount());
            FluidStack amount = fluidHandler.drain(tankFluid, FluidAction.SIMULATE);
            if (!amount.isEmpty() && (this.fluidStack.isEmpty() || FluidStack.isSameFluidSameComponents(this.fluidStack, amount))) {
                amount = fluidHandler.drain(tankFluid, FluidAction.EXECUTE);
                amount.grow(this.fluidStack.getAmount());
                this.fluidStack = amount;
                SoundEvent soundEvent = this.fluidStack.getFluid().getFluidType().getSound(SoundActions.BUCKET_EMPTY);
                if (soundEvent != null) {
                    player.playSound(soundEvent);
                }
                this.dataChanged();
                return true;
            }
        }
        return false;
    }
}
