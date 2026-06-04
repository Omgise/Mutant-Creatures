/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.Side
 *  cpw.mods.fml.relauncher.SideOnly
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelRenderer
 */
package thehippomaster.AnimationAPI.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(value=Side.CLIENT)
public class ModelJoint
extends ModelRenderer {
    private ModelRenderer model;

    public ModelJoint(ModelBase base) {
        this(base, null);
        this.model = new ModelRenderer(base);
        super.addChild(this.model);
    }

    public ModelJoint(ModelBase base, int x, int y) {
        this(base);
        this.model = new ModelRenderer(base, x, y);
        this.model.setTextureOffset(x, y);
        super.addChild(this.model);
    }

    public ModelJoint(ModelBase base, String name) {
        super(base, name);
        this.model = new ModelRenderer(base, name);
        this.model.setTextureOffset(0, 0);
        this.model.setTextureSize(base.textureWidth, base.textureHeight);
        super.addChild(this.model);
    }

    public ModelRenderer setModel(ModelRenderer newModel) {
        this.childModels.remove(this.model);
        this.model = newModel;
        super.addChild(newModel);
        return this;
    }

    public ModelRenderer setTextureOffset(int x, int y) {
        if (this.model != null) {
            this.model.setTextureOffset(x, y);
        }
        return this;
    }

    public ModelRenderer setTextureSize(int w, int h) {
        if (this.model != null) {
            this.model.setTextureSize(w, h);
        }
        return this;
    }

    public ModelRenderer addBox(String name, float x, float y, float z, int w, int h, int d) {
        this.model.addBox(name, x, y, z, w, h, d);
        return this;
    }

    public ModelRenderer addBox(float x, float y, float z, int w, int h, int d) {
        this.model.addBox(x, y, z, w, h, d);
        return this;
    }

    public void addBox(float x, float y, float z, int w, int h, int d, float offset) {
        this.model.addBox(x, y, z, w, h, d, offset);
    }

    public void addChild(ModelRenderer child) {
        this.model.addChild(child);
    }

    public ModelRenderer getModel() {
        return this.model;
    }
}

