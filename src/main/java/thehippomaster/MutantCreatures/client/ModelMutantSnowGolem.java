package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import thehippomaster.AnimationAPI.client.ModelJoint;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.MutantSnowGolem;

@SideOnly(Side.CLIENT)
public class ModelMutantSnowGolem extends ModelBase {
   public ModelRenderer pelvis;
   public ModelRenderer abdomen;
   public ModelRenderer chest;
   public ModelJoint head;
   public ModelRenderer headCore;
   public ModelJoint arm1;
   public ModelJoint arm2;
   public ModelJoint forearm1;
   public ModelJoint forearm2;
   public ModelJoint leg1;
   public ModelJoint leg2;
   public ModelJoint foreleg1;
   public ModelJoint foreleg2;
   protected float animTick;
   public static final float PI = (float)Math.PI;

   public ModelMutantSnowGolem() {
      this.textureWidth = 128;
      this.textureHeight = 64;
      this.animTick = 0.0F;
      this.pelvis = new ModelRenderer(this);
      this.pelvis.setRotationPoint(0.0F, 13.5F, 5.0F);
      this.abdomen = new ModelRenderer(this, 0, 32);
      this.abdomen.addBox(-5.0F, -8.0F, -4.0F, 10, 8, 8);
      this.pelvis.addChild(this.abdomen);
      this.chest = new ModelRenderer(this, 24, 36);
      this.chest.addBox(-8.0F, -12.0F, -6.0F, 16, 12, 12);
      this.chest.setRotationPoint(0.0F, -6.0F, 0.0F);
      this.head = (ModelJoint)(new ModelJoint(this, 0, 0)).setTextureSize(64, 32);
      this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
      this.head.setRotationPoint(0.0F, -12.0F, -2.0F);
      this.chest.addChild(this.head);
      this.headCore = new ModelRenderer(this, 64, 0);
      this.headCore.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
      this.headCore.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.head.addChild(this.headCore);
      this.abdomen.addChild(this.chest);
      this.arm1 = new ModelJoint(this, 68, 16);
      this.arm1.addBox(-2.5F, 0.0F, -2.5F, 5, 10, 5);
      this.arm1.setRotationPoint(-9.0F, -11.0F, 0.0F);
      this.chest.addChild(this.arm1);
      this.forearm1 = new ModelJoint(this, 96, 0);
      this.forearm1.addBox(-3.0F, 0.0F, -3.0F, 6, 12, 6);
      this.forearm1.setRotationPoint(0.0F, 10.0F, 0.0F);
      this.arm1.addChild(this.forearm1);
      this.arm2 = new ModelJoint(this, 68, 16);
      this.arm2.mirror = true;
      this.arm2.addBox(-2.5F, 0.0F, -2.5F, 5, 10, 5);
      this.arm2.setRotationPoint(9.0F, -11.0F, 0.0F);
      this.chest.addChild(this.arm2);
      this.forearm2 = new ModelJoint(this, 96, 0);
      this.forearm2.mirror = true;
      this.forearm2.addBox(-3.0F, 0.0F, -3.0F, 6, 12, 6);
      this.forearm2.setRotationPoint(0.0F, 10.0F, 0.0F);
      this.arm2.addChild(this.forearm2);
      this.leg1 = new ModelJoint(this, 88, 18);
      this.leg1.addBox(-3.0F, 0.0F, -3.0F, 6, 8, 6);
      this.leg1.setRotationPoint(-4.0F, -1.0F, -3.0F);
      this.pelvis.addChild(this.leg1);
      this.foreleg1 = new ModelJoint(this, 88, 32);
      this.foreleg1.addBox(-3.0F, 0.0F, -3.0F, 6, 8, 6);
      this.foreleg1.setRotationPoint(-1.0F, 6.0F, -0.0F);
      this.leg1.addChild(this.foreleg1);
      this.leg2 = new ModelJoint(this, 88, 18);
      this.leg2.mirror = true;
      this.leg2.addBox(-3.0F, 0.0F, -3.0F, 6, 8, 6);
      this.leg2.setRotationPoint(4.0F, -1.0F, -3.0F);
      this.pelvis.addChild(this.leg2);
      this.foreleg2 = new ModelJoint(this, 88, 32);
      this.foreleg2.mirror = true;
      this.foreleg2.addBox(-3.0F, 0.0F, -3.0F, 6, 8, 6);
      this.foreleg2.setRotationPoint(1.0F, 6.0F, -0.0F);
      this.leg2.addChild(this.foreleg2);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      this.setAngles();
      this.animTick = MutantCreatures.proxy.getPartialTicks();
      this.animate((MutantSnowGolem)entity, f, f1, f2, f3, f4, f5);
      this.pelvis.render(f5);
   }

