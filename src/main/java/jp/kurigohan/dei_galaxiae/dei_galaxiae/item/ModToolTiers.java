package jp.kurigohan.dei_galaxiae.dei_galaxiae.item;

import jp.kurigohan.dei_galaxiae.dei_galaxiae.Dei_galaxiae;
import jp.kurigohan.dei_galaxiae.dei_galaxiae.utils.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ModToolTiers {
    public static final Tier RUBY = TierSortingRegistry.registerTier(
            new ForgeTier(
                    3,
                    1000,
                    6f,
                    2.5f,
                    25,
                    ModTags.Blocks.NEEDS_RUBY_TOOL,
                    () -> Ingredient.of(ModItems.RUBY.get())
            ),
            new ResourceLocation(Dei_galaxiae.MODID, "ruby"),
            List.of(Tiers.IRON),
            List.of()
    );
}