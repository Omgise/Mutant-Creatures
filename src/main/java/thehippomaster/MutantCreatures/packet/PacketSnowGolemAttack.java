package thehippomaster.MutantCreatures.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.MutantSnowGolem;

public class PacketSnowGolemAttack implements IMessage {
   private byte attackId;
   private int entityId;
   private boolean attack;

   public PacketSnowGolemAttack() {
   }

   public PacketSnowGolemAttack(int id, MutantSnowGolem golem, boolean a) {
      this.attackId = (byte)id;
      this.entityId = golem.getEntityId();
      this.attack = a;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeByte(this.attackId);
      buffer.writeInt(this.entityId);
      buffer.writeBoolean(this.attack);
   }

   public void fromBytes(ByteBuf buffer) {
      this.attackId = buffer.readByte();
      this.entityId = buffer.readInt();
      this.attack = buffer.readBoolean();
   }

   public static class Handler implements IMessageHandler<PacketSnowGolemAttack, IMessage> {
      public IMessage onMessage(PacketSnowGolemAttack packet, MessageContext ctx) {
         World world = MutantCreatures.proxy.getWorldClient();
         MutantSnowGolem golem = (MutantSnowGolem)world.getEntityByID(packet.entityId);
         if (golem != null && packet.attackId == 0) {
            golem.throwAttack = packet.attack;
            if (!packet.attack) {
               golem.throwTick = 0;
            }
         }

         return null;
      }
   }
}
