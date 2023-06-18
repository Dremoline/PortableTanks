package com.dremoline.portabletanks.generators;

import com.dremoline.portabletanks.PortableTankType;
import com.dremoline.portabletanks.PortableTankUpgradeRecipe;
import com.supermartijn642.core.generator.RecipeGenerator;
import com.supermartijn642.core.generator.ResourceCache;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

public class PortableTanksRecipeGenerator extends RecipeGenerator {
    public PortableTanksRecipeGenerator(ResourceCache cache) {
        super("portabletanks", cache);
    }

    @Override
    public void generate() {
        this.shaped(PortableTankType.BASIC.getBlock())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .input('A', Tags.Items.INGOTS_IRON)
                .input('B', Tags.Items.GLASS_PANES)
                .input('C', Items.BUCKET)
                .unlockedBy(Tags.Items.INGOTS_IRON);

        this.shaped(PortableTankType.ADVANCED.getBlock())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .input('A', Tags.Items.INGOTS_GOLD)
                .input('B', Tags.Items.GLASS_PANES)
                .input('C', PortableTankType.BASIC.getBlock())
                .unlockedBy(PortableTankType.BASIC.getBlock())
                .customSerializer(PortableTankUpgradeRecipe.SERIALIZER);

        this.shaped(PortableTankType.EXPERT.getBlock())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .input('A', Tags.Items.GEMS_DIAMOND)
                .input('B', Tags.Items.GLASS_PANES)
                .input('C', PortableTankType.ADVANCED.getBlock())
                .unlockedBy(PortableTankType.ADVANCED.getBlock())
                .customSerializer(PortableTankUpgradeRecipe.SERIALIZER);

        this.shaped(PortableTankType.ULTIMATE.getBlock())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .input('A', Tags.Items.INGOTS_NETHERITE)
                .input('B', Tags.Items.GLASS_PANES)
                .input('C', PortableTankType.EXPERT.getBlock())
                .unlockedBy(PortableTankType.EXPERT.getBlock())
                .customSerializer(PortableTankUpgradeRecipe.SERIALIZER);

    }
}
