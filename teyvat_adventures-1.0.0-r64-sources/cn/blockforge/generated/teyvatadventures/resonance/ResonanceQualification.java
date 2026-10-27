package cn.blockforge.generated.teyvatadventures.resonance;

import cn.blockforge.generated.teyvatadventures.element.Element;

public final class ResonanceQualification {
   private final Element element;
   private final boolean qualified;

   public ResonanceQualification(Element element, boolean qualified) {
      this.element = element;
      this.qualified = qualified;
   }

   public Element element() {
      return this.element;
   }

   public boolean qualified() {
      return this.qualified;
   }
}
