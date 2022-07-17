package com.dremoline.portabletanks;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Created 7/19/2021 by SuperMartijn642
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy {

    @SubscribeEvent
    public static void onSetup(FMLClientSetupEvent e){
        for(PortableTankType type : PortableTankType.values())
            ItemBlockRenderTypes.setRenderLayer(type.getBlock(), RenderType.cutout());
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers e){
        for(PortableTankType type : PortableTankType.values())
            e.registerBlockEntityRenderer(type.getTileEntityType(), context -> new PortableTankRenderer());
    }

    @SubscribeEvent
    public static void onModelBake(ModelEvent.BakingCompleted e){
        // replace the reservoir item models
        for(PortableTankType type : PortableTankType.values()){
            ResourceLocation location = new ModelResourceLocation("portabletanks:" + type.getRegistryName(), "inventory");
            BakedModel model = e.getModels().get(location);
            if(model != null)
                e.getModels().put(location, new PortableTankBakedItemModel(model));
        }
    }
}
