package cn.blockforge.generated.teyvatadventures.resonance;

import cn.blockforge.generated.teyvatadventures.ElementalResonanceItem;
import cn.blockforge.generated.teyvatadventures.element.Element;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ElementResonanceHandler {
   public static final int ANEMO_GOAT_HORN_TARGET = 6;
   private static final Set<UUID> PENDING_REFRESH = ConcurrentHashMap.newKeySet();

   private ElementResonanceHandler() {
   }

   @SubscribeEvent
   public static void onHornPlayed(RightClickItem event) {
      Level level = event.getLevel();
      if (level != null && !level.m_5776_() && level instanceof ServerLevel serverLevel) {
         if (serverLevel.m_46472_().equals(Level.f_46428_)) {
            Player player = event.getEntity();
            if (!(player.m_20186_() < serverLevel.m_151558_() - 1)) {
               ItemStack held = event.getItemStack();
               if (!held.m_41619_() && held.m_41720_() instanceof InstrumentItem) {
                  String instrumentId = ElementTargets.resolveInstrumentId(held);
                  if (instrumentId != null) {
                     PlayerElementData.addTrackedItem(player, Element.ANEMO, "anemo_goat_horns", instrumentId);
                     if (PlayerElementData.getTrackedItems(player, Element.ANEMO, "anemo_goat_horns").size() >= 6
                        && PlayerElementData.getStage(player, Element.ANEMO) == Stage.EXCLUSION) {
                        PlayerElementData.advanceStage(player, Element.ANEMO);
                     }
                  }
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
      requestStageRefresh(event.getEntity());
   }

   @SubscribeEvent
   public static void onPlayerRespawn(PlayerRespawnEvent event) {
      requestStageRefresh(event.getEntity());
   }

   @SubscribeEvent
   public static void onPlayerClone(Clone event) {
      PlayerElementData.copyFrom(event.getEntity(), event.getOriginal());
      requestStageRefresh(event.getEntity());
   }

   @SubscribeEvent
   public static void onChangedDimension(PlayerChangedDimensionEvent event) {
      requestStageRefresh(event.getEntity());
   }

   @SubscribeEvent
   public static void onPlayerLoggedOut(PlayerLoggedOutEvent event) {
      UUID uuid = event.getEntity().m_20148_();
      ElementSyncScheduler.forget(uuid);
      PENDING_REFRESH.remove(uuid);
   }

   @SubscribeEvent
   public static void onItemPickup(ItemPickupEvent event) {
      Player player = event.getEntity();
      if (!player.m_9236_().m_5776_()) {
         if (event.getStack().m_41720_() instanceof ElementalResonanceItem) {
            ResonanceItemStageService.refresh(player);
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerTick(PlayerTickEvent event) {
      if (event.phase == Phase.END) {
         Player player = event.player;
         if (!player.m_9236_().m_5776_() && player.m_6084_() && !player.m_213877_()) {
            if (player.m_9236_().m_46467_() % 20L == 0L) {
               ResonanceItemStageService.refresh(player);
            }
         }
      }
   }

   private static void requestStageRefresh(Player player) {
      if (player != null && !player.m_9236_().m_5776_()) {
         PENDING_REFRESH.add(player.m_20148_());
      }
   }

   @SubscribeEvent
   public static void onServerTickEnd(ServerTickEvent event) {
      if (event.phase == Phase.END && !PENDING_REFRESH.isEmpty() && event.getServer() != null) {
         for (UUID uuid : new ArrayList(PENDING_REFRESH)) {
            PENDING_REFRESH.remove(uuid);
            ServerPlayer player = event.getServer().m_6846_().m_11259_(uuid);
            if (player != null) {
               ResonanceItemStageService.refresh(player);
            }
         }
      }
   }
}
