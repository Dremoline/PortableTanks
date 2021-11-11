package com.dremoline.portabletanks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod("portabletanks")
public class PortableTanks {

    public static final CreativeModeTab GROUP = new CreativeModeTab("portabletanks") {
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
        public static void onTileRegistry(final RegistryEvent.Register<BlockEntityType<?>> e){
            for(PortableTankType type : PortableTankType.values())
                type.registerTileEntityType(e);
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> e){
            for(PortableTankType type : PortableTankType.values())
                type.registerItem(e);
        }

        @SubscribeEvent
        public static void onRecipeRegistry(final RegistryEvent.Register<RecipeSerializer<?>> e){
            e.getRegistry().register(PortableTankUpgradeRecipe.SERIALIZER.setRegistryName(new ResourceLocation("portabletanks", "upgrade_tank")));
        }

        @SubscribeEvent
        public static void onGatherData(GatherDataEvent e){
            e.getGenerator().addProvider(new PortableTankBlockTagsProvider(e));
        }
    }

}
