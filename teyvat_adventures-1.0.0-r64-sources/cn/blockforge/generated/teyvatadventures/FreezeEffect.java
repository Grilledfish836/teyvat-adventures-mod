package cn.blockforge.generated.teyvatadventures;

import cn.blockforge.generated.teyvatadventures.service.CurseEffectManager;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public final class FreezeEffect extends MobEffect {
   public FreezeEffect() {
      super(MobEffectCategory.HARMFUL, 9230312);
   }

   public void m_6742_(LivingEntity entity, int amplifier) {
      CurseEffectManager.applyFreeze(entity, amplifier);
   }

   public boolean m_6584_(int duration, int amplifier) {
      return true;
   }
}
