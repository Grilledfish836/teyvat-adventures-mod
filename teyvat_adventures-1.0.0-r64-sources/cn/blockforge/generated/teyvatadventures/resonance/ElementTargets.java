package cn.blockforge.generated.teyvatadventures.resonance;

import cn.blockforge.generated.teyvatadventures.element.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.InstrumentTags;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class ElementTargets {
   public static final String ANEMO_GOAT_HORNS = "anemo_goat_horns";
   public static final String TAG_INSTRUMENT = "instrument";

   private ElementTargets() {
   }

   public static List<String> trackedKeys(Element element, Stage stage) {
      return element == Element.ANEMO && stage == Stage.EXCLUSION ? List.of("anemo_goat_horns") : List.of();
   }

   public static List<String> possibleValues(Level level, String key) {
      return "anemo_goat_horns".equals(key) ? goatHornIds(level) : List.of();
   }

   public static List<String> goatHornIds(Level level) {
      Registry<Instrument> registry = instrumentRegistry(level);
      if (registry == null) {
         return List.of();
      }

      try {
         List<String> ids = new ArrayList();

         for (ResourceLocation id : registry.m_6566_()) {
            ids.add(id.toString());
         }

         Collections.sort(ids);
         return ids;
      } catch (RuntimeException e) {
         return List.of();
      }
   }

   public static String resolveInstrumentId(ItemStack stack) {
      if (stack != null && !stack.m_41619_()) {
         Registry<Instrument> registry = BuiltInRegistries.f_256896_;
         if (registry == null) {
            return null;
         }

         try {
            String raw = stack.m_41782_() ? stack.m_41783_().m_128461_("instrument") : "";
            if (raw.isEmpty()) {
               for (Holder<Instrument> holder : registry.m_206058_(InstrumentTags.f_215858_)) {
                  Optional<ResourceKey<Instrument>> key = holder.m_203543_();
                  if (key.isPresent()) {
                     return ((ResourceKey)key.get()).m_135782_().toString();
                  }
               }

               return null;
            } else {
               ResourceLocation id = ResourceLocation.m_135820_(raw);
               return id != null && registry.m_7804_(id) ? id.toString() : null;
            }
         } catch (RuntimeException e) {
            return null;
         }
      } else {
         return null;
      }
   }

   private static Registry<Instrument> instrumentRegistry(Level level) {
      try {
         if (level != null) {
            Optional<Registry<Instrument>> registry = level.m_9598_().m_6632_(Registries.f_257010_);
            if (registry.isPresent()) {
               return (Registry<Instrument>)registry.get();
            }
         }

         return BuiltInRegistries.f_256896_;
      } catch (RuntimeException e) {
         return null;
      }
   }
}
