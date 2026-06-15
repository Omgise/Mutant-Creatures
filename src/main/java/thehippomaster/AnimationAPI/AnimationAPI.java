package thehippomaster.AnimationAPI;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.Entity;
import thehippomaster.AnimationAPI.packet.PacketAnim;

public class AnimationAPI {
    public static AnimationAPI instance = new AnimationAPI();
    @SidedProxy(clientSide="thehippomaster.AnimationAPI.client.ClientProxy", serverSide="thehippomaster.AnimationAPI.CommonProxy")
    public static CommonProxy proxy;
    public static SimpleNetworkWrapper wrapper;
    public static final String[] fTimer;
    private static boolean initialized = false;

    public static synchronized void ensureInitialized() {
        if (initialized) {
            return;
        }
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("AnimAPI");
        wrapper.registerMessage(PacketAnim.Handler.class, PacketAnim.class, 0, Side.CLIENT);
        proxy.initTimer();
        initialized = true;
    }

    public static boolean isClient() {
        return FMLCommonHandler.instance().getSide().isClient();
    }

    public static boolean isEffectiveClient() {
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }

    public static void sendAnimPacket(IAnimatedEntity entity, int animID) {
        ensureInitialized();
        if (AnimationAPI.isEffectiveClient()) {
            return;
        }
        entity.setAnimID(animID);
        wrapper.sendToAll((IMessage)new PacketAnim((byte)animID, ((Entity)entity).getEntityId()));
    }

    static {
        fTimer = new String[]{"field_71428_T", "timer", "S"};
    }
}
