package jp.kurigohan.dei_galaxiae.dei_galaxiae.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import jp.kurigohan.dei_galaxiae.dei_galaxiae.utils.SetBonus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Ruby Armor アイテム基底クラス。
 *
 * ─ 変更点 ──────────────────────────────────────────────────────────────────
 * 1. appendHoverText で FullSet Bonus ツールチップを表示。
 *    装備数に応じて (X/4) のカラーが変化。
 *
 * 2. MAX_HEALTH +2 は「装備1個ごと」に付与済みのため変更なし。
 *    (各スロットに UUID を分けて AttributeModifier を登録している既存実装を維持)
 *
 * 3. フルセット装備で Regeneration II を継続付与するために
 *    ServerPlayerTick イベントを inner class で購読。
 *    → Dei_galaxiae.java の forge EventBus に RubySetTickHandler を register すること。
 * ──────────────────────────────────────────────────────────────────────────
 */
public class ModArmorItem extends ArmorItem {

    // ── UUID (既存のまま変更なし) ─────────────────────────────────────────────
    private static final Map<ArmorItem.Type, UUID> ARMOR_MODIFIERS = ImmutableMap.of(
            Type.HELMET,     UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"),
            Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
            Type.LEGGINGS,   UUID.fromString("D2019F14-878C-422C-84D7-6961A3511247"),
            Type.BOOTS,      UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B20")
    );

    public ModArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    // ── AttributeModifier (既存のまま変更なし) ────────────────────────────────
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> modifiers =
                HashMultimap.create(super.getDefaultAttributeModifiers(slot));

        if (slot == this.type.getSlot()) {
            UUID uuid = ARMOR_MODIFIERS.get(this.type);
            if (uuid != null) {
                modifiers.put(Attributes.MAX_HEALTH,
                        new AttributeModifier(uuid, "Armor health boost",
                                2.0, AttributeModifier.Operation.ADDITION));
            }
        }
        return modifiers;
    }

    // ── ツールチップ注入 ──────────────────────────────────────────────────────
    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level level,
            List<Component> tooltipComponents,
            TooltipFlag isAdvanced
    ) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);

        // 装備枚数を取得 (クライアント側のみ; サーバーや level==null では 0)
        int equipped = 0;
        if (level != null && level.isClientSide()) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                equipped = ArmorSetHelper.countEquippedRubyPieces(player);
            }
        }

        tooltipComponents.add(Component.empty());
        tooltipComponents.addAll(SetBonus.buildTooltipLines(equipped));
    }

    // ── フルセット時の Regeneration II 付与 (サーバー Tick) ───────────────────
    /**
     * Forge の MinecraftForge.EVENT_BUS に登録するハンドラ。
     *
     * 登録方法 (Dei_galaxiae.java のコンストラクタ等に追記):
     *   MinecraftForge.EVENT_BUS.register(new ModArmorItem.RubySetTickHandler());
     */
    public static class RubySetTickHandler {

        // Regeneration II (amplifier=1), 60tick持続, パーティクル非表示
        private static final int REGEN_DURATION  = 60;  // tick (3秒ごとに再付与)
        private static final int REGEN_AMPLIFIER = 1;   // Regeneration II = amplifier 1

        @SubscribeEvent
        public void onPlayerTick(TickEvent.PlayerTickEvent event) {
            // サーバー側・END フェーズのみ処理
            if (event.phase != TickEvent.Phase.END) return;
            if (event.player.level().isClientSide()) return;

            Player player = event.player;
            int equipped  = ArmorSetHelper.countEquippedRubyPieces(player);

            if (equipped >= 4) {
                // 残り時間が短くなったら再付与 (途切れを防ぐ)
                MobEffectInstance current = player.getEffect(MobEffects.REGENERATION);
                if (current == null || current.getDuration() <= 20) {
                    player.addEffect(new MobEffectInstance(
                            MobEffects.REGENERATION,
                            REGEN_DURATION,
                            REGEN_AMPLIFIER,
                            false,  // ambient
                            false   // パーティクル非表示 (true にすると渦巻きが出る)
                    ));
                }
            }
            // フルセットでなくなったら Regen を即解除
            else {
                player.removeEffect(MobEffects.REGENERATION);
            }
        }
    }
}