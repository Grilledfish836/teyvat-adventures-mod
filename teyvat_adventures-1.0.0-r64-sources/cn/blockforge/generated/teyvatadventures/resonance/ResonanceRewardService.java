package cn.blockforge.generated.teyvatadventures.resonance;

import cn.blockforge.generated.teyvatadventures.CuriosCompat;
import cn.blockforge.generated.teyvatadventures.element.Element;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public final class ResonanceRewardService {
   private static final ResonanceTrigger TRIGGER = new ResonanceTrigger();

   private ResonanceRewardService() {
   }

   public static ResonanceTrigger trigger() {
      return TRIGGER;
   }

   public static boolean grantOnce(Player player, Element element) {
      return grantOnce(player, element, true);
   }

   public static boolean grantOnce(Player player, Element element, boolean giveItem) {
      if (player == null || element == null || player.m_9236_().m_5776_()) {
         return false;
      }

      if (!CuriosCompat.hasCoreInSeal(player)) {
         return false;
      }

      if (PlayerElementData.hasQualification(player, element)) {
         return false;
      }

      PlayerElementData.setQualification(player, element, true);
      PlayerElementData.setLegacyStage(player, element, 0);
      if (giveItem) {
         giveResonanceItem(player, element);
      }

      triggerAchievement(player, element);
      ResonanceNetwork.syncTo(player);
      return true;
   }

   private static void giveResonanceItem(Player player, Element element) {
      ItemStack stack = new ItemStack((ItemLike)element.item().get());
      ResonanceItemStageService.applyStage(stack, PlayerElementData.getStage(player, element));
      if (!player.m_150109_().m_36054_(stack)) {
         player.m_36176_(stack, false);
      }
   }

   public static void giveItemOnly(Player player, Element element) {
      if (player != null && element != null && !player.m_9236_().m_5776_()) {
         if (CuriosCompat.hasCoreInSeal(player)) {
            giveResonanceItem(player, element);
            ResonanceNetwork.syncTo(player);
         }
      }
   }

   public static boolean hasQualification(Player player, Element element) {
      return PlayerElementData.hasQualification(player, element);
   }

   private static void triggerAchievement(Player player, Element element) {
      if (player instanceof ServerPlayer serverPlayer) {
         TRIGGER.trigger(serverPlayer, element);
      }
   }
}
