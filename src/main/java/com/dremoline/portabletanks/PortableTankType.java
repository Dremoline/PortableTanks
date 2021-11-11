package com.dremoline.portabletanks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
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
    private BlockEntityType<PortableTankTileEntity> tileEntityType;
    private PortableTankItem item;

    PortableTankType(String registryName, Supplier<Integer> tankCapacity) {
        this.registryName = registryName;
        this.tankCapacity = tankCapacity;
    }

    public String getRegistryName() {
        return this.registryName;
    }

    public BlockBehaviour.Properties getBlockProperties() {
        return BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).strength(5);
    }

    public PortableTankBlock getBlock() {
        return this.block;
    }

    public BlockEntityType<PortableTankTileEntity> getTileEntityType() {
        return this.tileEntityType;
    }

    public PortableTankTileEntity createTileEntity(BlockPos pos, BlockState state) {
        return new PortableTankTileEntity(this, pos, state);
    }

    public void registerBlock(RegistryEvent.Register<Block> e) {
        this.block = new PortableTankBlock(this);
        e.getRegistry().register(this.block);
    }

    public void registerTileEntityType(RegistryEvent.Register<BlockEntityType<?>> e) {
        this.tileEntityType = BlockEntityType.Builder.of((pos, state) -> new PortableTankTileEntity(this, pos, state), this.block).build(null);
        this.tileEntityType.setRegistryName(this.getRegistryName() + "_tile");
        e.getRegistry().register(this.tileEntityType);
    }

    public void registerItem(RegistryEvent.Register<Item> e) {
        this.item = new PortableTankItem(this);
        e.getRegistry().register(this.item);
    }
}
