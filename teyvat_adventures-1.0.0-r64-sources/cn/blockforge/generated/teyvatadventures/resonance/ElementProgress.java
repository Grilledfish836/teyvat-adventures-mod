package cn.blockforge.generated.teyvatadventures.resonance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

public final class ElementProgress {
   private static final String TAG_STAGE = "stage";
   private static final String TAG_SETS = "sets";
   private static final String TAG_TS = "ts";
   private static final String TAG_FLAGS = "flags";
   private static final String TAG_COUNTERS = "cnt";
   private Stage stage = Stage.EXCLUSION;
   private final Map<String, Set<String>> trackedSets = new LinkedHashMap();
   private final Map<String, Long> timestamps = new LinkedHashMap();
   private final Map<String, Boolean> flags = new LinkedHashMap();
   private final Map<String, Integer> counters = new LinkedHashMap();

   public Stage stage() {
      return this.stage;
   }

   public void setStage(Stage value) {
      this.stage = value == null ? Stage.EXCLUSION : value;
   }

   public boolean addTracked(String key, String value) {
      return key != null && !key.isEmpty() && value != null && !value.isEmpty()
         ? ((Set)this.trackedSets.computeIfAbsent(key, k -> new HashSet())).add(value)
         : false;
   }

   public Set<String> trackedView(String key) {
      Set<String> set = (Set<String>)this.trackedSets.get(key);
      return set == null ? Collections.emptySet() : Collections.unmodifiableSet(set);
   }

   public int trackedCount(String key) {
      Set<String> set = (Set<String>)this.trackedSets.get(key);
      return set == null ? 0 : set.size();
   }

   public void setTimestamp(String key, long gameTime) {
      if (key != null && !key.isEmpty()) {
         this.timestamps.put(key, gameTime);
      }
   }

   public long getTimestamp(String key) {
      Long value = (Long)this.timestamps.get(key);
      return value == null ? -1L : value;
   }

   public void setFlag(String key, boolean value) {
      if (key != null && !key.isEmpty()) {
         this.flags.put(key, value);
      }
   }

   public boolean getFlag(String key) {
      Boolean value = (Boolean)this.flags.get(key);
      return value != null && value;
   }

   public void incrementCounter(String key) {
      if (key != null && !key.isEmpty()) {
         this.counters.merge(key, 1, (old, one) -> old == Integer.MAX_VALUE ? old : old + 1);
      }
   }

   public int getCounter(String key) {
      Integer value = (Integer)this.counters.get(key);
      return value == null ? 0 : value;
   }

   public CompoundTag toNbt() {
      CompoundTag root = new CompoundTag();
      root.m_128359_("stage", this.stage.name());
      if (!this.trackedSets.isEmpty()) {
         CompoundTag sets = new CompoundTag();

         for (Entry<String, Set<String>> entry : this.trackedSets.entrySet()) {
            ListTag list = new ListTag();

            for (String value : new TreeSet((Collection)entry.getValue())) {
               list.add(StringTag.m_129297_(value));
            }

            sets.m_128365_((String)entry.getKey(), list);
         }

         root.m_128365_("sets", sets);
      }

      if (!this.timestamps.isEmpty()) {
         CompoundTag ts = new CompoundTag();

         for (Entry<String, Long> entry : this.timestamps.entrySet()) {
            ts.m_128356_((String)entry.getKey(), (Long)entry.getValue());
         }

         root.m_128365_("ts", ts);
      }

      if (!this.flags.isEmpty()) {
         CompoundTag f = new CompoundTag();

         for (Entry<String, Boolean> entry : this.flags.entrySet()) {
            f.m_128379_((String)entry.getKey(), (Boolean)entry.getValue());
         }

         root.m_128365_("flags", f);
      }

      if (!this.counters.isEmpty()) {
         CompoundTag c = new CompoundTag();

         for (Entry<String, Integer> entry : this.counters.entrySet()) {
            c.m_128405_((String)entry.getKey(), (Integer)entry.getValue());
         }

         root.m_128365_("cnt", c);
      }

      return root;
   }

   public static ElementProgress fromNbt(CompoundTag root) {
      ElementProgress progress = new ElementProgress();
      if (root == null) {
         return progress;
      }

      progress.stage = Stage.parse(root.m_128461_("stage"));
      CompoundTag sets = root.m_128469_("sets");

      for (String key : sets.m_128431_()) {
         if (sets.m_128423_(key) instanceof ListTag list) {
            Set<String> values = new HashSet();
            List<String> safe = new ArrayList();

            for (int i = 0; i < list.size(); i++) {
               if (list.get(i) instanceof StringTag stringTag) {
                  safe.add(stringTag.m_7916_());
               }
            }

            values.addAll(safe);
            if (!values.isEmpty()) {
               progress.trackedSets.put(key, values);
            }
         }
      }

      CompoundTag ts = root.m_128469_("ts");

      for (String key : ts.m_128431_()) {
         if (ts.m_128425_(key, 4)) {
            progress.timestamps.put(key, ts.m_128454_(key));
         }
      }

      CompoundTag f = root.m_128469_("flags");

      for (String key : f.m_128431_()) {
         if (f.m_128425_(key, 99)) {
            progress.flags.put(key, f.m_128471_(key));
         }
      }

      CompoundTag c = root.m_128469_("cnt");

      for (String key : c.m_128431_()) {
         if (c.m_128425_(key, 3)) {
            progress.counters.put(key, c.m_128451_(key));
         }
      }

      return progress;
   }
}
