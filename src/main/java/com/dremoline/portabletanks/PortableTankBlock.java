package com.dremoline.portabletanks;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.block.BlockShape;
import com.supermartijn642.core.block.EntityHoldingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;

public class PortableTankBlock extends BaseBlock implements EntityHoldingBlock {

    public static final BlockShape SHAPE = BlockShape.createBlockShape(2.5, 0, 2.5, 13.5, 16, 13.5);
    public static final BooleanProperty OUTPUT = BooleanProperty.create("output");

    public final PortableTankType type;

    public PortableTankBlock(PortableTankType type) {
        super(true, type.getBlockProperties());
        this.type = type;
        this.registerDefaultState(this.defaultBlockState().setValue(OUTPUT, false));
    }

    @Override
    protected InteractionFeedback interact(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, Direction hitSide, Vector3d hitLocation) {
        ItemStack stack = player.getItemInHand(hand).copy();
        ItemStack fillStack = stack.copy();
        fillStack.setCount(1);
        LazyOptional<IFluidHandlerItem> fluidHandlerOptional = fillStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
        if (fluidHandlerOptional.isPresent()) {
            TileEntity entity = level.getBlockEntity(pos);
            if (entity instanceof PortableTankBlockEntity) {
                IFluidHandlerItem fluidHandler = fluidHandlerOptional.resolve().get();
                if (((PortableTankBlockEntity) entity).interactWithItemFluidHandler(fluidHandler, player)) {
                    stack.shrink(1);
                    if (stack.isEmpty())
                        player.setItemInHand(hand, fluidHandler.getContainer());
                    else {
                        player.setItemInHand(hand, stack);
                        if (!player.inventory.add(fluidHandler.getContainer()))
                            player.drop(fluidHandler.getContainer(), false);
                    }
                    return InteractionFeedback.SUCCESS;
                }
            }
            return InteractionFeedback.CONSUME;
        } else if (stack.isEmpty() && player.isCrouching()) {
            TileEntity entity = level.getBlockEntity(pos);
            if (entity instanceof PortableTankBlockEntity) {
                ITextComponent output = TextComponents.translation("portabletanks.portable_tank.info.output." + (((PortableTankBlockEntity) entity).toggleOutput() ? "on" : "off")).color(TextFormatting.GOLD).get();
                player.displayClientMessage(TextComponents.translation("portabletanks.portable_tank.info.output", output).color(TextFormatting.GRAY).get(), true);
                return InteractionFeedback.SUCCESS;
            }
        }
        return InteractionFeedback.PASS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        CompoundNBT compound = context.getItemInHand().getOrCreateTag();
        if (compound.getCompound("tileData").contains("output"))
            state = state.setValue(OUTPUT, compound.getCompound("tileData").getBoolean("output"));
        return state;
    }

    @Override
    public TileEntity createNewBlockEntity() {
        return this.type.createBlockEntity();
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE.getUnderlying();
    }

    @Override
    public VoxelShape getVisualShape(BlockState p_230322_1_, IBlockReader p_230322_2_, BlockPos p_230322_3_, ISelectionContext p_230322_4_) {
        return BlockShape.empty().getUnderlying();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(OUTPUT);
    }
}
