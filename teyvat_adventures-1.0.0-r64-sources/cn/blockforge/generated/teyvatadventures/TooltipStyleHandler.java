package cn.blockforge.generated.teyvatadventures;

import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent.Color;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "teyvat_adventures", value = Dist.CLIENT)
public final class TooltipStyleHandler {
   private TooltipStyleHandler() {
   }

   @SubscribeEvent
   public static void onTooltipColor(Color event) {
      if (event.getItemStack().m_150930_((Item)GeneratedMod.WORLD_BEYOND_DESCENDER.get())
         || event.getItemStack().m_150930_((Item)GeneratedMod.PAIMONS_AUTHORITY.get())) {
         event.setBackgroundStart(-266660824);
         event.setBackgroundEnd(-267581164);
         event.setBorderStart(-1853325);
         event.setBorderEnd(-9680230);
      }
   }
}
