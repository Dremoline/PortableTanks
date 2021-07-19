package com.dremoline.portabletanks;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod("portabletanks")
public class PortableTanks {

    public static final ItemGroup GROUP = new ItemGroup("portabletanks") {
        @Override
        public ItemStack makeIcon(){
            return new ItemStack(PortableTankType.BASIC.getBlock());
        }
    };

    public PortableTanks(){
        PortableTanksConfig.init();
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onBlockRegistry(final RegistryEvent.Register<Block> e){
            for(PortableTankType type : PortableTankType.values())
                type.registerBlock(e);
        }

        @SubscribeEvent
        public static void onTileRegistry(final RegistryEvent.Register<TileEntityType<?>> e){
            for(PortableTankType type : PortableTankType.values())
                type.registerTileEntityType(e);
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> e){
            for(PortableTankType type : PortableTankType.values())
                type.registerItem(e);
        }

        @SubscribeEvent
        public static void onRecipeRegistry(final RegistryEvent.Register<IRecipeSerializer<?>> e){
            e.getRegistry().register(PortableTankUpgradeRecipe.SERIALIZER.setRegistryName(new ResourceLocation("portabletanks", "upgrade_tank")));
        }
    }

}
