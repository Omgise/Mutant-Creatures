package thehippomaster.MutantCreatures.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;
import thehippomaster.MutantCreatures.MutantEnderman;

public class EndermanArm {
   public EnderArmRenderer arm;
   public ModelRenderer forearm;
   public ModelRenderer hand;
   public ModelRenderer[] finger;
   public ModelRenderer[] foreFinger;
   public ModelRenderer thumb;
   private boolean right;
   public static final float PI = (float)Math.PI;

   public EndermanArm(boolean right) {
      this.right = right;
   }

   public void init(ModelBase model, ModelRenderer connect) {
      this.finger = new ModelRenderer[3];
      this.foreFinger = new ModelRenderer[3];
      this.arm = new EnderArmRenderer(model, 92, 0);
      this.arm.mirror = !this.right;
      this.arm.addBox(-1.5F, 0.0F, -1.5F, 3, 22, 3, 0.1F);
      this.arm.setRotationPoint(this.right ? -4.0F : 4.0F, -14.0F, 0.0F);
      connect.addChild(this.arm);
      this.forearm = new ModelRenderer(model, 104, 0);
      this.forearm.mirror = !this.right;
      this.forearm.addBox(-1.5F, 0.0F, -1.5F, 3, 18, 3);
      this.forearm.setRotationPoint(0.0F, 21.0F, 1.0F);
      this.arm.addChild(this.forearm);
      this.hand = new ModelRenderer(model);
      this.hand.setRotationPoint(0.0F, 17.5F, 0.0F);
      this.forearm.addChild(this.hand);
      float fingerScale = 0.6F;

      for(int i = 0; i < this.finger.length; ++i) {
         this.finger[i] = new ModelRenderer(model, 76, 0);
         this.finger[i].mirror = !this.right;
         this.finger[i].addBox(-0.5F, 0.0F, -0.5F, 1, i == 1 ? 6 : 5, 1, fingerScale);
      }

      this.finger[0].setRotationPoint(this.right ? -0.5F : 0.5F, 0.0F, -1.0F);
      this.finger[1].setRotationPoint(this.right ? -0.5F : 0.5F, 0.0F, 0.0F);
      this.finger[2].setRotationPoint(this.right ? -0.5F : 0.5F, 0.0F, 1.0F);

      for(int i = 0; i < this.foreFinger.length; ++i) {
         this.foreFinger[i] = new ModelRenderer(model, 76, 0);
         this.foreFinger[i].mirror = !this.right;
         this.foreFinger[i].addBox(-0.5F, 0.0F, -0.5F, 1, i == 1 ? 6 : 5, 1, fingerScale - 0.01F);
         this.foreFinger[i].setRotationPoint(0.0F, 0.5F + (float)(i == 1 ? 6 : 5), 0.0F);
      }

      for(int i = 0; i < this.finger.length; ++i) {
         this.hand.addChild(this.finger[i]);
         this.finger[i].addChild(this.foreFinger[i]);
      }

      this.thumb = new ModelRenderer(model, 76, 0);
      this.thumb.mirror = this.right;
      this.thumb.addBox(-0.5F, 0.0F, -0.5F, 1, 5, 1, fingerScale);
      this.thumb.setRotationPoint(this.right ? 0.5F : -0.5F, 0.0F, -0.5F);
      this.hand.addChild(this.thumb);
   }

   private void resetAngles(ModelRenderer model) {
      model.rotateAngleX = 0.0F;
      model.rotateAngleY = 0.0F;
      model.rotateAngleZ = 0.0F;
   }

   public void setAngles() {
      this.resetAngles(this.arm);
      this.resetAngles(this.forearm);
      this.resetAngles(this.hand);

      for(int i = 0; i < this.finger.length; ++i) {
         this.resetAngles(this.finger[i]);
         this.resetAngles(this.foreFinger[i]);
      }

      this.resetAngles(this.thumb);
      if (this.right) {
         this.arm.rotateAngleX = (-(float)Math.PI / 6F);
         this.arm.rotateAngleZ = ((float)Math.PI / 6F);
         this.forearm.rotateAngleX = (-(float)Math.PI / 5F);
         this.hand.rotateAngleY = (-(float)Math.PI / 8F);
         this.finger[0].rotateAngleX = -0.2617994F;
         this.finger[1].rotateAngleZ = 0.17453294F;
         this.finger[2].rotateAngleX = 0.2617994F;
         this.foreFinger[0].rotateAngleZ = -0.2617994F;
         this.foreFinger[1].rotateAngleZ = (-(float)Math.PI / 8F);
         this.foreFinger[2].rotateAngleZ = -0.2617994F;
         this.thumb.rotateAngleX = (-(float)Math.PI / 5F);
         this.thumb.rotateAngleZ = (-(float)Math.PI / 8F);
      } else {
         this.arm.rotateAngleX = (-(float)Math.PI / 6F);
         this.arm.rotateAngleZ = (-(float)Math.PI / 6F);
         this.forearm.rotateAngleX = (-(float)Math.PI / 5F);
         this.hand.rotateAngleY = ((float)Math.PI / 8F);
         this.finger[0].rotateAngleX = -0.2617994F;
         this.finger[1].rotateAngleZ = -0.17453294F;
         this.finger[2].rotateAngleX = 0.2617994F;
         this.foreFinger[0].rotateAngleZ = 0.2617994F;
         this.foreFinger[1].rotateAngleZ = ((float)Math.PI / 8F);
         this.foreFinger[2].rotateAngleZ = 0.2617994F;
         this.thumb.rotateAngleX = (-(float)Math.PI / 5F);
         this.thumb.rotateAngleZ = ((float)Math.PI / 8F);
      }

   }

   public void postRender(float scale) {
      this.arm.postRender(scale);
      this.forearm.postRender(scale);
      this.hand.postRender(scale);
   }

   public static class EnderArmRenderer extends ModelRenderer {
      public float animTick = 0.0F;
      public MutantEnderman enderman = null;

      public EnderArmRenderer(ModelBase model) {
         super(model);
      }

      public EnderArmRenderer(ModelBase model, int x, int y) {
         super(model, x, y);
      }

      public void render(float scale) {
         if (this.enderman != null) {
            float a = (float)this.enderman.preTargetTick + (float)(this.enderman.hasTargetTick - this.enderman.preTargetTick) * this.animTick;
            GL11.glPushMatrix();
            GL11.glScalef(a / 10.0F, a / 10.0F, a / 10.0F);
         }

         super.render(scale);
         if (this.enderman != null) {
            GL11.glPopMatrix();
         }

      }
   }
}
