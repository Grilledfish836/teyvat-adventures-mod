package cn.blockforge.generated.teyvatadventures.resonance;

import cn.blockforge.generated.teyvatadventures.GeneratedMod;
import cn.blockforge.generated.teyvatadventures.resonance.condition.DendroResonanceCondition;
import cn.blockforge.generated.teyvatadventures.resonance.condition.ElectroResonanceCondition;
import cn.blockforge.generated.teyvatadventures.resonance.condition.GeoResonanceCondition;
import cn.blockforge.generated.teyvatadventures.resonance.condition.HydroResonanceCondition;
import cn.blockforge.generated.teyvatadventures.service.CurseEffectManager;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;

public final class ResonanceEventHandler {
   private static final String PENDING_DENDRO = "teyvat_adventures_pending_dendro";
   private static final Map<UUID, Long> GEO_CONTEXT = new HashMap();
   private static final Map<Integer, ResonanceEventHandler.ElectroDrop> ELECTRO_DROPS = new HashMap();
   private static final int MAX_TRACKED_DROPS = 512;

   private ResonanceEventHandler() {
   }

   @SubscribeEvent
   public static void onProjectileImpact(ProjectileImpactEvent event) {
      Projectile projectile = event.getProjectile();
      if (projectile != null && !projectile.m_9236_().m_5776_()) {
         if (projectile instanceof AbstractArrow arrow) {
            if (event.getRayTraceResult() instanceof EntityHitResult entityHit) {
               if (entityHit.m_82443_() instanceof Player player) {
                  ResonanceConditionService.onAnemoHit(arrow, player, player);
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onGeoDamage(LivingDamageEvent event) {
      Entity entity = event.getEntity();
      if (entity != null && !entity.m_9236_().m_5776_() && entity instanceof Creeper) {
         if (GeoResonanceCondition.isFallSuffocationSource(event.getSource())) {
            if (isGravityBlockNearby(entity)) {
               GEO_CONTEXT.put(entity.m_20148_(), entity.m_9236_().m_46467_() + 60L);
            }
         }
      }
   }

   private static boolean isGravityBlockNearby(Entity entity) {
      Level level = entity.m_9236_();
      BlockPos p = entity.m_20183_();
      return GeoResonanceCondition.isGravityBlock(level.m_8055_(p))
         || GeoResonanceCondition.isGravityBlock(level.m_8055_(p.m_7494_()))
         || GeoResonanceCondition.isGravityBlock(level.m_8055_(p.m_7495_()));
   }

   private static boolean isGeoContextActive(Entity entity) {
      Long expiry = (Long)GEO_CONTEXT.get(entity.m_20148_());
      return expiry != null && entity.m_9236_().m_46467_() < expiry;
   }

   @SubscribeEvent
   public static void onItemTossed(ItemTossEvent event) {
      ItemEntity dropped = event.getEntity();
      Player thrower = event.getPlayer();
      if (dropped != null && thrower != null && !thrower.m_9236_().m_5776_()) {
         if (ElectroResonanceCondition.isDroppedObsidian(dropped.m_32055_())) {
            long now = dropped.m_9236_().m_46467_();
            sweepElectroDrops(now);
            if (ELECTRO_DROPS.size() < 512) {
               ELECTRO_DROPS.put(dropped.m_19879_(), new ResonanceEventHandler.ElectroDrop(thrower.m_20148_(), now + 6000L));
            }
         }
      }
   }

   @SubscribeEvent
   public static void onEntityStruck(EntityStruckByLightningEvent event) {
      if (event.getEntity() instanceof ItemEntity item && !item.m_9236_().m_5776_()) {
         ResonanceEventHandler.ElectroDrop drop = (ResonanceEventHandler.ElectroDrop)ELECTRO_DROPS.get(item.m_19879_());
         if (drop != null) {
            if (!ElectroResonanceCondition.isDroppedObsidian(item.m_32055_())) {
               ELECTRO_DROPS.remove(item.m_19879_());
            } else {
               long now = item.m_9236_().m_46467_();
               if (now >= drop.expiry) {
                  ELECTRO_DROPS.remove(item.m_19879_());
               } else if (ElectroResonanceCondition.isThunderstorm(item.m_9236_())) {
                  ServerPlayer owner = item.m_9236_() instanceof ServerLevel serverLevel ? findPlayer(serverLevel, drop.ownerId) : null;
                  if (ResonanceConditionService.onElectroObsidianStruck(item, owner)) {
                     ELECTRO_DROPS.remove(item.m_19879_());
                  }
               }
            }
         }
      }
   }

   private static ServerPlayer findPlayer(ServerLevel level, UUID playerId) {
      for (ServerPlayer player : level.m_6907_()) {
         if (player.m_20148_().equals(playerId)) {
            return player;
         }
      }

      return null;
   }

   private static void sweepElectroDrops(long now) {
      ELECTRO_DROPS.entrySet().removeIf(entry -> now >= ((ResonanceEventHandler.ElectroDrop)entry.getValue()).expiry);
   }

   @SubscribeEvent
   public static void onPlayerTick(PlayerTickEvent event) {
      if (event.phase == Phase.END) {
         Player player = event.player;
         if (!player.m_9236_().m_5776_()) {
            if (player.m_5803_() && DendroResonanceCondition.holdsGrass(player)) {
               player.getPersistentData().m_128379_("teyvat_adventures_pending_dendro", true);
            }
         }
      }
   }

   @SubscribeEvent
   public static void onWakeUp(PlayerWakeUpEvent event) {
      Player player = event.getEntity();
      if (!player.m_9236_().m_5776_()) {
         if (player.getPersistentData().m_128471_("teyvat_adventures_pending_dendro")) {
            player.getPersistentData().m_128473_("teyvat_adventures_pending_dendro");
            if (!event.wakeImmediately() && !event.updateLevel()) {
               ResonanceConditionService.onDendroNightSlept(player);
            }
         }
      }
   }

   @SubscribeEvent
   public static void onRightClickBlock(RightClickBlock event) {
      Player player = event.getEntity();
      if (!player.m_9236_().m_5776_()) {
         Level level = event.getLevel();
         BlockPos pos = event.getPos();
         BlockState state = level.m_8055_(pos);
         ItemStack held = player.m_21120_(event.getHand());
         if (HydroResonanceCondition.matches(level, pos, state, held)) {
            event.setUseItem(Result.DENY);
            event.setUseBlock(Result.DENY);
            ResonanceConditionService.onHydroCauldron(level, pos, state, player, held);
         }
      }
   }

   @SubscribeEvent
   public static void onItemDestroyed(PlayerDestroyItemEvent event) {
      Player player = event.getEntity();
      if (!player.m_9236_().m_5776_()) {
         boolean hasScorch = player.m_21023_((MobEffect)GeneratedMod.SCORCH_EFFECT.get());
         ResonanceConditionService.onPyroSwordBroken(player, event.getOriginal(), hasScorch);
      }
   }

   @SubscribeEvent
   public static void onLivingDeath(LivingDeathEvent event) {
      Entity entity = event.getEntity();
      if (entity != null && !entity.m_9236_().m_5776_()) {
         boolean geoActive = isGeoContextActive(entity);
         if (geoActive || GeoResonanceCondition.isFallSuffocationSource(event.getSource())) {
            ResonanceConditionService.onGeoDeath(entity, event.getSource(), geoActive);
         }

         if (geoActive) {
            GEO_CONTEXT.remove(entity.m_20148_());
         }

         if (entity instanceof Player player) {
            player.getPersistentData().m_128473_("teyvat_adventures_pending_dendro");
         } else {
            if (event.getSource().m_7639_() instanceof Player killer && entity instanceof LivingEntity victim) {
               ResonanceConditionService.onCryoKill(killer, victim);
            }
         }
      }
   }

   @SubscribeEvent
   public static void onFreezeReconcileTick(PlayerTickEvent event) {
      if (event.phase == Phase.END) {
         CurseEffectManager.reconcile(event.player);
      }
   }

   private static final class ElectroDrop {
      private final UUID ownerId;
      private final long expiry;

      private ElectroDrop(UUID ownerId, long expiry) {
         this.ownerId = ownerId;
         this.expiry = expiry;
      }
   }
}
