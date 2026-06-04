/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.network.simpleimpl.IMessage
 *  cpw.mods.fml.common.network.simpleimpl.IMessageHandler
 *  cpw.mods.fml.common.network.simpleimpl.MessageContext
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.world.World
 */
package thehippomaster.AnimationAPI.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.AnimationAPI;
import thehippomaster.AnimationAPI.IAnimatedEntity;

public class PacketAnim
implements IMessage {
    private byte animID;
    private int entityID;

    public PacketAnim() {
    }

    public PacketAnim(byte anim, int entity) {
        this.animID = anim;
        this.entityID = entity;
    }

    public void toBytes(ByteBuf buffer) {
        buffer.writeByte((int)this.animID);
        buffer.writeInt(this.entityID);
    }

    public void fromBytes(ByteBuf buffer) {
        this.animID = buffer.readByte();
        this.entityID = buffer.readInt();
    }

    public static class Handler
    implements IMessageHandler<PacketAnim, IMessage> {
        public IMessage onMessage(PacketAnim packet, MessageContext ctx) {
            World world = AnimationAPI.proxy.getWorldClient();
            IAnimatedEntity entity = (IAnimatedEntity)world.getEntityByID(packet.entityID);
            if (entity != null && packet.animID != -1) {
                entity.setAnimID(packet.animID);
                if (packet.animID == 0) {
                    entity.setAnimTick(0);
                }
            }
            return null;
        }
    }
}

