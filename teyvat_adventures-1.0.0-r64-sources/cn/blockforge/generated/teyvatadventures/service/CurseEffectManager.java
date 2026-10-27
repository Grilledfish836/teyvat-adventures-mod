package cn.blockforge.generated.teyvatadventures.service;

import cn.blockforge.generated.teyvatadventures.GeneratedMod;
import java.util.UUID;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;

public final class CurseEffectManager {
   private static final UUID FREEZE_SPEED_UUID = UUID.fromString("7f0cf6a0-0b70-4a2e-a5e8-000000000023");
   private static final UUID FREEZE_ATTACK_SPEED_UUID = UUID.fromString("7f0cf6a0-0b70-4a2e-a5e8-000000000024");
   private static final UUID FREEZE_ARMOR_UUID = UUID.fromString("7f0cf6a0-0b70-4a2e-a5e8-000000000025");
   private static final UUID FREEZE_FLYING_SPEED_UUID = UUID.fromString("7f0cf6a0-0b70-4a2e-a5e8-000000000026");

   private CurseEffectManager() {
   }

   public static void applyFreeze(LivingEntity entity, int amplifier) {
      if (entity != null && !entity.m_213877_()) {
         int level = Math.min(34, Math.max(1, amplifier + 1));
         double speedPenalty = Math.max(-0.99, -0.05 * level);
         setModifier(entity.m_21051_(Attributes.f_22279_), FREEZE_SPEED_UUID, "World Beyond Descender: Frozen movement speed", speedPenalty);
         setModifier(entity.m_21051_(Attributes.f_22283_), FREEZE_ATTACK_SPEED_UUID, "World Beyond Descender: Frozen attack speed", speedPenalty);
      }
   }

   public static void clearFreeze(LivingEntity entity) {
      if (entity != null && !entity.m_213877_()) {
         removeModifier(entity.m_21051_(Attributes.f_22279_), FREEZE_SPEED_UUID);
         removeModifier(entity.m_21051_(Attributes.f_22283_), FREEZE_ATTACK_SPEED_UUID);
         removeModifier(entity.m_21051_(Attributes.f_22284_), FREEZE_ARMOR_UUID);
         removeModifier(entity.m_21051_(Attributes.f_22280_), FREEZE_FLYING_SPEED_UUID);
      }
   }

   public static void removeLegacyFlyingSpeedModifier(LivingEntity entity) {
      if (entity != null && !entity.m_213877_()) {
         removeModifier(entity.m_21051_(Attributes.f_22280_), FREEZE_FLYING_SPEED_UUID);
      }
   }

   public static void reconcile(LivingEntity entity) {
      if (entity != null && !entity.m_213877_() && entity.m_6084_()) {
         MobEffectInstance freeze = entity.m_21124_((MobEffect)GeneratedMod.FREEZE_EFFECT.get());
         if (freeze == null) {
            clearFreeze(entity);
         } else {
            applyFreeze(entity, freeze.m_19564_());
         }
      }
   }

   private static void setModifier(AttributeInstance attribute, UUID id, String name, double amount) {
      if (attribute != null) {
         AttributeModifier existing = attribute.m_22111_(id);
         if (existing == null || Double.compare(existing.m_22218_(), amount) != 0) {
            if (existing != null) {
               attribute.m_22120_(id);
            }

            try {
               attribute.m_22118_(new AttributeModifier(id, name, amount, Operation.MULTIPLY_TOTAL));
            } catch (IllegalArgumentException duplicated) {
               try {
                  attribute.m_22120_(id);
                  attribute.m_22118_(new AttributeModifier(id, name, amount, Operation.MULTIPLY_TOTAL));
               } catch (IllegalArgumentException var8) {
               }
            }
         }
      }
   }

   private static void removeModifier(AttributeInstance attribute, UUID id) {
      if (attribute != null) {
         attribute.m_22120_(id);
      }
   }
}
