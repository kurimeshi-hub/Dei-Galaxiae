package jp.kurigohan.dei_galaxiae.dei_galaxiae.utils;

import jp.kurigohan.dei_galaxiae.dei_galaxiae.Dei_galaxiae;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks{
        public static final TagKey<Block> NEEDS_RUBY_TOOL = Tag("needs_ruby_tool");

        private static TagKey<Block> Tag(String name) {
            return BlockTags.create(new ResourceLocation(Dei_galaxiae.MODID, name));
        }
    }
    public static class Items{
        private static TagKey<Item> Tag(String name) {
            return ItemTags.create(new ResourceLocation(Dei_galaxiae.MODID, name));
        }
    }
}
