package com.dremoline.portabletanks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.supermartijn642.core.block.BaseBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

/**
 * Created 7/19/2021 by SuperMartijn642
 */
public class PortableTankUpgradeRecipe extends ShapedRecipe {

    public static final RecipeSerializer<PortableTankUpgradeRecipe> SERIALIZER = new PortableTankUpgradeRecipe.Serializer();

    private final String group;
    private final CraftingBookCategory category;
    private final ShapedRecipePattern pattern;
    private final ItemStack result;
    private final boolean showNotification;

    public PortableTankUpgradeRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack output, boolean showNotification) {
        super(group, category, pattern, output, showNotification);
        this.group = group;
        this.category = category;
        this.pattern = pattern;
        this.result = output;
        this.showNotification = showNotification;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, HolderLookup.Provider provider) {
        CompoundTag compound = null;
        loop:
        for (int i = 0; i < inv.getHeight(); i++) {
            for (int j = 0; j < inv.getWidth(); j++) {
                ItemStack stack = inv.getItem(i * inv.getWidth() + j);
                if (stack.getItem() instanceof PortableTankItem) {
                    CompoundTag tag = stack.get(BaseBlock.TILE_DATA);
                    if (tag != null) {
                        compound = tag;
                        break loop;
                    }
                }
            }
        }

        if (compound != null) {
            ItemStack result = this.getResultItem(provider).copy();
            result.set(BaseBlock.TILE_DATA, compound);
            return result;
        }

        return super.assemble(inv, provider);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<PortableTankUpgradeRecipe> {

        private static final MapCodec<PortableTankUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
                        CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(recipe -> recipe.category),
                        ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(recipe -> recipe.showNotification)
                ).apply(instance, PortableTankUpgradeRecipe::new));

        @Override
        public MapCodec<PortableTankUpgradeRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PortableTankUpgradeRecipe> streamCodec() {
            return StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);
        }

        public static PortableTankUpgradeRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            return fromShapedRecipe(ShapedRecipe.Serializer.fromNetwork(buffer));
        }

        public static void toNetwork(RegistryFriendlyByteBuf buffer, PortableTankUpgradeRecipe recipe) {
            ShapedRecipe.Serializer.toNetwork(buffer, recipe);
        }

        private static PortableTankUpgradeRecipe fromShapedRecipe(ShapedRecipe recipe) {
            return new PortableTankUpgradeRecipe(recipe.getGroup(), recipe.category(), recipe.pattern, recipe.getResultItem(null), recipe.showNotification());
        }
    }
}
