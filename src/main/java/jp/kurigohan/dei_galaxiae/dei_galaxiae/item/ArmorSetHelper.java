package jp.kurigohan.dei_galaxiae.dei_galaxiae.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * プレイヤーが装備中のルビーアーマー枚数を返すユーティリティ。
 * ModItems の RegistryObject を直接参照するため、他パッケージ不要。
 */
public class ArmorSetHelper {

    private ArmorSetHelper() {}

    /**
     * @param player 対象プレイヤー
     * @return 装備中のルビーアーマーの数 (0〜4)
     */
    public static int countEquippedRubyPieces(Player player) {
        int count = 0;
        for (ItemStack stack : player.getArmorSlots()) {
            if (isRubyArmor(stack)) count++;
        }
        return count;
    }

    private static boolean isRubyArmor(ItemStack stack) {
        return stack.is(ModItems.RUBY_HELMET.get())
                || stack.is(ModItems.RUBY_CHESTPLATE.get())
                || stack.is(ModItems.RUBY_LEGGINGS.get())
                || stack.is(ModItems.RUBY_BOOTS.get());
    }
}
