package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Arrays;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.MutantEnderman;

@SideOnly(Side.CLIENT)
public class ModelMutantEnderman extends ModelBase {
   public ModelRenderer pelvis;
   public ModelRenderer abdomen;
   public ModelRenderer chest;
   public ModelRenderer neck;
   public ModelRenderer head;
   public ModelRenderer mouth;
   public EndermanArm rightArm;
   public EndermanArm leftArm;
   public EndermanArm lowerRightArm;
   public EndermanArm lowerLeftArm;
   public ModelRenderer legjoint1;
   public ModelRenderer legjoint2;
   public ModelRenderer leg1;
   public ModelRenderer leg2;
   public ModelRenderer foreleg1;
   public ModelRenderer foreleg2;
   private float animTick;
   public static final float PI = (float)Math.PI;

   public ModelMutantEnderman() {
      this.textureWidth = 128;
      this.textureHeight = 64;
      this.animTick = 0.0F;
      this.pelvis = new ModelRenderer(this);
      this.pelvis.setRotationPoint(0.0F, -15.5F, 8.0F);
      this.abdomen = new ModelRenderer(this, 32, 0);
      this.abdomen.addBox(-4.0F, -10.0F, -2.0F, 8, 10, 4);
      this.pelvis.addChild(this.abdomen);
      this.chest = new ModelRenderer(this, 50, 8);
      this.chest.addBox(-5.0F, -16.0F, -3.0F, 10, 16, 6);
      this.chest.setRotationPoint(0.0F, -8.0F, 0.0F);
      this.abdomen.addChild(this.chest);
      this.neck = new ModelRenderer(this, 32, 14);
      this.neck.addBox(-1.5F, -4.0F, -1.5F, 3, 4, 3);
      this.neck.setRotationPoint(0.0F, -15.0F, 0.0F);
      this.chest.addChild(this.neck);
      this.head = new ModelRenderer(this);
      this.head.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8, 6, 8, 0.5F);
      this.head.setTextureOffset(0, 14).addBox(-4.0F, 3.0F, -8.0F, 8, 2, 8, 0.5F);
      this.head.setRotationPoint(0.0F, -5.0F, 3.0F);
      this.neck.addChild(this.head);
      this.mouth = new ModelRenderer(this, 0, 24);
      this.mouth.addBox(-4.0F, 3.0F, -8.0F, 8, 2, 8);
      this.head.addChild(this.mouth);
      this.rightArm = new EndermanArm(true);
      this.rightArm.init(this, this.chest);
      this.leftArm = new EndermanArm(false);
      this.leftArm.init(this, this.chest);
      this.lowerRightArm = new EndermanArm(true);
      this.lowerRightArm.init(this, this.chest);
      EndermanArm.EnderArmRenderer var10000 = this.lowerRightArm.arm;
      var10000.rotationPointY += 6.0F;
      this.lowerLeftArm = new EndermanArm(false);
      this.lowerLeftArm.init(this, this.chest);
      var10000 = this.lowerLeftArm.arm;
      var10000.rotationPointY += 6.0F;
      this.legjoint1 = new ModelRenderer(this);
      this.legjoint1.setRotationPoint(-1.5F, 0.0F, 0.75F);
      this.abdomen.addChild(this.legjoint1);
      this.legjoint2 = new ModelRenderer(this);
      this.legjoint2.setRotationPoint(1.5F, 0.0F, 0.75F);
      this.abdomen.addChild(this.legjoint2);
      this.leg1 = new ModelRenderer(this, 0, 34);
      this.leg1.addBox(-1.5F, 0.0F, -1.5F, 3, 24, 3, 0.5F);
      this.leg1.setRotationPoint(0.0F, -2.0F, 0.0F);
      this.legjoint1.addChild(this.leg1);
      this.leg2 = new ModelRenderer(this, 0, 34);
      this.leg2.mirror = true;
      this.leg2.addBox(-1.5F, 0.0F, -1.5F, 3, 24, 3, 0.5F);
      this.leg2.setRotationPoint(0.0F, -2.0F, 0.0F);
      this.legjoint2.addChild(this.leg2);
      this.foreleg1 = new ModelRenderer(this, 12, 34);
      this.foreleg1.addBox(-1.5F, 0.0F, -1.5F, 3, 24, 3, 0.5F);
      this.foreleg1.setRotationPoint(0.0F, 23.0F, 0.0F);
      this.leg1.addChild(this.foreleg1);
      this.foreleg2 = new ModelRenderer(this, 12, 34);
      this.foreleg2.mirror = true;
      this.foreleg2.addBox(-1.5F, 0.0F, -1.5F, 3, 24, 3, 0.5F);
      this.foreleg2.setRotationPoint(0.0F, 23.0F, 0.0F);
      this.leg2.addChild(this.foreleg2);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      MutantEnderman enderman = (MutantEnderman)entity;
      this.setAngles();
      this.animTick = MutantCreatures.proxy.getPartialTicks();
      this.animate(enderman, f, f1, f2, f3, f4, f5);
      this.lowerRightArm.arm.animTick = this.animTick;
      this.lowerRightArm.arm.enderman = enderman;
      this.lowerLeftArm.arm.animTick = this.animTick;
      this.lowerLeftArm.arm.enderman = enderman;
      this.pelvis.render(f5);
   }

   public void setAngles() {
      this.pelvis.rotationPointY = -15.5F;
      this.abdomen.rotateAngleX = ((float)Math.PI / 10F);
      this.chest.rotateAngleX = ((float)Math.PI / 8F);
      this.chest.rotateAngleY = 0.0F;
      this.chest.rotateAngleZ = 0.0F;
      this.neck.rotateAngleX = 0.19634955F;
      this.neck.rotateAngleZ = 0.0F;
      this.head.rotateAngleX = (-(float)Math.PI / 4F);
      this.head.rotateAngleY = 0.0F;
      this.head.rotateAngleZ = 0.0F;
      this.mouth.rotateAngleX = 0.0F;
      this.rightArm.setAngles();
      this.leftArm.setAngles();
      this.lowerRightArm.setAngles();
      EndermanArm.EnderArmRenderer var10000 = this.lowerRightArm.arm;
      var10000.rotateAngleX += 0.1F;
      var10000 = this.lowerRightArm.arm;
      var10000.rotateAngleZ -= 0.2F;
      this.lowerLeftArm.setAngles();
      var10000 = this.lowerLeftArm.arm;
      var10000.rotateAngleX += 0.1F;
      var10000 = this.lowerLeftArm.arm;
      var10000.rotateAngleZ += 0.2F;
      this.legjoint1.rotateAngleX = 0.0F;
      this.legjoint2.rotateAngleX = 0.0F;
      this.leg1.rotateAngleX = -0.8975979F;
      this.leg1.rotateAngleY = 0.0F;
      this.leg1.rotateAngleZ = 0.2617994F;
      this.leg2.rotateAngleX = -0.8975979F;
      this.leg2.rotateAngleY = 0.0F;
      this.leg2.rotateAngleZ = -0.2617994F;
      this.foreleg1.rotateAngleX = ((float)Math.PI / 4F);
      this.foreleg1.rotateAngleZ = -0.1308997F;
      this.foreleg2.rotateAngleX = ((float)Math.PI / 4F);
      this.foreleg2.rotateAngleZ = 0.1308997F;
   }

   public void animate(MutantEnderman enderman, float f, float f1, float f2, float f3, float f4, float f5) {
      float walkSpeed = 0.3F;
      float walkAnim1 = (MathHelper.sin((f - 0.8F) * walkSpeed) + 0.8F) * f1;
      float walkAnim2 = -(MathHelper.sin((f + 0.8F) * walkSpeed) - 0.8F) * f1;
      float walkAnim3 = (MathHelper.sin((f + 0.8F) * walkSpeed) - 0.8F) * f1;
      float walkAnim4 = -(MathHelper.sin((f - 0.8F) * walkSpeed) + 0.8F) * f1;
      float[] walkAnim = new float[5];
      Arrays.fill(walkAnim, MathHelper.sin(f * walkSpeed) * f1);
      float breatheAnim = MathHelper.sin(f2 * 0.15F);
      float faceYaw = f3 * (float)Math.PI / 180.0F;
      float facePitch = f4 * (float)Math.PI / 180.0F;

      for(int i = 1; i < enderman.heldBlock.length; ++i) {
         if (enderman.heldBlock[i] != 0) {
            this.animateHoldBlock(enderman.heldBlockTick[i], i, enderman.hasTarget > 0);
            walkAnim[i] *= 0.4F;
         }
      }

      if (enderman.currentAttackID == 1) {
         int arm = enderman.getMeleeArm();
         this.animateMelee(enderman.animTick, arm);
         walkAnim[arm] = 0.0F;
      }

      if (enderman.currentAttackID == 2) {
         int arm = enderman.getThrownBlock();
         this.animateThrowBlock(enderman.animTick, arm);
      }

      if (enderman.currentAttackID == 5) {
         this.animateScream(enderman.animTick);
         float scale = 1.0F - MathHelper.clamp_float((float)enderman.deathTick / 6.0F, 0.0F, 1.0F);
         faceYaw *= scale;
         facePitch *= scale;
         walkAnim1 *= scale;
         walkAnim2 *= scale;
         walkAnim3 *= scale;
         walkAnim4 *= scale;
         Arrays.fill(walkAnim, 0.0F);
      }

      if (enderman.currentAttackID == 7) {
         this.animateTeleSmash(enderman.animTick);
      }

      if (enderman.currentAttackID == 10) {
         this.animateDeath(enderman.deathTick);
         float scale = 1.0F - MathHelper.clamp_float((float)enderman.deathTick / 6.0F, 0.0F, 1.0F);
         faceYaw *= scale;
         facePitch *= scale;
         walkAnim1 *= scale;
         walkAnim2 *= scale;
         walkAnim3 *= scale;
         walkAnim4 *= scale;
         Arrays.fill(walkAnim, 0.0F);
      }

      ModelRenderer var10000 = this.head;
      var10000.rotateAngleX += facePitch * 0.5F;
      var10000 = this.head;
      var10000.rotateAngleY += faceYaw * 0.7F;
      var10000 = this.head;
      var10000.rotateAngleZ -= faceYaw * 0.7F;
      var10000 = this.neck;
      var10000.rotateAngleX += facePitch * 0.3F;
      var10000 = this.chest;
      var10000.rotateAngleX += facePitch * 0.2F;
      var10000 = this.mouth;
      var10000.rotateAngleX += breatheAnim * 0.02F + 0.02F;
      var10000 = this.neck;
      var10000.rotateAngleX -= breatheAnim * 0.02F;
      var10000 = this.rightArm.arm;
      var10000.rotateAngleZ += breatheAnim * 0.004F;
      var10000 = this.leftArm.arm;
      var10000.rotateAngleZ -= breatheAnim * 0.004F;

      for(ModelRenderer finger : this.rightArm.finger) {
         finger.rotateAngleZ += breatheAnim * 0.05F;
      }

      var10000 = this.rightArm.thumb;
      var10000.rotateAngleZ -= breatheAnim * 0.05F;

      for(ModelRenderer finger : this.leftArm.finger) {
         finger.rotateAngleZ -= breatheAnim * 0.05F;
      }

      var10000 = this.leftArm.thumb;
      var10000.rotateAngleZ += breatheAnim * 0.05F;
      var10000 = this.lowerRightArm.arm;
      var10000.rotateAngleZ += breatheAnim * 0.002F;
      var10000 = this.lowerLeftArm.arm;
      var10000.rotateAngleZ -= breatheAnim * 0.002F;

      for(ModelRenderer finger : this.lowerRightArm.finger) {
         finger.rotateAngleZ += breatheAnim * 0.02F;
      }

      var10000 = this.lowerRightArm.thumb;
      var10000.rotateAngleZ -= breatheAnim * 0.02F;

      for(ModelRenderer finger : this.lowerLeftArm.finger) {
         finger.rotateAngleZ -= breatheAnim * 0.02F;
      }

      var10000 = this.lowerLeftArm.thumb;
      var10000.rotateAngleZ += breatheAnim * 0.02F;
      var10000 = this.pelvis;
      var10000.rotationPointY -= Math.abs(walkAnim[0]);
      var10000 = this.chest;
      var10000.rotateAngleY -= walkAnim[0] * 0.06F;
      var10000 = this.rightArm.arm;
      var10000.rotateAngleX -= walkAnim[1] * 0.6F;
      var10000 = this.leftArm.arm;
      var10000.rotateAngleX += walkAnim[2] * 0.6F;
      var10000 = this.rightArm.forearm;
      var10000.rotateAngleX -= walkAnim[1] * 0.2F;
      var10000 = this.leftArm.forearm;
      var10000.rotateAngleX += walkAnim[2] * 0.2F;
      var10000 = this.lowerRightArm.arm;
      var10000.rotateAngleX -= walkAnim[3] * 0.3F;
      var10000 = this.lowerLeftArm.arm;
      var10000.rotateAngleX += walkAnim[4] * 0.3F;
      var10000 = this.lowerRightArm.forearm;
      var10000.rotateAngleX -= walkAnim[3] * 0.1F;
      var10000 = this.lowerLeftArm.forearm;
      var10000.rotateAngleX += walkAnim[4] * 0.1F;
      var10000 = this.legjoint1;
      var10000.rotateAngleX += walkAnim1 * 0.6F;
      var10000 = this.legjoint2;
      var10000.rotateAngleX += walkAnim2 * 0.6F;
      var10000 = this.foreleg1;
      var10000.rotateAngleX += walkAnim3 * 0.3F;
      var10000 = this.foreleg2;
      var10000.rotateAngleX += walkAnim4 * 0.3F;
   }

   private void animateHoldBlock(int fullTick, int armID, boolean hasTarget) {
      float tick = ((float)fullTick + this.animTick) / 10.0F;
      if (!hasTarget) {
         tick = fullTick == 0 ? 0.0F : ((float)fullTick - this.animTick) / 10.0F;
      }

      float f = MathHelper.sin(tick * (float)Math.PI / 2.0F);
      if (armID == 1) {
         EndermanArm.EnderArmRenderer var10000 = this.rightArm.arm;
         var10000.rotateAngleZ += f * 0.8F;
         ModelRenderer var10 = this.rightArm.forearm;
         var10.rotateAngleZ += f * 0.6F;
         var10 = this.rightArm.hand;
         var10.rotateAngleY += f * 0.8F;
         var10 = this.rightArm.finger[0];
         var10.rotateAngleX += -f * 0.2F;
         var10 = this.rightArm.finger[2];
         var10.rotateAngleX += f * 0.2F;

         for(int i = 0; i < this.rightArm.finger.length; ++i) {
            var10 = this.rightArm.finger[i];
            var10.rotateAngleZ += f * 0.6F;
         }

         var10 = this.rightArm.thumb;
         var10.rotateAngleZ += -f * 0.4F;
      } else if (armID == 2) {
         EndermanArm.EnderArmRenderer var16 = this.leftArm.arm;
         var16.rotateAngleZ += -f * 0.8F;
         ModelRenderer var17 = this.leftArm.forearm;
         var17.rotateAngleZ += -f * 0.6F;
         var17 = this.leftArm.hand;
         var17.rotateAngleY += -f * 0.8F;
         var17 = this.leftArm.finger[0];
         var17.rotateAngleX += -f * 0.2F;
         var17 = this.leftArm.finger[2];
         var17.rotateAngleX += f * 0.2F;

         for(int i = 0; i < this.leftArm.finger.length; ++i) {
            var17 = this.leftArm.finger[i];
            var17.rotateAngleZ += -f * 0.6F;
         }

         var17 = this.leftArm.thumb;
         var17.rotateAngleZ += f * 0.4F;
      } else if (armID == 3) {
         EndermanArm.EnderArmRenderer var23 = this.lowerRightArm.arm;
         var23.rotateAngleZ += f * 0.5F;
         ModelRenderer var24 = this.lowerRightArm.forearm;
         var24.rotateAngleZ += f * 0.4F;
         var24 = this.lowerRightArm.hand;
         var24.rotateAngleY += f * 0.4F;
         var24 = this.lowerRightArm.finger[0];
         var24.rotateAngleX += -f * 0.2F;
         var24 = this.lowerRightArm.finger[2];
         var24.rotateAngleX += f * 0.2F;

         for(int i = 0; i < this.lowerRightArm.finger.length; ++i) {
            var24 = this.lowerRightArm.finger[i];
            var24.rotateAngleZ += f * 0.6F;
         }

         var24 = this.lowerRightArm.thumb;
         var24.rotateAngleZ += -f * 0.4F;
      } else if (armID == 4) {
         EndermanArm.EnderArmRenderer var30 = this.lowerLeftArm.arm;
         var30.rotateAngleZ += -f * 0.5F;
         ModelRenderer var31 = this.lowerLeftArm.forearm;
         var31.rotateAngleZ += -f * 0.4F;
         var31 = this.lowerLeftArm.hand;
         var31.rotateAngleY += -f * 0.4F;
         var31 = this.lowerLeftArm.finger[0];
         var31.rotateAngleX += -f * 0.2F;
         var31 = this.lowerLeftArm.finger[2];
         var31.rotateAngleX += f * 0.2F;

         for(int i = 0; i < this.lowerLeftArm.finger.length; ++i) {
            var31 = this.lowerLeftArm.finger[i];
            var31.rotateAngleZ += -f * 0.6F;
         }

         var31 = this.lowerLeftArm.thumb;
         var31.rotateAngleZ += f * 0.4F;
      }

   }

   private void animateMelee(int fullTick, int armID) {
      int right = (armID & 1) == 1 ? 1 : -1;
      boolean lower = armID >= 3;
      EndermanArm arm = this.getArmFromID(armID);
      if (fullTick < 2) {
         float tick = ((float)fullTick + this.animTick) / 2.0F;
         float f = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         EndermanArm.EnderArmRenderer var10000 = arm.arm;
         var10000.rotateAngleX += f * 0.2F;
         ModelRenderer var13 = arm.finger[0];
         var13.rotateAngleZ += f * 0.3F * (float)right;
         var13 = arm.finger[1];
         var13.rotateAngleZ += f * 0.3F * (float)right;
         var13 = arm.finger[2];
         var13.rotateAngleZ += f * 0.3F * (float)right;
         var13 = arm.foreFinger[0];
         var13.rotateAngleZ += -f * 0.5F * (float)right;
         var13 = arm.foreFinger[1];
         var13.rotateAngleZ += -f * 0.5F * (float)right;
         var13 = arm.foreFinger[2];
         var13.rotateAngleZ += -f * 0.5F * (float)right;
      } else if (fullTick < 5) {
         float tick = ((float)(fullTick - 2) + this.animTick) / 3.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         float f1 = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var19 = this.chest;
         var19.rotateAngleY += -f1 * 0.1F * (float)right;
         var19 = arm.arm;
         var19.rotateAngleX += f * 1.1F - 1.1F;
         var19 = arm.forearm;
         var19.rotateAngleX += -f * 0.4F;
         var19 = arm.finger[0];
         var19.rotateAngleZ += 0.3F * (float)right;
         var19 = arm.finger[1];
         var19.rotateAngleZ += 0.3F * (float)right;
         var19 = arm.finger[2];
         var19.rotateAngleZ += 0.3F * (float)right;
         var19 = arm.foreFinger[0];
         var19.rotateAngleZ += -0.5F * (float)right;
         var19 = arm.foreFinger[1];
         var19.rotateAngleZ += -0.5F * (float)right;
         var19 = arm.foreFinger[2];
         var19.rotateAngleZ += -0.5F * (float)right;
      } else if (fullTick < 6) {
         ModelRenderer var28 = this.chest;
         var28.rotateAngleY += -0.1F * (float)right;
         var28 = arm.arm;
         var28.rotateAngleX += -1.1F;
         var28 = arm.forearm;
         var28.rotateAngleX += -0.4F;
         var28 = arm.finger[0];
         var28.rotateAngleZ += 0.3F * (float)right;
         var28 = arm.finger[1];
         var28.rotateAngleZ += 0.3F * (float)right;
         var28 = arm.finger[2];
         var28.rotateAngleZ += 0.3F * (float)right;
         var28 = arm.foreFinger[0];
         var28.rotateAngleZ += -0.5F * (float)right;
         var28 = arm.foreFinger[1];
         var28.rotateAngleZ += -0.5F * (float)right;
         var28 = arm.foreFinger[2];
         var28.rotateAngleZ += -0.5F * (float)right;
      } else if (fullTick < 10) {
         float tick = ((float)(fullTick - 6) + this.animTick) / 4.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         ModelRenderer var37 = this.chest;
         var37.rotateAngleY += -f * 0.1F * (float)right;
         var37 = arm.arm;
         var37.rotateAngleX += -f * 1.1F;
         var37 = arm.forearm;
         var37.rotateAngleX += -f * 0.4F;
         var37 = arm.finger[0];
         var37.rotateAngleZ += f * 0.3F * (float)right;
         var37 = arm.finger[1];
         var37.rotateAngleZ += f * 0.3F * (float)right;
         var37 = arm.finger[2];
         var37.rotateAngleZ += f * 0.3F * (float)right;
         var37 = arm.foreFinger[0];
         var37.rotateAngleZ += -f * 0.5F * (float)right;
         var37 = arm.foreFinger[1];
         var37.rotateAngleZ += -f * 0.5F * (float)right;
         var37 = arm.foreFinger[2];
         var37.rotateAngleZ += -f * 0.5F * (float)right;
      }

   }

   private void animateThrowBlock(int fullTick, int armID) {
      if (armID == 1) {
         if (fullTick < 4) {
            float tick = ((float)fullTick + this.animTick) / 4.0F;
            float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
            float f1 = MathHelper.sin(tick * (float)Math.PI / 2.0F);
            EndermanArm.EnderArmRenderer var10000 = this.rightArm.arm;
            var10000.rotateAngleX += -f1 * 1.5F;
            var10000 = this.rightArm.arm;
            var10000.rotateAngleZ += f * 0.8F;
            ModelRenderer var28 = this.rightArm.forearm;
            var28.rotateAngleZ += f * 0.6F;
            var28 = this.rightArm.hand;
            var28.rotateAngleY += f * 0.8F;
            var28 = this.rightArm.finger[0];
            var28.rotateAngleX += -f * 0.2F;
            var28 = this.rightArm.finger[2];
            var28.rotateAngleX += f * 0.2F;

            for(int i = 0; i < this.rightArm.finger.length; ++i) {
               var28 = this.rightArm.finger[i];
               var28.rotateAngleZ += f * 0.6F;
            }

            var28 = this.rightArm.thumb;
            var28.rotateAngleZ += -f * 0.4F;
         } else if (fullTick < 7) {
            EndermanArm.EnderArmRenderer var34 = this.rightArm.arm;
            var34.rotateAngleX += -1.5F;
         } else if (fullTick < 14) {
            float tick = ((float)(fullTick - 7) + this.animTick) / 7.0F;
            float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
            EndermanArm.EnderArmRenderer var35 = this.rightArm.arm;
            var35.rotateAngleX += -f * 1.5F;
         }
      } else if (armID == 2) {
         if (fullTick < 4) {
            float tick = ((float)fullTick + this.animTick) / 4.0F;
            float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
            float f1 = MathHelper.sin(tick * (float)Math.PI / 2.0F);
            EndermanArm.EnderArmRenderer var36 = this.leftArm.arm;
            var36.rotateAngleX += -f1 * 1.5F;
            var36 = this.leftArm.arm;
            var36.rotateAngleZ += -f * 0.8F;
            ModelRenderer var38 = this.leftArm.forearm;
            var38.rotateAngleZ += -f * 0.6F;
            var38 = this.leftArm.hand;
            var38.rotateAngleY += -f * 0.8F;
            var38 = this.leftArm.finger[0];
            var38.rotateAngleX += -f * 0.2F;
            var38 = this.leftArm.finger[2];
            var38.rotateAngleX += f * 0.2F;

            for(int i = 0; i < this.leftArm.finger.length; ++i) {
               var38 = this.leftArm.finger[i];
               var38.rotateAngleZ += -f * 0.6F;
            }

            var38 = this.leftArm.thumb;
            var38.rotateAngleZ += f * 0.4F;
         } else if (fullTick < 7) {
            EndermanArm.EnderArmRenderer var44 = this.leftArm.arm;
            var44.rotateAngleX += -1.5F;
         } else if (fullTick < 14) {
            float tick = ((float)(fullTick - 7) + this.animTick) / 7.0F;
            float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
            EndermanArm.EnderArmRenderer var45 = this.leftArm.arm;
            var45.rotateAngleX += -f * 1.5F;
         }
      } else if (armID == 3) {
         if (fullTick < 4) {
            float tick = ((float)fullTick + this.animTick) / 4.0F;
            float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
            float f1 = MathHelper.sin(tick * (float)Math.PI / 2.0F);
            EndermanArm.EnderArmRenderer var46 = this.lowerRightArm.arm;
            var46.rotateAngleX += -f1 * 1.5F;
            var46 = this.lowerRightArm.arm;
            var46.rotateAngleZ += f * 0.5F;
            ModelRenderer var48 = this.lowerRightArm.forearm;
            var48.rotateAngleZ += f * 0.4F;
            var48 = this.lowerRightArm.hand;
            var48.rotateAngleY += f * 0.4F;
            var48 = this.lowerRightArm.finger[0];
            var48.rotateAngleX += -f * 0.2F;
            var48 = this.lowerRightArm.finger[2];
            var48.rotateAngleX += f * 0.2F;

            for(int i = 0; i < this.lowerRightArm.finger.length; ++i) {
               var48 = this.lowerRightArm.finger[i];
               var48.rotateAngleZ += f * 0.6F;
            }

            var48 = this.lowerRightArm.thumb;
            var48.rotateAngleZ += -f * 0.4F;
         } else if (fullTick < 7) {
            EndermanArm.EnderArmRenderer var54 = this.lowerRightArm.arm;
            var54.rotateAngleX += -1.5F;
         } else if (fullTick < 14) {
            float tick = ((float)(fullTick - 7) + this.animTick) / 7.0F;
            float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
            EndermanArm.EnderArmRenderer var55 = this.lowerRightArm.arm;
            var55.rotateAngleX += -f * 1.5F;
         }
      } else if (armID == 4) {
         if (fullTick < 4) {
            float tick = ((float)fullTick + this.animTick) / 4.0F;
            float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
            float f1 = MathHelper.sin(tick * (float)Math.PI / 2.0F);
            EndermanArm.EnderArmRenderer var56 = this.lowerLeftArm.arm;
            var56.rotateAngleX += -f1 * 1.5F;
            var56 = this.lowerLeftArm.arm;
            var56.rotateAngleZ += -f * 0.5F;
            ModelRenderer var58 = this.lowerLeftArm.forearm;
            var58.rotateAngleZ += -f * 0.4F;
            var58 = this.lowerLeftArm.hand;
            var58.rotateAngleY += -f * 0.4F;
            var58 = this.lowerLeftArm.finger[0];
            var58.rotateAngleX += -f * 0.2F;
            var58 = this.lowerLeftArm.finger[2];
            var58.rotateAngleX += f * 0.2F;

            for(int i = 0; i < this.lowerLeftArm.finger.length; ++i) {
               var58 = this.lowerLeftArm.finger[i];
               var58.rotateAngleZ += -f * 0.6F;
            }

            var58 = this.lowerLeftArm.thumb;
            var58.rotateAngleZ += f * 0.4F;
         } else if (fullTick < 7) {
            EndermanArm.EnderArmRenderer var64 = this.lowerLeftArm.arm;
            var64.rotateAngleX += -1.5F;
         } else if (fullTick < 14) {
            float tick = ((float)(fullTick - 7) + this.animTick) / 7.0F;
            float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
            EndermanArm.EnderArmRenderer var65 = this.lowerLeftArm.arm;
            var65.rotateAngleX += -f * 1.5F;
         }
      }

   }

   private void animateScream(int fullTick) {
      if (fullTick < 35) {
         float tick = ((float)fullTick + this.animTick) / 35.0F;
         float f = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var10000 = this.abdomen;
         var10000.rotateAngleX += f * 0.3F;
         var10000 = this.chest;
         var10000.rotateAngleX += f * 0.4F;
         var10000 = this.neck;
         var10000.rotateAngleX += f * 0.2F;
         var10000 = this.head;
         var10000.rotateAngleX += f * 0.3F;
         var10000 = this.rightArm.arm;
         var10000.rotateAngleX += -f * 0.6F;
         var10000 = this.rightArm.arm;
         var10000.rotateAngleY += f * 0.4F;
         var10000 = this.rightArm.forearm;
         var10000.rotateAngleX += -f * 0.8F;
         var10000 = this.rightArm.hand;
         var10000.rotateAngleZ += -f * 0.4F;

         for(int i = 0; i < 3; ++i) {
            var10000 = this.rightArm.finger[i];
            var10000.rotateAngleZ += f * 0.3F;
            var10000 = this.rightArm.foreFinger[i];
            var10000.rotateAngleZ += -f * 0.5F;
         }

         var10000 = this.leftArm.arm;
         var10000.rotateAngleX += -f * 0.6F;
         var10000 = this.leftArm.arm;
         var10000.rotateAngleY += -f * 0.4F;
         var10000 = this.leftArm.forearm;
         var10000.rotateAngleX += -f * 0.8F;
         var10000 = this.leftArm.hand;
         var10000.rotateAngleZ += f * 0.4F;

         for(int i = 0; i < 3; ++i) {
            var10000 = this.leftArm.finger[i];
            var10000.rotateAngleZ += -f * 0.3F;
            var10000 = this.leftArm.foreFinger[i];
            var10000.rotateAngleZ += f * 0.5F;
         }

         var10000 = this.lowerRightArm.arm;
         var10000.rotateAngleX += -f * 0.4F;
         var10000 = this.lowerRightArm.arm;
         var10000.rotateAngleY += f * 0.2F;
         var10000 = this.lowerRightArm.forearm;
         var10000.rotateAngleX += -f * 0.8F;
         var10000 = this.lowerRightArm.hand;
         var10000.rotateAngleZ += -f * 0.4F;

         for(int i = 0; i < 3; ++i) {
            var10000 = this.lowerRightArm.finger[i];
            var10000.rotateAngleZ += f * 0.3F;
            var10000 = this.lowerRightArm.foreFinger[i];
            var10000.rotateAngleZ += -f * 0.5F;
         }

         var10000 = this.lowerLeftArm.arm;
         var10000.rotateAngleX += -f * 0.4F;
         var10000 = this.lowerLeftArm.arm;
         var10000.rotateAngleY += -f * 0.2F;
         var10000 = this.lowerLeftArm.forearm;
         var10000.rotateAngleX += -f * 0.8F;
         var10000 = this.lowerLeftArm.hand;
         var10000.rotateAngleZ += f * 0.4F;

         for(int i = 0; i < 3; ++i) {
            var10000 = this.lowerLeftArm.finger[i];
            var10000.rotateAngleZ += -f * 0.3F;
            var10000 = this.lowerLeftArm.foreFinger[i];
            var10000.rotateAngleZ += f * 0.5F;
         }
      } else if (fullTick < 40) {
         ModelRenderer var50 = this.abdomen;
         var50.rotateAngleX += 0.3F;
         var50 = this.chest;
         var50.rotateAngleX += 0.4F;
         var50 = this.neck;
         var50.rotateAngleX += 0.2F;
         var50 = this.head;
         var50.rotateAngleX += 0.3F;
         var50 = this.rightArm.arm;
         var50.rotateAngleX += -0.6F;
         var50 = this.rightArm.arm;
         var50.rotateAngleY += 0.4F;
         var50 = this.rightArm.forearm;
         var50.rotateAngleX += -0.8F;
         var50 = this.rightArm.hand;
         var50.rotateAngleZ += -0.4F;

         for(int i = 0; i < 3; ++i) {
            var50 = this.rightArm.finger[i];
            var50.rotateAngleZ += 0.3F;
            var50 = this.rightArm.foreFinger[i];
            var50.rotateAngleZ += -0.5F;
         }

         var50 = this.leftArm.arm;
         var50.rotateAngleX += -0.6F;
         var50 = this.leftArm.arm;
         var50.rotateAngleY += -0.4F;
         var50 = this.leftArm.forearm;
         var50.rotateAngleX += -0.8F;
         var50 = this.leftArm.hand;
         var50.rotateAngleZ += 0.4F;

         for(int i = 0; i < 3; ++i) {
            var50 = this.leftArm.finger[i];
            var50.rotateAngleZ += -0.3F;
            var50 = this.leftArm.foreFinger[i];
            var50.rotateAngleZ += 0.5F;
         }

         var50 = this.lowerRightArm.arm;
         var50.rotateAngleX += -0.4F;
         var50 = this.lowerRightArm.arm;
         var50.rotateAngleY += 0.2F;
         var50 = this.lowerRightArm.forearm;
         var50.rotateAngleX += -0.8F;
         var50 = this.lowerRightArm.hand;
         var50.rotateAngleZ += -0.4F;

         for(int i = 0; i < 3; ++i) {
            var50 = this.lowerRightArm.finger[i];
            var50.rotateAngleZ += 0.3F;
            var50 = this.lowerRightArm.foreFinger[i];
            var50.rotateAngleZ += -0.5F;
         }

         var50 = this.lowerLeftArm.arm;
         var50.rotateAngleX += -0.4F;
         var50 = this.lowerLeftArm.arm;
         var50.rotateAngleY += -0.2F;
         var50 = this.lowerLeftArm.forearm;
         var50.rotateAngleX += -0.8F;
         var50 = this.lowerLeftArm.hand;
         var50.rotateAngleZ += 0.4F;

         for(int i = 0; i < 3; ++i) {
            var50 = this.lowerLeftArm.finger[i];
            var50.rotateAngleZ += -0.3F;
            var50 = this.lowerLeftArm.foreFinger[i];
            var50.rotateAngleZ += 0.5F;
         }
      } else if (fullTick < 44) {
         float tick = ((float)(fullTick - 40) + this.animTick) / 4.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         float f1 = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var78 = this.abdomen;
         var78.rotateAngleX += -f * 0.1F + 0.4F;
         var78 = this.chest;
         var78.rotateAngleX += f * 0.1F + 0.3F;
         var78 = this.chest;
         var78.rotateAngleZ += f1 * 0.5F;
         var78 = this.neck;
         var78.rotateAngleX += f * 0.2F;
         var78 = this.neck;
         var78.rotateAngleZ += f1 * 0.2F;
         var78 = this.head;
         var78.rotateAngleX += f * 1.2F - 0.8F;
         var78 = this.head;
         var78.rotateAngleZ += f1 * 0.4F;
         var78 = this.mouth;
         var78.rotateAngleX += f1 * 0.6F;
         var78 = this.rightArm.arm;
         var78.rotateAngleX += -f * 0.6F;
         var78 = this.rightArm.arm;
         var78.rotateAngleY += 0.4F;
         var78 = this.rightArm.forearm;
         var78.rotateAngleX += -f * 0.8F;
         var78 = this.rightArm.hand;
         var78.rotateAngleZ += -f * 0.4F;

         for(int i = 0; i < 3; ++i) {
            var78 = this.rightArm.finger[i];
            var78.rotateAngleZ += f * 0.3F;
            var78 = this.rightArm.foreFinger[i];
            var78.rotateAngleZ += -f * 0.5F;
         }

         var78 = this.leftArm.arm;
         var78.rotateAngleX += -f * 0.6F;
         var78 = this.leftArm.arm;
         var78.rotateAngleY += -0.4F;
         var78 = this.leftArm.forearm;
         var78.rotateAngleX += -f * 0.8F;
         var78 = this.leftArm.hand;
         var78.rotateAngleZ += f * 0.4F;

         for(int i = 0; i < 3; ++i) {
            var78 = this.leftArm.finger[i];
            var78.rotateAngleZ += -f * 0.3F;
            var78 = this.leftArm.foreFinger[i];
            var78.rotateAngleZ += f * 0.5F;
         }

         var78 = this.lowerRightArm.arm;
         var78.rotateAngleX += -f * 0.4F;
         var78 = this.lowerRightArm.arm;
         var78.rotateAngleY += -f * 0.1F + 0.3F;
         var78 = this.lowerRightArm.forearm;
         var78.rotateAngleX += -f * 0.8F;
         var78 = this.lowerRightArm.hand;
         var78.rotateAngleZ += -f * 0.4F;

         for(int i = 0; i < 3; ++i) {
            var78 = this.lowerRightArm.finger[i];
            var78.rotateAngleZ += f * 0.3F;
            var78 = this.lowerRightArm.foreFinger[i];
            var78.rotateAngleZ += -f * 0.5F;
         }

         var78 = this.lowerLeftArm.arm;
         var78.rotateAngleX += -f * 0.4F;
         var78 = this.lowerLeftArm.arm;
         var78.rotateAngleY += f * 0.1F - 0.3F;
         var78 = this.lowerLeftArm.forearm;
         var78.rotateAngleX += -f * 0.8F;
         var78 = this.lowerLeftArm.hand;
         var78.rotateAngleZ += f * 0.4F;

         for(int i = 0; i < 3; ++i) {
            var78 = this.lowerLeftArm.finger[i];
            var78.rotateAngleZ += -f * 0.3F;
            var78 = this.lowerLeftArm.foreFinger[i];
            var78.rotateAngleZ += f * 0.5F;
         }

         var78 = this.leg1;
         var78.rotateAngleZ += f1 * 0.1F;
         var78 = this.leg2;
         var78.rotateAngleZ += -f1 * 0.1F;
      } else if (fullTick < 155) {
         float tick = ((float)(fullTick - 44) + this.animTick) / 111.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         ModelRenderer var112 = this.abdomen;
         var112.rotateAngleX += 0.4F;
         var112 = this.chest;
         var112.rotateAngleX += 0.3F;
         var112 = this.chest;
         var112.rotateAngleZ += f * 1.0F - 0.5F;
         var112 = this.neck;
         var112.rotateAngleZ += f * 0.4F - 0.2F;
         var112 = this.head;
         var112.rotateAngleX += -0.8F;
         var112 = this.head;
         var112.rotateAngleZ += f * 0.8F - 0.4F;
         var112 = this.mouth;
         var112.rotateAngleX += 0.6F;
         var112 = this.rightArm.arm;
         var112.rotateAngleY += 0.4F;
         var112 = this.leftArm.arm;
         var112.rotateAngleY += -0.4F;
         var112 = this.lowerRightArm.arm;
         var112.rotateAngleY += 0.3F;
         var112 = this.lowerLeftArm.arm;
         var112.rotateAngleY += -0.3F;
         var112 = this.leg1;
         var112.rotateAngleZ += 0.1F;
         var112 = this.leg2;
         var112.rotateAngleZ += -0.1F;
      } else if (fullTick < 160) {
         float tick = ((float)(fullTick - 155) + this.animTick) / 5.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         ModelRenderer var125 = this.abdomen;
         var125.rotateAngleX += f * 0.4F;
         var125 = this.chest;
         var125.rotateAngleX += f * 0.3F;
         var125 = this.chest;
         var125.rotateAngleZ += -f * 0.5F;
         var125 = this.neck;
         var125.rotateAngleZ += -f * 0.2F;
         var125 = this.head;
         var125.rotateAngleX += -f * 0.8F;
         var125 = this.head;
         var125.rotateAngleZ += -f * 0.4F;
         var125 = this.mouth;
         var125.rotateAngleX += f * 0.6F;
         var125 = this.rightArm.arm;
         var125.rotateAngleY += f * 0.4F;
         var125 = this.leftArm.arm;
         var125.rotateAngleY += -f * 0.4F;
         var125 = this.lowerRightArm.arm;
         var125.rotateAngleY += f * 0.3F;
         var125 = this.lowerLeftArm.arm;
         var125.rotateAngleY += -f * 0.3F;
         var125 = this.leg1;
         var125.rotateAngleZ += f * 0.1F;
         var125 = this.leg2;
         var125.rotateAngleZ += -f * 0.1F;
      }

   }

   private void animateTeleSmash(int fullTick) {
      if (fullTick < 18) {
         float tick = ((float)fullTick + this.animTick) / 18.0F;
         float f = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var10000 = this.chest;
         var10000.rotateAngleX += -f * 0.3F;
         var10000 = this.rightArm.arm;
         var10000.rotateAngleY += f * 0.2F;
         var10000 = this.rightArm.arm;
         var10000.rotateAngleZ += f * 0.8F;
         var10000 = this.rightArm.hand;
         var10000.rotateAngleY += f * 1.7F;
         var10000 = this.leftArm.arm;
         var10000.rotateAngleY += -f * 0.2F;
         var10000 = this.leftArm.arm;
         var10000.rotateAngleZ += -f * 0.8F;
         var10000 = this.leftArm.hand;
         var10000.rotateAngleY += -f * 1.7F;
         var10000 = this.lowerRightArm.arm;
         var10000.rotateAngleY += f * 0.2F;
         var10000 = this.lowerRightArm.arm;
         var10000.rotateAngleZ += f * 0.6F;
         var10000 = this.lowerRightArm.hand;
         var10000.rotateAngleY += f * 1.7F;
         var10000 = this.lowerLeftArm.arm;
         var10000.rotateAngleY += -f * 0.2F;
         var10000 = this.lowerLeftArm.arm;
         var10000.rotateAngleZ += -f * 0.6F;
         var10000 = this.lowerLeftArm.hand;
         var10000.rotateAngleY += -f * 1.7F;
      } else if (fullTick < 20) {
         float tick = ((float)(fullTick - 18) + this.animTick) / 2.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         float f1 = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var21 = this.chest;
         var21.rotateAngleX += -f * 0.3F;
         var21 = this.rightArm.arm;
         var21.rotateAngleX += -f1 * 0.8F;
         var21 = this.rightArm.arm;
         var21.rotateAngleY += 0.2F;
         var21 = this.rightArm.arm;
         var21.rotateAngleZ += 0.8F;
         ++this.rightArm.hand.rotateAngleY;
         var21 = this.leftArm.arm;
         var21.rotateAngleX += -f1 * 0.8F;
         var21 = this.leftArm.arm;
         var21.rotateAngleY += -0.2F;
         var21 = this.leftArm.arm;
         var21.rotateAngleZ += -0.8F;
         var21 = this.leftArm.hand;
         var21.rotateAngleY += -1.7F;
         var21 = this.lowerRightArm.arm;
         var21.rotateAngleX += -f1 * 0.9F;
         var21 = this.lowerRightArm.arm;
         var21.rotateAngleY += 0.2F;
         var21 = this.lowerRightArm.arm;
         var21.rotateAngleZ += 0.6F;
         ++this.lowerRightArm.hand.rotateAngleY;
         var21 = this.lowerLeftArm.arm;
         var21.rotateAngleX += -f1 * 0.9F;
         var21 = this.lowerLeftArm.arm;
         var21.rotateAngleY += -0.2F;
         var21 = this.lowerLeftArm.arm;
         var21.rotateAngleZ += -0.6F;
         var21 = this.lowerLeftArm.hand;
         var21.rotateAngleY += -1.7F;
      } else if (fullTick < 24) {
         EndermanArm.EnderArmRenderer var36 = this.rightArm.arm;
         var36.rotateAngleX += -0.8F;
         var36 = this.rightArm.arm;
         var36.rotateAngleY += 0.2F;
         var36 = this.rightArm.arm;
         var36.rotateAngleZ += 0.8F;
         ++this.rightArm.hand.rotateAngleY;
         var36 = this.leftArm.arm;
         var36.rotateAngleX += -0.8F;
         var36 = this.leftArm.arm;
         var36.rotateAngleY += -0.2F;
         var36 = this.leftArm.arm;
         var36.rotateAngleZ += -0.8F;
         ModelRenderer var42 = this.leftArm.hand;
         var42.rotateAngleY += -1.7F;
         var42 = this.lowerRightArm.arm;
         var42.rotateAngleX += -0.9F;
         var42 = this.lowerRightArm.arm;
         var42.rotateAngleY += 0.2F;
         var42 = this.lowerRightArm.arm;
         var42.rotateAngleZ += 0.6F;
         ++this.lowerRightArm.hand.rotateAngleY;
         var42 = this.lowerLeftArm.arm;
         var42.rotateAngleX += -0.9F;
         var42 = this.lowerLeftArm.arm;
         var42.rotateAngleY += -0.2F;
         var42 = this.lowerLeftArm.arm;
         var42.rotateAngleZ += -0.6F;
         var42 = this.lowerLeftArm.hand;
         var42.rotateAngleY += -1.7F;
      } else if (fullTick < 30) {
         float tick = ((float)(fullTick - 24) + this.animTick) / 6.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         EndermanArm.EnderArmRenderer var50 = this.rightArm.arm;
         var50.rotateAngleX += -f * 0.8F;
         var50 = this.rightArm.arm;
         var50.rotateAngleY += f * 0.2F;
         var50 = this.rightArm.arm;
         var50.rotateAngleZ += f * 0.8F;
         ModelRenderer var53 = this.rightArm.hand;
         var53.rotateAngleY += f * 1.7F;
         var53 = this.leftArm.arm;
         var53.rotateAngleX += -f * 0.8F;
         var53 = this.leftArm.arm;
         var53.rotateAngleY += -f * 0.2F;
         var53 = this.leftArm.arm;
         var53.rotateAngleZ += -f * 0.8F;
         var53 = this.leftArm.hand;
         var53.rotateAngleY += -f * 1.7F;
         var53 = this.lowerRightArm.arm;
         var53.rotateAngleX += -f * 0.9F;
         var53 = this.lowerRightArm.arm;
         var53.rotateAngleY += f * 0.2F;
         var53 = this.lowerRightArm.arm;
         var53.rotateAngleZ += f * 0.6F;
         var53 = this.lowerRightArm.hand;
         var53.rotateAngleY += f * 1.7F;
         var53 = this.lowerLeftArm.arm;
         var53.rotateAngleX += -f * 0.9F;
         var53 = this.lowerLeftArm.arm;
         var53.rotateAngleY += -f * 0.2F;
         var53 = this.lowerLeftArm.arm;
         var53.rotateAngleZ += -f * 0.6F;
         var53 = this.lowerLeftArm.hand;
         var53.rotateAngleY += -f * 1.7F;
      }

   }

   private void animateDeath(int deathTick) {
      if (deathTick < 80) {
         float tick = ((float)deathTick + this.animTick) / 80.0F;
         float f = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var10000 = this.head;
         var10000.rotateAngleX += f * 0.4F;
         var10000 = this.neck;
         var10000.rotateAngleX += f * 0.3F;
         var10000 = this.pelvis;
         var10000.rotationPointY += -f * 12.0F;
         var10000 = this.rightArm.arm;
         var10000.rotateAngleX += -f * 0.4F;
         var10000 = this.rightArm.arm;
         var10000.rotateAngleY += f * 0.4F;
         var10000 = this.rightArm.arm;
         var10000.rotateAngleZ += f * 0.6F;
         var10000 = this.rightArm.forearm;
         var10000.rotateAngleX += -f * 1.2F;
         var10000 = this.leftArm.arm;
         var10000.rotateAngleX += -f * 0.4F;
         var10000 = this.leftArm.arm;
         var10000.rotateAngleY += -f * 0.2F;
         var10000 = this.leftArm.arm;
         var10000.rotateAngleZ += -f * 0.6F;
         var10000 = this.leftArm.forearm;
         var10000.rotateAngleX += -f * 1.2F;
         var10000 = this.lowerRightArm.arm;
         var10000.rotateAngleX += -f * 0.4F;
         var10000 = this.lowerRightArm.arm;
         var10000.rotateAngleY += f * 0.4F;
         var10000 = this.lowerRightArm.arm;
         var10000.rotateAngleZ += f * 0.6F;
         var10000 = this.lowerRightArm.forearm;
         var10000.rotateAngleX += -f * 1.2F;
         var10000 = this.lowerLeftArm.arm;
         var10000.rotateAngleX += -f * 0.4F;
         var10000 = this.lowerLeftArm.arm;
         var10000.rotateAngleY += -f * 0.2F;
         var10000 = this.lowerLeftArm.arm;
         var10000.rotateAngleZ += -f * 0.6F;
         var10000 = this.lowerLeftArm.forearm;
         var10000.rotateAngleX += -f * 1.2F;
         var10000 = this.leg1;
         var10000.rotateAngleX += -f * 0.9F;
         var10000 = this.leg1;
         var10000.rotateAngleY += f * 0.3F;
         var10000 = this.leg2;
         var10000.rotateAngleX += -f * 0.9F;
         var10000 = this.leg2;
         var10000.rotateAngleY += -f * 0.3F;
         var10000 = this.foreleg1;
         var10000.rotateAngleX += f * 1.6F;
         var10000 = this.foreleg2;
         var10000.rotateAngleX += f * 1.6F;
      } else if (deathTick < 84) {
         float tick = ((float)(deathTick - 80) + this.animTick) / 4.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         float f1 = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var31 = this.head;
         var31.rotateAngleX += f * 0.4F;
         var31 = this.neck;
         var31.rotateAngleX += f * 0.4F - 0.1F;
         var31 = this.chest;
         var31.rotateAngleX += -f1 * 0.8F;
         var31 = this.abdomen;
         var31.rotateAngleX += -f1 * 0.2F;
         var31 = this.pelvis;
         var31.rotationPointY += -12.0F;
         var31 = this.rightArm.arm;
         var31.rotateAngleX += -f * 0.4F;
         var31 = this.rightArm.arm;
         var31.rotateAngleY += -f * 1.4F + 1.8F;
         var31 = this.rightArm.arm;
         var31.rotateAngleZ += f * 0.6F;
         var31 = this.rightArm.forearm;
         var31.rotateAngleX += -f * 1.2F;
         var31 = this.leftArm.arm;
         var31.rotateAngleX += -f * 0.4F;
         var31 = this.leftArm.arm;
         var31.rotateAngleY += f * 1.6F - 1.8F;
         var31 = this.leftArm.arm;
         var31.rotateAngleZ += -f * 0.6F;
         var31 = this.leftArm.forearm;
         var31.rotateAngleX += -f * 1.2F;
         var31 = this.lowerRightArm.arm;
         var31.rotateAngleX += -f * 0.5F + 0.1F;
         var31 = this.lowerRightArm.arm;
         var31.rotateAngleY += -f * 1.1F + 1.5F;
         var31 = this.lowerRightArm.arm;
         var31.rotateAngleZ += f * 0.6F;
         var31 = this.lowerRightArm.forearm;
         var31.rotateAngleX += -f * 1.2F;
         var31 = this.lowerLeftArm.arm;
         var31.rotateAngleX += -f * 0.5F + 0.1F;
         var31 = this.lowerLeftArm.arm;
         var31.rotateAngleY += f * 1.1F - 1.5F;
         var31 = this.lowerLeftArm.arm;
         var31.rotateAngleZ += -f * 0.6F;
         var31 = this.lowerLeftArm.forearm;
         var31.rotateAngleX += -f * 1.2F;
         var31 = this.leg1;
         var31.rotateAngleX += -f * 1.7F + 0.8F;
         var31 = this.leg1;
         var31.rotateAngleY += f * 0.3F;
         var31 = this.leg1;
         var31.rotateAngleZ += f1 * 0.2F;
         var31 = this.leg2;
         var31.rotateAngleX += -f * 1.7F + 0.8F;
         var31 = this.leg2;
         var31.rotateAngleY += -f * 0.3F;
         var31 = this.leg2;
         var31.rotateAngleZ += -f1 * 0.2F;
         var31 = this.foreleg1;
         var31.rotateAngleX += f * 1.6F;
         var31 = this.foreleg2;
         var31.rotateAngleX += f * 1.6F;
      } else {
         ModelRenderer var60 = this.neck;
         var60.rotateAngleX += -0.1F;
         var60 = this.chest;
         var60.rotateAngleX += -0.8F;
         var60 = this.abdomen;
         var60.rotateAngleX += -0.2F;
         var60 = this.pelvis;
         var60.rotationPointY += -12.0F;
         ++this.rightArm.arm.rotateAngleY;
         var60 = this.leftArm.arm;
         var60.rotateAngleY += -1.8F;
         var60 = this.lowerRightArm.arm;
         var60.rotateAngleX += 0.1F;
         ++this.lowerRightArm.arm.rotateAngleY;
         var60 = this.lowerLeftArm.arm;
         var60.rotateAngleX += 0.1F;
         var60 = this.lowerLeftArm.arm;
         var60.rotateAngleY += -1.5F;
         var60 = this.leg1;
         var60.rotateAngleX += 0.8F;
         var60 = this.leg1;
         var60.rotateAngleZ += 0.2F;
         var60 = this.leg2;
         var60.rotateAngleX += 0.8F;
         var60 = this.leg2;
         var60.rotateAngleZ += -0.2F;
      }

   }

   public EndermanArm getArmFromID(int armID) {
      return armID == 1 ? this.rightArm : (armID == 2 ? this.leftArm : (armID == 3 ? this.lowerRightArm : this.lowerLeftArm));
   }
}
