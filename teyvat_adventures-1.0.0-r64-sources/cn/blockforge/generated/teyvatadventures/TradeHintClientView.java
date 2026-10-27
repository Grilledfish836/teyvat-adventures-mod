package cn.blockforge.generated.teyvatadventures;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent.Closing;
import net.minecraftforge.client.event.ScreenEvent.Render.Post;
import net.minecraftforge.client.event.ScreenEvent.Render.Pre;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "teyvat_adventures", value = Dist.CLIENT)
public final class TradeHintClientView {
   private static final int GUI_WIDTH = 276;
   private static final int GUI_HEIGHT = 167;
   private static final int VISIBLE_ROWS = 7;
   private static final int BUTTON_Y = 18;
   private static final int BUTTON_H = 20;
   private static final int PALE_RED = -32640;
   private static final int PALE_RED_HOVER = -19533;
   private static final int BADGE_RED = -4317666;
   private static final int BADGE_BORDER = 2;
   private static final String BADGE = "[世界之外的降臨] 交易 +20% 生效中";
   private static final int SUPPRESS_DIFF = -1073741824;
   private static final ResourceLocation VILLAGER_TEXTURE = new ResourceLocation("textures/gui/container/villager2.png");
   private static volatile int containerId = -1;
   private static volatile int[] originalA = new int[0];
   private static volatile int[] originalB = new int[0];
   private static volatile int[] shownA = new int[0];
   private static volatile int[] shownB = new int[0];
   private static volatile boolean[] adjustedA = new boolean[0];
   private static volatile Object[] suppressedA = new Object[0];
   private static volatile Object[] suppressedB = new Object[0];
   private static volatile int[] savedBaseA = new int[0];
   private static volatile int[] savedDiffA = new int[0];
   private static Field scrollField;
   private static boolean probed;

   private TradeHintClientView() {
   }

   public static void applySession(int menuId, int[] a, int[] b) {
      int n = Math.max(a == null ? 0 : a.length, b == null ? 0 : b.length);
      containerId = menuId;
      originalA = a == null ? new int[0] : a;
      originalB = b == null ? new int[0] : b;
      shownA = new int[n];
      shownB = new int[n];
      adjustedA = new boolean[n];
      suppressedA = new Object[n];
      suppressedB = new Object[n];
      savedBaseA = new int[n];
      savedDiffA = new int[n];
   }

   private static void clearSession() {
      containerId = -1;
      originalA = new int[0];
      originalB = new int[0];
      shownA = new int[0];
      shownB = new int[0];
      adjustedA = new boolean[0];
      suppressedA = new Object[0];
      suppressedB = new Object[0];
      savedBaseA = new int[0];
      savedDiffA = new int[0];
   }

   @SubscribeEvent
   public static void onScreenClosing(Closing event) {
      if (event.getScreen() instanceof MerchantScreen) {
         clearSession();
      }
   }

   @SubscribeEvent
   public static void onScreenRenderPre(Pre event) {
      MerchantOffers offers = sessionOffers(event.getScreen());
      if (offers != null) {
         suppressCostDigits(offers);
      }
   }

   @SubscribeEvent
   public static void onMouseClickPre(net.minecraftforge.client.event.ScreenEvent.MouseButtonPressed.Pre event) {
      MerchantOffers offers = sessionOffers(event.getScreen());
      if (offers != null) {
         restoreCostDigits(offers);
      }
   }

   private static MerchantOffers sessionOffers(Screen screen) {
      int menu = containerId;
      if (menu >= 0 && screen instanceof MerchantScreen merchantScreen) {
         MerchantMenu merchantMenu = (MerchantMenu)merchantScreen.m_6262_();
         if (merchantMenu.f_38840_ != menu) {
            return null;
         }

         MerchantOffers offers = merchantMenu.m_40075_();
         int size = offers.size();
         return size != 0 && size == originalA.length && size == originalB.length && size == shownA.length && size == suppressedA.length ? offers : null;
      } else {
         return null;
      }
   }

