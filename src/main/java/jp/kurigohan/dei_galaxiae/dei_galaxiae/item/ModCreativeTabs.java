package jp.kurigohan.dei_galaxiae.dei_galaxiae.item;

import jp.kurigohan.dei_galaxiae.dei_galaxiae.Dei_galaxiae;
import jp.kurigohan.dei_galaxiae.dei_galaxiae.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Dei_galaxiae.MODID);

    public static final RegistryObject<CreativeModeTab> MATERIALS_TAB = CREATIVE_MODE_TABS.register("dei_galaxiae_materials_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.RUBY.get()))
                    .title(Component.translatable("creativetab.materials.tab"))
                    .displayItems((p_Parameters, pOutput) -> {
                        pOutput.accept(ModItems.RUBY.get());
                        pOutput.accept(ModBlocks.RUBY_BLOCK.get());
                    })
                    .build());
    public static final RegistryObject<CreativeModeTab> NATURES_TAB = CREATIVE_MODE_TABS.register("dei_galaxiae_natures_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.RUBY_ORE.get()))
                    .title(Component.translatable("creativetab.natures.tab"))
                    .displayItems((p_Parameters, pOutput) -> {
                        pOutput.accept(ModBlocks.DEEPSLATE_RUBY_ORE.get());
                        pOutput.accept(ModBlocks.RUBY_ORE.get());
                    })
                    .build());
    public static final RegistryObject<CreativeModeTab> WEAPONS_TAB = CREATIVE_MODE_TABS.register("dei_galaxiae_weapons_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.RUBY_SWORD.get()))
                    .title(Component.translatable("creativetab.weapons.tab"))
                    .displayItems((p_Parameters, pOutput) -> {
                        pOutput.accept(ModItems.RUBY_SWORD.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> TOOLS_TAB = CREATIVE_MODE_TABS.register("dei_galaxiae_tools_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.RUBY_PICKAXE.get()))
                    .title(Component.translatable("creativetab.tools.tab"))
                    .displayItems((p_Parameters, pOutput) -> {
                        pOutput.accept(ModItems.RUBY_AXE.get());
                        pOutput.accept(ModItems.RUBY_PICKAXE.get());
                        pOutput.accept(ModItems.RUBY_SHOVEL.get());
                        pOutput.accept(ModItems.RUBY_HOE.get());
                    })
                    .build());
    public static final RegistryObject<CreativeModeTab> ARMORS_TAB = CREATIVE_MODE_TABS.register("dei_galaxiae_armors_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.RUBY_CHESTPLATE.get()))
                    .title(Component.translatable("creativetab.armors.tab"))
                    .displayItems((p_Parameters, pOutput) -> {
                        pOutput.accept(ModItems.RUBY_HELMET.get());
                        pOutput.accept(ModItems.RUBY_CHESTPLATE.get());
                        pOutput.accept(ModItems.RUBY_LEGGINGS.get());
                        pOutput.accept(ModItems.RUBY_BOOTS.get());
                    })
                    .build());

    public static void register(IEventBus eventBus)
        {
        CREATIVE_MODE_TABS.register(eventBus);
        }
}
