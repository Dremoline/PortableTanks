package com.dremoline.portabletanks;

import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.data.event.GatherDataEvent;

public class PortableTankBlockTagsProvider extends BlockTagsProvider {

    public PortableTankBlockTagsProvider(GatherDataEvent e){
        super(e.getGenerator(), "portabletanks", e.getExistingFileHelper());
    }

    @Override
    protected void addTags(){
        TagsProvider.TagAppender<Block> pickaxeTag = this.tag(BlockTags.MINEABLE_WITH_PICKAXE);
        for(PortableTankType type : PortableTankType.values()){
            pickaxeTag.add(type.getBlock());
        }
    }
}
