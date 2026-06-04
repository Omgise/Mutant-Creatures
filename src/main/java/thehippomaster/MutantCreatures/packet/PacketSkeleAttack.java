package thehippomaster.MutantCreatures.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.MutantSkeleton;

public class PacketSkeleAttack implements IMessage {
   private byte attackId;
   private int entityId;

   public PacketSkeleAttack() {
   }

   public PacketSkeleAttack(int id, MutantSkeleton skele) {
      this.attackId = (byte)id;
      this.entityId = skele.getEntityId();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeByte(this.attackId);
      buffer.writeInt(this.entityId);
   }

   public void fromBytes(ByteBuf buffer) {
      this.attackId = buffer.readByte();
      this.entityId = buffer.readInt();
   }

   public static class Handler implements IMessageHandler<PacketSkeleAttack, IMessage> {
      public IMessage onMessage(PacketSkeleAttack packet, MessageContext ctx) {
         World world = MutantCreatures.proxy.getWorldClient();
         MutantSkeleton skele = (MutantSkeleton)world.getEntityByID(packet.entityId);
         if (skele != null && packet.attackId != -1) {
            skele.currentAttackID = packet.attackId;
            if (packet.attackId == 0) {
               skele.animTick = 0;
            }
         }

         return null;
      }
   }
}
