/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.client.FMLClientHandler
 *  cpw.mods.fml.relauncher.ReflectionHelper
 *  cpw.mods.fml.relauncher.Side
 *  cpw.mods.fml.relauncher.SideOnly
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Timer
 *  net.minecraft.world.World
 */
package thehippomaster.AnimationAPI.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.AnimationAPI;
import thehippomaster.AnimationAPI.CommonProxy;

@SideOnly(value=Side.CLIENT)
public class ClientProxy
extends CommonProxy {
    private Timer mcTimer;

    @Override
    public void initTimer() {
        this.mcTimer = (Timer)ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), AnimationAPI.fTimer);
    }

    @Override
    public float getPartialTick() {
        return this.mcTimer.renderPartialTicks;
    }

    @Override
    public World getWorldClient() {
        return FMLClientHandler.instance().getWorldClient();
    }
}

