/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.Side
 *  cpw.mods.fml.relauncher.SideOnly
 */
package thehippomaster.AnimationAPI.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class Transform {
    public float rotX;
    public float rotY;
    public float rotZ;
    public float offsetX;
    public float offsetY;
    public float offsetZ;

    public Transform() {
        this.rotZ = 0.0f;
        this.rotY = 0.0f;
        this.rotX = 0.0f;
        this.offsetZ = 0.0f;
        this.offsetY = 0.0f;
        this.offsetX = 0.0f;
    }

    public Transform(float rx, float ry, float rz) {
        this.rotX = rx;
        this.rotY = ry;
        this.rotZ = rz;
        this.offsetZ = 0.0f;
        this.offsetY = 0.0f;
        this.offsetX = 0.0f;
    }

    public Transform(float x, float y, float z, float rx, float ry, float rz) {
        this(rx, ry, rz);
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
    }

    public void addRot(float x, float y, float z) {
        this.rotX += x;
        this.rotY += y;
        this.rotZ += z;
    }

    public void addOffset(float x, float y, float z) {
        this.offsetX += x;
        this.offsetY += y;
        this.offsetZ += z;
    }
}

