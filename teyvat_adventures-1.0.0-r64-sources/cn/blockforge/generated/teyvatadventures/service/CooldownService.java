package cn.blockforge.generated.teyvatadventures.service;

import cn.blockforge.generated.teyvatadventures.CuriosCompat;
import com.mojang.logging.LogUtils;
import java.lang.reflect.Field;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ServerItemCooldowns;

public final class CooldownService {
   private static final double MULTIPLIER = 1.25;
   private static final int MAX_SCALE_INPUT_TICKS = 72000;
   private static final Object LOCK = new Object();
   private static volatile Field cooldownsField;
   private static volatile boolean disabled;
   private static volatile boolean loggedSkip;

   private CooldownService() {
   }

   public static void install(Player player) {
      if (!disabled && player instanceof ServerPlayer serverPlayer) {
         ItemCooldowns current = serverPlayer.m_36335_();
         if (!(current instanceof CooldownService.ExtendingCooldowns)) {
            Field field = cooldownsField();
            if (field != null) {
               if (current.getClass() != ServerItemCooldowns.class) {
                  skipOnce("player cooldowns were replaced by another mod (" + current.getClass().getName() + "), cooldown extension disabled.");
               } else {
                  try {
                     field.set(serverPlayer, new CooldownService.ExtendingCooldowns(serverPlayer));
                  } catch (IllegalAccessException | RuntimeException failure) {
                     disable(failure);
                  }
               }
            }
         }
      }
   }

   private static Field cooldownsField() {
      Field cached = cooldownsField;
      if (cached == null && !disabled) {
         synchronized (LOCK) {
            if (cooldownsField == null && !disabled) {
               Field found = null;

               for (Field field : Player.class.getDeclaredFields()) {
                  if (ItemCooldowns.class.isAssignableFrom(field.getType())) {
                     if (found != null) {
                        found = null;
                        break;
                     }

                     found = field;
                  }
               }

               if (found == null) {
                  disabled = true;
                  LogUtils.getLogger()
                     .warn(
                        "[TeyvatAdventures] CooldownService: vanilla cooldown field not found on Player, cooldown extension disabled (vanilla behaviour kept)."
                     );
                  return null;
               }

               try {
                  found.setAccessible(true);
               } catch (RuntimeException failure) {
                  disabled = true;
                  LogUtils.getLogger()
                     .warn("[TeyvatAdventures] CooldownService: cooldown field is not writable, cooldown extension disabled (vanilla behaviour kept).", failure);
                  return null;
               }

               cooldownsField = found;
               return found;
            } else {
               return cooldownsField;
            }
         }
      } else {
         return cached;
      }
   }

   private static void skipOnce(String reason) {
      if (!loggedSkip) {
         synchronized (LOCK) {
            if (loggedSkip) {
               return;
            }

            loggedSkip = true;
         }

         LogUtils.getLogger().warn("[TeyvatAdventures] CooldownService skipped: " + reason);
      }
   }

   private static void disable(Exception failure) {
      synchronized (LOCK) {
         if (disabled) {
            return;
         }

         disabled = true;
         cooldownsField = null;
      }

      LogUtils.getLogger().warn("[TeyvatAdventures] CooldownService disabled after access failure; vanilla cooldown behaviour kept.", failure);
   }

   private static final class ExtendingCooldowns extends ServerItemCooldowns {
      private final ServerPlayer wearer;

      private ExtendingCooldowns(ServerPlayer wearer) {
         super(wearer);
         this.wearer = wearer;
      }

      public void m_41524_(Item item, int ticks) {
         super.m_41524_(item, this.extend(ticks));
      }

      private int extend(int ticks) {
         if (ticks <= 0 || ticks > 72000) {
            return ticks;
         }

         if (!CuriosCompat.hasCoreInSeal(this.wearer)) {
            return ticks;
         }

         long scaled = Math.round(ticks * 1.25);
         return scaled > ticks && scaled <= 2147483647L ? (int)scaled : ticks;
      }
   }
}