   private static void suppressCostDigits(MerchantOffers offers) {
      int size = offers.size();
      int[] origA = originalA;
      int[] origB = originalB;
      Object[] refA = suppressedA;
      Object[] refB = suppressedB;
      int[] cacheA = shownA;
      int[] cacheB = shownB;
      boolean[] adj = adjustedA;
      int[] savedBase = savedBaseA;
      int[] savedDiff = savedDiffA;

      for (int i = 0; i < size; i++) {
         MerchantOffer offer = (MerchantOffer)offers.get(i);
         if (offer == null) {
            refA[i] = null;
            refB[i] = null;
            cacheA[i] = 0;
            cacheB[i] = 0;
         } else {
            if (origA[i] > 0) {
               ItemStack base = offer.m_45352_();
               if (base == refA[i] && cacheA[i] > 0) {
                  if (base.m_41613_() == 1) {
                     continue;
                  }

                  base.m_41764_(1);
                  offer.m_45359_(-1073741824);
               } else {
                  cacheA[i] = 0;
                  adj[i] = false;
                  refA[i] = base.m_41619_() ? null : base;
                  if (!base.m_41619_()) {
                     int cur = offer.m_45358_().m_41613_();
                     if (cur - origA[i] > 0) {
                        cacheA[i] = cur;
                        adj[i] = cur != base.m_41613_();
                        savedBase[i] = base.m_41613_();
                        savedDiff[i] = offer.m_45377_();
                        base.m_41764_(1);
                        offer.m_45359_(-1073741824);
                     }
                  }
               }
            }

            if (origB[i] > 0) {
               ItemStack costB = offer.m_45364_();
               if (costB == refB[i] && cacheB[i] > 0) {
                  if (costB.m_41613_() != 1) {
                     costB.m_41764_(1);
                  }
               } else {
                  cacheB[i] = 0;
                  refB[i] = costB.m_41619_() ? null : costB;
                  if (!costB.m_41619_()) {
                     int cur = costB.m_41613_();
                     if (cur - origB[i] > 0) {
                        cacheB[i] = cur;
                        costB.m_41764_(1);
                     }
                  }
               }
            }
         }
      }
   }

   private static void restoreCostDigits(MerchantOffers offers) {
      int size = offers.size();
      int[] cacheA = shownA;
      int[] cacheB = shownB;
      Object[] refA = suppressedA;
      Object[] refB = suppressedB;

      for (int i = 0; i < size; i++) {
         MerchantOffer offer = (MerchantOffer)offers.get(i);
         if (offer != null) {
            if (cacheA[i] > 0) {
               ItemStack base = offer.m_45352_();
               if (base == refA[i] && base.m_41613_() == 1) {
                  base.m_41764_(savedBaseA[i]);
                  offer.m_45359_(savedDiffA[i]);
               }
            }

            if (cacheB[i] > 0) {
               ItemStack costB = offer.m_45364_();
               if (costB == refB[i] && costB.m_41613_() == 1) {
                  costB.m_41764_(cacheB[i]);
               }
            }
         }
      }
   }

   @SubscribeEvent
   public static void onScreenRendered(Post event) {
      MerchantOffers offers = sessionOffers(event.getScreen());
      if (offers != null) {
         int size = offers.size();
         int[] cacheA = shownA;
         int[] cacheB = shownB;
         int[] savedBase = savedBaseA;
         int left = (Minecraft.m_91087_().m_91268_().m_85445_() - 276) / 2;
         int top = (Minecraft.m_91087_().m_91268_().m_85446_() - 167) / 2;
         MerchantScreen screen = (MerchantScreen)event.getScreen();
         int scroll = size > 7 ? Math.max(0, readScrollOff(screen)) : 0;
         int hovered = hoveredSlotMask(screen, top);
         Font font = Minecraft.m_91087_().f_91062_;
         GuiGraphics graphics = event.getGuiGraphics();
         graphics.m_280168_().m_85836_();
         graphics.m_280168_().m_252880_(0.0F, 0.0F, 500.0F);
         int row = 0;

         for (int i = 0; i < size; i++) {
            if (size <= 7 || i >= scroll && i < scroll + 7) {
               int y = top + 19 + 20 * row;
               int color = (hovered & 1 << row) != 0 ? -19533 : -32640;
               row++;
               if (cacheA[i] > 0) {
                  paintFormula(font, graphics, cacheA[i], savedBase[i], originalA[i], adjustedA[i], left + 10, y, color);
               }

               if (cacheB[i] > 0) {
                  paintFormula(font, graphics, cacheB[i], cacheB[i], originalB[i], false, left + 40, y, color);
               }
            }
         }

         graphics.m_280168_().m_85849_();
         renderBadge(graphics, font, left, top);
         restoreCostDigits(offers);
      }
   }

