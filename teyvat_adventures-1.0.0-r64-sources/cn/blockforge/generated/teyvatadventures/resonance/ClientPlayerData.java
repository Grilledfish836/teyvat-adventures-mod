package cn.blockforge.generated.teyvatadventures.resonance;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.nbt.CompoundTag;

public final class ClientPlayerData {
   private static final Map<UUID, PlayerElementData> STORE = new ConcurrentHashMap();

   private ClientPlayerData() {
   }

   public static void put(UUID playerUUID, CompoundTag data) {
      if (playerUUID != null && data != null) {
         PlayerElementData parsed = new PlayerElementData();
         parsed.deserializeNBT(data);
         STORE.put(playerUUID, parsed);
      }
   }

   public static PlayerElementData get(UUID playerUUID) {
      return playerUUID == null ? null : (PlayerElementData)STORE.get(playerUUID);
   }

   public static void remove(UUID playerUUID) {
      if (playerUUID != null) {
         STORE.remove(playerUUID);
      }
   }

   public static void clear() {
      STORE.clear();
   }
}
