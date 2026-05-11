package jp.kurigohan.dei_galaxiae.dei_galaxiae.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import jp.kurigohan.dei_galaxiae.dei_galaxiae.Dei_galaxiae;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
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

public class ModArmorItem extends ArmorItem {

    // 部位ごとの MaxHealth UUID (+1ハート × 4部位 = +4ハート)
    private static final Map<ArmorItem.Type, UUID> PIECE_HEALTH_UUIDS = ImmutableMap.of(
            Type.HELMET,     UUID.fromString("A1B2C3D4-0001-0000-0000-000000000001"),
            Type.CHESTPLATE, UUID.fromString("A1B2C3D4-0002-0000-0000-000000000002"),
            Type.LEGGINGS,   UUID.fromString("A1B2C3D4-0003-0000-0000-000000000003"),
            Type.BOOTS,      UUID.fromString("A1B2C3D4-0004-0000-0000-000000000004")
    );

    // ハート1個 = 2.0 (ハーフハート単位)
    private static final double HEALTH_PER_PIECE = 2.0;

    // フルセットボーナス用 UUID (+1ハート)
    private static final UUID   FULLSET_HEALTH_UUID  = UUID.fromString("A1B2C3D4-0005-0000-0000-000000000005");
    private static final double FULLSET_HEALTH_BONUS = 2.0;

    // persistentData キー
    private static final String NBT_REGEN_ACTIVE = "ruby_set_regen";
    private static final String NBT_HP_ACTIVE    = "ruby_set_hp";

    public ModArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    // 部位ごとの MaxHealth +1ハート
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> modifiers =
                HashMultimap.create(super.getDefaultAttributeModifiers(slot));

        if (slot == this.type.getSlot()) {
            UUID uuid = PIECE_HEALTH_UUIDS.get(this.type);
            if (uuid != null) {
                modifiers.put(
                        Attributes.MAX_HEALTH,
                        new AttributeModifier(
                                uuid,
                                "ruby_piece_health_" + this.type.getName(),
                                HEALTH_PER_PIECE,
                                AttributeModifier.Operation.ADDITION
                        )
                );
            }
        }
        return modifiers;
    }

    // ツールチップ
    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level level,
            List<Component> tooltipComponents,
            TooltipFlag isAdvanced
    ) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);

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

    // フルセットボーナス管理
    // @Mod.EventBusSubscriber により Forge が自動登録。Dei_galaxiae.java への追記不要。
    @Mod.EventBusSubscriber(modid = Dei_galaxiae.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class RubySetTickHandler {

        private static final int REGEN_DURATION  = 60;
        private static final int REGEN_AMPLIFIER = 1;  // Regeneration II

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;
            if (event.player.level().isClientSide()) return;

            Player player     = event.player;
            CompoundTag tag   = player.getPersistentData();
            boolean isFullSet = ArmorSetHelper.countEquippedRubyPieces(player) >= 4;

            // フルセット HP ボーナス (+1ハート) の付け外し
            AttributeInstance healthAttr = player.getAttribute(Attributes.MAX_HEALTH);
            if (healthAttr != null) {
                boolean hasBonus = healthAttr.getModifier(FULLSET_HEALTH_UUID) != null;

                if (isFullSet && !hasBonus) {
                    healthAttr.addPermanentModifier(new AttributeModifier(
                            FULLSET_HEALTH_UUID,
                            "ruby_fullset_health",
                            FULLSET_HEALTH_BONUS,
                            AttributeModifier.Operation.ADDITION
                    ));
                    tag.putBoolean(NBT_HP_ACTIVE, true);

                } else if (!isFullSet && hasBonus) {
                    healthAttr.removeModifier(FULLSET_HEALTH_UUID);
                    if (player.getHealth() > healthAttr.getValue()) {
                        player.setHealth((float) healthAttr.getValue());
                    }
                    tag.remove(NBT_HP_ACTIVE);
                }
            }

            // Regeneration II の付け外し
            if (isFullSet) {
                MobEffectInstance current = player.getEffect(MobEffects.REGENERATION);
                if (current == null || current.getDuration() <= 20) {
                    player.addEffect(new MobEffectInstance(
                            MobEffects.REGENERATION,
                            REGEN_DURATION,
                            REGEN_AMPLIFIER,
                            false,  // ambient
                            false   // showParticles
                    ));
                    tag.putBoolean(NBT_REGEN_ACTIVE, true);
                }
            } else {
                if (tag.getBoolean(NBT_REGEN_ACTIVE)) {
                    player.removeEffect(MobEffects.REGENERATION);
                    tag.remove(NBT_REGEN_ACTIVE);
                }
            }
        }
    }
}