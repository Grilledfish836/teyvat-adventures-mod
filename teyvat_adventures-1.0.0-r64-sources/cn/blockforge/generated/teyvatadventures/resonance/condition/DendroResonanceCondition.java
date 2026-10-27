package cn.blockforge.generated.teyvatadventures.resonance.condition;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public final class DendroResonanceCondition {
   private DendroResonanceCondition() {
   }

   public static boolean isGrassBlock(ItemStack stack) {
      return stack != null && stack.m_150930_(Blocks.f_50440_.m_5456_());
   }

   public static boolean holdsGrass(Player player) {
      return grassHand(player) != null;
   }

   public static InteractionHand grassHand(Player player) {
      if (player == null) {
         return null;
      } else if (isGrassBlock(player.m_21205_())) {
         return InteractionHand.MAIN_HAND;
      } else {
         return isGrassBlock(player.m_21206_()) ? InteractionHand.OFF_HAND : null;
      }
   }
}
