package com.dremoline.portabletanks;

import com.dremoline.portabletanks.generators.*;
import com.supermartijn642.core.CommonUtils;
import com.supermartijn642.core.item.CreativeItemGroup;
import com.supermartijn642.core.registry.GeneratorRegistrationHandler;
import com.supermartijn642.core.registry.RegistrationHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

/**
 * Created 7/7/2020 by SuperMartijn642
 */
@Mod("portabletanks")
public class PortableTanks {

    public static final CreativeItemGroup GROUP = CreativeItemGroup.create("portabletanks", () -> PortableTankType.BASIC.getBlock().asItem());

    public PortableTanks(IEventBus bus) {
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

        bus.addListener(this::registerCapabilities);

        GeneratorRegistrationHandler generatorHandler = GeneratorRegistrationHandler.get("portabletanks");
        generatorHandler.addGenerator(PortableTanksBlockStateGenerator::new);
        generatorHandler.addGenerator(PortableTanksLanguageGenerator::new);
        generatorHandler.addGenerator(PortableTanksLootTableGenerator::new);
        generatorHandler.addGenerator(PortableTanksModelGenerator::new);
        generatorHandler.addGenerator(PortableTanksRecipeGenerator::new);
        generatorHandler.addGenerator(PortableTanksTagGenerator::new);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (PortableTankType type : PortableTankType.values()) {
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, type.getBlockEntityType(), (entity, context) -> entity);
            event.registerItem(Capabilities.FluidHandler.ITEM, (stack, context) -> new PortableTankItem.ItemFluidHandler(stack, type), type.getBlock());
        }
    }
}
