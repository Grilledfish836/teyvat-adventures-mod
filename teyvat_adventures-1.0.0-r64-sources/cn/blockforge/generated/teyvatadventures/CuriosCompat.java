package cn.blockforge.generated.teyvatadventures;

import cn.blockforge.generated.teyvatadventures.element.Element;
import com.google.common.collect.ImmutableMultimap;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class CuriosCompat {
   private static final String CURIOS_API = "top.theillusivec4.curios.api.CuriosApi";
   private static final String CURIOS_ITEM = "top.theillusivec4.curios.api.type.capability.ICurioItem";

   private CuriosCompat() {
   }

   public static boolean hasCoreInSeal(Player player) {
      ItemStack stack = getCoreInSeal(player);
      return !stack.m_41619_() && stack.m_150930_((Item)GeneratedMod.WORLD_BEYOND_DESCENDER.get());
   }

   public static boolean carriesItem(Player player, Item item) {
      if (player != null && item != null) {
         for (int slot = 0; slot < player.m_150109_().m_6643_(); slot++) {
            if (player.m_150109_().m_8020_(slot).m_150930_(item)) {
               return true;
            }
         }

         if (player.m_21206_().m_150930_(item)) {
            return true;
         }

         for (ItemStack armor : player.m_150109_().f_35975_) {
            if (armor.m_150930_(item)) {
               return true;
            }
         }

         try {
            Object handler = getHandler(player);
            if (handler == null) {
               return false;
            } else {
               Class<?> handlerType = Class.forName("top.theillusivec4.curios.api.type.capability.ICuriosItemHandler");
               if (!(handlerType.getMethod("getCurios", new Class[0]).invoke(handler, new Object[0]) instanceof Map<?, ?> map)) {
                  return false;
               } else {
                  Class<?> slotType = Class.forName("top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler");
                  Class<?> stacksType = Class.forName("top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler");
                  Method getStacks = slotType.getMethod("getStacks", new Class[0]);
                  Method getSlots = stacksType.getMethod("getSlots", new Class[0]);
                  Method getStackInSlot = stacksType.getMethod("getStackInSlot", new Class[]{int.class});

                  for (Object stacksHandler : map.values()) {
                     if (stacksHandler != null) {
                        Object stacks = getStacks.invoke(stacksHandler, new Object[0]);
                        int count = (Integer)getSlots.invoke(stacks, new Object[0]);

                        for (int i = 0; i < count; i++) {
                           ItemStack curio = (ItemStack)getStackInSlot.invoke(stacks, new Object[]{i});
                           if (curio != null && curio.m_150930_(item)) {
                              return true;
                           }
                        }
                     }
                  }

                  return false;
               }
            }
         } catch (ReflectiveOperationException | RuntimeException ignored) {
            return true;
         }
      } else {
         return true;
      }
   }

   public static void forEachWornStack(Player player, Consumer<ItemStack> action) {
      if (player != null && action != null) {
         try {
            Object handler = getHandler(player);
            if (handler == null) {
               return;
            }

            Class<?> handlerType = Class.forName("top.theillusivec4.curios.api.type.capability.ICuriosItemHandler");
            if (!(handlerType.getMethod("getCurios", new Class[0]).invoke(handler, new Object[0]) instanceof Map<?, ?> map)) {
               return;
            }

            Class<?> slotType = Class.forName("top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler");
            Class<?> stacksType = Class.forName("top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler");
            Method getStacks = slotType.getMethod("getStacks", new Class[0]);
            Method getSlots = stacksType.getMethod("getSlots", new Class[0]);
            Method getStackInSlot = stacksType.getMethod("getStackInSlot", new Class[]{int.class});

            for (Object stacksHandler : map.values()) {
               if (stacksHandler != null) {
                  Object stacks = getStacks.invoke(stacksHandler, new Object[0]);
                  int count = (Integer)getSlots.invoke(stacks, new Object[0]);

                  for (int i = 0; i < count; i++) {
                     ItemStack curio = (ItemStack)getStackInSlot.invoke(stacks, new Object[]{i});
                     if (curio != null && !curio.m_41619_()) {
                        action.accept(curio);
                     }
                  }
               }
            }
         } catch (ReflectiveOperationException | RuntimeException var17) {
         }
      }
   }

   public static ItemStack getCoreInSeal(Player player) {
      try {
         Object handler = getHandler(player);
         if (handler == null) {
            return ItemStack.f_41583_;
         }

         Class<?> handlerType = Class.forName("top.theillusivec4.curios.api.type.capability.ICuriosItemHandler");
         Object slot = unwrap(handlerType.getMethod("getStacksHandler", new Class[]{String.class}).invoke(handler, new Object[]{"descender_seal"}));
         if (slot == null) {
            return ItemStack.f_41583_;
         }

         Class<?> slotType = Class.forName("top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler");
         Object stacks = slotType.getMethod("getStacks", new Class[0]).invoke(slot, new Object[0]);
         Class<?> stacksType = Class.forName("top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler");
         ItemStack stack = (ItemStack)stacksType.getMethod("getStackInSlot", new Class[]{int.class}).invoke(stacks, new Object[]{0});
         return stack == null ? ItemStack.f_41583_ : stack;
      } catch (ReflectiveOperationException | ClassCastException ignored) {
         return ItemStack.f_41583_;
      }
   }

   public static boolean setCoreInSeal(Player player, ItemStack stack) {
      try {
         Object handler = getHandler(player);
         if (handler == null) {
            return false;
         }

         Class<?> handlerType = Class.forName("top.theillusivec4.curios.api.type.capability.ICuriosItemHandler");
         handlerType.getMethod("setEquippedCurio", new Class[]{String.class, int.class, ItemStack.class})
            .invoke(handler, new Object[]{"descender_seal", 0, stack});
         return true;
      } catch (ReflectiveOperationException ignored) {
         try {
            Object helper = getCuriosApi().getMethod("getCuriosHelper", new Class[0]).invoke(null, new Object[0]);
            Class<?> helperType = Class.forName("top.theillusivec4.curios.api.type.util.ICuriosHelper");
            helperType.getMethod("setEquippedCurio", new Class[]{LivingEntity.class, String.class, int.class, ItemStack.class})
               .invoke(helper, new Object[]{player, "descender_seal", 0, stack});
            return true;
         } catch (ReflectiveOperationException ignoredAgain) {
            return false;
         }
      }
   }

   public static boolean toggleCore(Player player) {
      if (hasCoreInSeal(player)) {
         ItemStack core = getCoreInSeal(player).m_41777_();
         if (!setCoreInSeal(player, ItemStack.f_41583_)) {
            return false;
         }

         player.m_150109_().m_150079_(core);
         return true;
      } else {
         for (int slot = 0; slot < player.m_150109_().m_6643_(); slot++) {
            ItemStack stack = player.m_150109_().m_8020_(slot);
            if (stack.m_150930_((Item)GeneratedMod.WORLD_BEYOND_DESCENDER.get())) {
               ItemStack core = stack.m_255036_(1);
               if (!setCoreInSeal(player, core)) {
                  return false;
               }

               stack.m_41774_(1);
               return true;
            }
         }

         return false;
      }
   }

   public static void registerCoreCurio() {
      try {
         Class<?> curioItem = Class.forName("top.theillusivec4.curios.api.type.capability.ICurioItem");
         Object proxy = Proxy.newProxyInstance(curioItem.getClassLoader(), new Class[]{curioItem}, new CuriosCompat.CoreCurioInvocationHandler());
         getCuriosApi()
            .getMethod("registerCurio", new Class[]{Item.class, curioItem})
            .invoke(null, new Object[]{GeneratedMod.WORLD_BEYOND_DESCENDER.get(), proxy});
      } catch (ReflectiveOperationException var2) {
      }
   }

   public static void registerResonanceCurios() {
      try {
         Class<?> curioItem = Class.forName("top.theillusivec4.curios.api.type.capability.ICurioItem");
         Object proxy = Proxy.newProxyInstance(curioItem.getClassLoader(), new Class[]{curioItem}, new CuriosCompat.ResonanceCurioInvocationHandler());

         for (Element element : Element.values()) {
            getCuriosApi().getMethod("registerCurio", new Class[]{Item.class, curioItem}).invoke(null, new Object[]{element.item().get(), proxy});
         }
      } catch (ReflectiveOperationException var6) {
      }
   }

   public static Object createSealSlotMessage() {
      return createSlotTypeMessage("descender_seal", 1, "slot/descender_seal");
   }

   public static Object createResonanceSlotMessage() {
      return createSlotTypeMessage("elemental_resonance", 0, "slot/elemental_resonance");
   }

   private static Object createSlotTypeMessage(String identifier, int size, String iconPath) {
      try {
         Class<?> builder = Class.forName("top.theillusivec4.curios.api.SlotTypeMessage$Builder");
         Object instance = builder.getConstructor(new Class[]{String.class}).newInstance(new Object[]{identifier});
         instance = builder.getMethod("size", new Class[]{int.class}).invoke(instance, new Object[]{size});
         instance = builder.getMethod("priority", new Class[]{int.class}).invoke(instance, new Object[]{identifier.equals("descender_seal") ? -1000 : -999});
         instance = builder.getMethod("icon", new Class[]{ResourceLocation.class})
            .invoke(instance, new Object[]{ResourceLocation.fromNamespaceAndPath("teyvat_adventures", iconPath)});
         return builder.getMethod("build", new Class[0]).invoke(instance, new Object[0]);
      } catch (ReflectiveOperationException exception) {
         return null;
      }
   }

   public static boolean updateResonanceSlots(Player player, boolean enabled) {
      try {
         Object handler = getHandler(player);
         if (handler == null) {
            return false;
         }

         Class<?> handlerType = Class.forName("top.theillusivec4.curios.api.type.capability.ICuriosItemHandler");
         Object slot = unwrap(handlerType.getMethod("getStacksHandler", new Class[]{String.class}).invoke(handler, new Object[]{"elemental_resonance"}));
         int currentSlots = 0;
         if (slot != null) {
            Class<?> slotType = Class.forName("top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler");
            currentSlots = (Integer)slotType.getMethod("getSlots", new Class[0]).invoke(slot, new Object[0]);
         }

         int targetSlots = enabled ? 7 : 0;
         if (currentSlots < targetSlots) {
            handlerType.getMethod("growSlotType", new Class[]{String.class, int.class})
               .invoke(handler, new Object[]{"elemental_resonance", targetSlots - currentSlots});
         } else if (currentSlots > targetSlots) {
            handlerType.getMethod("shrinkSlotType", new Class[]{String.class, int.class})
               .invoke(handler, new Object[]{"elemental_resonance", currentSlots - targetSlots});
         }

         player.getPersistentData().m_128379_("teyvat_adventures_resonance_slots", enabled);
         return true;
      } catch (ReflectiveOperationException | ClassCastException ignored) {
         return false;
      }
   }

   private static Class<?> getCuriosApi() throws ClassNotFoundException {
      return Class.forName("top.theillusivec4.curios.api.CuriosApi");
   }

   private static Object getHandler(Player player) throws ReflectiveOperationException {
      Object helper = getCuriosApi().getMethod("getCuriosHelper", new Class[0]).invoke(null, new Object[0]);
      Class<?> helperType = Class.forName("top.theillusivec4.curios.api.type.util.ICuriosHelper");
      Object lazy = helperType.getMethod("getCuriosHandler", new Class[]{LivingEntity.class}).invoke(helper, new Object[]{player});
      return unwrap(lazy);
   }

   private static Object unwrap(Object value) throws ReflectiveOperationException {
      if (value == null) {
         return null;
      } else if (value instanceof Optional<?> optional) {
         return optional.orElse(null);
      } else {
         Method resolve = value.getClass().getMethod("resolve", new Class[0]);
         Object resolved = resolve.invoke(value, new Object[0]);
         return resolved instanceof Optional<?> optional ? optional.orElse(null) : resolved;
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

   private static final class CoreCurioInvocationHandler implements InvocationHandler {
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         String name = method.getName();
         if (name.equals("canUnequip")) {
            Player player = this.playerFromArgs(args);
            return player != null && CuriosCompat.hasAuthority(player);
         }

         if (name.equals("canEquip")) {
            return args != null && args.length > 0 && this.slotMatches(args[0]);
         }

         if (name.equals("getDropRule")) {
            Class<?> dropRule = Class.forName("top.theillusivec4.curios.api.type.capability.ICurio$DropRule");
            return Enum.valueOf(dropRule.asSubclass(Enum.class), "ALWAYS_KEEP");
         }

         if (name.equals("hasCurioCapability")) {
            return true;
         }

         if (!name.equals("getSlotsTooltip") && !name.equals("getAttributesTooltip") && !name.equals("getTagsTooltip")) {
            if (name.equals("getAttributeModifiers")) {
               return ImmutableMultimap.of();
            } else if (method.getReturnType() == boolean.class) {
               return false;
            } else {
               return method.getReturnType() == int.class ? 0 : null;
            }
         } else {
            return args != null && args.length > 0 ? args[0] : null;
         }
      }

      private Player playerFromArgs(Object[] args) {
         if (args == null) {
            return null;
         }

         for (Object arg : args) {
            if (arg instanceof Player player) {
               return player;
            }

            if (arg != null) {
               try {
                  if (arg.getClass().getMethod("getWearer", new Class[0]).invoke(arg, new Object[0]) instanceof Player player) {
                     return player;
                  }
               } catch (ReflectiveOperationException var8) {
               }
            }
         }

         return null;
      }

      private boolean slotMatches(Object value) {
         if (value instanceof String identifier) {
            return "descender_seal".equals(identifier);
         } else {
            try {
               return "descender_seal".equals(value.getClass().getMethod("getIdentifier", new Class[0]).invoke(value, new Object[0]));
            } catch (ReflectiveOperationException ignored) {
               return false;
            }
         }
      }
   }

   private static final class ResonanceCurioInvocationHandler implements InvocationHandler {
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         String name = method.getName();
         if (name.equals("canEquip")) {
            return args != null && args.length > 0 && this.slotMatches(args[0], "elemental_resonance");
         }

         if (name.equals("canUnequip")) {
            return true;
         }

         if (name.equals("getDropRule")) {
            Class<?> dropRule = Class.forName("top.theillusivec4.curios.api.type.capability.ICurio$DropRule");
            return Enum.valueOf(dropRule.asSubclass(Enum.class), "NORMAL");
         }

         if (name.equals("hasCurioCapability")) {
            return true;
         }

         if (!name.equals("getSlotsTooltip") && !name.equals("getAttributesTooltip") && !name.equals("getTagsTooltip")) {
            if (name.equals("getAttributeModifiers")) {
               return ImmutableMultimap.of();
            } else if (method.getReturnType() == boolean.class) {
               return false;
            } else {
               return method.getReturnType() == int.class ? 0 : null;
            }
         } else {
            return args != null && args.length > 0 ? args[0] : null;
         }
      }

      private boolean slotMatches(Object value, String slotIdentifier) {
         if (value instanceof String identifier) {
            return slotIdentifier.equals(identifier);
         } else {
            try {
               return slotIdentifier.equals(value.getClass().getMethod("getIdentifier", new Class[0]).invoke(value, new Object[0]));
            } catch (ReflectiveOperationException ignored) {
               return false;
            }
         }
      }
   }
}
