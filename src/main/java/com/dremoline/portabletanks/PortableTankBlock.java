package com.dremoline.portabletanks;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.block.BlockShape;
import com.supermartijn642.core.block.EntityHoldingBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
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
    protected InteractionFeedback interact(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, Direction hitSide, Vec3 hitLocation) {
        ItemStack stack = player.getItemInHand(hand).copy();
        ItemStack fillStack = stack.copy();
        fillStack.setCount(1);
        LazyOptional<IFluidHandlerItem> fluidHandlerOptional = fillStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
        if (fluidHandlerOptional.isPresent()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof PortableTankBlockEntity) {
                IFluidHandlerItem fluidHandler = fluidHandlerOptional.resolve().get();
                if (((PortableTankBlockEntity) entity).interactWithItemFluidHandler(fluidHandler, player)) {
                    stack.shrink(1);
                    if (stack.isEmpty())
                        player.setItemInHand(hand, fluidHandler.getContainer());
                    else {
                        player.setItemInHand(hand, stack);
                        if (!player.getInventory().add(fluidHandler.getContainer()))
                            player.drop(fluidHandler.getContainer(), false);
                    }
                    return InteractionFeedback.SUCCESS;
                }
            }
            return InteractionFeedback.CONSUME;
        } else if (stack.isEmpty() && player.isCrouching()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof PortableTankBlockEntity) {
                Component output = TextComponents.translation("portabletanks.portable_tank.info.output." + (((PortableTankBlockEntity) entity).toggleOutput() ? "on" : "off")).color(ChatFormatting.GOLD).get();
                player.displayClientMessage(TextComponents.translation("portabletanks.portable_tank.info.output", output).color(ChatFormatting.GRAY).get(), true);
                return InteractionFeedback.SUCCESS;
            }
        }
        return InteractionFeedback.PASS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        CompoundTag compound = context.getItemInHand().getOrCreateTag();
        if (compound.getCompound("tileData").contains("output"))
            state = state.setValue(OUTPUT, compound.getCompound("tileData").getBoolean("output"));
        return state;
    }

    @Nullable
    @Override
    public BlockEntity createNewBlockEntity(BlockPos pos, BlockState state) {
        return this.type.createBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return SHAPE.getUnderlying();
    }

    @Override
    public VoxelShape getVisualShape(BlockState p_230322_1_, BlockGetter p_230322_2_, BlockPos p_230322_3_, CollisionContext p_230322_4_) {
        return BlockShape.empty().getUnderlying();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OUTPUT);
    }
}
