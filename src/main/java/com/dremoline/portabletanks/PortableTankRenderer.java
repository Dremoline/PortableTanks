package com.dremoline.portabletanks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.core.render.CustomBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Matrix4f;

/**
 * Created 7/19/2021 by SuperMartijn642
 */
public class PortableTankRenderer implements CustomBlockEntityRenderer<PortableTankBlockEntity> {

    private static final float SIDE_MARGIN = (float) PortableTankBlock.SHAPE.getStart(Direction.Axis.X) + 0.01f, MIN_Y = 1 / 16f, MAX_Y = 1 - MIN_Y;

    @Override
    public void render(PortableTankBlockEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        FluidStack fluidStack = entity.getFluidInTank(0);
        if (fluidStack.isEmpty())
            return;

        float fillPercentage = Math.min(1, (float) fluidStack.getAmount() / entity.getTankCapacity(0));
        if (fluidStack.getFluid().getFluidType().isLighterThanAir())
            renderFluid(poseStack, bufferSource, fluidStack, fillPercentage, 1, combinedLight);
        else
            renderFluid(poseStack, bufferSource, fluidStack, 1, fillPercentage, combinedLight);
    }

    private static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, FluidStack fluidStack, float alpha, float heightPercentage, int combinedLight) {
        VertexConsumer vertexBuilder = bufferSource.getBuffer(RenderType.translucent());
        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        TextureAtlasSprite sprite = ClientUtils.getMinecraft().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(fluidTypeExtensions.getStillTexture(fluidStack));
        int color = fluidTypeExtensions.getTintColor();
        alpha *= (color >> 24 & 255) / 255f;
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;

        renderQuads(poseStack.last().pose(), vertexBuilder, sprite, red, green, blue, alpha, heightPercentage, combinedLight);
    }

    private static void renderQuads(Matrix4f matrix, VertexConsumer buffer, TextureAtlasSprite sprite, float r, float g, float b, float alpha, float heightPercentage, int light) {
        float height = MIN_Y + (MAX_Y - MIN_Y) * heightPercentage;
        float minU = sprite.getU(SIDE_MARGIN * 16), maxU = sprite.getU((1 - SIDE_MARGIN) * 16);
        float minV = sprite.getV(MIN_Y * 16), maxV = sprite.getV(height * 16);
        // min z
        buffer.vertex(matrix, SIDE_MARGIN, MIN_Y, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(0, 0, -1).endVertex();
        buffer.vertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(0, 0, -1).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(0, 0, -1).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, MIN_Y, SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(0, 0, -1).endVertex();
        // max z
        buffer.vertex(matrix, SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(0, 0, 1).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(0, 0, 1).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(0, 0, 1).endVertex();
        buffer.vertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(0, 0, 1).endVertex();
        // min x
        buffer.vertex(matrix, SIDE_MARGIN, MIN_Y, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(-1, 0, 0).endVertex();
        buffer.vertex(matrix, SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(-1, 0, 0).endVertex();
        buffer.vertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(-1, 0, 0).endVertex();
        buffer.vertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(-1, 0, 0).endVertex();
        // max x
        buffer.vertex(matrix, 1 - SIDE_MARGIN, MIN_Y, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(1, 0, 0).endVertex();
        // top
        if (heightPercentage < 1) {
            minV = sprite.getV(SIDE_MARGIN * 16);
            maxV = sprite.getV((1 - SIDE_MARGIN) * 16);
            buffer.vertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(0, 1, 0).endVertex();
            buffer.vertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(0, 1, 0).endVertex();
            buffer.vertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(0, 1, 0).endVertex();
            buffer.vertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(0, 1, 0).endVertex();
        }
    }
}
