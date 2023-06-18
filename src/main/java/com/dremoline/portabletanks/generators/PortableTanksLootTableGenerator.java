package com.dremoline.portabletanks.generators;

import com.dremoline.portabletanks.PortableTankType;
import com.supermartijn642.core.generator.LootTableGenerator;
import com.supermartijn642.core.generator.ResourceCache;

public class PortableTanksLootTableGenerator extends LootTableGenerator {
    public PortableTanksLootTableGenerator(ResourceCache cache) {
        super("portabletanks", cache);
    }

    @Override
    public void generate() {
        for (PortableTankType type : PortableTankType.values()) {
            this.dropSelf(type.getBlock());
        }
    }
}
