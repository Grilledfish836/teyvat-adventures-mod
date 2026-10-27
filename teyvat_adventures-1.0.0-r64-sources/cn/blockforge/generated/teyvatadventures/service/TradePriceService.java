package cn.blockforge.generated.teyvatadventures.service;

import cn.blockforge.generated.teyvatadventures.resonance.ResonanceNetwork;
import com.mojang.logging.LogUtils;
import java.lang.reflect.Field;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public final class TradePriceService {
   private static final String BACKUP_TAG = "teyvat_adventures_trade_backup";
   private static final String VIEWERS = "Viewers";
   private static final String OFFERS = "Offers";
   private static volatile Field merchantField;
   private static volatile boolean probed;

   private TradePriceService() {
   }

   public static void onOpen(ServerPlayer player, MerchantMenu menu, boolean applyMarkup) {
      try {
         if (!(merchantOf(menu) instanceof Entity entity)) {
            return;
         }

         CompoundTag root = entity.getPersistentData();
         CompoundTag backup = root.m_128469_("teyvat_adventures_trade_backup");
         if (backup.m_128441_("Viewers")) {
            backup.m_128405_("Viewers", backup.m_128451_("Viewers") + 1);
            root.m_128365_("teyvat_adventures_trade_backup", backup);
            resync(player, menu);
            sendHint(player, menu, backup.m_128437_("Offers", 10));
            return;
         }

         if (!applyMarkup) {
            return;
         }

         ListTag saved = applyMarkup(menu.m_40075_());
         CompoundTag fresh = new CompoundTag();
         fresh.m_128405_("Viewers", 1);
         fresh.m_128365_("Offers", saved);
         root.m_128365_("teyvat_adventures_trade_backup", fresh);
         LogUtils.getLogger().debug("[TeyvatAdventures] trade markup applied to {} offers (+20%, pure rounding).", saved.size());
         resync(player, menu);
         sendHint(player, menu, saved);
      } catch (ReflectiveOperationException | RuntimeException failure) {
         logFailure("open", failure);
      }
   }

   public static void onClosed(ServerPlayer player, MerchantMenu menu) {
      try {
         if (!(merchantOf(menu) instanceof Entity entity)) {
            return;
         }

         CompoundTag root = entity.getPersistentData();
         CompoundTag backup = root.m_128469_("teyvat_adventures_trade_backup");
         if (!backup.m_128441_("Viewers")) {
            return;
         }

         int viewers = backup.m_128451_("Viewers") - 1;
         if (viewers > 0) {
            backup.m_128405_("Viewers", viewers);
            root.m_128365_("teyvat_adventures_trade_backup", backup);
            return;
         }

         root.m_128473_("teyvat_adventures_trade_backup");
         restoreMarkup(menu.m_40075_(), backup.m_128437_("Offers", 10));
      } catch (ReflectiveOperationException | RuntimeException failure) {
         logFailure("close", failure);
      }
   }

   public static void restoreOnLoad(Entity entity) {
      try {
         if (!(entity instanceof Merchant merchant)) {
            return;
         }

         CompoundTag root = entity.getPersistentData();
         CompoundTag backup = root.m_128469_("teyvat_adventures_trade_backup");
         if (!backup.m_128441_("Viewers")) {
            return;
         }

         root.m_128473_("teyvat_adventures_trade_backup");
         restoreMarkup(merchant.m_6616_(), backup.m_128437_("Offers", 10));
      } catch (RuntimeException failure) {
         logFailure("load-restore", failure);
      }
   }

   private static void resync(ServerPlayer player, MerchantMenu menu) {
      MerchantOffers offers = menu.m_40075_();
      if (!offers.isEmpty()) {
         player.m_7662_(menu.f_38840_, offers, menu.m_40071_(), menu.m_40065_(), menu.m_40076_(), menu.m_40074_());
      }
   }

   private static ListTag applyMarkup(MerchantOffers offers) {
      ListTag saved = new ListTag();

      for (MerchantOffer offer : offers) {
         CompoundTag entry = new CompoundTag();
         ItemStack costA = offer.m_45352_();
         entry.m_128359_("ItemA", BuiltInRegistries.f_257033_.m_7981_(costA.m_41720_()).toString());
         entry.m_128405_("A", costA.m_41613_());
         costA.m_41764_(markupCount(costA));
         ItemStack costB = offer.m_45364_();
         entry.m_128359_("ItemB", BuiltInRegistries.f_257033_.m_7981_(costB.m_41720_()).toString());
         entry.m_128405_("B", costB.m_41613_());
         costB.m_41764_(markupCount(costB));
         saved.add(entry);
      }

      return saved;
   }

   private static void restoreMarkup(MerchantOffers offers, ListTag saved) {
      int size = Math.min(offers.size(), saved.size());

      for (int i = 0; i < size; i++) {
         MerchantOffer offer = (MerchantOffer)offers.get(i);
         if (offer != null) {
            CompoundTag entry = saved.m_128728_(i);
            ItemStack costA = offer.m_45352_();
            if (!costA.m_41619_() && BuiltInRegistries.f_257033_.m_7981_(costA.m_41720_()).toString().equals(entry.m_128461_("ItemA"))) {
               costA.m_41764_(Math.max(1, entry.m_128451_("A")));
            }

            ItemStack costB = offer.m_45364_();
            if (!costB.m_41619_() && BuiltInRegistries.f_257033_.m_7981_(costB.m_41720_()).toString().equals(entry.m_128461_("ItemB"))) {
               costB.m_41764_(Math.max(1, entry.m_128451_("B")));
            }
         }
      }
   }

   private static void sendHint(ServerPlayer player, MerchantMenu menu, ListTag saved) {
      MerchantOffers offers = menu.m_40075_();
      int n = Math.min(offers.size(), saved.size());
      int[] a = new int[n];
      int[] b = new int[n];

      for (int i = 0; i < n; i++) {
         MerchantOffer offer = (MerchantOffer)offers.get(i);
         if (offer != null) {
            CompoundTag entry = saved.m_128728_(i);
            ItemStack costA = offer.m_45352_();
            int origA = entry.m_128451_("A");
            if (origA > 0
               && !costA.m_41619_()
               && costA.m_41613_() != origA
               && BuiltInRegistries.f_257033_.m_7981_(costA.m_41720_()).toString().equals(entry.m_128461_("ItemA"))) {
               a[i] = origA;
            }

            ItemStack costB = offer.m_45364_();
            int origB = entry.m_128451_("B");
            if (origB > 0
               && !costB.m_41619_()
               && costB.m_41613_() != origB
               && BuiltInRegistries.f_257033_.m_7981_(costB.m_41720_()).toString().equals(entry.m_128461_("ItemB"))) {
               b[i] = origB;
            }
         }
      }

      ResonanceNetwork.sendTradeHint(player, menu.f_38840_, a, b);
   }

   private static int markupCount(ItemStack cost) {
      if (cost.m_41619_()) {
         return 0;
      }

      int n = cost.m_41613_();
      int scaled = (n * 12 + 5) / 10;
      return Math.max(1, Math.min(cost.m_41741_(), scaled));
   }

   private static Merchant merchantOf(MerchantMenu menu) throws ReflectiveOperationException {
      Field field = merchantField();
      return field == null ? null : (Merchant)field.get(menu);
   }

   private static Field merchantField() {
      Field cached = merchantField;
      if (cached == null && !probed) {
         synchronized (TradePriceService.class) {
            if (probed) {
               return merchantField;
            }

            Field found = null;

            for (Field field : MerchantMenu.class.getDeclaredFields()) {
               if (Merchant.class.isAssignableFrom(field.getType())) {
                  if (found != null) {
                     found = null;
                     break;
                  }

                  found = field;
               }
            }

            if (found != null) {
               try {
                  found.setAccessible(true);
               } catch (RuntimeException failure) {
                  found = null;
               }
            }

            merchantField = found;
            probed = true;
            if (found == null) {
               LogUtils.getLogger()
                  .warn("[TeyvatAdventures] TradePriceService: merchant field not found on MerchantMenu, trade markup disabled (vanilla prices kept).");
            }

            return found;
         }
      } else {
         return cached;
      }
   }

   private static void logFailure(String stage, Throwable failure) {
      LogUtils.getLogger().error("[TeyvatAdventures] TradePriceService failed during " + stage + "; vanilla price behaviour kept.", failure);
   }
}
