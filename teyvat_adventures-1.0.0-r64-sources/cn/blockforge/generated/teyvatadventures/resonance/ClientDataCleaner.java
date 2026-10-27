package cn.blockforge.generated.teyvatadventures.resonance;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggingOut;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "teyvat_adventures", value = Dist.CLIENT)
public final class ClientDataCleaner {
   private ClientDataCleaner() {
   }

   @SubscribeEvent
   public static void onClientLogout(LoggingOut event) {
      ClientPlayerData.clear();
   }
}
