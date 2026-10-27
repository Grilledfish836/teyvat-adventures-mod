package cn.blockforge.generated.teyvatadventures;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public final class PaimonsAuthorityItem extends Item {
   public PaimonsAuthorityItem(Properties properties) {
      super(properties);
   }

   public InteractionResultHolder<ItemStack> m_7203_(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.m_21120_(hand);
      if (!level.m_5776_()) {
         boolean toggled = CuriosCompat.toggleCore(player);
         if (!toggled) {
            player.m_5661_(Component.m_237115_("item.teyvat_adventures.paimons_authority.no_core"), true);
         } else {
            player.m_5661_(
               Component.m_237115_(
                  CuriosCompat.hasCoreInSeal(player)
                     ? "item.teyvat_adventures.paimons_authority.equipped"
                     : "item.teyvat_adventures.paimons_authority.unequipped"
               ),
               true
            );
         }
      }

      return InteractionResultHolder.m_19092_(stack, level.m_5776_());
   }

   public boolean m_5812_(ItemStack stack) {
      return true;
   }

   public void m_7373_(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.m_237115_("item.teyvat_adventures.paimons_authority.desc").m_130940_(ChatFormatting.GRAY));
      tooltip.add(Component.m_237119_());
      tooltip.add(
         Component.m_237115_("item.teyvat_adventures.paimons_authority.obtain")
            .m_130944_(new ChatFormatting[]{ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC})
      );
   }
}
