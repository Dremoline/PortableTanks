package com.dremoline.portabletanks.generators;

import com.dremoline.portabletanks.PortableTankBlock;
import com.dremoline.portabletanks.PortableTankType;
import com.supermartijn642.core.generator.BlockStateGenerator;
import com.supermartijn642.core.generator.ResourceCache;

public class PortableTanksBlockStateGenerator extends BlockStateGenerator {
    public PortableTanksBlockStateGenerator(ResourceCache cache) {
        super("portabletanks", cache);
    }

    @Override
    public void generate() {
        for (PortableTankType type : PortableTankType.values()) {
            this.blockState(type.getBlock())
                    .variantsForProperty(PortableTankBlock.OUTPUT, (state, variant) -> {
                        boolean output = state.get(PortableTankBlock.OUTPUT);
                        variant.model("block/" + type.getRegistryName() + (output ? "_on" : "_off"));
                    });
        }
    }
}
