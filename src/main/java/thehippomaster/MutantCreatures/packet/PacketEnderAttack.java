package thehippomaster.MutantCreatures.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.MutantEnderman;

public class PacketEnderAttack implements IMessage {
   private byte attackId;
   private int entityId;

   public PacketEnderAttack() {
   }

   public PacketEnderAttack(int id, MutantEnderman enderman) {
      this.attackId = (byte)id;
      this.entityId = enderman.getEntityId();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeByte(this.attackId);
      buffer.writeInt(this.entityId);
   }

   public void fromBytes(ByteBuf buffer) {
      this.attackId = buffer.readByte();
      this.entityId = buffer.readInt();
   }

   public static class Handler implements IMessageHandler<PacketEnderAttack, IMessage> {
      public IMessage onMessage(PacketEnderAttack packet, MessageContext ctx) {
         World world = MutantCreatures.proxy.getWorldClient();
         MutantEnderman enderman = (MutantEnderman)world.getEntityByID(packet.entityId);
         if (enderman != null && packet.attackId != -1) {
            enderman.currentAttackID = packet.attackId;
            if (packet.attackId == 0) {
               enderman.animTick = 0;
            }
         }

         return null;
      }
   }
}
