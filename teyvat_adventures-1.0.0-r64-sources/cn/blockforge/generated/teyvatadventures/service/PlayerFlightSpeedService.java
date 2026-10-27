package cn.blockforge.generated.teyvatadventures.service;

import cn.blockforge.generated.teyvatadventures.CuriosCompat;
import cn.blockforge.generated.teyvatadventures.GeneratedMod;
import cn.blockforge.generated.teyvatadventures.WorldBeyondDescenderCurseHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

public final class PlayerFlightSpeedService {
   private static final float VANILLA_DEFAULT_FLYING_SPEED = 0.05F;
   private static final float LEGACY_BROKEN_BASE = 0.4F;
   private static final String BASE_FLYING_SPEED = "teyvat_adventures_base_flying_speed";
   private static final String APPLIED_FLYING_SPEED = "teyvat_adventures_applied_flying_speed";
   private static final String FLYING_SPEED_SCHEMA = "teyvat_adventures_flying_speed_schema";
   private static final int FLYING_SPEED_SCHEMA_VERSION = 2;
   public static final float WIND_CURSE_MULTIPLIER = 0.8F;
   public static final float WIND_EXHAUSTION_MULTIPLIER = 0.7F;
   private static final float FREEZE_LEVEL_PENALTY = 0.05F;
   private static final float MIN_MULTIPLIER = 0.01F;

   private PlayerFlightSpeedService() {
   }

   public static void sync(Player player) {
      if (!player.m_9236_().m_5776_() && !player.m_213877_()) {
         recompute(player);
      }
   }

   private static void recompute(Player player) {
      CompoundTag data = player.getPersistentData();
      CurseEffectManager.removeLegacyFlyingSpeedModifier(player);
      WorldBeyondDescenderCurseHandler.removeLegacyFlyingSpeedModifier(player);
      if (data.m_128451_("teyvat_adventures_flying_speed_schema") != 2) {
         migrateLegacyBaseline(player, data);
         data.m_128405_("teyvat_adventures_flying_speed_schema", 2);
      }

      float current = player.m_150110_().m_35942_();
      float windMultiplier = currentWindMultiplier(player);
      float freezeMultiplier = currentFreezeMultiplier(player);
      boolean slowed = windMultiplier < 1.0F || freezeMultiplier < 1.0F;
      float base = data.m_128441_("teyvat_adventures_base_flying_speed") ? data.m_128457_("teyvat_adventures_base_flying_speed") : 0.05F;
      float lastApplied = data.m_128441_("teyvat_adventures_applied_flying_speed") ? data.m_128457_("teyvat_adventures_applied_flying_speed") : Float.NaN;
      if (!slowed && Float.compare(current, lastApplied) != 0 && Float.compare(current, base) != 0) {
         base = current;
      }

      data.m_128350_("teyvat_adventures_base_flying_speed", base);
      float target = base * windMultiplier * freezeMultiplier;
      if (Float.compare(current, target) != 0) {
         player.m_150110_().m_35943_(target);
         player.m_6885_();
      }

      data.m_128350_("teyvat_adventures_applied_flying_speed", target);
   }

   private static void migrateLegacyBaseline(Player player, CompoundTag data) {
      float current = player.m_150110_().m_35942_();
      boolean brokenBaseStored = data.m_128441_("teyvat_adventures_base_flying_speed")
         && Float.compare(data.m_128457_("teyvat_adventures_base_flying_speed"), 0.4F) == 0;
      boolean appliedMatchesCurrent = data.m_128441_("teyvat_adventures_applied_flying_speed")
         && Float.compare(current, data.m_128457_("teyvat_adventures_applied_flying_speed")) == 0;
      boolean stuckOnBrokenBase = Float.compare(current, 0.4F) == 0;
      if (brokenBaseStored || !data.m_128441_("teyvat_adventures_applied_flying_speed")) {
         data.m_128473_("teyvat_adventures_base_flying_speed");
         data.m_128473_("teyvat_adventures_applied_flying_speed");
      }

      data.m_128473_("teyvat_adventures_flight_speed_captured");
      if (stuckOnBrokenBase || brokenBaseStored && appliedMatchesCurrent) {
         player.m_150110_().m_35943_(0.05F);
         player.m_6885_();
      }
   }

   private static float currentWindMultiplier(Player player) {
      float multiplier = 1.0F;
      if (CuriosCompat.hasCoreInSeal(player)) {
         multiplier *= 0.8F;
      }

      if (player.m_21023_((MobEffect)GeneratedMod.EXHAUSTION_EFFECT.get())) {
         multiplier *= 0.7F;
      }

      return multiplier;
   }

   private static float currentFreezeMultiplier(Player player) {
      MobEffectInstance freeze = player.m_21124_((MobEffect)GeneratedMod.FREEZE_EFFECT.get());
      if (freeze == null) {
         return 1.0F;
      }

      int level = Math.min(34, Math.max(1, freeze.m_19564_() + 1));
      return Math.max(0.01F, 1.0F - 0.05F * level);
   }

   public static void copyOntoClone(Player fresh, Player original) {
      CompoundTag originalData = original.getPersistentData();
      CompoundTag freshData = fresh.getPersistentData();
      if (originalData.m_128441_("teyvat_adventures_base_flying_speed")) {
         freshData.m_128350_("teyvat_adventures_base_flying_speed", originalData.m_128457_("teyvat_adventures_base_flying_speed"));
      }

      if (originalData.m_128441_("teyvat_adventures_applied_flying_speed")) {
         freshData.m_128350_("teyvat_adventures_applied_flying_speed", originalData.m_128457_("teyvat_adventures_applied_flying_speed"));
      }

      if (originalData.m_128441_("teyvat_adventures_flying_speed_schema")) {
         freshData.m_128405_("teyvat_adventures_flying_speed_schema", originalData.m_128451_("teyvat_adventures_flying_speed_schema"));
      }
   }
}
