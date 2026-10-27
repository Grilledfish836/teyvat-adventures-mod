package cn.blockforge.generated.teyvatadventures.resonance;

import cn.blockforge.generated.teyvatadventures.element.Element;

public final class ResonanceStage {
   private final Element element;
   private final int stage;

   public ResonanceStage(Element element, int stage) {
      this.element = element;
      this.stage = stage;
   }

   public Element element() {
      return this.element;
   }

   public int stage() {
      return this.stage;
   }
}
