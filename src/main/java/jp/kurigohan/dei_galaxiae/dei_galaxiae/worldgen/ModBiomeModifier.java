package jp.kurigohan.dei_galaxiae.dei_galaxiae.worldgen;

import jp.kurigohan.dei_galaxiae.dei_galaxiae.Dei_galaxiae;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBiomeModifier {

    public static final ResourceKey<BiomeModifier> ADD_RUBY_ORE =
            ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS,
                    new ResourceLocation(Dei_galaxiae.MODID, "add_ruby_ore"));

    public static final ResourceKey<BiomeModifier> ADD_DEEPSLATE_RUBY_ORE =
            ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS,
                    new ResourceLocation(Dei_galaxiae.MODID, "add_deepslate_ruby_ore"));

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes         = context.lookup(Registries.BIOME);

        // オーバーワールド全バイオームに生成
        context.register(ADD_RUBY_ORE,
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                        HolderSet.direct(placedFeatures.getOrThrow(ModWorldGen.RUBY_ORE_PLACED_KEY)),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                ));

        context.register(ADD_DEEPSLATE_RUBY_ORE,
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                        HolderSet.direct(placedFeatures.getOrThrow(ModWorldGen.DEEPSLATE_RUBY_ORE_PLACED_KEY)),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                ));
    }
}