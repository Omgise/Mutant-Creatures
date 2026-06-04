/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.ai.EntityAIBase
 */
package thehippomaster.AnimationAPI;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import thehippomaster.AnimationAPI.AnimationAPI;
import thehippomaster.AnimationAPI.IAnimatedEntity;

public abstract class AIAnimation
extends EntityAIBase {
    private IAnimatedEntity animatedEntity;

    public AIAnimation(IAnimatedEntity entity) {
        this.animatedEntity = entity;
        this.setMutexBits(7);
    }

    public abstract int getAnimID();

    public <T extends EntityLiving> T getEntity() {
        return (T)((EntityLiving)this.animatedEntity);
    }

    public abstract boolean isAutomatic();

    public abstract int getDuration();

    public boolean shouldAnimate() {
        return false;
    }

    public boolean shouldExecute() {
        if (this.isAutomatic()) {
            return this.animatedEntity.getAnimID() == this.getAnimID();
        }
        return this.shouldAnimate();
    }

    public void startExecuting() {
        if (!this.isAutomatic()) {
            AnimationAPI.sendAnimPacket(this.animatedEntity, this.getAnimID());
        }
        this.animatedEntity.setAnimTick(0);
    }

    public boolean continueExecuting() {
        return this.animatedEntity.getAnimTick() < this.getDuration();
    }

    public void resetTask() {
        AnimationAPI.sendAnimPacket(this.animatedEntity, 0);
    }
}

