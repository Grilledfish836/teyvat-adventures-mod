package cn.blockforge.generated.teyvatadventures.resonance.condition;

import cn.blockforge.generated.teyvatadventures.GeneratedMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;

public final class CryoResonanceCondition {
   private CryoResonanceCondition() {
   }

   public static boolean hasFreezeLevel(Player player) {
      if (player == null) {
         return false;
      }

      MobEffectInstance effect = player.m_21124_((MobEffect)GeneratedMod.FREEZE_EFFECT.get());
      return effect != null && effect.m_19564_() >= 10;
   }

   public static boolean isHostile(LivingEntity victim) {
      return victim instanceof Enemy;
   }

   public static boolean withinRange(Player player, LivingEntity victim) {
      return player != null && victim != null ? player.m_20280_(victim) <= 256.0 : false;
   }

   public static boolean matches(Player player, LivingEntity victim) {
      return player != null && victim != null && hasFreezeLevel(player) && isHostile(victim) && withinRange(player, victim);
   }
}
