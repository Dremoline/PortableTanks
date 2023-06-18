package com.dremoline.portabletanks.generators;

import com.dremoline.portabletanks.PortableTankType;
import com.supermartijn642.core.generator.ResourceCache;
import com.supermartijn642.core.generator.TagGenerator;

public class PortableTanksTagGenerator extends TagGenerator {
    public PortableTanksTagGenerator(ResourceCache cache) {
        super("portabletanks", cache);
    }

    @Override
    public void generate() {
        for (PortableTankType type : PortableTankType.values()) {
            this.blockMineableWithPickaxe().add(type.getBlock());
        }
    }
}
