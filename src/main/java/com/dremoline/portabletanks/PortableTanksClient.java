package com.dremoline.portabletanks;

import com.supermartijn642.core.registry.ClientRegistrationHandler;
import com.supermartijn642.core.render.CustomRendererBakedModelWrapper;

/**
 * Created 7/19/2021 by SuperMartijn642
 */
public class PortableTanksClient {

    public static void initialize() {
        ClientRegistrationHandler handler = ClientRegistrationHandler.get("portabletanks");
        for (PortableTankType type : PortableTankType.values()) {
            handler.registerCustomBlockEntityRenderer(type::getBlockEntityType, PortableTankRenderer::new);
            handler.registerBlockModelCutoutRenderType(type::getBlock);
            handler.registerItemModelOverwrite(() -> type.getBlock().asItem(), CustomRendererBakedModelWrapper::wrap);
            handler.registerCustomItemRenderer(() -> type.getBlock().asItem(), PortableTankItemStackRenderer::new);
        }
    }
}
