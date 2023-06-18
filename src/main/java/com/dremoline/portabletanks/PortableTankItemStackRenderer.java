package com.dremoline.portabletanks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.CustomItemRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

/**
 * Created 7/19/2021 by SuperMartijn642
 */
public class PortableTankItemStackRenderer implements CustomItemRenderer {

    @Override
    public void render(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack poseStack, IRenderTypeBuffer bufferSource, int combinedLight, int combinedOverlay) {
        IBakedModel model = ClientUtils.getMinecraft().getItemRenderer().getItemModelShaper().getItemModel(stack);
        renderDefaultItem(stack, poseStack, transformType, bufferSource, combinedLight, combinedOverlay, model);

        if (!stack.hasTag() || !stack.getTag().contains("tileData"))
            return;

        PortableTankBlockEntity entity = ((PortableTankBlock) ((BlockItem) stack.getItem()).getBlock()).type.createBlockEntity();
        entity.readData(stack.getTag().getCompound("tileData"));

        TileEntityRendererDispatcher.instance.renderItem(entity, poseStack, bufferSource, combinedLight, combinedOverlay);
    }

    private static void renderDefaultItem(ItemStack stack, MatrixStack poseStack, ItemCameraTransforms.TransformType transformType, IRenderTypeBuffer bufferSource, int combinedLight, int combinedOverlay, IBakedModel model) {
        ItemRenderer renderer = ClientUtils.getMinecraft().getItemRenderer();

        poseStack.pushPose();

        if (model.isLayered()) {
            net.minecraftforge.client.ForgeHooksClient.drawItemLayered(renderer, model, stack, poseStack, bufferSource, combinedLight, combinedOverlay, true);
        } else {
            RenderType rendertype = RenderTypeLookup.getRenderType(stack, true);
            IVertexBuilder ivertexbuilder;

            ivertexbuilder = ItemRenderer.getFoilBufferDirect(bufferSource, rendertype, true, stack.hasFoil());

            renderer.renderModelLists(model, stack, combinedLight, combinedOverlay, poseStack, ivertexbuilder);
        }

        poseStack.popPose();
    }

}
