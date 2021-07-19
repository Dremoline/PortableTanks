package com.dremoline.portabletanks;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;

public enum PortableTankType {
    BASIC(8000), ADVANCED(16000);

    public final int tankCapacity;
    private PortableTankBlock block;
    private TileEntityType<PortableTankTileEntity> tileEntityType;

    PortableTankType(int tankCapacity){
        this.tankCapacity = tankCapacity;
    }

    public String getRegistryName(){
        if(this == BASIC) {
            return "basicportabletank";
        }
        else if(this == ADVANCED) {
            return "advancedportabletank";
        }
        return null;
    }

    public void registerBlock(RegistryEvent.Register<Block> e){
        this.block = new PortableTankBlock(this);
        e.getRegistry().register(this.block);
    }

    public void registerTileEntityType(RegistryEvent.Register<TileEntityType<?>> e){
        this.tileEntityType = TileEntityType.Builder.of(() -> new PortableTankTileEntity(this),this.block).build(null);
        e.getRegistry().register(this.tileEntityType);
    }

    public void registerItem(RegistryEvent.Register<Item> e){
    }
}
