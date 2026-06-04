package thehippomaster.MutantCreatures.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import thehippomaster.MutantCreatures.MutantCreatures;

public class PacketEnderTPlayer implements IMessage {
   public void toBytes(ByteBuf buffer) {
   }

   public void fromBytes(ByteBuf buffer) {
   }

   public static class Handler implements IMessageHandler<PacketEnderTPlayer, IMessage> {
      public IMessage onMessage(PacketEnderTPlayer packet, MessageContext ctx) {
         EntityPlayer player = MutantCreatures.proxy.getClientPlayer();
         double x = player.posX + (double)((player.getRNG().nextFloat() - 0.5F) * 14.0F);
         double y = player.posY + (double)player.getRNG().nextFloat() + (double)13.0F;
         double z = player.posZ + (double)((player.getRNG().nextFloat() - 0.5F) * 14.0F);
         player.setPosition(x, y, z);
         MutantCreatures.spawnEnderParticles(player);
         player.worldObj.playSoundAtEntity(player, "random.explode", 1.2F, 0.9F + player.getRNG().nextFloat() * 0.2F);
         return null;
      }
   }
}
