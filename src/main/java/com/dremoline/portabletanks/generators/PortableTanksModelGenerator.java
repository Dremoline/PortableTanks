package com.dremoline.portabletanks.generators;

import com.dremoline.portabletanks.PortableTankType;
import com.supermartijn642.core.generator.ModelGenerator;
import com.supermartijn642.core.generator.ResourceCache;

public class PortableTanksModelGenerator extends ModelGenerator {
    public PortableTanksModelGenerator(ResourceCache cache) {
        super("portabletanks", cache);
    }

    @Override
    public void generate() {
        for (PortableTankType type : PortableTankType.values()) {
            this.model("block/" + type.getRegistryName() + "_off")
                    .parent("block/portable_tank")
                    .texture("bottom", "block/" + type.getRegistryName() + "_bottom_off")
                    .texture("side", "block/" + type.getRegistryName() + "_side")
                    .texture("top", "block/" + type.getRegistryName() + "_top");
            this.model("block/" + type.getRegistryName() + "_on")
                    .parent("block/portable_tank")
                    .texture("bottom", "block/" + type.getRegistryName() + "_bottom_on")
                    .texture("side", "block/" + type.getRegistryName() + "_side")
                    .texture("top", "block/" + type.getRegistryName() + "_top");
            this.model("item/" + type.getRegistryName())
                    .parent("block/" + type.getRegistryName() + "_off");
        }
    }
}
