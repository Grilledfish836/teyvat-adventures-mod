package cn.blockforge.generated.teyvatadventures.meme;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class MemeAdvancementHandler {
   public static final TagKey<EntityType<?>> HOSTILE_OR_NEUTRAL_MOBS = TagKey.m_203882_(
      Registries.f_256939_, ResourceLocation.fromNamespaceAndPath("teyvat_adventures", "hostile_or_neutral_mobs")
   );
   private static final String FLAME_ARROW_KEY = "teyvat_meme_flame_arrow_shot";
   private static final double BORDER_TOUCH_REACH = 0.3001;
   private static final double AIR_SPEED_MIN_PER_TICK = 2.25;
   private static final double AIR_SPEED_MAX_PER_TICK = 8.0;
   private static final float SKY_GAZE_PITCH = -45.0F;
   private static final int SKY_GAZE_TICKS = 200;
   private static final double BOAT_MOVE_MIN_PER_TICK = 0.01;
   private static final int BOAT_RIDE_TICKS = 500;
   private static final int DARKNESS_TICKS = 140;
   private static final int FOOD_TRACK_TTL_TICKS = 1200;
   private static final int FOOD_TRACK_MAX = 512;
   private static final Map<UUID, Vec3> LAST_POSITION = new HashMap();
   private static final Map<UUID, Integer> SKY_GAZE_COUNTER = new HashMap();
   private static final Map<UUID, Integer> DARKNESS_COUNTER = new HashMap();
   private static final Map<UUID, MemeAdvancementHandler.BoatRide> BOAT_RIDES = new HashMap();
   private static final Map<Integer, MemeAdvancementHandler.FoodToss> THROWN_FOOD = new HashMap();
   private static final float HALF_HEART_KILL_MAX_HP = 1.0F;
   private static final float HALF_HEART_KILL_HP_EPSILON = 1.0E-4F;

   private MemeAdvancementHandler() {
   }

   @SubscribeEvent
   public static void onEntityStruckByLightning(EntityStruckByLightningEvent event) {
      if (event.getEntity() instanceof ServerPlayer player) {
         if (!player.m_9236_().m_5776_()) {
            LightningBolt bolt = event.getLightning();
            if (bolt != null) {
               if (bolt.m_147158_() != player) {
                  MemeFlagTrigger.STRUCK_BY_LIGHTNING.grant(player);
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onItemCrafted(ItemCraftedEvent event) {
      if (event.getEntity() instanceof ServerPlayer player) {
         if (!player.m_9236_().m_5776_()) {
            if (event.getCrafting().m_41720_() instanceof SwordItem) {
               MemeFlagTrigger.FIRST_SWORD.grant(player);
            }
         }
      }
   }

   @SubscribeEvent
   public static void onArrowSpawn(EntityJoinLevelEvent event) {
      if (!event.getLevel().m_5776_()) {
         if (event.getEntity() instanceof AbstractArrow arrow) {
            if (arrow.m_19749_() instanceof ServerPlayer shooter) {
               if (isFlameBow(shooter.m_21205_()) || isFlameBow(shooter.m_21206_())) {
                  arrow.getPersistentData().m_128379_("teyvat_meme_flame_arrow_shot", true);
               }
            }
         }
      }
   }

   private static boolean isFlameBow(ItemStack stack) {
      return stack.m_150930_(Items.f_42411_) && EnchantmentHelper.m_44843_(Enchantments.f_44990_, stack) > 0;
   }

   @SubscribeEvent
   public static void onFlameArrowDamage(LivingDamageEvent event) {
      LivingEntity target = event.getEntity();
      if (!target.m_9236_().m_5776_() && !(event.getAmount() <= 0.0F)) {
         DamageSource source = event.getSource();
         if (source.m_7640_() instanceof AbstractArrow arrow) {
            if (arrow.getPersistentData().m_128471_("teyvat_meme_flame_arrow_shot")) {
               if (source.m_7639_() instanceof ServerPlayer shooter && shooter != target) {
                  if (target instanceof Mob) {
                     MemeFlagTrigger.FLAME_BOW_HIT.grant(shooter);
                  }
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onLivingDeath(LivingDeathEvent event) {
      LivingEntity victimEntity = event.getEntity();
      if (!victimEntity.m_9236_().m_5776_()) {
         if (victimEntity instanceof Mob victim) {
            if (victim.m_6095_().m_204041_().m_203656_(HOSTILE_OR_NEUTRAL_MOBS)) {
               ServerPlayer killer = resolvePlayerKiller(event.getSource());
               if (killer != null) {
                  if (killer.m_21223_() <= 1.0001F) {
                     MemeFlagTrigger.HALF_HEART_KILL.grant(killer);
                  }
               }
            }
         }
      }
   }

   private static ServerPlayer resolvePlayerKiller(DamageSource source) {
      if (source.m_7639_() instanceof ServerPlayer player) {
         return player;
      } else {
         Entity direct = source.m_7640_();
         if (direct instanceof ServerPlayer player) {
            return player;
         } else {
            return direct instanceof Projectile projectile && projectile.m_19749_() instanceof ServerPlayer player ? player : null;
         }
      }
   }

   @SubscribeEvent
   public static void onFoodTossed(ItemTossEvent event) {
      ItemEntity thrown = event.getEntity();
      Player thrower = event.getPlayer();
      if (thrown != null && thrower != null && !thrown.m_9236_().m_5776_()) {
         if (thrown.m_32055_().m_41614_()) {
            long now = thrown.m_9236_().m_46467_();
            sweepThrownFood(now);
            if (THROWN_FOOD.size() < 512) {
               THROWN_FOOD.put(thrown.m_19879_(), new MemeAdvancementHandler.FoodToss(thrower.m_20148_(), now + 1200L));
            }
         }
      }
   }

   @SubscribeEvent
   public static void onFoodPickedUp(EntityItemPickupEvent event) {
      if (event.getEntity() instanceof ServerPlayer picker) {
         ItemEntity item = event.getItem();
         if (item != null && !picker.m_9236_().m_5776_()) {
            MemeAdvancementHandler.FoodToss toss = (MemeAdvancementHandler.FoodToss)THROWN_FOOD.remove(item.m_19879_());
            if (toss != null && picker.m_9236_().m_46467_() < toss.expiry) {
               if (!toss.throwerId.equals(picker.m_20148_())) {
                  if (picker.m_9236_() instanceof ServerLevel level) {
                     ServerPlayer thrower = level.m_7654_().m_6846_().m_11259_(toss.throwerId);
                     if (thrower != null) {
                        MemeFlagTrigger.FOOD_GIFT.grant(thrower);
                     }
                  }
               }
            }
         }
      }
   }

   private static void sweepThrownFood(long now) {
      THROWN_FOOD.entrySet().removeIf(entry -> now >= ((MemeAdvancementHandler.FoodToss)entry.getValue()).expiry);
   }

   @SubscribeEvent
   public static void onPlayerTick(PlayerTickEvent event) {
      if (event.phase == Phase.END) {
         if (event.player instanceof ServerPlayer player) {
            if (player.m_9236_() instanceof ServerLevel level) {
               UUID var11 = player.m_20148_();
               Vec3 position = player.m_20182_();
               double borderDistance = level.m_6857_().m_61941_(position.f_82479_, position.f_82481_);
               if (borderDistance <= 0.3001) {
                  MemeFlagTrigger.WORLD_BORDER_TOUCHED.grant(player);
               }

               Vec3 previous = (Vec3)LAST_POSITION.put(var11, position);
               if (previous != null) {
                  double moved = position.m_82554_(previous);
                  if (!player.m_20096_() && moved >= 2.25 && moved <= 8.0 && player.m_20184_().m_82556_() >= 0.25) {
                     MemeFlagTrigger.AIR_SPEED_45.grant(player);
                  }
               }

               if (level.m_46467_() % 20L == 0L && level.m_7654_().m_6846_().m_11309_() >= 2) {
                  MemeFlagTrigger.PLAYERS_ONLINE.grant(player);
               }

               if (player.m_146909_() <= -45.0F && skyVisibleAbove(level, player)) {
                  int gaze = (Integer)SKY_GAZE_COUNTER.merge(var11, 1, Integer::sum);
                  if (gaze >= 200) {
                     MemeFlagTrigger.SKY_GAZE.grant(player);
                     SKY_GAZE_COUNTER.put(var11, 0);
                  }
               } else {
                  SKY_GAZE_COUNTER.remove(var11);
               }

               if (level.m_46803_(player.m_20183_()) <= 0) {
                  int dark = (Integer)DARKNESS_COUNTER.merge(var11, 1, Integer::sum);
                  if (dark >= 140) {
                     MemeFlagTrigger.DARKNESS.grant(player);
                     DARKNESS_COUNTER.put(var11, 0);
                  }
               } else {
                  DARKNESS_COUNTER.remove(var11);
               }

               Entity vehicle = player.m_20202_();
               MemeAdvancementHandler.BoatRide ride = (MemeAdvancementHandler.BoatRide)BOAT_RIDES.get(var11);
               if (vehicle instanceof Boat) {
                  if (ride == null || ride.vehicleId != vehicle.m_19879_()) {
                     BOAT_RIDES.put(var11, new MemeAdvancementHandler.BoatRide(vehicle.m_19879_(), vehicle.m_20182_()));
                  } else if (vehicle.m_20182_().m_82554_(ride.lastPosition) < 0.01) {
                     BOAT_RIDES.remove(var11);
                  } else {
                     ride.lastPosition = vehicle.m_20182_();
                     ride.ticks++;
                     if (ride.ticks >= 500) {
                        BOAT_RIDES.remove(var11);
                        MemeFlagTrigger.BOAT_RIDE.grant(player);
                     }
                  }
               } else {
                  BOAT_RIDES.remove(var11);
               }
            }
         }
      }
   }

   private static boolean skyVisibleAbove(ServerLevel level, ServerPlayer player) {
      int surface = level.m_6924_(Types.MOTION_BLOCKING, player.m_146903_(), player.m_146907_());
      return player.m_20183_().m_123342_() >= surface;
   }

   @SubscribeEvent
   public static void onPlayerLoggedOut(PlayerLoggedOutEvent event) {
      UUID id = event.getEntity().m_20148_();
      LAST_POSITION.remove(id);
      SKY_GAZE_COUNTER.remove(id);
      DARKNESS_COUNTER.remove(id);
      BOAT_RIDES.remove(id);
   }

   private static final class BoatRide {
      private final int vehicleId;
      private Vec3 lastPosition;
      private int ticks;

      private BoatRide(int vehicleId, Vec3 lastPosition) {
         this.vehicleId = vehicleId;
         this.lastPosition = lastPosition;
         this.ticks = 0;
      }
   }

   private static final class FoodToss {
      private final UUID throwerId;
      private final long expiry;

      private FoodToss(UUID throwerId, long expiry) {
         this.throwerId = throwerId;
         this.expiry = expiry;
      }
   }
}
