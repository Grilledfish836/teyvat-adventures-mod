package cn.blockforge.generated.teyvatadventures.resonance;

import cn.blockforge.generated.teyvatadventures.element.Element;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public final class ResonanceClientView {
   private ResonanceClientView() {
   }

   public static boolean hasQualification(Element element) {
      Player localPlayer = Minecraft.m_91087_().f_91074_;
      return localPlayer != null && PlayerElementData.hasQualification(localPlayer, element);
   }

   public static PlayerElementData ownData() {
      Player localPlayer = Minecraft.m_91087_().f_91074_;
      return localPlayer == null ? null : ClientPlayerData.get(localPlayer.m_20148_());
   }
}
