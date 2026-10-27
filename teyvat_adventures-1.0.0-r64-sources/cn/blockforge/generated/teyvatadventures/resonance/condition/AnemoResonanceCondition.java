package cn.blockforge.generated.teyvatadventures.resonance.condition;

import cn.blockforge.generated.teyvatadventures.GeneratedMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;

public final class AnemoResonanceCondition {
   private AnemoResonanceCondition() {
   }

   public static boolean matches(AbstractArrow arrow, Entity hitEntity, Player player) {
      if (arrow != null && player != null && hitEntity == player) {
         if (arrow.m_19749_() != player) {
            return false;
         } else {
            return !player.m_21023_((MobEffect)GeneratedMod.EXHAUSTION_EFFECT.get()) ? false : player.m_20186_() > 160.0;
         }
      } else {
         return false;
      }
   }
}
