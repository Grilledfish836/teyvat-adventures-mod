package cn.blockforge.generated.teyvatadventures.resonance;

import java.util.Locale;

public enum Stage {
   EXCLUSION,
   NEUTRAL,
   RESONANCE;

   public String id() {
      return this.name().toLowerCase(Locale.ROOT);
   }

   public Stage next() {
      return this.ordinal() + 1 < values().length ? values()[this.ordinal() + 1] : null;
   }

   public static Stage parse(String raw) {
      if (raw != null && !raw.isEmpty()) {
         for (Stage stage : values()) {
            if (stage.name().equalsIgnoreCase(raw)) {
               return stage;
            }
         }

         return EXCLUSION;
      } else {
         return EXCLUSION;
      }
   }
}
