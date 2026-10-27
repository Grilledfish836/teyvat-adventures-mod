package cn.blockforge.generated.teyvatadventures;

import cn.blockforge.generated.teyvatadventures.resonance.PlayerElementData;
import cn.blockforge.generated.teyvatadventures.service.CooldownService;
import cn.blockforge.generated.teyvatadventures.service.CurseEffectManager;
import cn.blockforge.generated.teyvatadventures.service.PlayerFlightSpeedService;
import cn.blockforge.generated.teyvatadventures.service.TradePriceService;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Finish;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Start;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Stop;
import net.minecraftforge.event.entity.living.MobEffectEvent.Applicable;
import net.minecraftforge.event.entity.living.MobEffectEvent.Remove;
import net.minecraftforge.event.entity.player.PlayerContainerEvent.Close;
import net.minecraftforge.event.entity.player.PlayerContainerEvent.Open;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerXpEvent.XpChange;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import org.slf4j.Logger;

public final class WorldBeyondDescenderCurseHandler {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final String SPRINT_TICKS = "teyvat_adventures_sprint_ticks";
   private static final String FOOD_EFFECT_SKIP = "teyvat_adventures_food_effect_skip";
   private static final String FOOD_EFFECT_SNAPSHOT = "teyvat_adventures_food_effect_snapshot";
   private static final String CORE_INITIALIZED = "teyvat_adventures_core_initialized";
   private static final String CORE_WORN = "teyvat_adventures_core_worn";
   private static final String PENDING_SCORCH_COUNT = "teyvat_adventures_pending_scorch_count";
   private static final String WATER_BREATH_REMAINDER = "teyvat_adventures_water_breath_remainder";
   private static final String RESTORE_FOOD_TICK = "teyvat_adventures_restore_food_tick";
   private static final String EFFECT_UPGRADE_GUARD = "teyvat_adventures_effect_upgrade_guard";
   private static final String PENDING_EFFECT_UPGRADES = "teyvat_adventures_pending_effect_upgrades";
   private static final String PENDING_CORE_REEQUIP = "teyvat_adventures_pending_core_reequip";
   private static final UUID FLYING_SPEED_MODIFIER = UUID.fromString("7f0cf6a0-0b70-4a2e-a5e8-000000000001");
   private static final UUID ARMOR_MODIFIER = UUID.fromString("7f0cf6a0-0b70-4a2e-a5e8-000000000002");
   private static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("7f0cf6a0-0b70-4a2e-a5e8-000000000003");
   private static final UUID MOVEMENT_SPEED_MODIFIER = UUID.fromString("7f0cf6a0-0b70-4a2e-a5e8-000000000004");
   private static final UUID BLOCK_REACH_MODIFIER = UUID.fromString("7f0cf6a0-0b70-4a2e-a5e8-000000000005");
   private static final UUID ENTITY_REACH_MODIFIER = UUID.fromString("7f0cf6a0-0b70-4a2e-a5e8-000000000006");
   private static final UUID KNOCKBACK_RESISTANCE_MODIFIER = UUID.fromString("7f0cf6a0-0b70-4a2e-a5e8-000000000007");

   private WorldBeyondDescenderCurseHandler() {
   }

   private static void safe(String stage, Runnable body) {
      try {
         body.run();
      } catch (Exception crash) {
         LOGGER.error("[TeyvatAdventures] handler '{}' failed; kept vanilla behaviour for this call.", stage, crash);
      }
   }

