package com.dremoline.portabletanks;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.block.BlockShape;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;

public class PortableTankBlock extends BaseBlock implements EntityBlock {

    public static final BlockShape SHAPE = BlockShape.createBlockShape(2.5, 0, 2.5, 13.5, 16, 13.5);
    public static final BooleanProperty OUTPUT = BooleanProperty.create("output");

    public final PortableTankType type;

    public PortableTankBlock(PortableTankType type){
        super(type.getRegistryName(), true, type.getBlockProperties());
        this.type = type;
        this.registerDefaultState(this.defaultBlockState().setValue(OUTPUT, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult){
        ItemStack stack = player.getItemInHand(hand).copy();
        ItemStack fillStack = stack.copy();
        fillStack.setCount(1);
        LazyOptional<IFluidHandlerItem> fluidHandlerOptional = fillStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
        if(fluidHandlerOptional.isPresent()){
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if(tileEntity instanceof PortableTankTileEntity){
                IFluidHandlerItem fluidHandler = fluidHandlerOptional.resolve().get();
                if(((PortableTankTileEntity)tileEntity).interactWithItemFluidHandler(fluidHandler, player)){
                    stack.shrink(1);
                    if(stack.isEmpty())
                        player.setItemInHand(hand, fluidHandler.getContainer());
                    else{
                        player.setItemInHand(hand, stack);
                        if(!player.getInventory().add(fluidHandler.getContainer()))
                            player.drop(fluidHandler.getContainer(), false);
                    }
                    return InteractionResult.sidedSuccess(world.isClientSide);
                }
            }
            return InteractionResult.CONSUME;
        }else if(stack.isEmpty() && player.isCrouching()){
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if(tileEntity instanceof PortableTankTileEntity){
                if(((PortableTankTileEntity)tileEntity).toggleOutput())
                    player.displayClientMessage(TextComponents.translation("portabletanks.portable_tank.info.output_on").get(), true);
                else
                    player.displayClientMessage(TextComponents.translation("portabletanks.portable_tank.info.output_off").get(), true);
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        BlockState state = super.getStateForPlacement(context);
        CompoundTag compound = context.getItemInHand().getOrCreateTag();
        if(compound.getCompound("tileData").contains("output"))
            state = state.setValue(OUTPUT, compound.getCompound("tileData").getBoolean("output"));
        return state;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state){
        return this.type.createTileEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> entityType){
        return entityType == this.type.getTileEntityType() ?
            (world2, pos, state2, entity) -> ((PortableTankTileEntity)entity).tick() : null;
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_){
        return SHAPE.getUnderlying();
    }

    @Override
    public VoxelShape getVisualShape(BlockState p_230322_1_, BlockGetter p_230322_2_, BlockPos p_230322_3_, CollisionContext p_230322_4_){
        return BlockShape.empty().getUnderlying();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder){
        builder.add(OUTPUT);
    }
}
