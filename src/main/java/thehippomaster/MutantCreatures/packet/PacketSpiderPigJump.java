package thehippomaster.MutantCreatures.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;
import thehippomaster.MutantCreatures.SpiderPig;

public class PacketSpiderPigJump implements IMessage {
   private int entityId;

   public PacketSpiderPigJump() {
   }

   public PacketSpiderPigJump(SpiderPig pig) {
      this.entityId = pig.getEntityId();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.entityId);
   }

   public void fromBytes(ByteBuf buffer) {
      this.entityId = buffer.readInt();
   }

   public static class Handler implements IMessageHandler<PacketSpiderPigJump, IMessage> {
      public IMessage onMessage(PacketSpiderPigJump packet, MessageContext ctx) {
         Entity entity = null;
         WorldServer[] worlds = MinecraftServer.getServer().worldServers;

         for(int i = 0; i < worlds.length; ++i) {
            entity = worlds[i].getEntityByID(packet.entityId);
            if (entity != null) {
               break;
            }
         }

         SpiderPig pig = (SpiderPig)entity;
         if ((pig.onGround || pig.isInWater()) && !pig.chargeExhausted) {
            float pitch = pig.rotationPitch;
            pig.rotationPitch = 0.0F;
            Vec3 vec = pig.getLookVec();
            pig.rotationPitch = pitch;
            pig.motionX = vec.xCoord * (double)1.6F;
            pig.motionY = (double)0.3F;
            pig.motionZ = vec.zCoord * (double)1.6F;
            if (pig.riddenByEntity != null) {
               ((EntityLivingBase)pig.riddenByEntity).addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 8, 0, true));
            }

            pig.exhaustAmount += 50;
            pig.chargingTick = 8;
         }

         return null;
      }
   }
}
