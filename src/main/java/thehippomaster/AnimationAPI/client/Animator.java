/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.Side
 *  cpw.mods.fml.relauncher.SideOnly
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.util.MathHelper
 */
package thehippomaster.AnimationAPI.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.MathHelper;
import thehippomaster.AnimationAPI.AnimationAPI;
import thehippomaster.AnimationAPI.IAnimatedEntity;
import thehippomaster.AnimationAPI.client.Transform;

@SideOnly(value=Side.CLIENT)
public class Animator {
    private int tempTick = 0;
    private int prevTempTick;
    private boolean correctAnim = false;
    private ModelBase mainModel;
    private IAnimatedEntity animEntity;
    private HashMap<ModelRenderer, Transform> transformMap;
    private HashMap<ModelRenderer, Transform> prevTransformMap;
    public static final float PI = (float)Math.PI;

    public Animator(ModelBase model) {
        this.mainModel = model;
        this.transformMap = new HashMap();
        this.prevTransformMap = new HashMap();
    }

    public IAnimatedEntity getEntity() {
        return this.animEntity;
    }

    public void update(IAnimatedEntity entity) {
        this.prevTempTick = 0;
        this.tempTick = 0;
        this.correctAnim = false;
        this.animEntity = entity;
        this.transformMap.clear();
        this.prevTransformMap.clear();
        for (int i = 0; i < this.mainModel.boxList.size(); ++i) {
            ModelRenderer box = (ModelRenderer)this.mainModel.boxList.get(i);
            box.rotateAngleX = 0.0f;
            box.rotateAngleY = 0.0f;
            box.rotateAngleZ = 0.0f;
        }
    }

    public boolean setAnim(int animID) {
        this.prevTempTick = 0;
        this.tempTick = 0;
        this.correctAnim = this.animEntity.getAnimID() == animID;
        return this.correctAnim;
    }

    public void startPhase(int duration) {
        if (!this.correctAnim) {
            return;
        }
        this.prevTempTick = this.tempTick;
        this.tempTick += duration;
    }

    public void setStationaryPhase(int duration) {
        this.startPhase(duration);
        this.endPhase(true);
    }

    public void resetPhase(int duration) {
        this.startPhase(duration);
        this.endPhase();
    }

    public void rotate(ModelRenderer box, float x, float y, float z) {
        if (!this.correctAnim) {
            return;
        }
        if (!this.transformMap.containsKey(box)) {
            this.transformMap.put(box, new Transform(x, y, z));
        } else {
            this.transformMap.get(box).addRot(x, y, z);
        }
    }

    public void move(ModelRenderer box, float x, float y, float z) {
        if (!this.correctAnim) {
            return;
        }
        if (!this.transformMap.containsKey(box)) {
            this.transformMap.put(box, new Transform(x, y, z, 0.0f, 0.0f, 0.0f));
        } else {
            this.transformMap.get(box).addOffset(x, y, z);
        }
    }

    public void endPhase() {
        this.endPhase(false);
    }

    private void endPhase(boolean stationary) {
        if (!this.correctAnim) {
            return;
        }
        int animTick = this.animEntity.getAnimTick();
        if (animTick >= this.prevTempTick && animTick < this.tempTick) {
            if (stationary) {
                for (ModelRenderer box : this.prevTransformMap.keySet()) {
                    Transform transform = this.prevTransformMap.get(box);
                    box.rotateAngleX += transform.rotX;
                    box.rotateAngleY += transform.rotY;
                    box.rotateAngleZ += transform.rotZ;
                    box.rotationPointX += transform.offsetX;
                    box.rotationPointY += transform.offsetY;
                    box.rotationPointZ += transform.offsetZ;
                }
            } else {
                Transform transform;
                float tick = ((float)(animTick - this.prevTempTick) + AnimationAPI.proxy.getPartialTick()) / (float)(this.tempTick - this.prevTempTick);
                float inc = MathHelper.sin((float)(tick * (float)Math.PI / 2.0f));
                float dec = 1.0f - inc;
                for (ModelRenderer box : this.prevTransformMap.keySet()) {
                    transform = this.prevTransformMap.get(box);
                    box.rotateAngleX += dec * transform.rotX;
                    box.rotateAngleY += dec * transform.rotY;
                    box.rotateAngleZ += dec * transform.rotZ;
                    box.rotationPointX += dec * transform.offsetX;
                    box.rotationPointY += dec * transform.offsetY;
                    box.rotationPointZ += dec * transform.offsetZ;
                }
                for (ModelRenderer box : this.transformMap.keySet()) {
                    transform = this.transformMap.get(box);
                    box.rotateAngleX += inc * transform.rotX;
                    box.rotateAngleY += inc * transform.rotY;
                    box.rotateAngleZ += inc * transform.rotZ;
                    box.rotationPointX += inc * transform.offsetX;
                    box.rotationPointY += inc * transform.offsetY;
                    box.rotationPointZ += inc * transform.offsetZ;
                }
            }
        }
        if (!stationary) {
            this.prevTransformMap.clear();
            this.prevTransformMap.putAll(this.transformMap);
            this.transformMap.clear();
        }
    }
}

