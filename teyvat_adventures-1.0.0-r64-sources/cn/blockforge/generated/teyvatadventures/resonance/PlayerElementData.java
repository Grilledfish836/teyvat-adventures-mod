package cn.blockforge.generated.teyvatadventures.resonance;

import cn.blockforge.generated.teyvatadventures.GeneratedMod;
import cn.blockforge.generated.teyvatadventures.element.Element;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public final class PlayerElementData implements INBTSerializable<CompoundTag> {
   public static final int DATA_VERSION = 1;
   private static final String TAG_DATA_VERSION = "data_version";
   private static final String Q_PREFIX = "q_";
   private static final String S_PREFIX = "s_";
   private static final String P_PREFIX = "p_";
   private final EnumMap<Element, Boolean> qualifications = new EnumMap(Element.class);
   private final EnumMap<Element, Integer> legacyStages = new EnumMap(Element.class);
   private final EnumMap<Element, ElementProgress> progress = new EnumMap(Element.class);

   public static PlayerElementData get(Player player) {
      if (player == null) {
         throw new IllegalStateException("[teyvat] PlayerElementData.get: player is null");
      }

      if (player.m_9236_().m_5776_()) {
         return ClientPlayerData.get(player.m_20148_());
      }

      PlayerElementData data = (PlayerElementData)player.getCapability(PlayerElementDataCapability.PLAYER_ELEMENT_DATA).resolve().orElse(null);
      if (data == null) {
         throw new IllegalStateException("[teyvat] PlayerElementData capability missing on " + player.m_6302_() + " (attach listener not registered?)");
      }

      data.reload(player);
      return data;
   }

   public static PlayerElementData peek(Player player) {
      if (player == null) {
         return null;
      }

      if (player.m_9236_().m_5776_()) {
         return ClientPlayerData.get(player.m_20148_());
      }

      PlayerElementData data = (PlayerElementData)player.getCapability(PlayerElementDataCapability.PLAYER_ELEMENT_DATA).resolve().orElse(null);
      if (data == null) {
         return null;
      }

      data.reload(player);
      return data;
   }

   public static ElementProgress getProgress(Player player, Element element) {
      PlayerElementData data = peek(player);
      return data == null ? new ElementProgress() : data.progressFor(element);
   }

   public static boolean addTrackedItem(Player player, Element element, String key, String value) {
      if (player != null && element != null && !player.m_9236_().m_5776_()) {
         PlayerElementData data = get(player);
         boolean added = data.progressFor(element).addTracked(key, value);
         if (added) {
            data.markDirty(player);
            int current = data.progressFor(element).trackedCount(key);
            int total = ElementTargets.possibleValues(player.m_9236_(), key).size();
            GeneratedMod.LOGGER
               .debug("[teyvat] {} tracked {}/{}: {} ({}/{})", new Object[]{player.m_6302_(), element.id(), key, value, current, total > 0 ? total : current});
         }

         return added;
      } else {
         return false;
      }
   }

   public static Set<String> getTrackedItems(Player player, Element element, String key) {
      PlayerElementData data = peek(player);
      return data != null && element != null ? data.trackedSnapshot(element, key) : Collections.emptySet();
   }

   public static void setTimestamp(Player player, Element element, String key, long gameTime) {
      if (player != null && element != null && !player.m_9236_().m_5776_()) {
         PlayerElementData data = get(player);
         data.progressFor(element).setTimestamp(key, gameTime);
         data.markDirty(player);
      }
   }

   public static long getTimestamp(Player player, Element element, String key) {
      PlayerElementData data = peek(player);
      return data != null && element != null ? data.progressFor(element).getTimestamp(key) : -1L;
   }

   public static void setFlag(Player player, Element element, String key, boolean value) {
      if (player != null && element != null && !player.m_9236_().m_5776_()) {
         PlayerElementData data = get(player);
         data.progressFor(element).setFlag(key, value);
         data.markDirty(player);
      }
   }

   public static boolean getFlag(Player player, Element element, String key) {
      PlayerElementData data = peek(player);
      return data != null && element != null ? data.progressFor(element).getFlag(key) : false;
   }

   public static void incrementCounter(Player player, Element element, String key) {
      if (player != null && element != null && !player.m_9236_().m_5776_()) {
         PlayerElementData data = get(player);
         data.progressFor(element).incrementCounter(key);
         data.markDirty(player);
      }
   }

   public static int getCounter(Player player, Element element, String key) {
      PlayerElementData data = peek(player);
      return data != null && element != null ? data.progressFor(element).getCounter(key) : 0;
   }

   public static Stage getStage(Player player, Element element) {
      PlayerElementData data = peek(player);
      return data != null && element != null ? data.stageOf(element) : Stage.EXCLUSION;
   }

   public static boolean advanceStage(Player player, Element element) {
      if (player != null && element != null && !player.m_9236_().m_5776_()) {
         PlayerElementData data = get(player);
         ElementProgress progress = data.progressFor(element);
         Stage next = progress.stage().next();
         if (next == null) {
            return false;
         }

         progress.setStage(next);
         data.markDirty(player);
         ResonanceItemStageService.refresh(player);
         GeneratedMod.LOGGER.info("[teyvat] player {} advanced {} to {}", new Object[]{player.m_6302_(), element.id(), next.name()});
         return true;
      } else {
         return false;
      }
   }

   public static boolean hasQualification(Player player, Element element) {
      PlayerElementData data = peek(player);
      return data != null && element != null && data.isQualified(element);
   }

   public static void setQualification(Player player, Element element, boolean value) {
      if (player != null && element != null && !player.m_9236_().m_5776_()) {
         PlayerElementData data = get(player);
         if (data.isQualified(element) != value) {
            data.qualifications.put(element, value);
            data.markDirty(player);
         }
      }
   }

   public static int getLegacyStage(Player player, Element element) {
      PlayerElementData data = peek(player);
      return data != null && element != null ? data.legacyStageOf(element) : 0;
   }

   public static void setLegacyStage(Player player, Element element, int stage) {
      if (player != null && element != null && !player.m_9236_().m_5776_()) {
         PlayerElementData data = get(player);
         int value = Math.max(0, stage);
         if (data.legacyStageOf(element) != value) {
            data.legacyStages.put(element, value);
            data.markDirty(player);
         }
      }
   }

   public static void copyFrom(Player target, Player original) {
      if (target != null && original != null && !target.m_9236_().m_5776_()) {
         PlayerElementData source = peek(original);
         if (source != null) {
            PlayerElementData dest = get(target);
            dest.deserializeNBT(source.serializeNBT());
            dest.markDirty(target);
         }
      }
   }

   public static ResonanceQualification qualification(Player player, Element element) {
      return new ResonanceQualification(element, hasQualification(player, element));
   }

   public static ResonanceStage legacyStage(Player player, Element element) {
      return new ResonanceStage(element, getLegacyStage(player, element));
   }

   private void reload(Player player) {
      this.applyFrom(player.getPersistentData().m_128469_("teyvat_adventures_resonance"));
   }

   private void markDirty(Player player) {
      if (!player.m_9236_().m_5776_()) {
         player.getPersistentData().m_128365_("teyvat_adventures_resonance", this.serializeNBT());
         ElementSyncScheduler.request(player.m_20148_());
      }
   }

   private ElementProgress progressFor(Element element) {
      return (ElementProgress)this.progress.computeIfAbsent(element, k -> new ElementProgress());
   }

   private Stage stageOf(Element element) {
      ElementProgress progressEntry = (ElementProgress)this.progress.get(element);
      return progressEntry == null ? Stage.EXCLUSION : progressEntry.stage();
   }

   private boolean isQualified(Element element) {
      return Boolean.TRUE.equals(this.qualifications.get(element));
   }

   private int legacyStageOf(Element element) {
      Integer value = (Integer)this.legacyStages.get(element);
      return value == null ? 0 : value;
   }

   private Set<String> trackedSnapshot(Element element, String key) {
      ElementProgress progressEntry = (ElementProgress)this.progress.get(element);
      return (Set<String>)(progressEntry == null ? Collections.emptySet() : new HashSet(progressEntry.trackedView(key)));
   }

   public ElementProgress progressSnapshot(Element element) {
      ElementProgress progressEntry = element == null ? null : (ElementProgress)this.progress.get(element);
      return progressEntry == null ? new ElementProgress() : progressEntry;
   }

   public Stage stageOfElement(Element element) {
      return element == null ? Stage.EXCLUSION : this.stageOf(element);
   }

   private void applyFrom(CompoundTag root) {
      this.qualifications.clear();
      this.legacyStages.clear();
      this.progress.clear();
      if (root != null && !root.m_128456_()) {
         for (Element element : Element.values()) {
            if (root.m_128471_("q_" + element.id())) {
               this.qualifications.put(element, true);
            }

            if (root.m_128441_("s_" + element.id())) {
               this.legacyStages.put(element, Math.max(0, root.m_128451_("s_" + element.id())));
            }

            if (root.m_128441_("p_" + element.id())) {
               this.progress.put(element, ElementProgress.fromNbt(root.m_128469_("p_" + element.id())));
            }
         }
      }
   }

   public CompoundTag serializeNBT() {
      CompoundTag root = new CompoundTag();
      root.m_128405_("data_version", 1);

      for (Element element : Element.values()) {
         if (this.isQualified(element)) {
            root.m_128379_("q_" + element.id(), true);
         }

         Integer legacyStage = (Integer)this.legacyStages.get(element);
         if (legacyStage != null) {
            root.m_128405_("s_" + element.id(), legacyStage);
         }

         ElementProgress progressEntry = (ElementProgress)this.progress.get(element);
         if (progressEntry != null) {
            root.m_128365_("p_" + element.id(), progressEntry.toNbt());
         }
      }

      return root;
   }

   public void deserializeNBT(CompoundTag nbt) {
      this.applyFrom(nbt);
   }

   public Map<Element, ElementProgress> progressView() {
      return Collections.unmodifiableMap(this.progress);
   }
}
