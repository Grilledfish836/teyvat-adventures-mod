package cn.blockforge.generated.teyvatadventures.resonance.condition;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class HydroResonanceCondition {
   private HydroResonanceCondition() {
   }

   private static boolean isMundanePotion(ItemStack stack) {
      return stack != null && !stack.m_41619_() ? stack.m_150930_(Items.f_42589_) && PotionUtils.m_43579_(stack) == Potions.f_43600_ : false;
   }

   private static boolean isRainingRain(Level level, BlockPos pos) {
      if (level == null || pos == null) {
         return false;
      } else {
         return !level.m_46471_() ? false : ((Biome)level.m_204166_(pos).m_203334_()).m_264600_(pos) == Precipitation.RAIN;
      }
   }

   private static boolean isWaterFilledCauldron(BlockState state) {
      return state != null && state.m_60713_(Blocks.f_152476_);
   }

   public static boolean matches(Level level, BlockPos pos, BlockState cauldronState, ItemStack heldItem) {
      if (cauldronState == null || pos == null) {
         return false;
      } else if (!isWaterFilledCauldron(cauldronState)) {
         return false;
      } else {
         return !isMundanePotion(heldItem) ? false : isRainingRain(level, pos);
      }
   }
}
