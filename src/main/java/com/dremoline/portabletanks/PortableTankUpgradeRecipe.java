package com.dremoline.portabletanks;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

/**
 * Created 7/19/2021 by SuperMartijn642
 */
public class PortableTankUpgradeRecipe extends ShapedRecipe {

    public static final RecipeSerializer<PortableTankUpgradeRecipe> SERIALIZER = new PortableTankUpgradeRecipe.Serializer();

    public PortableTankUpgradeRecipe(ResourceLocation location, String group, int recipeWidth, int recipeHeight, NonNullList<Ingredient> ingredients, ItemStack output){
        super(location, group, recipeWidth, recipeHeight, ingredients, output);
    }

    @Override
    public ItemStack assemble(CraftingContainer inv){
        CompoundTag compound = null;
        loop:
        for(int i = 0; i < inv.getHeight(); i++){
            for(int j = 0; j < inv.getWidth(); j++){
                ItemStack stack = inv.getItem(i * inv.getWidth() + j);
                if(stack.hasTag() && stack.getItem() instanceof PortableTankItem){
                    compound = stack.getTag();
                    break loop;
                }
            }
        }

        if(compound != null){
            ItemStack result = this.getResultItem().copy();
            result.getOrCreateTag().merge(compound);
            return result;
        }

        return super.assemble(inv);
    }

    @Override
    public RecipeSerializer<?> getSerializer(){
        return SERIALIZER;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<PortableTankUpgradeRecipe> {

        @Override
        public PortableTankUpgradeRecipe fromJson(ResourceLocation recipeId, JsonObject json){
            ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json);
            return new PortableTankUpgradeRecipe(recipeId, recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getResultItem());
        }

        @Nullable
        @Override
        public PortableTankUpgradeRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer){
            ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer);
            return new PortableTankUpgradeRecipe(recipeId, recipe.getGroup(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.getResultItem());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, PortableTankUpgradeRecipe recipe){
            RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
        }
    }
}
