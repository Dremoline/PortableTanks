package com.dremoline.portabletanks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.supermartijn642.core.ClientUtils;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

/**
 * Created 7/19/2021 by SuperMartijn642
 */
public class PortableTankItemStackRenderer extends ItemStackTileEntityRenderer {

    public static final PortableTankItemStackRenderer INSTANCE = new PortableTankItemStackRenderer();

    public static PortableTankItemStackRenderer getInstance(){
        return INSTANCE;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemCameraTransforms.TransformType cameraTransforms, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay){
        IBakedModel model = ClientUtils.getMinecraft().getItemRenderer().getItemModelShaper().getItemModel(stack);
        renderDefaultItem(stack, matrixStack, cameraTransforms, buffer, combinedLight, combinedOverlay, model);

        if(!stack.hasTag() || !stack.getTag().contains("tileData"))
            return;

        PortableTankTileEntity tile = ((PortableTankBlock)((BlockItem)stack.getItem()).getBlock()).type.createTileEntity();
        tile.readData(stack.getTag().getCompound("tileData"));

        TileEntityRendererDispatcher.instance.renderItem(tile, matrixStack, buffer, combinedLight, combinedOverlay);
    }

    private static void renderDefaultItem(ItemStack itemStack, MatrixStack matrixStack, ItemCameraTransforms.TransformType cameraTransforms, IRenderTypeBuffer renderTypeBuffer, int combinedLight, int combinedOverlay, IBakedModel model){
        ItemRenderer renderer = ClientUtils.getMinecraft().getItemRenderer();

        matrixStack.pushPose();

        if(model.isLayered()){
            net.minecraftforge.client.ForgeHooksClient.drawItemLayered(renderer, model, itemStack, matrixStack, renderTypeBuffer, combinedLight, combinedOverlay, true);
        }else{
            RenderType rendertype = RenderTypeLookup.getRenderType(itemStack, true);
            IVertexBuilder ivertexbuilder;

            ivertexbuilder = ItemRenderer.getFoilBufferDirect(renderTypeBuffer, rendertype, true, itemStack.hasFoil());

            renderer.renderModelLists(model, itemStack, combinedLight, combinedOverlay, matrixStack, ivertexbuilder);
        }

        matrixStack.popPose();
    }

}
