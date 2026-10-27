package cn.blockforge.generated.teyvatadventures.element;

import cn.blockforge.generated.teyvatadventures.ElementalResonanceItem;
import cn.blockforge.generated.teyvatadventures.GeneratedMod;
import java.util.Locale;
import net.minecraftforge.registries.RegistryObject;

public enum Element {
   ANEMO("anemo"),
   GEO("geo"),
   ELECTRO("electro"),
   DENDRO("dendro"),
   HYDRO("hydro"),
   PYRO("pyro"),
   CRYO("cryo");

   private final String id;

   Element(String id) {
      this.id = id;
   }

   public String id() {
      return this.id;
   }

   public String itemId() {
      return "elemental_resonance_" + this.id;
   }

   public RegistryObject<ElementalResonanceItem> item() {
      switch (this) {
         case ANEMO:
            return GeneratedMod.ELEMENTAL_RESONANCE_ANEMO;
         case GEO:
            return GeneratedMod.ELEMENTAL_RESONANCE_GEO;
         case ELECTRO:
            return GeneratedMod.ELEMENTAL_RESONANCE_ELECTRO;
         case DENDRO:
            return GeneratedMod.ELEMENTAL_RESONANCE_DENDRO;
         case HYDRO:
            return GeneratedMod.ELEMENTAL_RESONANCE_HYDRO;
         case PYRO:
            return GeneratedMod.ELEMENTAL_RESONANCE_PYRO;
         case CRYO:
            return GeneratedMod.ELEMENTAL_RESONANCE_CRYO;
         default:
            throw new IllegalStateException("未知元素: " + this);
      }
   }

   public static Element byId(String id) {
      if (id == null) {
         return null;
      }

      String normalized = id.toLowerCase(Locale.ROOT);

      for (Element element : values()) {
         if (element.id.equals(normalized)) {
            return element;
         }
      }

      return null;
   }

   public static Element byItemId(String itemId) {
      return itemId != null && itemId.startsWith("elemental_resonance_") ? byId(itemId.substring("elemental_resonance_".length())) : null;
   }

   public static String[] ids() {
      String[] ids = new String[values().length];

      for (int i = 0; i < values().length; i++) {
         ids[i] = values()[i].id;
      }

      return ids;
   }
}
