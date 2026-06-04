package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import thehippomaster.AnimationAPI.IAnimatedEntity;
import thehippomaster.AnimationAPI.client.Animator;
import thehippomaster.AnimationAPI.client.ModelJoint;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.MutantSkeleton;

@SideOnly(Side.CLIENT)
public class ModelMutantSkeleton extends ModelBase {
   public ModelRenderer skeleBase;
   public ModelRenderer pelvis;
   public ModelRenderer waist;
   public SkeletonSpine[] spine;
   public ModelRenderer neck;
   public ModelJoint head;
   public ModelRenderer jaw;
   public ModelRenderer shoulder1;
   public ModelRenderer shoulder2;
   public ModelJoint arm1;
   public ModelJoint arm2;
   public ModelJoint forearm1;
   public ModelJoint forearm2;
   public ModelJoint leg1;
   public ModelJoint leg2;
   public ModelJoint foreleg1;
   public ModelJoint foreleg2;
   public SkeletonBow bow;
   protected float partialTick;
   private Animator animator;
   public static final float PI = (float)Math.PI;

   public ModelMutantSkeleton() {
      this.textureWidth = 128;
      this.textureHeight = 128;
      this.partialTick = 0.0F;
      this.skeleBase = new ModelRenderer(this);
      this.skeleBase.setRotationPoint(0.0F, 3.0F, 0.0F);
      this.pelvis = new ModelRenderer(this, 0, 16);
      this.pelvis.addBox(-4.0F, -6.0F, -3.0F, 8, 6, 6);
      this.skeleBase.addChild(this.pelvis);
      this.waist = new ModelRenderer(this, 32, 0);
      this.waist.addBox(-2.5F, -8.0F, -2.0F, 5, 8, 4);
      this.waist.setRotationPoint(0.0F, -5.0F, 0.0F);
      this.pelvis.addChild(this.waist);
      this.spine = new SkeletonSpine[3];
      this.spine[0] = new SkeletonSpine(this);
      this.spine[0].middle.setRotationPoint(0.0F, -7.0F, 0.0F);
      this.waist.addChild(this.spine[0].middle);

      for(int i = 1; i < this.spine.length; ++i) {
         this.spine[i] = new SkeletonSpine(this);
         this.spine[i].middle.setRotationPoint(0.0F, -5.0F, 0.0F);
         this.spine[i - 1].middle.addChild(this.spine[i].middle);
      }

      this.neck = new ModelRenderer(this, 64, 0);
      this.neck.addBox(-1.5F, -4.0F, -1.5F, 3, 4, 3);
      this.neck.setRotationPoint(0.0F, -4.0F, 0.0F);
      this.spine[2].middle.addChild(this.neck);
      this.head = new ModelJoint(this, 0, 0);
      this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.4F);
      this.head.setRotationPoint(0.0F, -4.0F, -1.0F);
      this.neck.addChild(this.head);
      this.jaw = new ModelRenderer(this, 72, 0);
      this.jaw.addBox(-4.0F, -3.0F, -8.0F, 8, 3, 8, 0.7F);
      this.jaw.setRotationPoint(0.0F, -0.2F, 3.5F);
      this.head.addChild(this.jaw);
      this.shoulder1 = new ModelRenderer(this, 28, 16);
      this.shoulder1.addBox(-4.0F, -3.0F, -3.0F, 8, 3, 6);
      this.shoulder1.setRotationPoint(-7.0F, -3.0F, -1.0F);
      this.spine[2].middle.addChild(this.shoulder1);
      this.shoulder2 = new ModelRenderer(this, 28, 16);
      this.shoulder2.mirror = true;
      this.shoulder2.addBox(-4.0F, -3.0F, -3.0F, 8, 3, 6);
      this.shoulder2.setRotationPoint(7.0F, -3.0F, -1.0F);
      this.spine[2].middle.addChild(this.shoulder2);
      this.arm1 = new ModelJoint(this, 0, 28);
      this.arm1.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.arm1.setRotationPoint(-1.0F, -1.0F, 0.0F);
      this.shoulder1.addChild(this.arm1);
      this.arm2 = new ModelJoint(this, 0, 28);
      this.arm2.mirror = true;
      this.arm2.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.arm2.setRotationPoint(1.0F, -1.0F, 0.0F);
      this.shoulder2.addChild(this.arm2);
      this.forearm1 = new ModelJoint(this, 16, 28);
      this.forearm1.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, -0.01F);
      this.forearm1.setRotationPoint(0.0F, 11.0F, 0.0F);
      this.arm1.addChild(this.forearm1);
      this.forearm2 = new ModelJoint(this, 16, 28);
      this.forearm2.mirror = true;
      this.forearm2.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4, -0.01F);
      this.forearm2.setRotationPoint(0.0F, 11.0F, 0.0F);
      this.arm2.addChild(this.forearm2);
      this.leg1 = new ModelJoint(this, 0, 28);
      this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.leg1.setRotationPoint(-2.5F, -2.5F, 0.0F);
      this.pelvis.addChild(this.leg1);
      this.leg2 = new ModelJoint(this, 0, 28);
      this.leg2.mirror = true;
      this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.leg2.setRotationPoint(2.5F, -2.5F, 0.0F);
      this.pelvis.addChild(this.leg2);
      this.foreleg1 = new ModelJoint(this, 32, 28);
      this.foreleg1.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.foreleg1.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.leg1.addChild(this.foreleg1);
      this.foreleg2 = new ModelJoint(this, 32, 28);
      this.foreleg2.mirror = true;
      this.foreleg2.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.foreleg2.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.leg2.addChild(this.foreleg2);
      this.bow = new SkeletonBow(this);
      this.bow.armwear.setRotationPoint(0.0F, 8.0F, 0.0F);
      this.forearm1.addChild(this.bow.armwear);
      this.animator = new Animator(this);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      this.animator.update((IAnimatedEntity)entity);
      this.setAngles();
      this.partialTick = MutantCreatures.proxy.getPartialTicks();
      this.animate((MutantSkeleton)entity, f, f1, f2, f3, f4, f5);
      this.skeleBase.render(f5);
   }

   private void resetAngles(ModelRenderer... boxes) {
      for(ModelRenderer box : boxes) {
         box.rotateAngleX = 0.0F;
         box.rotateAngleY = 0.0F;
         box.rotateAngleZ = 0.0F;
      }

   }

   public void setAngles() {
      this.skeleBase.rotationPointY = 3.0F;
      this.pelvis.rotateAngleX = (-(float)Math.PI / 10F);
      this.waist.rotateAngleX = 0.22439948F;

      for(int i = 0; i < this.spine.length; ++i) {
         this.spine[i].setAngles((float)Math.PI, i == 1);
      }

      this.neck.rotateAngleX = -0.1308997F;
      this.head.rotateAngleX = -0.1308997F;
      this.jaw.rotateAngleX = 0.09817477F;
      this.shoulder1.rotateAngleX = (-(float)Math.PI / 4F);
      this.shoulder2.rotateAngleX = (-(float)Math.PI / 4F);
      this.arm1.getModel().rotateAngleX = ((float)Math.PI / 6F);
      this.arm1.getModel().rotateAngleZ = ((float)Math.PI / 10F);
      this.arm2.getModel().rotateAngleX = ((float)Math.PI / 6F);
      this.arm2.getModel().rotateAngleZ = (-(float)Math.PI / 10F);
      this.forearm1.getModel().rotateAngleX = (-(float)Math.PI / 6F);
      this.forearm2.getModel().rotateAngleX = (-(float)Math.PI / 6F);
      this.leg1.rotateAngleX = -0.2617994F - this.pelvis.rotateAngleX;
      this.leg1.rotateAngleZ = 0.19634955F;
      this.leg2.rotateAngleX = -0.2617994F - this.pelvis.rotateAngleX;
      this.leg2.rotateAngleZ = -0.19634955F;
      this.foreleg1.rotateAngleZ = -0.1308997F;
      this.foreleg1.getModel().rotateAngleX = ((float)Math.PI / 10F);
      this.foreleg2.rotateAngleZ = 0.1308997F;
      this.foreleg2.getModel().rotateAngleX = ((float)Math.PI / 10F);
      this.bow.setAngles((float)Math.PI);
   }

   public void animate(MutantSkeleton skele, float f, float f1, float f2, float f3, float f4, float f5) {
      float walkAnim1 = MathHelper.sin(f * 0.5F);
      float walkAnim2 = MathHelper.sin(f * 0.5F - 1.1F);
      float breatheAnim = MathHelper.sin(f2 * 0.1F);
      float faceYaw = f3 * (float)Math.PI / 180.0F;
      float facePitch = f4 * (float)Math.PI / 180.0F;
      if (skele.currentAttackID == 1) {
         this.animateMelee(skele.animTick);
         this.bow.rotateRope();
         float walkScale = 1.0F - MathHelper.clamp_float((float)skele.animTick / 4.0F, 0.0F, 1.0F);
         walkAnim1 *= walkScale;
         walkAnim2 *= walkScale;
      } else if (skele.currentAttackID == 2) {
         this.animateShoot(skele.animTick, facePitch, faceYaw);
         float scale = 1.0F - MathHelper.clamp_float((float)skele.animTick / 4.0F, 0.0F, 1.0F);
         walkAnim1 *= scale;
         walkAnim2 *= scale;
         facePitch *= scale;
         faceYaw *= scale;
      } else if (skele.currentAttackID == 3) {
         this.animateMultiShoot(skele.animTick, facePitch, faceYaw);
         float scale = 1.0F - MathHelper.clamp_float((float)skele.animTick / 4.0F, 0.0F, 1.0F);
         walkAnim1 *= scale;
         walkAnim2 *= scale;
         facePitch *= scale;
         faceYaw *= scale;
      } else if (this.animator.setAnim(4)) {
         this.animateConstrict();
         this.bow.rotateRope();
         float scale = 1.0F - MathHelper.clamp_float((float)skele.animTick / 6.0F, 0.0F, 1.0F);
         facePitch *= scale;
         faceYaw *= scale;
      } else {
         this.bow.rotateRope();
      }

      ModelRenderer var10000 = this.skeleBase;
      var10000.rotationPointY -= (-0.5F + Math.abs(walkAnim1)) * f1;
      var10000 = this.spine[0].middle;
      var10000.rotateAngleY -= walkAnim1 * 0.06F * f1;
      ModelJoint var19 = this.arm1;
      var19.rotateAngleX -= walkAnim1 * 0.9F * f1;
      var19 = this.arm2;
      var19.rotateAngleX += walkAnim1 * 0.9F * f1;
      var19 = this.leg1;
      var19.rotateAngleX += (0.2F + walkAnim1) * 1.0F * f1;
      var19 = this.leg2;
      var19.rotateAngleX -= (-0.2F + walkAnim1) * 1.0F * f1;
      ModelRenderer var23 = this.foreleg1.getModel();
      var23.rotateAngleX += (0.6F + walkAnim2) * 0.6F * f1;
      var23 = this.foreleg2.getModel();
      var23.rotateAngleX -= (-0.6F + walkAnim2) * 0.6F * f1;

      for(int i = 0; i < this.spine.length; ++i) {
         this.spine[i].animate(breatheAnim);
      }

      ModelJoint var25 = this.head;
      var25.rotateAngleX -= breatheAnim * 0.02F;
      ModelRenderer var26 = this.jaw;
      var26.rotateAngleX += breatheAnim * 0.04F + 0.04F;
      ModelJoint var27 = this.arm1;
      var27.rotateAngleZ += breatheAnim * 0.025F;
      var27 = this.arm2;
      var27.rotateAngleZ -= breatheAnim * 0.025F;
      ModelRenderer var29 = this.head.getModel();
      var29.rotateAngleX += facePitch;
      var29 = this.head.getModel();
      var29.rotateAngleY += faceYaw;
   }

   protected void animateMelee(int fullTick) {
      if (fullTick < 3) {
         float tick = ((float)fullTick + this.partialTick) / 3.0F;
         float f = MathHelper.sin(tick * (float)Math.PI / 2.0F);

         for(int i = 0; i < this.spine.length; ++i) {
            ModelRenderer var10000 = this.spine[i].middle;
            var10000.rotateAngleY += f * (float)Math.PI / 16.0F;
         }

         ModelJoint var12 = this.arm1;
         var12.rotateAngleY += f * (float)Math.PI / 10.0F;
         var12 = this.arm1;
         var12.rotateAngleZ += f * (float)Math.PI / 4.0F;
         var12 = this.arm2;
         var12.rotateAngleZ += f * -(float)Math.PI / 16.0F;
      } else if (fullTick < 5) {
         float tick = ((float)(fullTick - 3) + this.partialTick) / 2.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);

         for(int i = 0; i < this.spine.length; ++i) {
            ModelRenderer var15 = this.spine[i].middle;
            var15.rotateAngleY += f * 0.5890486F - ((float)Math.PI / 8F);
         }

         ModelJoint var16 = this.arm1;
         var16.rotateAngleY += f * 2.7307692F - 2.41661F;
         var16 = this.arm1;
         var16.rotateAngleZ += f * 1.1780972F - ((float)Math.PI / 8F);
         var16 = this.arm2;
         var16.rotateAngleZ += -0.19634955F;
      } else if (fullTick < 8) {
         for(int i = 0; i < this.spine.length; ++i) {
            ModelRenderer var19 = this.spine[i].middle;
            var19.rotateAngleY += (-(float)Math.PI / 8F);
         }

         ModelJoint var20 = this.arm1;
         var20.rotateAngleY += -2.41661F;
         var20 = this.arm1;
         var20.rotateAngleZ += (-(float)Math.PI / 8F);
         var20 = this.arm2;
         var20.rotateAngleZ += -0.19634955F;
      } else if (fullTick < 14) {
         float tick = ((float)(fullTick - 8) + this.partialTick) / 6.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);

         for(int i = 0; i < this.spine.length; ++i) {
            ModelRenderer var23 = this.spine[i].middle;
            var23.rotateAngleY += f * -(float)Math.PI / 8.0F;
         }

         ModelJoint var24 = this.arm1;
         var24.rotateAngleY += f * -(float)Math.PI / 1.3F;
         var24 = this.arm1;
         var24.rotateAngleZ += f * -(float)Math.PI / 8.0F;
         var24 = this.arm2;
         var24.rotateAngleZ += f * -(float)Math.PI / 16.0F;
      }

   }

   protected void animateShoot(int fullTick, float facePitch, float faceYaw) {
      if (fullTick < 5) {
         float tick = ((float)fullTick + this.partialTick) / 5.0F;
         float f = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var10000 = this.arm1.getModel();
         var10000.rotateAngleX += -f * (float)Math.PI / 4.0F;
         ModelJoint var17 = this.arm1;
         var17.rotateAngleY += -f * (float)Math.PI / 2.0F;
         var17 = this.arm1;
         var17.rotateAngleZ += f * (float)Math.PI / 16.0F;
         var17 = this.forearm1;
         var17.rotateAngleX += f * (float)Math.PI / 7.0F;
         ModelRenderer var20 = this.arm2.getModel();
         var20.rotateAngleX += -f * (float)Math.PI / 4.0F;
         ModelJoint var21 = this.arm2;
         var21.rotateAngleY += f * (float)Math.PI / 2.0F;
         var21 = this.arm2;
         var21.rotateAngleZ += -f * (float)Math.PI / 16.0F;
         ModelRenderer var23 = this.arm2.getModel();
         var23.rotateAngleZ += -f * (float)Math.PI / 8.0F;
         ModelJoint var24 = this.forearm2;
         var24.rotateAngleX += -f * (float)Math.PI / 6.0F;
         this.bow.rotateRope();
      } else if (fullTick < 12) {
         float tick = ((float)(fullTick - 5) + this.partialTick) / 7.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         float f1 = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         float f1s = MathHelper.sin(tick * (float)Math.PI / 2.0F * 0.4F);
         ModelRenderer var25 = this.head.getModel();
         var25.rotateAngleY += f1 * (float)Math.PI / 4.0F;

         for(int i = 0; i < this.spine.length; ++i) {
            var25 = this.spine[i].middle;
            var25.rotateAngleY += -f1 * (float)Math.PI / 12.0F;
            var25 = this.spine[i].middle;
            var25.rotateAngleX += f1 * facePitch / 3.0F;
            var25 = this.spine[i].middle;
            var25.rotateAngleY += f1 * faceYaw / 3.0F;
         }

         var25 = this.arm1.getModel();
         var25.rotateAngleX += f * 0.2617994F - ((float)Math.PI / 3F);
         ModelJoint var30 = this.arm1;
         var30.rotateAngleY += f * -0.9424778F - ((float)Math.PI / 5F);
         var30 = this.arm1;
         var30.rotateAngleZ += f * -0.850848F + ((float)Math.PI / 3F);
         var30 = this.forearm1;
         var30.rotateAngleX += 0.44879895F;
         ModelRenderer var33 = this.arm2.getModel();
         var33.rotateAngleX += f * 1.8325956F - 2.6179938F;
         ModelJoint var34 = this.arm2;
         var34.rotateAngleY += f * 0.9424778F + ((float)Math.PI / 5F);
         var34 = this.arm2;
         var34.rotateAngleZ += f * 0.850848F - ((float)Math.PI / 3F);
         ModelRenderer var36 = this.arm2.getModel();
         var36.rotateAngleZ += -f * (float)Math.PI / 8.0F;
         ModelJoint var37 = this.forearm2;
         var37.rotateAngleX += f * 0.10471976F - ((float)Math.PI / 5F);
         ModelRenderer var38 = this.bow.middle1;
         var38.rotateAngleX += -f1s * (float)Math.PI / 16.0F;
         var38 = this.bow.side1;
         var38.rotateAngleX += -f1s * (float)Math.PI / 24.0F;
         var38 = this.bow.middle2;
         var38.rotateAngleX += f1s * (float)Math.PI / 16.0F;
         var38 = this.bow.side2;
         var38.rotateAngleX += f1s * (float)Math.PI / 24.0F;
         this.bow.rotateRope();
         var38 = this.bow.rope1;
         var38.rotateAngleX += f1s * (float)Math.PI / 6.0F;
         var38 = this.bow.rope2;
         var38.rotateAngleX += -f1s * (float)Math.PI / 6.0F;
      } else if (fullTick < 26) {
         ModelRenderer var44 = this.head.getModel();
         var44.rotateAngleY += ((float)Math.PI / 4F);

         for(int i = 0; i < this.spine.length; ++i) {
            var44 = this.spine[i].middle;
            var44.rotateAngleY += -0.2617994F;
            var44 = this.spine[i].middle;
            var44.rotateAngleX += facePitch / 3.0F;
            var44 = this.spine[i].middle;
            var44.rotateAngleY += faceYaw / 3.0F;
         }

         var44 = this.arm1.getModel();
         var44.rotateAngleX += (-(float)Math.PI / 3F);
         ModelJoint var49 = this.arm1;
         var49.rotateAngleY += (-(float)Math.PI / 5F);
         ++this.arm1.rotateAngleZ;
         var49 = this.forearm1;
         var49.rotateAngleX += 0.44879895F;
         ModelRenderer var51 = this.arm2.getModel();
         var51.rotateAngleX += -2.6179938F;
         ModelJoint var52 = this.arm2;
         var52.rotateAngleY += ((float)Math.PI / 5F);
         var52 = this.arm2;
         var52.rotateAngleZ += (-(float)Math.PI / 3F);
         var52 = this.forearm2;
         var52.rotateAngleX += (-(float)Math.PI / 5F);
         float tick = MathHelper.clamp_float((float)(fullTick - 25) + this.partialTick, 0.0F, 1.0F);
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         ModelRenderer var55 = this.bow.middle1;
         var55.rotateAngleX += -f * (float)Math.PI / 16.0F;
         var55 = this.bow.side1;
         var55.rotateAngleX += -f * (float)Math.PI / 24.0F;
         var55 = this.bow.middle2;
         var55.rotateAngleX += f * (float)Math.PI / 16.0F;
         var55 = this.bow.side2;
         var55.rotateAngleX += f * (float)Math.PI / 24.0F;
         this.bow.rotateRope();
         var55 = this.bow.rope1;
         var55.rotateAngleX += f * (float)Math.PI / 6.0F;
         var55 = this.bow.rope2;
         var55.rotateAngleX += -f * (float)Math.PI / 6.0F;
      } else if (fullTick < 30) {
         float tick = ((float)(fullTick - 26) + this.partialTick) / 4.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         ModelRenderer var61 = this.head.getModel();
         var61.rotateAngleY += f * (float)Math.PI / 4.0F;

         for(int i = 0; i < this.spine.length; ++i) {
            var61 = this.spine[i].middle;
            var61.rotateAngleY += -f * (float)Math.PI / 12.0F;
            var61 = this.spine[i].middle;
            var61.rotateAngleX += f * facePitch / 3.0F;
            var61 = this.spine[i].middle;
            var61.rotateAngleY += f * faceYaw / 3.0F;
         }

         var61 = this.arm1.getModel();
         var61.rotateAngleX += -f * (float)Math.PI / 3.0F;
         ModelJoint var66 = this.arm1;
         var66.rotateAngleY += -f * (float)Math.PI / 5.0F;
         var66 = this.arm1;
         var66.rotateAngleZ += f * (float)Math.PI / 3.0F;
         var66 = this.forearm1;
         var66.rotateAngleX += f * (float)Math.PI / 7.0F;
         ModelRenderer var69 = this.arm2.getModel();
         var69.rotateAngleX += -f * (float)Math.PI / 1.2F;
         ModelJoint var70 = this.arm2;
         var70.rotateAngleY += f * (float)Math.PI / 5.0F;
         var70 = this.arm2;
         var70.rotateAngleZ += -f * (float)Math.PI / 3.0F;
         var70 = this.forearm2;
         var70.rotateAngleX += -f * (float)Math.PI / 5.0F;
         this.bow.rotateRope();
      }

   }

   protected void animateMultiShoot(int fullTick, float facePitch, float faceYaw) {
      if (fullTick < 10) {
         float tick = ((float)fullTick + this.partialTick) / 10.0F;
         float f = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var10000 = this.skeleBase;
         var10000.rotationPointY += f * 3.5F;
         var10000 = this.spine[0].middle;
         var10000.rotateAngleX += f * (float)Math.PI / 6.0F;
         ModelJoint var24 = this.head;
         var24.rotateAngleX += -f * (float)Math.PI / 4.0F;
         var24 = this.arm1;
         var24.rotateAngleX += f * (float)Math.PI / 6.0F;
         var24 = this.arm1;
         var24.rotateAngleZ += f * (float)Math.PI / 16.0F;
         var24 = this.arm2;
         var24.rotateAngleX += f * (float)Math.PI / 6.0F;
         var24 = this.arm2;
         var24.rotateAngleZ += -f * (float)Math.PI / 16.0F;
         var24 = this.leg1;
         var24.rotateAngleX += -f * (float)Math.PI / 8.0F;
         var24 = this.leg2;
         var24.rotateAngleX += -f * (float)Math.PI / 8.0F;
         ModelRenderer var31 = this.foreleg1.getModel();
         var31.rotateAngleX += f * (float)Math.PI / 4.0F;
         var31 = this.foreleg2.getModel();
         var31.rotateAngleX += f * (float)Math.PI / 4.0F;
         this.bow.rotateRope();
      } else if (fullTick < 12) {
         float tick = ((float)(fullTick - 10) + this.partialTick) / 2.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         float f1 = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var33 = this.skeleBase;
         var33.rotationPointY += f * 3.5F;
         var33 = this.spine[0].middle;
         var33.rotateAngleX += f * (float)Math.PI / 6.0F;
         ModelJoint var35 = this.head;
         var35.rotateAngleX += -f * (float)Math.PI / 4.0F;
         var35 = this.arm1;
         var35.rotateAngleX += f * (float)Math.PI / 6.0F;
         var35 = this.arm1;
         var35.rotateAngleZ += f * (float)Math.PI / 16.0F;
         var35 = this.arm2;
         var35.rotateAngleX += f * (float)Math.PI / 6.0F;
         var35 = this.arm2;
         var35.rotateAngleZ += -f * (float)Math.PI / 16.0F;
         var35 = this.leg1;
         var35.rotateAngleX += -f * (float)Math.PI / 8.0F;
         var35 = this.leg2;
         var35.rotateAngleX += -f * (float)Math.PI / 8.0F;
         ModelRenderer var42 = this.foreleg1.getModel();
         var42.rotateAngleX += f * (float)Math.PI / 4.0F;
         var42 = this.foreleg2.getModel();
         var42.rotateAngleX += f * (float)Math.PI / 4.0F;
         ModelJoint var44 = this.arm1;
         var44.rotateAngleZ += -f1 * (float)Math.PI / 14.0F;
         var44 = this.arm2;
         var44.rotateAngleZ += f1 * (float)Math.PI / 14.0F;
         var44 = this.leg1;
         var44.rotateAngleZ += -f1 * (float)Math.PI / 24.0F;
         var44 = this.leg2;
         var44.rotateAngleZ += f1 * (float)Math.PI / 24.0F;
         var44 = this.foreleg1;
         var44.rotateAngleZ += f1 * (float)Math.PI / 64.0F;
         var44 = this.foreleg2;
         var44.rotateAngleZ += -f1 * (float)Math.PI / 64.0F;
         this.bow.rotateRope();
      } else if (fullTick < 14) {
         ModelJoint var50 = this.arm1;
         var50.rotateAngleZ += -0.22439948F;
         var50 = this.arm2;
         var50.rotateAngleZ += 0.22439948F;
         var50 = this.leg1;
         var50.rotateAngleZ += -0.1308997F;
         var50 = this.leg2;
         var50.rotateAngleZ += 0.1308997F;
         var50 = this.foreleg1;
         var50.rotateAngleZ += 0.049087387F;
         var50 = this.foreleg2;
         var50.rotateAngleZ += -0.049087387F;
         this.bow.rotateRope();
      } else if (fullTick < 17) {
         float tick = ((float)(fullTick - 14) + this.partialTick) / 3.0F;
         float f = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         float f1 = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         ModelJoint var56 = this.arm1;
         var56.rotateAngleZ += -f1 * (float)Math.PI / 14.0F;
         var56 = this.arm2;
         var56.rotateAngleZ += f1 * (float)Math.PI / 14.0F;
         var56 = this.leg1;
         var56.rotateAngleZ += -f1 * (float)Math.PI / 24.0F;
         var56 = this.leg2;
         var56.rotateAngleZ += f1 * (float)Math.PI / 24.0F;
         var56 = this.foreleg1;
         var56.rotateAngleZ += f1 * (float)Math.PI / 64.0F;
         var56 = this.foreleg2;
         var56.rotateAngleZ += -f1 * (float)Math.PI / 64.0F;
         ModelRenderer var62 = this.arm1.getModel();
         var62.rotateAngleX += -f * (float)Math.PI / 4.0F;
         ModelJoint var63 = this.arm1;
         var63.rotateAngleY += -f * (float)Math.PI / 2.0F;
         var63 = this.arm1;
         var63.rotateAngleZ += f * (float)Math.PI / 16.0F;
         var63 = this.forearm1;
         var63.rotateAngleX += f * (float)Math.PI / 7.0F;
         ModelRenderer var66 = this.arm2.getModel();
         var66.rotateAngleX += -f * (float)Math.PI / 4.0F;
         ModelJoint var67 = this.arm2;
         var67.rotateAngleY += f * (float)Math.PI / 2.0F;
         var67 = this.arm2;
         var67.rotateAngleZ += -f * (float)Math.PI / 16.0F;
         ModelRenderer var69 = this.arm2.getModel();
         var69.rotateAngleZ += -f * (float)Math.PI / 8.0F;
         ModelJoint var70 = this.forearm2;
         var70.rotateAngleX += -f * (float)Math.PI / 6.0F;
         this.bow.rotateRope();
      } else if (fullTick < 20) {
         float tick = ((float)(fullTick - 17) + this.partialTick) / 3.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         float f1 = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         float f1s = MathHelper.sin(tick * (float)Math.PI / 2.0F * 0.4F);
         ModelRenderer var71 = this.head.getModel();
         var71.rotateAngleY += f1 * (float)Math.PI / 4.0F;

         for(int i = 0; i < this.spine.length; ++i) {
            var71 = this.spine[i].middle;
            var71.rotateAngleY += -f1 * (float)Math.PI / 12.0F;
            var71 = this.spine[i].middle;
            var71.rotateAngleX += f1 * facePitch / 3.0F;
            var71 = this.spine[i].middle;
            var71.rotateAngleY += f1 * faceYaw / 3.0F;
         }

         var71 = this.arm1.getModel();
         var71.rotateAngleX += f * 0.2617994F - ((float)Math.PI / 3F);
         ModelJoint var76 = this.arm1;
         var76.rotateAngleY += f * -0.9424778F - ((float)Math.PI / 5F);
         var76 = this.arm1;
         var76.rotateAngleZ += f * -0.850848F + ((float)Math.PI / 3F);
         var76 = this.forearm1;
         var76.rotateAngleX += 0.44879895F;
         ModelRenderer var79 = this.arm2.getModel();
         var79.rotateAngleX += f * 1.8325956F - 2.6179938F;
         ModelJoint var80 = this.arm2;
         var80.rotateAngleY += f * 0.9424778F + ((float)Math.PI / 5F);
         var80 = this.arm2;
         var80.rotateAngleZ += f * 0.850848F - ((float)Math.PI / 3F);
         ModelRenderer var82 = this.arm2.getModel();
         var82.rotateAngleZ += -f * (float)Math.PI / 8.0F;
         ModelJoint var83 = this.forearm2;
         var83.rotateAngleX += f * 0.10471976F - ((float)Math.PI / 5F);
         ModelRenderer var84 = this.bow.middle1;
         var84.rotateAngleX += -f1s * (float)Math.PI / 16.0F;
         var84 = this.bow.side1;
         var84.rotateAngleX += -f1s * (float)Math.PI / 24.0F;
         var84 = this.bow.middle2;
         var84.rotateAngleX += f1s * (float)Math.PI / 16.0F;
         var84 = this.bow.side2;
         var84.rotateAngleX += f1s * (float)Math.PI / 24.0F;
         this.bow.rotateRope();
         var84 = this.bow.rope1;
         var84.rotateAngleX += f1s * (float)Math.PI / 6.0F;
         var84 = this.bow.rope2;
         var84.rotateAngleX += -f1s * (float)Math.PI / 6.0F;
      } else if (fullTick < 24) {
         ModelRenderer var90 = this.head.getModel();
         var90.rotateAngleY += ((float)Math.PI / 4F);

         for(int i = 0; i < this.spine.length; ++i) {
            var90 = this.spine[i].middle;
            var90.rotateAngleY += -0.2617994F;
            var90 = this.spine[i].middle;
            var90.rotateAngleX += facePitch / 3.0F;
            var90 = this.spine[i].middle;
            var90.rotateAngleY += faceYaw / 3.0F;
         }

         var90 = this.arm1.getModel();
         var90.rotateAngleX += (-(float)Math.PI / 3F);
         ModelJoint var95 = this.arm1;
         var95.rotateAngleY += (-(float)Math.PI / 5F);
         ++this.arm1.rotateAngleZ;
         var95 = this.forearm1;
         var95.rotateAngleX += 0.44879895F;
         ModelRenderer var97 = this.arm2.getModel();
         var97.rotateAngleX += -2.6179938F;
         ModelJoint var98 = this.arm2;
         var98.rotateAngleY += ((float)Math.PI / 5F);
         var98 = this.arm2;
         var98.rotateAngleZ += (-(float)Math.PI / 3F);
         var98 = this.forearm2;
         var98.rotateAngleX += (-(float)Math.PI / 5F);
         float tick = MathHelper.clamp_float((float)(fullTick - 25) + this.partialTick, 0.0F, 1.0F);
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         ModelRenderer var101 = this.bow.middle1;
         var101.rotateAngleX += -f * (float)Math.PI / 16.0F;
         var101 = this.bow.side1;
         var101.rotateAngleX += -f * (float)Math.PI / 24.0F;
         var101 = this.bow.middle2;
         var101.rotateAngleX += f * (float)Math.PI / 16.0F;
         var101 = this.bow.side2;
         var101.rotateAngleX += f * (float)Math.PI / 24.0F;
         this.bow.rotateRope();
         var101 = this.bow.rope1;
         var101.rotateAngleX += f * (float)Math.PI / 6.0F;
         var101 = this.bow.rope2;
         var101.rotateAngleX += -f * (float)Math.PI / 6.0F;
      } else if (fullTick < 28) {
         float tick = ((float)(fullTick - 24) + this.partialTick) / 4.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         ModelRenderer var107 = this.head.getModel();
         var107.rotateAngleY += f * (float)Math.PI / 4.0F;

         for(int i = 0; i < this.spine.length; ++i) {
            var107 = this.spine[i].middle;
            var107.rotateAngleY += -f * (float)Math.PI / 12.0F;
            var107 = this.spine[i].middle;
            var107.rotateAngleX += f * facePitch / 3.0F;
            var107 = this.spine[i].middle;
            var107.rotateAngleY += f * faceYaw / 3.0F;
         }

         var107 = this.arm1.getModel();
         var107.rotateAngleX += -f * (float)Math.PI / 3.0F;
         ModelJoint var112 = this.arm1;
         var112.rotateAngleY += -f * (float)Math.PI / 5.0F;
         var112 = this.arm1;
         var112.rotateAngleZ += f * (float)Math.PI / 3.0F;
         var112 = this.forearm1;
         var112.rotateAngleX += f * (float)Math.PI / 7.0F;
         ModelRenderer var115 = this.arm2.getModel();
         var115.rotateAngleX += -f * (float)Math.PI / 1.2F;
         ModelJoint var116 = this.arm2;
         var116.rotateAngleY += f * (float)Math.PI / 5.0F;
         var116 = this.arm2;
         var116.rotateAngleZ += -f * (float)Math.PI / 3.0F;
         var116 = this.forearm2;
         var116.rotateAngleX += -f * (float)Math.PI / 5.0F;
         this.bow.rotateRope();
      }

   }

   protected void animateConstrict() {
      this.animator.startPhase(5);
      this.animator.rotate(this.waist, 0.1308997F, 0.0F, 0.0F);

      for(int i = 0; i < this.spine.length; ++i) {
         float f = i == 0 ? ((float)Math.PI / 8F) : (i == 2 ? (-(float)Math.PI / 8F) : 0.0F);
         float f1 = i == 1 ? ((float)Math.PI / 8F) : ((float)Math.PI / 10F);
         this.animator.rotate(this.spine[i].side1[0], f, f1, 0.0F);
         this.animator.rotate(this.spine[i].side1[1], 0.0F, 0.15707964F, 0.0F);
         this.animator.rotate(this.spine[i].side1[2], 0.0F, 0.2617994F, 0.0F);
         this.animator.rotate(this.spine[i].side2[0], f, -f1, 0.0F);
         this.animator.rotate(this.spine[i].side2[1], 0.0F, -0.15707964F, 0.0F);
         this.animator.rotate(this.spine[i].side2[2], 0.0F, -0.2617994F, 0.0F);
      }

      this.animator.rotate(this.arm1, 0.0F, 0.0F, 0.8975979F);
      this.animator.rotate(this.arm2, 0.0F, 0.0F, -0.8975979F);
      this.animator.move(this.skeleBase, 0.0F, 1.0F, 0.0F);
      this.animator.rotate(this.leg1, -0.44879895F, 0.0F, 0.0F);
      this.animator.rotate(this.leg2, -0.44879895F, 0.0F, 0.0F);
      this.animator.rotate(this.foreleg1.getModel(), ((float)Math.PI / 6F), 0.0F, 0.0F);
      this.animator.rotate(this.foreleg2.getModel(), ((float)Math.PI / 6F), 0.0F, 0.0F);
      this.animator.endPhase();
      this.animator.setStationaryPhase(2);
      this.animator.startPhase(1);
      this.animator.rotate(this.neck, 0.19634955F, 0.0F, 0.0F);
      this.animator.rotate(this.head, 0.15707964F, 0.0F, 0.0F);
      this.animator.rotate(this.waist, ((float)Math.PI / 10F), 0.0F, 0.0F);
      this.animator.rotate(this.spine[0].middle, 0.2617994F, 0.0F, 0.0F);

      for(int i = 0; i < this.spine.length; ++i) {
         float f = i == 0 ? 0.1308997F : (i == 2 ? -0.1308997F : 0.0F);
         float f1 = i == 1 ? -0.17453294F : -0.22439948F;
         this.animator.rotate(this.spine[i].side1[0], f - 0.08F, f1, 0.0F);
         this.animator.rotate(this.spine[i].side1[1], 0.0F, 0.15707964F, 0.0F);
         this.animator.rotate(this.spine[i].side1[2], 0.0F, 0.2617994F, 0.0F);
         this.animator.rotate(this.spine[i].side2[0], f + 0.08F, -f1, 0.0F);
         this.animator.rotate(this.spine[i].side2[1], 0.0F, -0.15707964F, 0.0F);
         this.animator.rotate(this.spine[i].side2[2], 0.0F, -0.2617994F, 0.0F);
      }

      this.animator.move(this.skeleBase, 0.0F, 1.0F, 0.0F);
      this.animator.rotate(this.leg1, -0.44879895F, 0.0F, 0.0F);
      this.animator.rotate(this.leg2, -0.44879895F, 0.0F, 0.0F);
      this.animator.rotate(this.foreleg1.getModel(), ((float)Math.PI / 6F), 0.0F, 0.0F);
      this.animator.rotate(this.foreleg2.getModel(), ((float)Math.PI / 6F), 0.0F, 0.0F);
      this.animator.endPhase();
      this.animator.setStationaryPhase(4);
      this.animator.resetPhase(8);
      int animTick = this.animator.getEntity().getAnimTick();
      if (animTick < 5) {
         float tick = ((float)animTick + this.partialTick) / 5.0F;
         float f = MathHelper.sin(tick * (float)Math.PI / 2.0F);

         for(int i = 0; i < this.spine.length; ++i) {
            ((ModelRendererScalable)this.spine[i].side1[0]).setScale(1.0F + f * 0.6F);
            ((ModelRendererScalable)this.spine[i].side2[0]).setScale(1.0F + f * 0.6F);
         }
      } else if (animTick < 12) {
         for(int i = 0; i < this.spine.length; ++i) {
            ((ModelRendererScalable)this.spine[i].side1[0]).setScale(1.6F);
            ((ModelRendererScalable)this.spine[i].side2[0]).setScale(1.6F);
         }
      } else if (animTick < 20) {
         float tick = ((float)(animTick - 12) + this.partialTick) / 8.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);

         for(int i = 0; i < this.spine.length; ++i) {
            ((ModelRendererScalable)this.spine[i].side1[0]).setScale(1.0F + f * 0.6F);
            ((ModelRendererScalable)this.spine[i].side2[0]).setScale(1.0F + f * 0.6F);
         }
      }

   }

   public static ModelRenderer createSkull(ModelBase base) {
      ModelRenderer head = new ModelRenderer(base, 0, 0);
      head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.4F);
      ModelRenderer jaw = new ModelRenderer(base, 32, 0);
      jaw.addBox(-4.0F, -3.0F, -8.0F, 8, 3, 8, 0.7F);
      jaw.setRotationPoint(0.0F, -0.2F, 3.5F);
      jaw.rotateAngleX = 0.09817477F;
      head.addChild(jaw);
      return head;
   }
}
