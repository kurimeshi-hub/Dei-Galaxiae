package jp.kurigohan.dei_galaxiae.dei_galaxiae;

import jp.kurigohan.dei_galaxiae.dei_galaxiae.block.ModBlocks;
import jp.kurigohan.dei_galaxiae.dei_galaxiae.item.ModCreativeTabs;
import jp.kurigohan.dei_galaxiae.dei_galaxiae.item.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Dei_galaxiae.MODID)
public class Dei_galaxiae {
    // MODのID（すべて小文字推奨）
    public static final String MODID = "dei_galaxiae";

    // ロガー
    private static final Logger LOGGER = LogManager.getLogger();

    public Dei_galaxiae() {
        // MODイベントバスの取得
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        LOGGER.info("Dei Galaxiae MOD initialized!");
    }


    private void addCreative(BuildCreativeModeTabContentsEvent event){
        //クリエイティブタブ
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            //event.acceptはここに入れていいよって意味
            event.accept(ModItems.RUBY);
        }
    };
}