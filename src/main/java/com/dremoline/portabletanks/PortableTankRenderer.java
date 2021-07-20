package com.dremoline.portabletanks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.supermartijn642.core.ClientUtils;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created 7/19/2021 by SuperMartijn642
 */
public class PortableTankRenderer extends TileEntityRenderer<PortableTankTileEntity> {

    private static final float SIDE_MARGIN = (float)PortableTankBlock.SHAPE.getStart(Direction.Axis.X) + 0.01f, MIN_Y = 1 / 16f, MAX_Y = 1 - MIN_Y;

    public PortableTankRenderer(TileEntityRendererDispatcher rendererDispatcher){
        super(rendererDispatcher);
    }

    @Override
    public void render(PortableTankTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLight, int combinedOverlay){
        FluidStack fluidStack = tileEntity.getFluidInTank(0);
        if(fluidStack.isEmpty())
            return;

        float fillPercentage = Math.min(1, (float)fluidStack.getAmount() / tileEntity.getTankCapacity(0));
        if(fluidStack.getFluid().getAttributes().isGaseous(fluidStack))
            renderFluid(matrixStack, renderTypeBuffer, fluidStack, fillPercentage, 1, combinedLight);
        else
            renderFluid(matrixStack, renderTypeBuffer, fluidStack, 1, fillPercentage, combinedLight);
    }

    private static void renderFluid(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, FluidStack fluidStack, float alpha, float heightPercentage, int combinedLight){
        IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(RenderType.translucent());
        TextureAtlasSprite sprite = ClientUtils.getMinecraft().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(fluidStack.getFluid().getAttributes().getStillTexture(fluidStack));
        int color = fluidStack.getFluid().getAttributes().getColor(fluidStack);
        alpha *= (color >> 24 & 255) / 255f;
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;

        renderQuads(matrixStack.last().pose(), vertexBuilder, sprite, red, green, blue, alpha, heightPercentage, combinedLight);
    }

    private static void renderQuads(Matrix4f matrix, IVertexBuilder vertexBuilder, TextureAtlasSprite sprite, float r, float g, float b, float alpha, float heightPercentage, int light){
        float height = MIN_Y + (MAX_Y - MIN_Y) * heightPercentage;
        float minU = sprite.getU(SIDE_MARGIN * 16), maxU = sprite.getU((1 - SIDE_MARGIN) * 16);
        float minV = sprite.getV(MIN_Y * 16), maxV = sprite.getV(height * 16);
        // min z
        vertexBuilder.vertex(matrix, SIDE_MARGIN, MIN_Y, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(0, 0, -1).endVertex();
        vertexBuilder.vertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(0, 0, -1).endVertex();
        vertexBuilder.vertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(0, 0, -1).endVertex();
        vertexBuilder.vertex(matrix, 1 - SIDE_MARGIN, MIN_Y, SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(0, 0, -1).endVertex();
        // max z
        vertexBuilder.vertex(matrix, SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(0, 0, 1).endVertex();
        vertexBuilder.vertex(matrix, 1 - SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(0, 0, 1).endVertex();
        vertexBuilder.vertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(0, 0, 1).endVertex();
        vertexBuilder.vertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(0, 0, 1).endVertex();
        // min x
        vertexBuilder.vertex(matrix, SIDE_MARGIN, MIN_Y, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(-1, 0, 0).endVertex();
        vertexBuilder.vertex(matrix, SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(-1, 0, 0).endVertex();
        vertexBuilder.vertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(-1, 0, 0).endVertex();
        vertexBuilder.vertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(-1, 0, 0).endVertex();
        // max x
        vertexBuilder.vertex(matrix, 1 - SIDE_MARGIN, MIN_Y, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(1, 0, 0).endVertex();
        vertexBuilder.vertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(1, 0, 0).endVertex();
        vertexBuilder.vertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(1, 0, 0).endVertex();
        vertexBuilder.vertex(matrix, 1 - SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(1, 0, 0).endVertex();
        // top
        if(heightPercentage < 1){
            minV = sprite.getV(SIDE_MARGIN * 16);
            maxV = sprite.getV((1 - SIDE_MARGIN) * 16);
            vertexBuilder.vertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(0, 1, 0).endVertex();
            vertexBuilder.vertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(0, 1, 0).endVertex();
            vertexBuilder.vertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(0, 1, 0).endVertex();
            vertexBuilder.vertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(0, 1, 0).endVertex();
        }
    }
}
