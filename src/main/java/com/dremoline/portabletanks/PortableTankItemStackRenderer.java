package com.dremoline.portabletanks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.CustomItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Created 7/19/2021 by SuperMartijn642
 */
public class PortableTankItemStackRenderer implements CustomItemRenderer {

    @Override
    public void render(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        BakedModel model = ClientUtils.getMinecraft().getItemRenderer().getItemModelShaper().getItemModel(stack);
        renderDefaultItem(stack, poseStack, bufferSource, combinedLight, combinedOverlay, model);

        if(!stack.hasTag() || !stack.getTag().contains("tileData"))
            return;

        PortableTankBlockEntity entity = ((PortableTankBlock) ((BlockItem) stack.getItem()).getBlock()).type.createBlockEntity(new BlockPos(0,0,0), ((BlockItem)stack.getItem()).getBlock().defaultBlockState());
        entity.readData(stack.getTag().getCompound("tileData"));

        Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(entity, poseStack, bufferSource, combinedLight, combinedOverlay);
    }

    private static void renderDefaultItem(ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model) {
        for(BakedModel passModel : model.getRenderPasses(stack, true)){
            for(RenderType renderType : passModel.getRenderTypes(stack, true)){
                VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(bufferSource, renderType, true, stack.hasFoil());
                ClientUtils.getItemRenderer().renderModelLists(passModel, stack, combinedLight, combinedOverlay, poseStack, vertexConsumer);
            }
        }
    }
}
