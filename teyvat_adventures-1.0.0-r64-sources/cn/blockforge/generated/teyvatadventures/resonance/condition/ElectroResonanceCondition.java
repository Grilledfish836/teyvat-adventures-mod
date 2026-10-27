package cn.blockforge.generated.teyvatadventures.resonance.condition;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public final class ElectroResonanceCondition {
   private ElectroResonanceCondition() {
   }

   public static boolean isDroppedObsidian(ItemStack stack) {
      return !stack.m_41619_() && stack.m_150930_(Blocks.f_50080_.m_5456_());
   }

   public static boolean isThunderstorm(Level level) {
      return level != null && level.m_46470_();
   }
}
