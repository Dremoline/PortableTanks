package com.dremoline.portabletanks;

import com.supermartijn642.core.block.BaseTileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class PortableTankTileEntity extends BaseTileEntity{
    public PortableTankTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    protected CompoundNBT writeData() {
        return null;
    }

    @Override
    protected void readData(CompoundNBT compoundNBT) {

    }
}
