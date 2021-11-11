package com.dremoline.portabletanks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraftforge.client.model.BakedModelWrapper;

/**
 * Created 7/19/2021 by SuperMartijn642
 */
public class PortableTankBakedItemModel extends BakedModelWrapper<BakedModel> {

    public PortableTankBakedItemModel(BakedModel originalModel){
        super(originalModel);
    }

    @Override
    public boolean isCustomRenderer(){
        return true;
    }

    @Override
    public BakedModel handlePerspective(ItemTransforms.TransformType cameraTransformType, PoseStack mat){
        super.handlePerspective(cameraTransformType, mat);
        return this;
    }

}
