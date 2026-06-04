package thehippomaster.MutantCreatures.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import thehippomaster.MutantCreatures.MCHandler;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.SGolemBlock;

public class PacketSpawnParticles implements IMessage {
   private byte particleId;
   private int entityId;
   private int amount;

   public PacketSpawnParticles() {
   }

   public PacketSpawnParticles(int id, int entity, int a) {
      this.particleId = (byte)id;
      this.entityId = entity;
      this.amount = a;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeByte(this.particleId);
      buffer.writeInt(this.entityId);
      buffer.writeInt(this.amount);
   }

   public void fromBytes(ByteBuf buffer) {
      this.particleId = buffer.readByte();
      this.entityId = buffer.readInt();
      this.amount = buffer.readInt();
   }

   public static class Handler implements IMessageHandler<PacketSpawnParticles, IMessage> {
      public IMessage onMessage(PacketSpawnParticles packet, MessageContext ctx) {
         Entity entity = MutantCreatures.proxy.getWorldClient().getEntityByID(packet.entityId);
         if (packet.particleId != -1 && entity != null && packet.amount != -1) {
            if (packet.particleId == 0) {
               MutantCreatures.spawnParticlesAtEntity(entity, "heart", packet.amount);
            } else if (packet.particleId == 1) {
               MutantCreatures.spawnParticlesAtEntity(entity, "flame", packet.amount);
            } else if (packet.particleId == 2) {
               ((SGolemBlock)entity).spawnIceParticles();
            } else if (packet.particleId == 3) {
               MutantCreatures.spawnParticlesAtEntity(entity, "blockcrack_80_0", packet.amount);
            } else if (packet.particleId == 4) {
               MCHandler var10000 = MutantCreatures.mcHandler;
               MCHandler.handleEnderParticles(entity);
            } else if (packet.particleId == 5) {
               MutantCreatures.spawnEnderParticles(entity, 64, 0.8F);
            }
         }

         return null;
      }
   }
}
