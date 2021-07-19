package com.dremoline.portabletanks;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Created 7/19/2021 by SuperMartijn642
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent e){
        for(PortableTankType type : PortableTankType.values())
            ClientRegistry.bindTileEntityRenderer(type.getTileEntityType(), PortableTankRenderer::new);
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent e){
        // replace the reservoir item models
        for(PortableTankType type : PortableTankType.values()){
            ResourceLocation location = new ModelResourceLocation("portabletanks:" + type.getRegistryName(), "inventory");
            IBakedModel model = e.getModelRegistry().get(location);
            if(model != null)
                e.getModelRegistry().put(location, new PortableTankBakedItemModel(model));
        }
    }
}