   public void setAngles() {
      this.pelvis.rotationPointY = 13.5F;
      this.abdomen.rotateAngleX = 0.1308997F;
      this.chest.rotateAngleX = 0.1308997F;
      this.chest.rotateAngleY = 0.0F;
      this.head.rotateAngleX = -0.2617994F;
      this.head.getModel().rotateAngleX = 0.0F;
      this.head.getModel().rotateAngleY = 0.0F;
      this.arm1.rotateAngleX = (-(float)Math.PI / 10F);
      this.arm1.rotateAngleZ = 0.0F;
      this.arm1.getModel().rotateAngleX = 0.0F;
      this.arm1.getModel().rotateAngleY = ((float)Math.PI / 6F);
      this.arm1.getModel().rotateAngleZ = ((float)Math.PI / 6F);
      this.forearm1.rotateAngleY = (-(float)Math.PI / 6F);
      this.forearm1.rotateAngleZ = -0.2617994F;
      this.forearm1.getModel().rotateAngleX = (-(float)Math.PI / 6F);
      this.arm2.rotateAngleX = (-(float)Math.PI / 10F);
      this.arm2.rotateAngleZ = 0.0F;
      this.arm2.getModel().rotateAngleX = 0.0F;
      this.arm2.getModel().rotateAngleY = (-(float)Math.PI / 6F);
      this.arm2.getModel().rotateAngleZ = (-(float)Math.PI / 6F);
      this.forearm2.rotateAngleY = ((float)Math.PI / 6F);
      this.forearm2.rotateAngleZ = 0.2617994F;
      this.forearm2.getModel().rotateAngleX = (-(float)Math.PI / 6F);
      this.leg1.rotateAngleX = (-(float)Math.PI / 5F);
      this.leg1.getModel().rotateAngleZ = ((float)Math.PI / 6F);
      this.foreleg1.rotateAngleZ = (-(float)Math.PI / 6F);
      this.foreleg1.getModel().rotateAngleX = 0.69813174F;
      this.leg2.rotateAngleX = (-(float)Math.PI / 5F);
      this.leg2.getModel().rotateAngleZ = (-(float)Math.PI / 6F);
      this.foreleg2.rotateAngleZ = ((float)Math.PI / 6F);
      this.foreleg2.getModel().rotateAngleX = 0.69813174F;
   }

