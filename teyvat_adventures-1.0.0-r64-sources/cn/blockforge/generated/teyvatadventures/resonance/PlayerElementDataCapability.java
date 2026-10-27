package cn.blockforge.generated.teyvatadventures.resonance;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public final class PlayerElementDataCapability {
   public static final Capability<PlayerElementData> PLAYER_ELEMENT_DATA = CapabilityManager.get(new CapabilityToken<PlayerElementData>() {});

   private PlayerElementDataCapability() {
   }

   public static void attach(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof Player) {
         event.addCapability(new ResourceLocation("teyvat_adventures", "player_element_data"), new PlayerElementDataCapability.Provider());
      }
   }

   static final class Provider implements ICapabilitySerializable<CompoundTag> {
      private final PlayerElementData data = new PlayerElementData();
      private final LazyOptional<PlayerElementData> holder = LazyOptional.of(() -> this.data);

      public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
         return PlayerElementDataCapability.PLAYER_ELEMENT_DATA.orEmpty(cap, this.holder);
      }

      public CompoundTag serializeNBT() {
         return this.data.serializeNBT();
      }

      public void deserializeNBT(CompoundTag nbt) {
         this.data.deserializeNBT(nbt);
      }
   }
}
