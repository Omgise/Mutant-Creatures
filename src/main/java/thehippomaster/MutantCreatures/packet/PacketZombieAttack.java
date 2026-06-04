package thehippomaster.MutantCreatures.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.MutantZombie;

public class PacketZombieAttack implements IMessage {
   private byte attackId;
   private int entityId;

   public PacketZombieAttack() {
   }

   public PacketZombieAttack(int id, MutantZombie zombie) {
      this.attackId = (byte)id;
      this.entityId = zombie.getEntityId();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeByte(this.attackId);
      buffer.writeInt(this.entityId);
   }

   public void fromBytes(ByteBuf buffer) {
      this.attackId = buffer.readByte();
      this.entityId = buffer.readInt();
   }

   public static class Handler implements IMessageHandler<PacketZombieAttack, IMessage> {
      public IMessage onMessage(PacketZombieAttack packet, MessageContext ctx) {
         MutantZombie zombie = (MutantZombie)MutantCreatures.proxy.getWorldClient().getEntityByID(packet.entityId);
         if (zombie != null && packet.attackId != -1) {
            zombie.currentAttackID = packet.attackId;
            if (packet.attackId == 0) {
               zombie.animTick = 0;
            }
         }

         return null;
      }
   }
}
