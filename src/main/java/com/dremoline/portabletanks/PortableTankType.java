package com.dremoline.portabletanks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.IForgeRegistry;

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

    public void registerBlock(IForgeRegistry<Block> registry) {
        this.block = new PortableTankBlock(this);
        registry.register(this.getRegistryName(), this.block);
    }

    public void registerTileEntityType(IForgeRegistry<BlockEntityType<?>> registry) {
        this.tileEntityType = BlockEntityType.Builder.of((pos, state) -> new PortableTankTileEntity(this, pos, state), this.block).build(null);
        registry.register(this.getRegistryName() + "_tile", this.tileEntityType);
    }

    public void registerItem(IForgeRegistry<Item> registry) {
        this.item = new PortableTankItem(this);
        registry.register(this.getRegistryName(), this.item);
    }
}
