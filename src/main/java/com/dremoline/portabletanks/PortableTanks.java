package com.dremoline.portabletanks;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

import java.util.Objects;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod("portabletanks")
public class PortableTanks {

    public static final CreativeModeTab GROUP = new CreativeModeTab("portabletanks") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(PortableTankType.BASIC.getBlock());
        }
    };

    public PortableTanks() {
        PortableTanksConfig.init();
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onRegisterEvent(RegisterEvent e) {
            if (e.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS))
                onBlockRegistry(Objects.requireNonNull(e.getForgeRegistry()));
            else if (e.getRegistryKey().equals(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES))
                onTileRegistry(Objects.requireNonNull(e.getForgeRegistry()));
            else if (e.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS))
                onItemRegistry(Objects.requireNonNull(e.getForgeRegistry()));
            else if (e.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS))
                onRecipeRegistry(Objects.requireNonNull(e.getForgeRegistry()));
        }

        public static void onBlockRegistry(IForgeRegistry<Block> registry) {
            for (PortableTankType type : PortableTankType.values())
                type.registerBlock(registry);
        }

        public static void onTileRegistry(IForgeRegistry<BlockEntityType<?>> registry) {
            for (PortableTankType type : PortableTankType.values())
                type.registerTileEntityType(registry);
        }

        public static void onItemRegistry(IForgeRegistry<Item> registry) {
            for (PortableTankType type : PortableTankType.values())
                type.registerItem(registry);
        }

        public static void onRecipeRegistry(IForgeRegistry<RecipeSerializer<?>> registry) {
            registry.register("upgrade_tank", PortableTankUpgradeRecipe.SERIALIZER);
        }

        @SubscribeEvent
        public static void onGatherData(GatherDataEvent e) {
            e.getGenerator().addProvider(e.includeServer(), new PortableTankBlockTagsProvider(e));
        }
    }

}
