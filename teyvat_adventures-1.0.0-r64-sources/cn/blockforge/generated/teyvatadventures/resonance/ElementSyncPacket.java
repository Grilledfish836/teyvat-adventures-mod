package cn.blockforge.generated.teyvatadventures.resonance;

import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public final class ElementSyncPacket {
   private final UUID playerUUID;
   private final CompoundTag data;

   public ElementSyncPacket(UUID playerUUID, CompoundTag data) {
      this.playerUUID = playerUUID;
      this.data = data == null ? new CompoundTag() : data;
   }

   public ElementSyncPacket(FriendlyByteBuf buf) {
      this.playerUUID = buf.m_130259_();
      CompoundTag read = buf.m_130260_();
      this.data = read == null ? new CompoundTag() : read;
   }

   void encode(FriendlyByteBuf buf) {
      buf.m_130077_(this.playerUUID);
      buf.m_130079_(this.data);
   }

   static void handle(ElementSyncPacket message, Supplier<Context> contextSupplier) {
      Context context = (Context)contextSupplier.get();
      context.enqueueWork(() -> ClientPlayerData.put(message.playerUUID, message.data));
      context.setPacketHandled(true);
   }
}
