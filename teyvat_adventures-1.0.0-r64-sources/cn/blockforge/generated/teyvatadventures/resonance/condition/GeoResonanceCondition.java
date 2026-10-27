package cn.blockforge.generated.teyvatadventures.resonance.condition;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class GeoResonanceCondition {
   private GeoResonanceCondition() {
   }

   public static boolean isGravityBlock(BlockState state) {
      return state != null && state.m_60734_() instanceof FallingBlock;
   }

   public static boolean isFallSuffocationSource(DamageSource source) {
      return source == null
         ? false
         : source.m_276093_(DamageTypes.f_268612_) || source.m_276093_(DamageTypes.f_268659_) || source.m_276093_(DamageTypes.f_268526_);
   }

   public static boolean isValidDeath(Entity victim, DamageSource source, boolean gravityContextActive) {
      if (!(victim instanceof Creeper)) {
         return false;
      } else {
         return !isFallSuffocationSource(source) ? false : gravityContextActive;
      }
   }
}
