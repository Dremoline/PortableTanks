package com.dremoline.portabletanks.generators;

import com.dremoline.portabletanks.PortableTankType;
import com.dremoline.portabletanks.PortableTanks;
import com.supermartijn642.core.generator.LanguageGenerator;
import com.supermartijn642.core.generator.ResourceCache;

public class PortableTanksLanguageGenerator extends LanguageGenerator {
    public PortableTanksLanguageGenerator(ResourceCache cache) {
        super("portabletanks", cache, "en_us");
    }

    @Override
    public void generate() {
        this.itemGroup(PortableTanks.GROUP, "Portable Tanks");
        this.translation("portabletanks.portable_tank.info.capacity", "Capacity: %d mB");
        this.translation("portabletanks.portable_tank.info.stored", "Stored: %1$s %2$d / %3$d mB");
        this.translation("portabletanks.portable_tank.info.output", "Output: %s");
        this.translation("portabletanks.portable_tank.info.output.off", "Off");
        this.translation("portabletanks.portable_tank.info.output.on", "On");
        this.translation("config.jade.plugin_portabletanks.portable_tank_component", "Portable Tanks");
        this.block(PortableTankType.BASIC.getBlock(), "Basic Portable Tank");
        this.block(PortableTankType.ADVANCED.getBlock(), "Advanced Portable Tank");
        this.block(PortableTankType.EXPERT.getBlock(), "Expert Portable Tank");
        this.block(PortableTankType.ULTIMATE.getBlock(), "Ultimate Portable Tank");
    }
}
