package cn.blockforge.generated.teyvatadventures;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public final class WorldBeyondDescenderItem extends Item {
   public WorldBeyondDescenderItem(Properties properties) {
      super(properties);
   }

   public boolean m_5812_(ItemStack stack) {
      return true;
   }

   public void m_7373_(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(
         Component.m_237115_("item.teyvat_adventures.world_beyond_descender.curse")
            .m_130944_(new ChatFormatting[]{ChatFormatting.DARK_RED, ChatFormatting.BOLD})
      );
      tooltip.add(curse("item.teyvat_adventures.world_beyond_descender.wind", ChatFormatting.AQUA));
      tooltip.add(curse("item.teyvat_adventures.world_beyond_descender.rock", ChatFormatting.GOLD));
      tooltip.add(curse("item.teyvat_adventures.world_beyond_descender.electro", ChatFormatting.LIGHT_PURPLE));
      tooltip.add(curse("item.teyvat_adventures.world_beyond_descender.dendro", ChatFormatting.GREEN));
      tooltip.add(curse("item.teyvat_adventures.world_beyond_descender.hydro", ChatFormatting.BLUE));
      tooltip.add(curse("item.teyvat_adventures.world_beyond_descender.pyro", ChatFormatting.RED));
      tooltip.add(curse("item.teyvat_adventures.world_beyond_descender.cryo", ChatFormatting.AQUA));
   }

   private static MutableComponent curse(String key, ChatFormatting color) {
      return Component.m_237113_("  • ").m_130940_(ChatFormatting.DARK_GRAY).m_7220_(Component.m_237115_(key).m_130940_(color));
   }
}
