package cn.blockforge.generated.teyvatadventures.resonance.condition;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class PyroResonanceCondition {
   private PyroResonanceCondition() {
   }

   public static boolean isIronSword(ItemStack stack) {
      return stack != null && stack.m_150930_(Items.f_42383_);
   }
}
