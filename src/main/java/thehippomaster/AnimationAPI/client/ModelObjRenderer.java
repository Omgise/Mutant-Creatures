/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.Side
 *  cpw.mods.fml.relauncher.SideOnly
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraftforge.client.model.IModelCustom
 *  org.lwjgl.opengl.GL11
 */
package thehippomaster.AnimationAPI.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class ModelObjRenderer
extends ModelRenderer {
    public IModelCustom model;
    private float theScale;
    private int displayList;
    private boolean compiled;

    public ModelObjRenderer(ModelBase bass) {
        this(bass, null, 1.0f);
    }

    public ModelObjRenderer(ModelBase bass, IModelCustom shape) {
        this(bass, shape, 1.0f);
    }

    public ModelObjRenderer(ModelBase bass, IModelCustom shape, float scale) {
        super(bass);
        this.theScale = scale;
        this.model = shape;
    }

    public void setScale(float scale) {
        this.theScale = scale;
    }

    public void render(float scale) {
        if (!this.isHidden && this.showModel) {
            if (!this.compiled) {
                this.compileDisplayList(scale);
            }
            GL11.glTranslatef((float)this.offsetX, (float)this.offsetY, (float)this.offsetZ);
            if (this.rotateAngleX == 0.0f && this.rotateAngleY == 0.0f && this.rotateAngleZ == 0.0f) {
                if (this.rotationPointX == 0.0f && this.rotationPointY == 0.0f && this.rotationPointZ == 0.0f) {
                    GL11.glPushMatrix();
                    GL11.glScalef((float)this.theScale, (float)this.theScale, (float)this.theScale);
                    GL11.glCallList((int)this.displayList);
                    GL11.glPopMatrix();
                    if (this.childModels != null) {
                        for (int i = 0; i < this.childModels.size(); ++i) {
                            ((ModelRenderer)this.childModels.get(i)).render(scale);
                        }
                    }
                } else {
                    GL11.glTranslatef((float)(this.rotationPointX * scale), (float)(this.rotationPointY * scale), (float)(this.rotationPointZ * scale));
                    GL11.glPushMatrix();
                    GL11.glScalef((float)this.theScale, (float)this.theScale, (float)this.theScale);
                    GL11.glCallList((int)this.displayList);
                    GL11.glPopMatrix();
                    if (this.childModels != null) {
                        for (int i = 0; i < this.childModels.size(); ++i) {
                            ((ModelRenderer)this.childModels.get(i)).render(scale);
                        }
                    }
                    GL11.glTranslatef((float)(-this.rotationPointX * scale), (float)(-this.rotationPointY * scale), (float)(-this.rotationPointZ * scale));
                }
            } else {
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(this.rotationPointX * scale), (float)(this.rotationPointY * scale), (float)(this.rotationPointZ * scale));
                if (this.rotateAngleZ != 0.0f) {
                    GL11.glRotatef((float)(this.rotateAngleZ * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
                }
                if (this.rotateAngleY != 0.0f) {
                    GL11.glRotatef((float)(this.rotateAngleY * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (this.rotateAngleX != 0.0f) {
                    GL11.glRotatef((float)(this.rotateAngleX * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
                }
                GL11.glPushMatrix();
                GL11.glScalef((float)this.theScale, (float)this.theScale, (float)this.theScale);
                GL11.glCallList((int)this.displayList);
                GL11.glPopMatrix();
                if (this.childModels != null) {
                    for (int i = 0; i < this.childModels.size(); ++i) {
                        ((ModelRenderer)this.childModels.get(i)).render(scale);
                    }
                }
                GL11.glPopMatrix();
            }
            GL11.glTranslatef((float)(-this.offsetX), (float)(-this.offsetY), (float)(-this.offsetZ));
        }
    }

    public void renderWithRotation(float scale) {
        if (!this.isHidden && this.showModel) {
            if (!this.compiled) {
                this.compileDisplayList(scale);
            }
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(this.rotationPointX * scale), (float)(this.rotationPointY * scale), (float)(this.rotationPointZ * scale));
            if (this.rotateAngleY != 0.0f) {
                GL11.glRotatef((float)(this.rotateAngleY * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
            }
            if (this.rotateAngleX != 0.0f) {
                GL11.glRotatef((float)(this.rotateAngleX * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
            }
            if (this.rotateAngleZ != 0.0f) {
                GL11.glRotatef((float)(this.rotateAngleZ * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GL11.glPushMatrix();
            GL11.glScalef((float)this.theScale, (float)this.theScale, (float)this.theScale);
            GL11.glCallList((int)this.displayList);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }

    protected void compileDisplayList(float scale) {
        this.displayList = GLAllocation.generateDisplayLists((int)1);
        GL11.glNewList((int)this.displayList, (int)4864);
        GL11.glPushMatrix();
        GL11.glScalef((float)0.76f, (float)0.76f, (float)0.76f);
        GL11.glRotatef((float)180.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        this.model.renderAll();
        GL11.glPopMatrix();
        GL11.glEndList();
        this.compiled = true;
    }
}