   public void animate(MutantSnowGolem golem, float f, float f1, float f2, float f3, float f4, float f5) {
      float temp = 0.5F;
      float walkAnim = MathHelper.sin(f * 0.45F) * f1;
      float walkAnim1 = (MathHelper.cos((f - temp) * 0.45F) + temp) * f1;
      float walkAnim2 = (MathHelper.cos((f - temp + ((float)Math.PI * 2F)) * 0.45F) + temp) * f1;
      float breatheAnim = MathHelper.sin(f2 * 0.11F);
      float faceYaw = f3 * (float)Math.PI / 180.0F;
      float facePitch = f4 * (float)Math.PI / 180.0F;
      if (golem.throwAttack) {
         this.animateThrow(golem.throwTick);
         float scale = 1.0F - MathHelper.clamp_float((float)golem.throwTick / 4.0F, 0.0F, 1.0F);
         walkAnim *= scale;
      }

      ModelRenderer var10000 = this.head.getModel();
      var10000.rotateAngleX -= breatheAnim * 0.01F;
      var10000 = this.chest;
      var10000.rotateAngleX -= breatheAnim * 0.01F;
      ModelJoint var17 = this.arm1;
      var17.rotateAngleZ += breatheAnim * 0.03F;
      var17 = this.arm2;
      var17.rotateAngleZ -= breatheAnim * 0.03F;
      ModelRenderer var19 = this.head.getModel();
      var19.rotateAngleX += facePitch;
      var19 = this.head.getModel();
      var19.rotateAngleY += faceYaw;
      var19 = this.pelvis;
      var19.rotationPointY += Math.abs(walkAnim) * 1.5F;
      var19 = this.abdomen;
      var19.rotateAngleX += f1 * 0.2F;
      var19 = this.chest;
      var19.rotateAngleY -= walkAnim * 0.1F;
      ModelJoint var24 = this.head;
      var24.rotateAngleX -= f1 * 0.2F;
      var24 = this.arm1;
      var24.rotateAngleX -= walkAnim * 0.6F;
      var24 = this.arm2;
      var24.rotateAngleX += walkAnim * 0.6F;
      ModelRenderer var27 = this.forearm1.getModel();
      var27.rotateAngleX -= walkAnim * 0.2F;
      var27 = this.forearm2.getModel();
      var27.rotateAngleX += walkAnim * 0.2F;
      ModelJoint var29 = this.leg1;
      var29.rotateAngleX += walkAnim1 * 1.1F;
      var29 = this.leg2;
      var29.rotateAngleX += walkAnim2 * 1.1F;
      ModelRenderer var31 = this.foreleg1.getModel();
      var31.rotateAngleX += walkAnim * 0.2F;
      var31 = this.foreleg2.getModel();
      var31.rotateAngleX -= walkAnim * 0.2F;
   }

   protected void animateThrow(int fullTick) {
      if (fullTick < 7) {
         float tick = ((float)fullTick + this.animTick) / 7.0F;
         float f = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var10000 = this.abdomen;
         var10000.rotateAngleX += -f * 0.2F;
         var10000 = this.chest;
         var10000.rotateAngleX += -f * 0.4F;
         ModelJoint var10 = this.arm1;
         var10.rotateAngleX += -f * 1.6F;
         var10 = this.arm1;
         var10.rotateAngleZ += f * 0.8F;
         var10 = this.arm2;
         var10.rotateAngleX += -f * 1.6F;
         var10 = this.arm2;
         var10.rotateAngleZ += -f * 0.8F;
      } else if (fullTick < 10) {
         float tick = ((float)(fullTick - 7) + this.animTick) / 3.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         float f1 = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var14 = this.abdomen;
         var14.rotateAngleX += -f * 0.4F + 0.2F;
         var14 = this.chest;
         var14.rotateAngleX += -f * 0.6F + 0.2F;
         ModelJoint var16 = this.arm1;
         var16.rotateAngleX += -f * 0.8F - 0.8F;
         var16 = this.arm1;
         var16.rotateAngleZ += 0.8F;
         var16 = this.arm2;
         var16.rotateAngleX += -f * 0.8F - 0.8F;
         var16 = this.arm2;
         var16.rotateAngleZ += -0.8F;
      } else if (fullTick < 14) {
         ModelRenderer var20 = this.abdomen;
         var20.rotateAngleX += 0.2F;
         var20 = this.chest;
         var20.rotateAngleX += 0.2F;
         ModelJoint var22 = this.arm1;
         var22.rotateAngleX += -0.8F;
         var22 = this.arm1;
         var22.rotateAngleZ += 0.8F;
         var22 = this.arm2;
         var22.rotateAngleX += -0.8F;
         var22 = this.arm2;
         var22.rotateAngleZ += -0.8F;
      } else if (fullTick < 20) {
         float tick = ((float)(fullTick - 14) + this.animTick) / 6.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         ModelRenderer var26 = this.abdomen;
         var26.rotateAngleX += f * 0.2F;
         var26 = this.chest;
         var26.rotateAngleX += f * 0.2F;
         ModelJoint var28 = this.arm1;
         var28.rotateAngleX += -f * 0.8F;
         var28 = this.arm1;
         var28.rotateAngleZ += f * 0.8F;
         var28 = this.arm2;
         var28.rotateAngleX += -f * 0.8F;
         var28 = this.arm2;
         var28.rotateAngleZ += -f * 0.8F;
      }

   }
}
