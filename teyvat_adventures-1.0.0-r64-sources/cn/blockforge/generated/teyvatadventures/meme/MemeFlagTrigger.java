package cn.blockforge.generated.teyvatadventures.meme;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public final class MemeFlagTrigger extends SimpleCriterionTrigger<MemeFlagTrigger.Instance> {
   private static final List<MemeFlagTrigger> ALL = new ArrayList();
   public static final MemeFlagTrigger WORLD_BORDER_TOUCHED = new MemeFlagTrigger("meme_world_border_touched");
   public static final MemeFlagTrigger STRUCK_BY_LIGHTNING = new MemeFlagTrigger("meme_struck_by_lightning");
   public static final MemeFlagTrigger FIRST_SWORD = new MemeFlagTrigger("meme_first_sword");
   public static final MemeFlagTrigger HALF_HEART_KILL = new MemeFlagTrigger("meme_half_heart_kill");
   public static final MemeFlagTrigger AIR_SPEED_45 = new MemeFlagTrigger("meme_air_speed_45");
   public static final MemeFlagTrigger PLAYERS_ONLINE = new MemeFlagTrigger("meme_players_online");
   public static final MemeFlagTrigger SKY_GAZE = new MemeFlagTrigger("meme_sky_gaze");
   public static final MemeFlagTrigger BOAT_RIDE = new MemeFlagTrigger("meme_boat_ride");
   public static final MemeFlagTrigger DARKNESS = new MemeFlagTrigger("meme_darkness");
   public static final MemeFlagTrigger FLAME_BOW_HIT = new MemeFlagTrigger("meme_flame_bow_hit");
   public static final MemeFlagTrigger FOOD_GIFT = new MemeFlagTrigger("meme_food_gift");
   private final ResourceLocation id;

   private MemeFlagTrigger(String name) {
      this.id = ResourceLocation.fromNamespaceAndPath("teyvat_adventures", name);
      ALL.add(this);
   }

   public static void registerAll() {
      for (MemeFlagTrigger trigger : ALL) {
         CriteriaTriggers.m_10595_(trigger);
      }
   }

   public ResourceLocation m_7295_() {
      return this.id;
   }

   protected MemeFlagTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext ctx) {
      return new MemeFlagTrigger.Instance(this.id, player);
   }

   public void grant(ServerPlayer player) {
      this.m_66234_(player, instance -> true);
   }

   public static final class Instance extends AbstractCriterionTriggerInstance {
      public Instance(ResourceLocation id, ContextAwarePredicate player) {
         super(id, player);
      }

      public JsonObject m_7683_(SerializationContext context) {
         return new JsonObject();
      }
   }
}