   private static void paintFormula(Font font, GuiGraphics graphics, int cur, int base, int original, boolean adjusted, int x, int y, int color) {
      int add = base - original;
      if (add > 0) {
         String origStr = String.valueOf(original);
         String plusStr = "+" + add;
         int halfPlusW = Math.max(1, font.m_92895_(plusStr) / 2);
         graphics.m_280056_(font, origStr, x + 17 - font.m_92895_(origStr), y + 9, color, true);
         graphics.m_280168_().m_85836_();
         graphics.m_280168_().m_85841_(0.5F, 0.5F, 1.0F);
         graphics.m_280056_(font, plusStr, 2 * (x + 18), 2 * (y + 11), color, true);
         graphics.m_280168_().m_85849_();
         if (adjusted && cur != base) {
            int lineEnd = x + 18 + halfPlusW;
            String curStr = String.valueOf(cur);
            int curX = Math.max(x + 31 - font.m_92895_(curStr), lineEnd + 3);
            graphics.m_280168_().m_85836_();
            graphics.m_280168_().m_252880_(0.0F, 0.0F, 200.0F);
            graphics.m_280056_(font, curStr, curX, y + 9, -1, true);
            graphics.m_280168_().m_85849_();
            graphics.m_280168_().m_85836_();
            graphics.m_280168_().m_252880_(0.0F, 0.0F, 300.0F);

            int px;
            for (px = x + 7; px + 9 <= lineEnd; px += 9) {
               graphics.m_280398_(VILLAGER_TEXTURE, px, y + 12, 0, 0.0F, 176.0F, 9, 2, 512, 256);
            }

            while (px < lineEnd) {
               graphics.m_280398_(VILLAGER_TEXTURE, px, y + 12, 0, (px - (x + 7)) % 9.0F, 176.0F, 1, 2, 512, 256);
               px++;
            }

            graphics.m_280168_().m_85849_();
         }
      }
   }

   private static void renderBadge(GuiGraphics graphics, Font font, int guiLeft, int guiTop) {
      int textW = font.m_92895_("[世界之外的降臨] 交易 +20% 生效中");
      int screenH = Minecraft.m_91087_().m_91268_().m_85446_();
      int x = guiLeft + (276 - textW) / 2;
      int y = guiTop + 167 + 2;
      if (y + 11 > screenH) {
         y = guiTop - 13;
      }

      int x0 = x - 4;
      int y0 = y - 2;
      int x1 = x + textW + 4;
      int y1 = y + 11;
      graphics.m_280509_(x0, y0, x1, y0 + 2, -4317666);
      graphics.m_280509_(x0, y1 - 2, x1, y1, -4317666);
      graphics.m_280509_(x0, y0, x0 + 2, y1, -4317666);
      graphics.m_280509_(x1 - 2, y0, x1, y1, -4317666);
      graphics.m_280056_(font, "[世界之外的降臨] 交易 +20% 生效中", x, y, -1, true);
   }

   private static int hoveredSlotMask(MerchantScreen screen, int guiTop) {
      int mask = 0;

      for (GuiEventListener element : screen.m_6702_()) {
         if (element instanceof Button button && button.m_198029_()) {
            int rel = button.m_252907_() - (guiTop + 18);
            if (rel >= 0 && rel < 140) {
               mask |= 1 << rel / 20;
            }
         }
      }

      return mask;
   }

   private static int readScrollOff(MerchantScreen screen) {
      Field field = scrollField();
      if (field == null) {
         return 0;
      }

      try {
         return Math.max(0, field.getInt(screen));
      } catch (ReflectiveOperationException | RuntimeException failure) {
         return 0;
      }
   }

   private static Field scrollField() {
      Field cached = scrollField;
      if (cached == null && !probed) {
         synchronized (TradeHintClientView.class) {
            if (probed) {
               return scrollField;
            }

            String[] var2 = new String[]{"scrollOff", "f_99119_"};
            int var3 = var2.length;
            int var4 = 0;

            while (var4 < var3) {
               String name = var2[var4];

               Field var10000;
               try {
                  Field field = MerchantScreen.class.getDeclaredField(name);
                  field.setAccessible(true);
                  scrollField = field;
                  probed = true;
                  var10000 = field;
               } catch (NoSuchFieldException var8) {
                  var4++;
                  continue;
               }

               return var10000;
            }

            for (Field field : MerchantScreen.class.getDeclaredFields()) {
               int mod = field.getModifiers();
               if (field.getType() == int.class && !Modifier.isStatic(mod) && !Modifier.isPrivate(mod)) {
                  field.setAccessible(true);
                  scrollField = field;
                  probed = true;
                  return field;
               }
            }

            probed = true;
            return null;
         }
      } else {
         return cached;
      }
   }
}
