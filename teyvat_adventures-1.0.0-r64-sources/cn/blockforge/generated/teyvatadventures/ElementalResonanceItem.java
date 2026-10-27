package cn.blockforge.generated.teyvatadventures;

import cn.blockforge.generated.teyvatadventures.element.Element;
import cn.blockforge.generated.teyvatadventures.resonance.ElementProgress;
import cn.blockforge.generated.teyvatadventures.resonance.ElementTargets;
import cn.blockforge.generated.teyvatadventures.resonance.PlayerElementData;
import cn.blockforge.generated.teyvatadventures.resonance.ResonanceClientView;
import cn.blockforge.generated.teyvatadventures.resonance.Stage;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;

public final class ElementalResonanceItem extends Item {
   private final Element element;

   public ElementalResonanceItem(Properties properties, Element element) {
      super(properties.m_41487_(1));
      this.element = element;
   }

   public Element getElement() {
      return this.element;
   }

   public boolean m_41475_() {
      return true;
   }

   public boolean m_41386_(DamageSource source) {
      return !source.m_269533_(DamageTypeTags.f_268415_) && !source.m_269533_(DamageTypeTags.f_268745_) && !source.m_276093_(DamageTypes.f_268450_);
   }

   public boolean m_5812_(ItemStack stack) {
      return true;
   }

   public Component m_7626_(ItemStack stack) {
      return Component.m_237115_(this.nameKey(stageOfStack(stack)));
   }

   private String nameKey(Stage stage) {
      return "item.teyvat_adventures.elemental_resonance." + this.element.id() + "." + stage.id() + ".name";
   }

   private static Stage stageOfStack(ItemStack stack) {
      try {
         CompoundTag tag = stack.m_41782_() ? stack.m_41783_() : null;
         return Stage.parse(tag == null ? null : tag.m_128461_("teyvat_stage"));
      } catch (RuntimeException e) {
         return Stage.EXCLUSION;
      }
   }

   private static ChatFormatting stageColor(Stage stage) {
      switch (stage) {
         case NEUTRAL:
            return ChatFormatting.GOLD;
         case RESONANCE:
            return ChatFormatting.AQUA;
         case EXCLUSION:
         default:
            return ChatFormatting.DARK_RED;
      }
   }

   public void m_7373_(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
      tooltip.add(Component.m_237115_("item.teyvat_adventures.elemental_resonance.desc").m_130940_(ChatFormatting.GRAY));
      tooltip.add(
         Component.m_237115_("item.teyvat_adventures.elemental_resonance.element")
            .m_130940_(ChatFormatting.DARK_GRAY)
            .m_7220_(Component.m_237115_("item.teyvat_adventures.elemental_resonance.element." + this.element.id()).m_130940_(ChatFormatting.AQUA))
      );
      boolean qualified = (level == null || level.m_5776_()) && ResonanceClientView.hasQualification(this.element);
      tooltip.add(
         Component.m_237115_(qualified ? "item.teyvat_adventures.elemental_resonance.qualify.done" : "item.teyvat_adventures.elemental_resonance.qualify.none")
            .m_130944_(new ChatFormatting[]{qualified ? ChatFormatting.DARK_GREEN : ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC})
      );

      try {
         this.appendProgressTooltip(stack, level, tooltip);
      } catch (RuntimeException | LinkageError e) {
         tooltip.clear();
         tooltip.add(
            Component.m_237115_("item.teyvat_adventures.elemental_resonance.tooltip.loading")
               .m_130944_(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC})
         );
      }
   }

   private void appendProgressTooltip(ItemStack stack, Level level, List<Component> tooltip) {
      Stage stage = stageOfStack(stack);
      String base = "item.teyvat_adventures.elemental_resonance." + this.element.id() + ".";
      tooltip.add(Component.m_237115_(this.nameKey(stage)).m_130944_(new ChatFormatting[]{stageColor(stage), ChatFormatting.BOLD}));
      tooltip.add(Component.m_237115_(base + (stage == Stage.RESONANCE ? "resonance.desc" : stage.id() + ".goal")).m_130940_(ChatFormatting.YELLOW));
      if (stage != Stage.RESONANCE) {
         PlayerElementData data = ResonanceClientView.ownData();
         if (data == null) {
            tooltip.add(
               Component.m_237115_("item.teyvat_adventures.elemental_resonance.tooltip.syncing")
                  .m_130944_(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC})
            );
         } else {
            ElementProgress progress = data.progressSnapshot(this.element);

            for (String key : ElementTargets.trackedKeys(this.element, stage)) {
               List<String> possible = ElementTargets.possibleValues(level, key);
               if (!possible.isEmpty()) {
                  Set<String> done = progress.trackedView(key);
                  tooltip.add(
                     Component.m_237110_("item.teyvat_adventures.elemental_resonance.tooltip.progress", new Object[]{done.size(), possible.size()})
                        .m_130940_(ChatFormatting.GRAY)
                  );

                  for (String id : possible) {
                     boolean tracked = done.contains(id);
                     tooltip.add(
                        Component.m_237113_((tracked ? "✓ " : "✗ ") + stripNamespace(id)).m_130940_(tracked ? ChatFormatting.GREEN : ChatFormatting.GRAY)
                     );
                  }
               }
            }
         }
      }
   }

   private static String stripNamespace(String id) {
      int colon = id.indexOf(58);
      return colon < 0 ? id : id.substring(colon + 1);
   }
}
