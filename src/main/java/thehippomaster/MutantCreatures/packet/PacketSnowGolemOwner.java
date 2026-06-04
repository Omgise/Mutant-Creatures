package thehippomaster.MutantCreatures.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.MutantSnowGolem;

public class PacketSnowGolemOwner implements IMessage {
   private int entityId;
   private int ownerId;

   public PacketSnowGolemOwner() {
   }

   public PacketSnowGolemOwner(MutantSnowGolem golem, EntityPlayer player) {
      this.entityId = golem.getEntityId();
      this.ownerId = player == null ? -1 : player.getEntityId();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.entityId);
      buffer.writeInt(this.ownerId);
   }

   public void fromBytes(ByteBuf buffer) {
      this.entityId = buffer.readInt();
      this.ownerId = buffer.readInt();
   }

   public static class Handler implements IMessageHandler<PacketSnowGolemOwner, IMessage> {
      public IMessage onMessage(PacketSnowGolemOwner packet, MessageContext ctx) {
         World world = MutantCreatures.proxy.getWorldClient();
         MutantSnowGolem golem = (MutantSnowGolem)world.getEntityByID(packet.entityId);
         EntityPlayer owner = (EntityPlayer)world.getEntityByID(packet.ownerId);
         if (golem != null) {
            golem.owner = owner;
         }

         return null;
      }
   }
}