   @SubscribeEvent
   public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
      safe("player-logged-in", () -> {
         CooldownService.install(event.getEntity());
         ensureCore(event.getEntity());
         PlayerFlightSpeedService.sync(event.getEntity());
      });
   }

   @SubscribeEvent
   public static void onPlayerRespawn(PlayerRespawnEvent event) {
      safe("player-respawn", () -> {
         CooldownService.install(event.getEntity());
         PlayerFlightSpeedService.sync(event.getEntity());
      });
   }

   @SubscribeEvent
   public static void onPlayerClone(Clone event) {
      safe("player-clone", () -> clonePlayerData(event));
   }

   private static void clonePlayerData(Clone event) {
      if (event.isWasDeath()) {
         Player player = event.getEntity();
         Player original = event.getOriginal();
         boolean originalHadCore = hasCore(original) || original.getPersistentData().m_128471_("teyvat_adventures_core_worn");
         if (original.getPersistentData().m_128471_("teyvat_adventures_core_initialized") || originalHadCore) {
            player.getPersistentData().m_128379_("teyvat_adventures_core_initialized", true);
         }

         if (originalHadCore && !hasCore(player)) {
            player.getPersistentData().m_128379_("teyvat_adventures_pending_core_reequip", true);
         }

         PlayerElementData.copyFrom(player, original);
         PlayerFlightSpeedService.copyOntoClone(player, original);
      }
   }

   @SubscribeEvent
   public static void onPlayerTick(PlayerTickEvent event) {
      Player player = event.player;
      if (event.phase == Phase.START) {
         if (!player.m_9236_().m_5776_()) {
            CooldownService.install(player);
         }
      } else if (player.m_6084_() && !player.m_213877_()) {
         safe("player-tick-end", () -> tickPlayer(player));
      }
   }

   private static void tickPlayer(Player player) {
      if (player.m_21023_((MobEffect)GeneratedMod.EXHAUSTION_EFFECT.get())) {
         player.m_6858_(false);
      }

      PlayerFlightSpeedService.sync(player);
      if (!player.m_9236_().m_5776_()) {
         restoreFoodEffects(player);
         if (player.getPersistentData().m_128471_("teyvat_adventures_pending_core_reequip")) {
            player.getPersistentData().m_128473_("teyvat_adventures_pending_core_reequip");
            if (!hasCore(player)) {
               CuriosCompat.setCoreInSeal(player, new ItemStack((ItemLike)GeneratedMod.WORLD_BEYOND_DESCENDER.get()));
            }
         }

         ensureCore(player);
         boolean hasCore = hasCore(player);
         player.getPersistentData().m_128379_("teyvat_adventures_core_worn", hasCore);
         if (hasCore) {
            processPendingEffectUpgrades(player);
         }

         CuriosCompat.updateResonanceSlots(player, hasCore);
         updateFlight(player);
         if (!hasCore) {
            clearAttributeCurses(player);
            player.m_21195_((MobEffect)GeneratedMod.EXHAUSTION_EFFECT.get());
            player.getPersistentData().m_128405_("teyvat_adventures_sprint_ticks", 0);
            player.getPersistentData().m_128473_("teyvat_adventures_pending_scorch_count");
            player.getPersistentData().m_128473_("teyvat_adventures_water_breath_remainder");
            player.getPersistentData().m_128473_("teyvat_adventures_restore_food_tick");
            player.getPersistentData().m_128473_("teyvat_adventures_pending_effect_upgrades");
         } else {
            applyAttributeCurses(player);
            applyWindCurse(player);
            applyPyroCurse(player);
            applyPendingScorch(player);
         }
      }
   }

   private static void processPendingEffectUpgrades(Player player) {
      CompoundTag data = player.getPersistentData();
      if (data.m_128425_("teyvat_adventures_pending_effect_upgrades", 9) && !data.m_128471_("teyvat_adventures_effect_upgrade_guard")) {
         ListTag pending = data.m_128437_("teyvat_adventures_pending_effect_upgrades", 10);
         data.m_128473_("teyvat_adventures_pending_effect_upgrades");
         data.m_128379_("teyvat_adventures_effect_upgrade_guard", true);

         try {
            for (int i = 0; i < pending.size(); i++) {
               CompoundTag entry = pending.m_128728_(i);
               ResourceLocation id = ResourceLocation.m_135820_(entry.m_128461_("Effect"));
               MobEffect effect = id == null ? null : (MobEffect)BuiltInRegistries.f_256974_.m_7745_(id);
               MobEffectInstance current = effect == null ? null : player.m_21124_(effect);
               if (effect != null && current != null && effect.m_19483_() == MobEffectCategory.HARMFUL) {
                  int incomingAmplifier = Math.max(0, Math.min(255, entry.m_128451_("Amplifier")));
                  int amplifier = Math.min(255, Math.max(current.m_19564_(), incomingAmplifier) + 1);
                  int finalDuration;
                  if (!current.m_267577_() && !entry.m_128471_("Infinite")) {
                     long duration = Math.max(0L, current.m_19557_()) + 60L;
                     finalDuration = (int)Math.min(2147483647L, duration);
                  } else {
                     finalDuration = -1;
                  }

                  player.m_147215_(new MobEffectInstance(effect, finalDuration, amplifier, current.m_19571_(), current.m_19572_(), current.m_19575_()), null);
               }
            }
         } finally {
            data.m_128379_("teyvat_adventures_effect_upgrade_guard", false);
         }
      }
   }

   private static void queueEffectUpgrade(Player player, MobEffectInstance incoming) {
      ResourceLocation id = BuiltInRegistries.f_256974_.m_7981_(incoming.m_19544_());
      if (id != null) {
         CompoundTag data = player.getPersistentData();
         ListTag pending = data.m_128425_("teyvat_adventures_pending_effect_upgrades", 9)
            ? data.m_128437_("teyvat_adventures_pending_effect_upgrades", 10)
            : new ListTag();
         CompoundTag entry = new CompoundTag();
         entry.m_128359_("Effect", id.toString());
         entry.m_128405_("Amplifier", Math.max(0, Math.min(255, incoming.m_19564_())));
         entry.m_128379_("Infinite", incoming.m_267577_());
         pending.add(entry);
         data.m_128365_("teyvat_adventures_pending_effect_upgrades", pending);
      }
   }

   private static void ensureCore(Player player) {
      if (!player.m_9236_().m_5776_()) {
         if (hasCore(player)) {
            player.getPersistentData().m_128379_("teyvat_adventures_core_initialized", true);
         } else if (!player.getPersistentData().m_128471_("teyvat_adventures_core_initialized")) {
            ItemStack core = findCoreInInventory(player);
            if (core.m_41619_()) {
               core = new ItemStack((ItemLike)GeneratedMod.WORLD_BEYOND_DESCENDER.get());
            }

            if (CuriosCompat.setCoreInSeal(player, core.m_255036_(1))) {
               player.getPersistentData().m_128379_("teyvat_adventures_core_initialized", true);
               removeCoreFromInventory(player);
            }
         }
      }
   }

   private static ItemStack findCoreInInventory(Player player) {
      for (int slot = 0; slot < player.m_150109_().m_6643_(); slot++) {
         ItemStack stack = player.m_150109_().m_8020_(slot);
         if (stack.m_150930_((Item)GeneratedMod.WORLD_BEYOND_DESCENDER.get())) {
            return stack;
         }
      }

      return ItemStack.f_41583_;
   }

   private static void removeCoreFromInventory(Player player) {
      for (int slot = 0; slot < player.m_150109_().m_6643_(); slot++) {
         ItemStack stack = player.m_150109_().m_8020_(slot);
         if (stack.m_150930_((Item)GeneratedMod.WORLD_BEYOND_DESCENDER.get())) {
            stack.m_41774_(1);
            return;
         }
      }
   }

   private static boolean hasCore(Player player) {
      return CuriosCompat.hasCoreInSeal(player);
   }

   private static void updateFlight(Player player) {
      boolean hasAuthority = hasAuthority(player);
      boolean wasGranted = player.getPersistentData().m_128471_("teyvat_adventures_authority_flight");
      if (hasAuthority) {
         if (!wasGranted) {
            player.getPersistentData().m_128379_("teyvat_adventures_authority_original_mayfly", player.m_150110_().f_35936_);
            player.getPersistentData().m_128379_("teyvat_adventures_authority_original_flying", player.m_150110_().f_35935_);
            player.getPersistentData().m_128379_("teyvat_adventures_authority_flight", true);
         }

         if (!player.m_150110_().f_35936_) {
            player.m_150110_().f_35936_ = true;
            player.m_6885_();
         }
      } else if (wasGranted) {
         boolean originalMayfly = player.getPersistentData().m_128471_("teyvat_adventures_authority_original_mayfly");
         boolean originalFlying = player.getPersistentData().m_128471_("teyvat_adventures_authority_original_flying");
         player.m_150110_().f_35936_ = originalMayfly;
         player.m_150110_().f_35935_ = originalFlying && originalMayfly;
         player.getPersistentData().m_128379_("teyvat_adventures_authority_flight", false);
         player.m_6885_();
      }
   }

   private static boolean hasAuthority(Player player) {
      for (int slot = 0; slot < player.m_150109_().m_6643_(); slot++) {
         if (player.m_150109_().m_8020_(slot).m_150930_((Item)GeneratedMod.PAIMONS_AUTHORITY.get())) {
            return true;
         }
      }

      return false;
   }

   private static void applyAttributeCurses(Player player) {
      addModifier(player, Attributes.f_22284_, ARMOR_MODIFIER, "World Beyond Descender: Rock", -0.15, Operation.MULTIPLY_TOTAL);
      addModifier(
         player, Attributes.f_22278_, KNOCKBACK_RESISTANCE_MODIFIER, "World Beyond Descender: Rock knockback resistance", -0.15, Operation.MULTIPLY_TOTAL
      );
      addModifier(player, Attributes.f_22283_, ATTACK_SPEED_MODIFIER, "World Beyond Descender: Electro", -0.1, Operation.MULTIPLY_TOTAL);
      addModifier(player, Attributes.f_22279_, MOVEMENT_SPEED_MODIFIER, "World Beyond Descender: Wind movement speed", -0.1, Operation.MULTIPLY_TOTAL);
      addModifier(player, (Attribute)ForgeMod.BLOCK_REACH.get(), BLOCK_REACH_MODIFIER, "World Beyond Descender: Wind block reach", -0.2, Operation.ADDITION);
      addModifier(player, (Attribute)ForgeMod.ENTITY_REACH.get(), ENTITY_REACH_MODIFIER, "World Beyond Descender: Wind entity reach", -0.2, Operation.ADDITION);
   }

   private static void addModifier(Player player, Attribute attribute, UUID id, String name, double amount, Operation operation) {
      AttributeInstance instance = player.m_21051_(attribute);
      if (instance != null && instance.m_22111_(id) == null) {
         instance.m_22118_(new AttributeModifier(id, name, amount, operation));
      }
   }

   private static void applyWindCurse(Player player) {
      if (player.m_21023_((MobEffect)GeneratedMod.EXHAUSTION_EFFECT.get())) {
         player.m_6858_(false);
         player.getPersistentData().m_128405_("teyvat_adventures_sprint_ticks", 0);
      } else if (!player.m_20142_()) {
         player.getPersistentData().m_128405_("teyvat_adventures_sprint_ticks", 0);
      } else {
         int sprintTicks = player.getPersistentData().m_128451_("teyvat_adventures_sprint_ticks") + 1;
         player.getPersistentData().m_128405_("teyvat_adventures_sprint_ticks", sprintTicks);
         if (sprintTicks >= 140) {
            player.m_6858_(false);
            player.m_147215_(new MobEffectInstance((MobEffect)GeneratedMod.EXHAUSTION_EFFECT.get(), 80, 0, false, true, true), null);
            player.getPersistentData().m_128405_("teyvat_adventures_sprint_ticks", 0);
         }
      }
   }

   @SubscribeEvent
   public static void onBreathe(LivingBreatheEvent event) {
      if (event.getEntity() instanceof Player player
         && !player.m_9236_().m_5776_()
         && hasCore(player)
         && !player.m_6040_()
         && player.m_204029_(FluidTags.f_13131_)) {
         if (!event.canBreathe() && event.getConsumeAirAmount() > 0) {
            CompoundTag data = player.getPersistentData();
            int remainder = data.m_128451_("teyvat_adventures_water_breath_remainder") + event.getConsumeAirAmount();
            int extra = remainder / 4;
            data.m_128405_("teyvat_adventures_water_breath_remainder", remainder % 4);
            event.setConsumeAirAmount(event.getConsumeAirAmount() + extra);
         }
      }
   }

   private static void applyPyroCurse(Player player) {
      if (player.m_6060_() && player.m_9236_().m_46467_() % 20L == 0L) {
         player.m_7311_(Math.min(player.m_20094_() + 20, 300));
      }
   }

   private static void applyPendingScorch(Player player) {
      int hitCount = player.getPersistentData().m_128451_("teyvat_adventures_pending_scorch_count");
      if (hitCount > 0) {
         player.getPersistentData().m_128405_("teyvat_adventures_pending_scorch_count", 0);
         player.m_147215_(new MobEffectInstance((MobEffect)GeneratedMod.SCORCH_EFFECT.get(), 60, 0, false, true, true), null);
      }
   }

   @SubscribeEvent
   public static void onBreakSpeed(BreakSpeed event) {
      Player player = event.getEntity();
      if (hasCore(player)) {
         float speed = event.getNewSpeed() * 0.9F;
         if (!player.m_20096_()) {
            speed *= 0.8F;
         }

         event.setNewSpeed(speed);
      }
   }

   @SubscribeEvent
   public static void onFall(LivingDamageEvent event) {
      if (event.getEntity() instanceof Player player && hasCore(player) && event.getSource().m_269533_(DamageTypeTags.f_268549_)) {
         event.setAmount(event.getAmount() * 1.1F);
      }
   }

   @SubscribeEvent
   public static void onHurt(LivingDamageEvent event) {
      if (event.getEntity() instanceof Player player && hasCore(player)) {
         DamageSource source = event.getSource();
         if (source.m_269533_(DamageTypeTags.f_268745_)) {
            event.setAmount(event.getAmount() * 1.1F);
            player.getPersistentData()
               .m_128405_("teyvat_adventures_pending_scorch_count", player.getPersistentData().m_128451_("teyvat_adventures_pending_scorch_count") + 1);
         }

         boolean lethalBlow = event.getAmount() >= player.m_21223_() || player.m_213877_();
         if (source.m_7640_() != null && !lethalBlow && player.m_9236_().m_213780_().m_188501_() < 0.15F) {
            applyOrRefreshEffect(player, (MobEffect)GeneratedMod.FREEZE_EFFECT.get(), 100, 0, false);
         }

         if (source.m_276093_(DamageTypes.f_268515_) || source.m_276093_(DamageTypes.f_268530_)) {
            event.setAmount(event.getAmount() * 1.1F);
         }
      }
   }

   @SubscribeEvent(priority = EventPriority.HIGHEST)
   public static void onFreezeDamage(LivingDamageEvent event) {
      MobEffectInstance effect = event.getEntity().m_21124_((MobEffect)GeneratedMod.FREEZE_EFFECT.get());
      if (effect != null) {
         int level = Mth.m_14045_(effect.m_19564_() + 1, 1, 34);
         float multiplier = 1.0F + level * 0.05F;
         event.setAmount(event.getAmount() * multiplier);
      }
   }

   private static void applyOrRefreshEffect(Player player, MobEffect effect, int duration, int amplifier, boolean ambient) {
      MobEffectInstance current = player.m_21124_(effect);
      int currentAmplifier = current == null ? -1 : Math.max(-1, current.m_19564_());
      int finalAmplifier = Math.min(33, Math.max(amplifier, currentAmplifier + 1));
      int finalDuration = Math.max(1, duration);
      if (current != null) {
         finalDuration = Math.max(finalDuration, Math.max(0, current.m_19557_()));
      }

      if (current != null && finalAmplifier > currentAmplifier && player.m_9236_().m_213780_().m_188501_() < 0.5F) {
         finalDuration += 40;
      }

      player.m_147215_(new MobEffectInstance(effect, finalDuration, finalAmplifier, ambient, true, true), null);
   }

   private static void restoreFoodEffects(Player player) {
      CompoundTag data = player.getPersistentData();
      if (data.m_128441_("teyvat_adventures_restore_food_tick") && player.m_9236_().m_46467_() >= data.m_128454_("teyvat_adventures_restore_food_tick")) {
         data.m_128473_("teyvat_adventures_restore_food_tick");
         if (!data.m_128471_("teyvat_adventures_food_effect_skip")) {
            data.m_128473_("teyvat_adventures_food_effect_snapshot");
         } else {
            data.m_128379_("teyvat_adventures_food_effect_skip", false);
            CompoundTag snapshot = data.m_128469_("teyvat_adventures_food_effect_snapshot");
            Map<MobEffect, MobEffectInstance> before = new HashMap();
            int count = snapshot.m_128451_("Count");

            for (int i = 0; i < count; i++) {
               MobEffectInstance effect = MobEffectInstance.m_19560_(snapshot.m_128469_("Effect" + i));
               if (effect != null) {
                  before.put(effect.m_19544_(), effect);
               }
            }

            data.m_128379_("teyvat_adventures_effect_upgrade_guard", true);

            try {
               for (MobEffectInstance effect : new ArrayList(player.m_21220_())) {
                  if (!before.containsKey(effect.m_19544_())) {
                     player.m_21195_(effect.m_19544_());
                  }
               }

               for (MobEffectInstance effect : before.values()) {
                  player.m_21195_(effect.m_19544_());
                  player.m_147215_(new MobEffectInstance(effect), null);
               }
            } finally {
               data.m_128379_("teyvat_adventures_effect_upgrade_guard", false);
               data.m_128473_("teyvat_adventures_food_effect_snapshot");
            }
         }
      }
   }

   @SubscribeEvent
   public static void onDamageDealt(LivingDamageEvent event) {
      if (event.getSource().m_7639_() instanceof Player player && hasCore(player)) {
         float amount = event.getAmount() * (player.m_21023_((MobEffect)GeneratedMod.SCORCH_EFFECT.get()) ? 0.75F : 0.9F);
         if (!player.m_20096_()) {
            amount *= 0.95F;
         }

         event.setAmount(amount);
      }
   }

   @SubscribeEvent
   public static void onEffectApplicable(Applicable event) {
      if (event.getEntity() instanceof Player player
         && !player.m_9236_().m_5776_()
         && hasCore(player)
         && !player.getPersistentData().m_128471_("teyvat_adventures_effect_upgrade_guard")) {
         MobEffectInstance incoming = event.getEffectInstance();
         if (incoming != null && incoming.m_19544_() != null) {
            MobEffect effect = incoming.m_19544_();
            MobEffectInstance current = player.m_21124_(effect);
            if (current != null
               && effect != GeneratedMod.SCORCH_EFFECT.get()
               && effect != GeneratedMod.FREEZE_EFFECT.get()
               && effect != GeneratedMod.EXHAUSTION_EFFECT.get()
               && effect.m_19483_() == MobEffectCategory.HARMFUL
               && !(player.m_9236_().m_213780_().m_188501_() >= 0.3F)) {
               queueEffectUpgrade(player, incoming);
            }
         }
      }
   }

   @SubscribeEvent
   public static void onFoodStart(Start event) {
      if (event.getEntity() instanceof Player player
         && !player.m_9236_().m_5776_()
         && hasCore(player)
         && event.getItem().m_41614_()
         && !player.getPersistentData().m_128471_("teyvat_adventures_food_effect_skip")) {
         FoodProperties food = event.getItem().getFoodProperties(player);
         if (food != null && !food.m_38749_().isEmpty()) {
            for (Pair<MobEffectInstance, Float> foodEffect : food.m_38749_()) {
               MobEffect type = ((MobEffectInstance)foodEffect.getFirst()).m_19544_();
               if (type != null && type.m_19483_() == MobEffectCategory.HARMFUL) {
                  return;
               }
            }

            if (!(player.m_9236_().m_213780_().m_188501_() >= 0.4F)) {
               player.getPersistentData().m_128379_("teyvat_adventures_food_effect_skip", true);
               List<CompoundTag> snapshot = new ArrayList();

               for (MobEffectInstance effect : player.m_21220_()) {
                  CompoundTag tag = new CompoundTag();
                  effect.m_19555_(tag);
                  snapshot.add(tag);
               }

               CompoundTag data = new CompoundTag();
               data.m_128405_("Count", snapshot.size());

               for (int i = 0; i < snapshot.size(); i++) {
                  data.m_128365_("Effect" + i, (Tag)snapshot.get(i));
               }

               player.getPersistentData().m_128365_("teyvat_adventures_food_effect_snapshot", data);
            }
         }
      }
   }

   @SubscribeEvent
   public static void onFoodStop(Stop event) {
      if (event.getEntity() instanceof Player player && !player.m_9236_().m_5776_()) {
         CompoundTag data = player.getPersistentData();
         if (data.m_128471_("teyvat_adventures_food_effect_skip") && !data.m_128441_("teyvat_adventures_restore_food_tick")) {
            data.m_128379_("teyvat_adventures_food_effect_skip", false);
            data.m_128473_("teyvat_adventures_food_effect_snapshot");
         }
      }
   }

   @SubscribeEvent
   public static void onFoodFinish(Finish event) {
      if (event.getEntity() instanceof Player player && !player.m_9236_().m_5776_()) {
         CompoundTag data = player.getPersistentData();
         if (data.m_128471_("teyvat_adventures_food_effect_skip") && data.m_128441_("teyvat_adventures_food_effect_snapshot")) {
            data.m_128356_("teyvat_adventures_restore_food_tick", player.m_9236_().m_46467_() + 1L);
         }
      }
   }

   @SubscribeEvent
   public static void onXp(XpChange event) {
      if (hasCore(event.getEntity())) {
         event.setAmount(Math.round(event.getAmount() * 0.8F));
      }
   }

   @SubscribeEvent
   public static void onContainerOpen(Open event) {
      if (event.getContainer() instanceof MerchantMenu menu && event.getEntity() instanceof ServerPlayer player) {
         safe("trade-menu-open", () -> TradePriceService.onOpen(player, menu, hasCore(player)));
      }
   }

   @SubscribeEvent
   public static void onContainerClose(Close event) {
      if (event.getContainer() instanceof MerchantMenu menu && event.getEntity() instanceof ServerPlayer player) {
         safe("trade-menu-close", () -> TradePriceService.onClosed(player, menu));
      }
   }

   @SubscribeEvent
   public static void onMerchantJoin(EntityJoinLevelEvent event) {
      if (!event.getLevel().m_5776_() && event.getEntity() instanceof Merchant) {
         safe("trade-load-restore", () -> TradePriceService.restoreOnLoad(event.getEntity()));
      }
   }

   @SubscribeEvent
   public static void onRightClickBlock(RightClickBlock event) {
      Player player = event.getEntity();
      if (hasCore(player) && event.getItemStack().m_41720_() instanceof BlockItem) {
         String key = "teyvat_adventures_place_tick";
         long now = event.getLevel().m_46467_();
         if (player.getPersistentData().m_128441_(key) && now - player.getPersistentData().m_128454_(key) < 4L) {
            event.setUseBlock(Result.DENY);
            event.setUseItem(Result.DENY);
            event.setCanceled(true);
         } else {
            player.getPersistentData().m_128356_(key, now);
         }
      }
   }

   @SubscribeEvent
   public static void onEffectRemoved(Remove event) {
      safe("effect-removed", () -> {
         MobEffectInstance removed = event.getEffectInstance();
         if (removed != null) {
            MobEffect effect = removed.m_19544_();
            if (effect == GeneratedMod.FREEZE_EFFECT.get()) {
               CurseEffectManager.clearFreeze(event.getEntity());
            }

            if (event.getEntity() instanceof Player player && (effect == GeneratedMod.FREEZE_EFFECT.get() || effect == GeneratedMod.EXHAUSTION_EFFECT.get())) {
               PlayerFlightSpeedService.sync(player);
            }
         }
      });
   }

   private static void clearAttributeCurses(Player player) {
      removeModifier(player, Attributes.f_22280_, FLYING_SPEED_MODIFIER);
      removeModifier(player, Attributes.f_22284_, ARMOR_MODIFIER);
      removeModifier(player, Attributes.f_22278_, KNOCKBACK_RESISTANCE_MODIFIER);
      removeModifier(player, Attributes.f_22283_, ATTACK_SPEED_MODIFIER);
      removeModifier(player, Attributes.f_22279_, MOVEMENT_SPEED_MODIFIER);
      removeModifier(player, (Attribute)ForgeMod.BLOCK_REACH.get(), BLOCK_REACH_MODIFIER);
      removeModifier(player, (Attribute)ForgeMod.ENTITY_REACH.get(), ENTITY_REACH_MODIFIER);
      CurseEffectManager.clearFreeze(player);
   }

   public static void removeLegacyFlyingSpeedModifier(Player player) {
      if (player != null && !player.m_213877_()) {
         removeModifier(player, Attributes.f_22280_, FLYING_SPEED_MODIFIER);
      }
   }

   private static void removeModifier(Player player, Attribute attribute, UUID id) {
      AttributeInstance instance = player.m_21051_(attribute);
      if (instance != null) {
         instance.m_22120_(id);
      }
   }
}
