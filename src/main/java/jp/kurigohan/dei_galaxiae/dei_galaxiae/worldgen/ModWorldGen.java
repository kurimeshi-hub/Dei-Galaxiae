package jp.kurigohan.dei_galaxiae.dei_galaxiae.worldgen;

import jp.kurigohan.dei_galaxiae.dei_galaxiae.Dei_galaxiae;
import jp.kurigohan.dei_galaxiae.dei_galaxiae.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModWorldGen {

    // ConfiguredFeature キー
    public static final ResourceKey<ConfiguredFeature<?, ?>> RUBY_ORE_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    new ResourceLocation(Dei_galaxiae.MODID, "ruby_ore"));

    public static final ResourceKey<ConfiguredFeature<?, ?>> DEEPSLATE_RUBY_ORE_KEY =
            ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    new ResourceLocation(Dei_galaxiae.MODID, "deepslate_ruby_ore"));

    // PlacedFeature キー
    public static final ResourceKey<PlacedFeature> RUBY_ORE_PLACED_KEY =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    new ResourceLocation(Dei_galaxiae.MODID, "ruby_ore_placed"));

    public static final ResourceKey<PlacedFeature> DEEPSLATE_RUBY_ORE_PLACED_KEY =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    new ResourceLocation(Dei_galaxiae.MODID, "deepslate_ruby_ore_placed"));

    // ConfiguredFeature 登録
    public static void bootstrapConfigured(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables     = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> rubyTargets = List.of(
                OreConfiguration.target(stoneReplaceables,
                        ModBlocks.RUBY_ORE.get().defaultBlockState()));

        List<OreConfiguration.TargetBlockState> deepslateRubyTargets = List.of(
                OreConfiguration.target(deepslateReplaceables,
                        ModBlocks.DEEPSLATE_RUBY_ORE.get().defaultBlockState()));

        // veinSize=8 はダイヤモンドと同じ値
        context.register(RUBY_ORE_KEY,
                new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(rubyTargets, 8)));
        context.register(DEEPSLATE_RUBY_ORE_KEY,
                new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(deepslateRubyTargets, 8)));
    }

    // PlacedFeature 登録
    public static void bootstrapPlaced(BootstapContext<PlacedFeature> context) {
        var cf = context.lookup(Registries.CONFIGURED_FEATURE);

        // ダイヤモンドと同等: count=7, Y=-64〜16 の三角分布
        context.register(RUBY_ORE_PLACED_KEY,
                new PlacedFeature(cf.getOrThrow(RUBY_ORE_KEY), List.of(
                        CountPlacement.of(7),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(0),
                                VerticalAnchor.absolute(32)),
                        BiomeFilter.biome()
                )));

        context.register(DEEPSLATE_RUBY_ORE_PLACED_KEY,
                new PlacedFeature(cf.getOrThrow(DEEPSLATE_RUBY_ORE_KEY), List.of(
                        CountPlacement.of(7),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.triangle(
                                VerticalAnchor.absolute(-64),
                                VerticalAnchor.absolute(0)),
                        BiomeFilter.biome()
                )));
    }
}