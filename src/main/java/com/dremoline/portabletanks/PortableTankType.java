package com.dremoline.portabletanks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;

import java.util.function.Supplier;

public enum PortableTankType {
    BASIC("basic_portable_tank", PortableTanksConfig.basicTankCapacity),
    ADVANCED("advanced_portable_tank", PortableTanksConfig.advancedTankCapacity),
    EXPERT("expert_portable_tank", PortableTanksConfig.expertTankCapacity),
    ULTIMATE("ultimate_portable_tank", PortableTanksConfig.ultimateTankCapacity);

    private final String registryName;
    public final Supplier<Integer> tankCapacity;
    private PortableTankBlock block;
    private TileEntityType<PortableTankTileEntity> tileEntityType;
    private PortableTankItem item;

    PortableTankType(String registryName, Supplier<Integer> tankCapacity){
        this.registryName = registryName;
        this.tankCapacity = tankCapacity;
    }

    public String getRegistryName(){
        return this.registryName;
    }

    public AbstractBlock.Properties getBlockProperties(){
        return AbstractBlock.Properties.of(Material.METAL).harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).strength(5);
    }

    public PortableTankBlock getBlock(){
        return this.block;
    }

    public TileEntityType<PortableTankTileEntity> getTileEntityType(){
        return this.tileEntityType;
    }

    public PortableTankTileEntity createTileEntity(){
        return new PortableTankTileEntity(this);
    }

    public void registerBlock(RegistryEvent.Register<Block> e){
        this.block = new PortableTankBlock(this);
        e.getRegistry().register(this.block);
    }

    public void registerTileEntityType(RegistryEvent.Register<TileEntityType<?>> e){
        this.tileEntityType = TileEntityType.Builder.of(() -> new PortableTankTileEntity(this), this.block).build(null);
        this.tileEntityType.setRegistryName(this.getRegistryName() + "_tile");
        e.getRegistry().register(this.tileEntityType);
    }

    public void registerItem(RegistryEvent.Register<Item> e){
        this.item = new PortableTankItem(this);
        e.getRegistry().register(this.item);
    }
}
