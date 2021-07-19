package com.dremoline.portabletanks;

import com.supermartijn642.core.block.BaseBlock;
import com.supermartijn642.core.block.BlockShape;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class PortableTanksBlock extends BaseBlock {
    private static final BlockShape SHAPE = BlockShape.createBlockShape(2,0,2,14,16,14);
    public PortableTanksBlock(String registryName,Properties properties) {
        super(registryName, true, properties);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE.getUnderlying();
    }
}
