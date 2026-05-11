package jp.kurigohan.dei_galaxiae.dei_galaxiae.datagen;

import jp.kurigohan.dei_galaxiae.dei_galaxiae.Dei_galaxiae;
import jp.kurigohan.dei_galaxiae.dei_galaxiae.worldgen.ModBiomeModifier;
import jp.kurigohan.dei_galaxiae.dei_galaxiae.worldgen.ModWorldGen;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

@Mod.EventBusSubscriber(modid = Dei_galaxiae.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDatapackRegistries {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModWorldGen::bootstrapConfigured)
            .add(Registries.PLACED_FEATURE,     ModWorldGen::bootstrapPlaced)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifier::bootstrap);

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                event.includeServer(),
                new DatapackBuiltinEntriesProvider(
                        event.getGenerator().getPackOutput(),
                        event.getLookupProvider(),
                        BUILDER,
                        Set.of(Dei_galaxiae.MODID)
                )
        );
    }
}