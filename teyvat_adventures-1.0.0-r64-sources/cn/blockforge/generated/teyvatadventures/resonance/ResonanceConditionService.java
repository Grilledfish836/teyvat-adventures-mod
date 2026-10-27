package cn.blockforge.generated.teyvatadventures.resonance;

import cn.blockforge.generated.teyvatadventures.CuriosCompat;
import cn.blockforge.generated.teyvatadventures.element.Element;
import cn.blockforge.generated.teyvatadventures.resonance.condition.AnemoResonanceCondition;
import cn.blockforge.generated.teyvatadventures.resonance.condition.CryoResonanceCondition;
import cn.blockforge.generated.teyvatadventures.resonance.condition.DendroResonanceCondition;
import cn.blockforge.generated.teyvatadventures.resonance.condition.GeoResonanceCondition;
import cn.blockforge.generated.teyvatadventures.resonance.condition.HydroResonanceCondition;
import cn.blockforge.generated.teyvatadventures.resonance.condition.PyroResonanceCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class ResonanceConditionService {
   private ResonanceConditionService() {
   }

   public static void onAnemoHit(AbstractArrow arrow, Entity hitEntity, Player player) {
      if (AnemoResonanceCondition.matches(arrow, hitEntity, player)) {
         ResonanceRewardService.grantOnce(player, Element.ANEMO);
      }
   }

   public static void onGeoDeath(Entity victim, DamageSource source, boolean gravityContextActive) {
      if (GeoResonanceCondition.isValidDeath(victim, source, gravityContextActive)) {
         Player player = nearestPlayer(victim);
         if (player != null) {
            if (ResonanceRewardService.grantOnce(player, Element.GEO, false)) {
               dropResonanceItem(victim, Element.GEO);
            }
         }
      }
   }

   public static boolean onElectroObsidianStruck(ItemEntity dropped, ServerPlayer owner) {
      Level level = dropped.m_9236_();
      if (level.m_5776_() || owner == null) {
         return false;
      }

      if (ResonanceRewardService.hasQualification(owner, Element.ELECTRO)) {
         return true;
      }

      if (!ResonanceRewardService.grantOnce(owner, Element.ELECTRO, false)) {
         return true;
      }

      ItemStack stack = dropped.m_32055_();
      stack.m_41774_(1);
      spawnResonanceItem(level, dropped.m_20185_(), dropped.m_20186_(), dropped.m_20189_(), Element.ELECTRO);
      return true;
   }

   public static boolean onDendroNightSlept(Player player) {
      if (player != null && !player.m_9236_().m_5776_()) {
         InteractionHand hand = DendroResonanceCondition.grassHand(player);
         if (hand == null) {
            return false;
         }

         ItemStack held = player.m_21120_(hand);
         if (held.m_41619_()) {
            return false;
         }

         if (ResonanceRewardService.hasQualification(player, Element.DENDRO)) {
            if (CuriosCompat.carriesItem(player, (Item)Element.DENDRO.item().get())) {
               return false;
            }

            if (!CuriosCompat.hasCoreInSeal(player)) {
               return false;
            }

            held.m_41774_(1);
            player.m_21008_(hand, held);
            ResonanceRewardService.giveItemOnly(player, Element.DENDRO);
            return true;
         } else {
            if (!ResonanceRewardService.grantOnce(player, Element.DENDRO, true)) {
               return false;
            }

            held.m_41774_(1);
            player.m_21008_(hand, held);
            return true;
         }
      } else {
         return false;
      }
   }

   public static void onHydroCauldron(Level level, BlockPos pos, BlockState state, Player player, ItemStack heldItem) {
      if (HydroResonanceCondition.matches(level, pos, state, heldItem)) {
         if (ResonanceRewardService.grantOnce(player, Element.HYDRO) && heldItem != null) {
            heldItem.m_41774_(1);
         }
      }
   }

   public static void onPyroSwordBroken(Player player, ItemStack brokenStack, boolean hasScorch) {
      if (hasScorch) {
         if (PyroResonanceCondition.isIronSword(brokenStack)) {
            ResonanceRewardService.grantOnce(player, Element.PYRO);
         }
      }
   }

   public static void onCryoKill(Player killer, LivingEntity victim) {
      if (killer != null && victim != null) {
         if (CryoResonanceCondition.matches(killer, victim)) {
            ResonanceRewardService.grantOnce(killer, Element.CRYO);
         }
      }
   }

   private static void dropResonanceItem(Entity at, Element element) {
      if (!at.m_9236_().m_5776_()) {
         spawnResonanceItem(at.m_9236_(), at.m_20185_(), at.m_20186_(), at.m_20189_(), element);
      }
   }

   private static void spawnResonanceItem(Level level, double x, double y, double z, Element element) {
      if (level != null && !level.m_5776_()) {
         ItemEntity entity = new ItemEntity(level, x, y, z, new ItemStack((ItemLike)element.item().get()));
         entity.m_32010_(10);
         level.m_7967_(entity);
      }
   }

   private static Player nearestPlayer(Entity from) {
      return from.m_9236_().m_5776_() ? null : nearestPlayerAt(from.m_9236_(), from.m_20183_());
   }

   private static Player nearestPlayerAt(Level level, BlockPos pos) {
      if (level instanceof ServerLevel serverLevel) {
         Player best = null;
         double bestDistance = 256.0;

         for (ServerPlayer p : serverLevel.m_6907_()) {
            double d = p.m_20275_(pos.m_123341_(), pos.m_123342_(), pos.m_123343_());
            if (d <= bestDistance) {
               bestDistance = d;
               best = p;
            }
         }

         return best;
      } else {
         return null;
      }
   }
}
