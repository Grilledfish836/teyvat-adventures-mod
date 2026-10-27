package cn.blockforge.generated.teyvatadventures.resonance;

import cn.blockforge.generated.teyvatadventures.CuriosCompat;
import cn.blockforge.generated.teyvatadventures.ElementalResonanceItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class ResonanceItemStageService {
   public static final String TAG_STAGE = "teyvat_stage";

   private ResonanceItemStageService() {
   }

   public static void applyStage(ItemStack stack, Stage stage) {
      if (stack != null && !stack.m_41619_() && stack.m_41720_() instanceof ElementalResonanceItem) {
         CompoundTag tag = stack.m_41784_();
         if (!stage.name().equals(tag.m_128461_("teyvat_stage"))) {
            tag.m_128359_("teyvat_stage", stage.name());
         }
      }
   }

   public static void refresh(Player player) {
      if (player != null && !player.m_9236_().m_5776_()) {
         PlayerElementData data = PlayerElementData.peek(player);
         if (data != null) {
            for (int slot = 0; slot < player.m_150109_().m_6643_(); slot++) {
               applyFrom(data, player.m_150109_().m_8020_(slot));
            }

            CuriosCompat.forEachWornStack(player, stack -> applyFrom(data, stack));
         }
      }
   }

   private static void applyFrom(PlayerElementData data, ItemStack stack) {
      if (stack != null && !stack.m_41619_() && stack.m_41720_() instanceof ElementalResonanceItem item) {
         applyStage(stack, data.stageOfElement(item.getElement()));
      }
   }
}
