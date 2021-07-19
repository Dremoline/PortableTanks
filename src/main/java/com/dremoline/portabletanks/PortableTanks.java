package com.dremoline.portabletanks;

import com.supermartijn642.core.network.PacketChannel;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod("portabletanks")
public class PortableTanks {
    public static final PacketChannel CHANNEL = PacketChannel.create("portabletanks");

    @ObjectHolder("portabletanks:portable_tank_tile")
    public static TileEntityType<?> portable_tank_tile;
    @ObjectHolder("portabletanks:portable_tank")
    public static Block portable_tank;

    public PortableTanks() {
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onBlockRegistry(final RegistryEvent.Register<Block> e) {
            e.getRegistry().register(new PortableTankBlock("portable_tank", AbstractBlock.Properties.of(Material.METAL).harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).strength(5)));
        }

        @SubscribeEvent
        public static void onTileRegistry(final RegistryEvent.Register<TileEntityType<?>> e) {
            e.getRegistry().register(TileEntityType.Builder.of(PortableTankTileEntity::new,portable_tank).build(null));
        }

        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> e) {
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> e) {

        }

        @SubscribeEvent
        public static void onEntityRegistry(RegistryEvent.Register<EntityType<?>> e) {
        }

        @SubscribeEvent
        public static void onFeatureRegistry(final RegistryEvent.Register<Feature<?>> e) {
        }

        @SubscribeEvent
        public static void onLootModifierSerializerRegistry(RegistryEvent.Register<GlobalLootModifierSerializer<?>> e) {
        }

        @SubscribeEvent
        public static void onSoundRegistry(RegistryEvent.Register<SoundEvent> e) {
        }
    }

}
