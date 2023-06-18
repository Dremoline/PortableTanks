package com.dremoline.portabletanks;

import com.supermartijn642.core.block.BaseBlockEntityType;
import com.supermartijn642.core.registry.RegistrationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public enum PortableTankType {
    BASIC("basic_portable_tank", PortableTanksConfig.basicTankCapacity),
    ADVANCED("advanced_portable_tank", PortableTanksConfig.advancedTankCapacity),
    EXPERT("expert_portable_tank", PortableTanksConfig.expertTankCapacity),
    ULTIMATE("ultimate_portable_tank", PortableTanksConfig.ultimateTankCapacity);

    private final String registryName;
    public final Supplier<Integer> tankCapacity;
    private PortableTankBlock block;
    private BaseBlockEntityType<PortableTankBlockEntity> blockEntityType;
    private PortableTankItem item;

    PortableTankType(String registryName, Supplier<Integer> tankCapacity) {
        this.registryName = registryName;
        this.tankCapacity = tankCapacity;
    }

    public String getRegistryName() {
        return this.registryName;
    }

    public BlockBehaviour.Properties getBlockProperties() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(5);
    }

    public PortableTankBlock getBlock() {
        return this.block;
    }

    public BaseBlockEntityType<PortableTankBlockEntity> getBlockEntityType() {
        return this.blockEntityType;
    }

    public PortableTankBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PortableTankBlockEntity(this, pos, state);
    }

    public void registerBlock(RegistrationHandler.Helper<Block> helper) {
        this.block = new PortableTankBlock(this);
        helper.register(this.registryName, this.block);
    }

    public void registerBlockEntityType(RegistrationHandler.Helper<BlockEntityType<?>> helper) {
        this.blockEntityType = BaseBlockEntityType.create(this::createBlockEntity, this.block);
        helper.register(this.registryName + "_tile", this.blockEntityType);
    }

    public void registerItem(RegistrationHandler.Helper<Item> helper) {
        this.item = new PortableTankItem(this);
        helper.register(this.registryName, this.item);
    }
}
