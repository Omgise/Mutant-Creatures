package thehippomaster.MutantCreatures.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import thehippomaster.MutantCreatures.CreeperMinion;

public class PacketCMOptions implements IMessage {
   private byte optionsId;
   private int entityId;
   private short nameLength;
   private String minionName;
   private boolean minionBoolean;

   public PacketCMOptions() {
   }

   public PacketCMOptions(int id, CreeperMinion minion, String name) {
      this.optionsId = (byte)id;
      this.entityId = minion.getEntityId();
      this.nameLength = (short)name.length();
      this.minionName = name;
   }

   public PacketCMOptions(int id, CreeperMinion minion, boolean flag) {
      this.optionsId = (byte)id;
      this.entityId = minion.getEntityId();
      this.minionBoolean = flag;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeByte(this.optionsId);
      buffer.writeInt(this.entityId);
      if (this.optionsId == 0) {
         buffer.writeShort(this.nameLength);

         for(int i = 0; i < this.nameLength; ++i) {
            buffer.writeChar(this.minionName.charAt(i));
         }
      } else {
         buffer.writeBoolean(this.minionBoolean);
      }

   }

   public void fromBytes(ByteBuf buffer) {
      this.optionsId = buffer.readByte();
      this.entityId = buffer.readInt();
      if (this.optionsId == 0) {
         this.nameLength = buffer.readShort();
         StringBuilder sb = new StringBuilder();

         for(int i = 0; i < this.nameLength; ++i) {
            sb.append(buffer.readChar());
         }

         this.minionName = sb.toString();
      } else {
         this.minionBoolean = buffer.readBoolean();
      }

   }

   public static class Handler implements IMessageHandler<PacketCMOptions, IMessage> {
      public IMessage onMessage(PacketCMOptions packet, MessageContext ctx) {
         Entity entity = null;
         WorldServer[] worlds = MinecraftServer.getServer().worldServers;

         for(int i = 0; i < worlds.length; ++i) {
            entity = worlds[i].getEntityByID(packet.entityId);
            if (entity != null) {
               break;
            }
         }

         CreeperMinion minion = (CreeperMinion)entity;
         if (packet.optionsId == 0) {
            minion.setName(packet.minionName);
         } else if (packet.optionsId == 1) {
            minion.setDestroyBlocks(packet.minionBoolean);
         } else if (packet.optionsId == 2) {
            minion.setShowName(packet.minionBoolean);
         }

         return null;
      }
   }
}
