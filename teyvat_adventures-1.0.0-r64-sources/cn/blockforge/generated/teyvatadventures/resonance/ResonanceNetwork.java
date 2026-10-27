package cn.blockforge.generated.teyvatadventures.resonance;

import cn.blockforge.generated.teyvatadventures.TradeHintClientView;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.simple.SimpleChannel;

public final class ResonanceNetwork {
   private static final String PROTOCOL = "1";
   private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
      new ResourceLocation("teyvat_adventures", "main"), () -> "1", "1"::equals, "1"::equals
   );

   private ResonanceNetwork() {
   }

   public static void register() {
      CHANNEL.messageBuilder(ResonanceNetwork.QualificationSync.class, 0, NetworkDirection.PLAY_TO_CLIENT)
         .encoder(ResonanceNetwork.QualificationSync::encode)
         .decoder(ResonanceNetwork.QualificationSync::new)
         .consumerMainThread(ResonanceNetwork.QualificationSync::handle)
         .add();
      CHANNEL.messageBuilder(ResonanceNetwork.TradeHintSync.class, 1, NetworkDirection.PLAY_TO_CLIENT)
         .encoder(ResonanceNetwork.TradeHintSync::encode)
         .decoder(ResonanceNetwork.TradeHintSync::new)
         .consumerMainThread(ResonanceNetwork.TradeHintSync::handle)
         .add();
      CHANNEL.messageBuilder(ElementSyncPacket.class, 2, NetworkDirection.PLAY_TO_CLIENT)
         .encoder(ElementSyncPacket::encode)
         .decoder(ElementSyncPacket::new)
         .consumerMainThread(ElementSyncPacket::handle)
         .add();
   }

   public static void sendTradeHint(ServerPlayer player, int menuId, int[] originalA, int[] originalB) {
      CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new ResonanceNetwork.TradeHintSync(menuId, originalA, originalB));
   }

   public static void syncTo(Player player) {
      if (player instanceof ServerPlayer serverPlayer) {
         ElementSyncScheduler.send(serverPlayer);
      }
   }

   static void sendElementSync(ServerPlayer serverPlayer) {
      CompoundTag data = serverPlayer.getPersistentData().m_128469_("teyvat_adventures_resonance").m_6426_();
      CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ElementSyncPacket(serverPlayer.m_20148_(), data));
   }

   @SubscribeEvent
   public static void onLoggedIn(PlayerLoggedInEvent event) {
      syncTo(event.getEntity());
   }

   @SubscribeEvent
   public static void onRespawn(PlayerRespawnEvent event) {
      syncTo(event.getEntity());
   }

   @SubscribeEvent
   public static void onChangedDimension(PlayerChangedDimensionEvent event) {
      syncTo(event.getEntity());
   }

   static final class ClientQualificationStore {
      private ClientQualificationStore() {
      }

      static void apply(CompoundTag data) {
         Player clientPlayer = Minecraft.m_91087_().f_91074_;
         if (clientPlayer != null) {
            clientPlayer.getPersistentData().m_128365_("teyvat_adventures_resonance", data);
         }
      }
   }

   static final class QualificationSync {
      private final CompoundTag data;

      QualificationSync(CompoundTag data) {
         this.data = data;
      }

      QualificationSync(FriendlyByteBuf buf) {
         CompoundTag read = buf.m_130260_();
         this.data = read == null ? new CompoundTag() : read;
      }

      void encode(FriendlyByteBuf buf) {
         buf.m_130079_(this.data);
      }

      static void handle(ResonanceNetwork.QualificationSync message, Supplier<Context> contextSupplier) {
         Context context = (Context)contextSupplier.get();
         context.enqueueWork(() -> ResonanceNetwork.ClientQualificationStore.apply(message.data));
         context.setPacketHandled(true);
      }
   }

   static final class TradeHintSync {
      private final int menuId;
      private final int[] originalA;
      private final int[] originalB;

      TradeHintSync(int menuId, int[] originalA, int[] originalB) {
         this.menuId = menuId;
         this.originalA = originalA;
         this.originalB = originalB;
      }

      TradeHintSync(FriendlyByteBuf buf) {
         this.menuId = buf.m_130242_();
         this.originalA = readArray(buf);
         this.originalB = readArray(buf);
      }

      void encode(FriendlyByteBuf buf) {
         buf.m_130130_(this.menuId);
         writeArray(buf, this.originalA);
         writeArray(buf, this.originalB);
      }

      private static int[] readArray(FriendlyByteBuf buf) {
         int size = buf.m_130242_();
         int[] values = new int[size];

         for (int i = 0; i < size; i++) {
            values[i] = buf.m_130242_();
         }

         return values;
      }

      private static void writeArray(FriendlyByteBuf buf, int[] values) {
         buf.m_130130_(values.length);

         for (int value : values) {
            buf.m_130130_(value);
         }
      }

      static void handle(ResonanceNetwork.TradeHintSync message, Supplier<Context> contextSupplier) {
         Context context = (Context)contextSupplier.get();
         context.enqueueWork(() -> TradeHintClientView.applySession(message.menuId, message.originalA, message.originalB));
         context.setPacketHandled(true);
      }
   }
}
