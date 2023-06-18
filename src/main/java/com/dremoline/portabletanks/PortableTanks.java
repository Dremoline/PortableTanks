package com.dremoline.portabletanks;

import com.dremoline.portabletanks.compatibility.PortableTanksTheOneProbePlugin;
import com.dremoline.portabletanks.generators.*;
import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.registry.GeneratorRegistrationHandler;
import com.supermartijn642.core.registry.RegistrationHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod("portabletanks")
public class PortableTanks {

    public static final CreativeItemGroup GROUP = CreativeItemGroup.create("portabletanks", () -> PortableTankType.BASIC.getBlock().asItem());

    public PortableTanks() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(PortableTanksTheOneProbePlugin::interModEnqueue);

        RegistrationHandler handler = RegistrationHandler.get("portabletanks");
        for (PortableTankType type : PortableTankType.values()) {
            handler.registerBlockCallback(type::registerBlock);
            handler.registerBlockEntityTypeCallback(type::registerBlockEntityType);
            handler.registerItemCallback(type::registerItem);
        }
        handler.registerRecipeSerializer("upgrade_tank", PortableTankUpgradeRecipe.SERIALIZER);

        if (CommonUtils.getEnvironmentSide().isClient())
            PortableTanksClient.initialize();
        PortableTanksConfig.init();

        GeneratorRegistrationHandler generatorHandler = GeneratorRegistrationHandler.get("portabletanks");
        generatorHandler.addGenerator(PortableTanksBlockStateGenerator::new);
        generatorHandler.addGenerator(PortableTanksLanguageGenerator::new);
        generatorHandler.addGenerator(PortableTanksLootTableGenerator::new);
        generatorHandler.addGenerator(PortableTanksModelGenerator::new);
        generatorHandler.addGenerator(PortableTanksRecipeGenerator::new);
        generatorHandler.addGenerator(PortableTanksTagGenerator::new);
    }
}
