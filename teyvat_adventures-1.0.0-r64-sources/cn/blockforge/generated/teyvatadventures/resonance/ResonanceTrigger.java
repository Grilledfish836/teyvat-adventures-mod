package cn.blockforge.generated.teyvatadventures.resonance;

import cn.blockforge.generated.teyvatadventures.element.Element;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public final class ResonanceTrigger extends SimpleCriterionTrigger<ResonanceTrigger.Instance> {
   private final ResourceLocation id = ResourceLocation.fromNamespaceAndPath("teyvat_adventures", "resonance");

   public ResourceLocation m_7295_() {
      return this.id;
   }

   protected ResonanceTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext ctx) {
      if (player == null) {
         player = ContextAwarePredicate.f_285567_;
      }

      Element element = json.has("element") && !json.get("element").isJsonNull() ? Element.byId(json.get("element").getAsString()) : null;
      return new ResonanceTrigger.Instance(this.id, player, element);
   }

   public void trigger(ServerPlayer player, Element element) {
      this.m_66234_(player, instance -> instance.matches(element));
   }

   public static final class Instance extends AbstractCriterionTriggerInstance {
      private final Element element;

      public Instance(ResourceLocation id, ContextAwarePredicate player, Element element) {
         super(id, player);
         this.element = element;
      }

      public boolean matches(Element element) {
         return this.element == element;
      }

      public Element element() {
         return this.element;
      }

      public JsonObject m_7683_(SerializationContext context) {
         JsonObject json = new JsonObject();
         if (this.element != null) {
            json.addProperty("element", this.element.id());
         }

         return json;
      }
   }
}
