package cn.blockforge.generated.teyvatadventures.resonance;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ElementSyncScheduler {
   private static final Set<UUID> PENDING = ConcurrentHashMap.newKeySet();
   private static final Map<UUID, Long> LAST_SENT = new ConcurrentHashMap();

   private ElementSyncScheduler() {
   }

   public static void request(UUID playerUUID) {
      if (playerUUID != null) {
         PENDING.add(playerUUID);
      }
   }

   public static boolean send(ServerPlayer player) {
      long now = player.m_9236_().m_46467_();
      Long last = (Long)LAST_SENT.get(player.m_20148_());
      if (last != null && last == now) {
         return false;
      }

      LAST_SENT.put(player.m_20148_(), now);
      ResonanceNetwork.sendElementSync(player);
      return true;
   }

   public static void forget(UUID playerUUID) {
      if (playerUUID != null) {
         PENDING.remove(playerUUID);
         LAST_SENT.remove(playerUUID);
      }
   }

   @SubscribeEvent
   public static void onServerTickEnd(ServerTickEvent event) {
      if (event.phase == Phase.END && !PENDING.isEmpty() && event.getServer() != null) {
         for (UUID uuid : new ArrayList(PENDING)) {
            ServerPlayer player = event.getServer().m_6846_().m_11259_(uuid);
            if (player == null) {
               PENDING.remove(uuid);
            } else if (send(player)) {
               PENDING.remove(uuid);
            }
         }
      }
   }
}
