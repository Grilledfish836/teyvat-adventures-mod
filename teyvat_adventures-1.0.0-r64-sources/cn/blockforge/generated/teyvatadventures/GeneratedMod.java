package cn.blockforge.generated.teyvatadventures;

import cn.blockforge.generated.teyvatadventures.element.Element;
import cn.blockforge.generated.teyvatadventures.meme.MemeAdvancementHandler;
import cn.blockforge.generated.teyvatadventures.meme.MemeFlagTrigger;
import cn.blockforge.generated.teyvatadventures.resonance.ElementResonanceHandler;
import cn.blockforge.generated.teyvatadventures.resonance.ElementSyncScheduler;
import cn.blockforge.generated.teyvatadventures.resonance.PlayerElementDataCapability;
import cn.blockforge.generated.teyvatadventures.resonance.ResonanceEventHandler;
import cn.blockforge.generated.teyvatadventures.resonance.ResonanceNetwork;
import cn.blockforge.generated.teyvatadventures.resonance.ResonanceRewardService;
import cn.blockforge.generated.teyvatadventures.resonance.ResonanceTrigger;
import com.mojang.logging.LogUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod("teyvat_adventures")
public final class GeneratedMod {
   public static final String MOD_ID = "teyvat_adventures";
   public static final Logger LOGGER = LogUtils.getLogger();
   public static final String DESCENDER_SEAL_SLOT = "descender_seal";
   public static final String ELEMENTAL_RESONANCE_SLOT = "elemental_resonance";
   public static final String RESONANCE_MARKER = "teyvat_adventures_resonance_slots";
   public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "teyvat_adventures");
   public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "teyvat_adventures");
   public static final RegistryObject<MobEffect> SCORCH_EFFECT = EFFECTS.register("scorch", ScorchEffect::new);
   public static final RegistryObject<MobEffect> FREEZE_EFFECT = EFFECTS.register("freeze", FreezeEffect::new);
   public static final RegistryObject<MobEffect> EXHAUSTION_EFFECT = EFFECTS.register("exhaustion", ExhaustionEffect::new);
   public static final RegistryObject<WorldBeyondDescenderItem> WORLD_BEYOND_DESCENDER = ITEMS.register(
      "world_beyond_descender", () -> new WorldBeyondDescenderItem(new Properties().m_41487_(1).m_41497_(Rarity.EPIC))
   );
   public static final RegistryObject<PaimonsAuthorityItem> PAIMONS_AUTHORITY = ITEMS.register(
      "paimons_authority", () -> new PaimonsAuthorityItem(new Properties().m_41487_(1).m_41497_(Rarity.EPIC))
   );
   public static final RegistryObject<ElementalResonanceItem> ELEMENTAL_RESONANCE_ANEMO = ITEMS.register(
      "elemental_resonance_anemo", () -> new ElementalResonanceItem(itemProps(), Element.ANEMO)
   );
   public static final RegistryObject<ElementalResonanceItem> ELEMENTAL_RESONANCE_GEO = ITEMS.register(
      "elemental_resonance_geo", () -> new ElementalResonanceItem(itemProps(), Element.GEO)
   );
   public static final RegistryObject<ElementalResonanceItem> ELEMENTAL_RESONANCE_ELECTRO = ITEMS.register(
      "elemental_resonance_electro", () -> new ElementalResonanceItem(itemProps(), Element.ELECTRO)
   );
   public static final RegistryObject<ElementalResonanceItem> ELEMENTAL_RESONANCE_DENDRO = ITEMS.register(
      "elemental_resonance_dendro", () -> new ElementalResonanceItem(itemProps(), Element.DENDRO)
   );
   public static final RegistryObject<ElementalResonanceItem> ELEMENTAL_RESONANCE_HYDRO = ITEMS.register(
      "elemental_resonance_hydro", () -> new ElementalResonanceItem(itemProps(), Element.HYDRO)
   );
   public static final RegistryObject<ElementalResonanceItem> ELEMENTAL_RESONANCE_PYRO = ITEMS.register(
      "elemental_resonance_pyro", () -> new ElementalResonanceItem(itemProps(), Element.PYRO)
   );
   public static final RegistryObject<ElementalResonanceItem> ELEMENTAL_RESONANCE_CRYO = ITEMS.register(
      "elemental_resonance_cryo", () -> new ElementalResonanceItem(itemProps(), Element.CRYO)
   );

   private static Properties itemProps() {
      return new Properties().m_41497_(Rarity.UNCOMMON).m_41486_();
   }

   public GeneratedMod() {
      IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
      ITEMS.register(modBus);
      EFFECTS.register(modBus);
      modBus.addListener(GeneratedMod::enqueueIMC);
      modBus.addListener(GeneratedMod::commonSetup);
      modBus.addListener(GeneratedMod::addCreativeItems);
      modBus.addListener(PlayerElementDataCapability::attach);
      MinecraftForge.EVENT_BUS.register(WorldBeyondDescenderCurseHandler.class);
      MinecraftForge.EVENT_BUS.register(ResonanceEventHandler.class);
      MinecraftForge.EVENT_BUS.register(ResonanceNetwork.class);
      MinecraftForge.EVENT_BUS.register(ElementResonanceHandler.class);
      MinecraftForge.EVENT_BUS.register(ElementSyncScheduler.class);
      MinecraftForge.EVENT_BUS.register(MemeAdvancementHandler.class);
   }

   private static void commonSetup(FMLCommonSetupEvent event) {
      event.enqueueWork(() -> {
         CuriosCompat.registerCoreCurio();
         CuriosCompat.registerResonanceCurios();
         ResonanceNetwork.register();
      });
      event.enqueueWork(() -> (ResonanceTrigger)CriteriaTriggers.m_10595_(ResonanceRewardService.trigger()));
      event.enqueueWork(MemeFlagTrigger::registerAll);
   }

   private static void enqueueIMC(InterModEnqueueEvent event) {
      InterModComms.sendTo("curios", "register_type", CuriosCompat::createSealSlotMessage);
      InterModComms.sendTo("curios", "register_type", CuriosCompat::createResonanceSlotMessage);
   }

   private static void addCreativeItems(BuildCreativeModeTabContentsEvent event) {
      ResourceKey<?> tab = event.getTabKey();
      if (CreativeModeTabs.f_256869_.equals(tab)) {
         event.accept(WORLD_BEYOND_DESCENDER);
         event.accept(PAIMONS_AUTHORITY);

         for (Element element : Element.values()) {
            event.accept(element.item());
         }
      }
   }
}
