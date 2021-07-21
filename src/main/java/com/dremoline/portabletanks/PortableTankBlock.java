package com.dremoline.portabletanks;

import com.supermartijn642.core.TextComponents;
import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.block.BlockShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;

public class PortableTankBlock extends BaseBlock {

    public static final BlockShape SHAPE = BlockShape.createBlockShape(2.5, 0, 2.5, 13.5, 16, 13.5);
    public static final BooleanProperty OUTPUT = BooleanProperty.create("output");

    public final PortableTankType type;

    public PortableTankBlock(PortableTankType type){
        super(type.getRegistryName(), true, type.getBlockProperties());
        this.type = type;
        this.registerDefaultState(this.defaultBlockState().setValue(OUTPUT, false));
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult){
        ItemStack stack = player.getItemInHand(hand);
        LazyOptional<IFluidHandlerItem> fluidHandlerOptional = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
        if(fluidHandlerOptional.isPresent()){
            TileEntity tileEntity = world.getBlockEntity(pos);
            if(tileEntity instanceof PortableTankTileEntity){
                IFluidHandlerItem fluidHandler = fluidHandlerOptional.resolve().get();
                if(((PortableTankTileEntity)tileEntity).interactWithItemFluidHandler(fluidHandler)){
                    player.setItemInHand(hand, fluidHandler.getContainer());
                    return ActionResultType.sidedSuccess(world.isClientSide);
                }
            }
            return ActionResultType.CONSUME;
        }else if(stack.isEmpty() && player.isCrouching()){
            TileEntity tileEntity = world.getBlockEntity(pos);
            if(tileEntity instanceof PortableTankTileEntity){
                if(((PortableTankTileEntity)tileEntity).toggleOutput())
                    player.displayClientMessage(TextComponents.translation("portabletanks.portable_tank.info.output_on").get(), true);
                else
                    player.displayClientMessage(TextComponents.translation("portabletanks.portable_tank.info.output_off").get(), true);
                return ActionResultType.sidedSuccess(world.isClientSide);
            }
        }
        return ActionResultType.PASS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context){
        BlockState state = super.getStateForPlacement(context);
        CompoundNBT compound = context.getItemInHand().getOrCreateTag();
        if(compound.getCompound("tileData").contains("output"))
            state = state.setValue(OUTPUT, compound.getCompound("tileData").getBoolean("output"));
        return state;
    }

    @Override
    public boolean hasTileEntity(BlockState state){
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world){
        return this.type.createTileEntity();
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_){
        return SHAPE.getUnderlying();
    }

    @Override
    public VoxelShape getVisualShape(BlockState p_230322_1_, IBlockReader p_230322_2_, BlockPos p_230322_3_, ISelectionContext p_230322_4_){
        return BlockShape.empty().getUnderlying();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block,BlockState> builder){
        builder.add(OUTPUT);
    }
}
