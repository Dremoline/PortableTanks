package com.dremoline.portabletanks;

import com.supermartijn642.core.block.BaseBlockEntityType;
import com.supermartijn642.core.registry.RegistrationHandler;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.ToolType;

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

    public AbstractBlock.Properties getBlockProperties() {
        return AbstractBlock.Properties.of(Material.METAL).harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).strength(5);
    }

    public PortableTankBlock getBlock() {
        return this.block;
    }

    public TileEntityType<PortableTankBlockEntity> getBlockEntityType() {
        return this.blockEntityType;
    }

    public PortableTankBlockEntity createBlockEntity() {
        return new PortableTankBlockEntity(this);
    }

    public void registerBlock(RegistrationHandler.Helper<Block> helper) {
        this.block = new PortableTankBlock(this);
        helper.register(this.registryName, this.block);
    }

    public void registerBlockEntityType(RegistrationHandler.Helper<TileEntityType<?>> helper) {
        this.blockEntityType = BaseBlockEntityType.create(() -> new PortableTankBlockEntity(this), this.block);
        helper.register(this.registryName + "_tile", this.blockEntityType);
    }

    public void registerItem(RegistrationHandler.Helper<Item> helper) {
        this.item = new PortableTankItem(this);
        helper.register(this.registryName, this.item);
    }
}
