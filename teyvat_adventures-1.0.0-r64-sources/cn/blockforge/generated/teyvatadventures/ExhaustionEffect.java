package cn.blockforge.generated.teyvatadventures;

import java.util.UUID;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;

public final class ExhaustionEffect extends MobEffect {
   private static final UUID SPEED_UUID = UUID.fromString("7f0cf6a0-0b70-4a2e-a5e8-000000000030");

   public ExhaustionEffect() {
      super(MobEffectCategory.HARMFUL, 7829367);
      this.m_19472_(Attributes.f_22279_, SPEED_UUID.toString(), -0.25, Operation.MULTIPLY_TOTAL);
   }

   public void m_6742_(LivingEntity entity, int amplifier) {
      super.m_6742_(entity, amplifier);
      if (entity instanceof Player player && player.m_20142_()) {
         player.m_6858_(false);
      }
   }

   public boolean m_6584_(int duration, int amplifier) {
      return true;
   }
}
