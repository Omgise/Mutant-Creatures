package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.MutantZombie;

@SideOnly(Side.CLIENT)
public class ModelMutantZombie extends ModelBase {
   public ModelRenderer pelvis;
   public ModelRenderer waist;
   public ModelRenderer chest;
   public ModelRenderer head;
   public ModelRenderer villagerHead;
   public ModelRenderer arm1;
   public ModelRenderer arm2;
   public ModelRenderer forearm1;
   public ModelRenderer forearm2;
   public ModelRenderer leg1;
   public ModelRenderer leg2;
   public ModelRenderer foreleg1;
   public ModelRenderer foreleg2;
   protected float animTick;
   public static final float PI = (float)Math.PI;

   public ModelMutantZombie() {
      this.textureWidth = 128;
      this.textureHeight = 128;
      this.animTick = 0.0F;
      this.pelvis = new ModelRenderer(this);
      this.pelvis.setRotationPoint(0.0F, 10.0F, 6.0F);
      this.waist = new ModelRenderer(this, 0, 44);
      this.waist.addBox(-7.0F, -16.0F, -6.0F, 14, 16, 12);
      this.pelvis.addChild(this.waist);
      this.chest = new ModelRenderer(this, 0, 16);
      this.chest.addBox(-12.0F, -12.0F, -8.0F, 24, 12, 16);
      this.chest.setRotationPoint(0.0F, -12.0F, 0.0F);
      this.waist.addChild(this.chest);
      this.head = new ModelRenderer(this, 0, 0);
      this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
      this.head.setRotationPoint(0.0F, -11.0F, -4.0F);
      this.chest.addChild(this.head);
      this.villagerHead = new ModelRenderer(this);
      this.villagerHead.setTextureOffset(0, 72).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8);
      this.villagerHead.setTextureOffset(24, 72).addBox(-1.0F, -3.0F, -6.0F, 2, 4, 2);
      this.villagerHead.setRotationPoint(0.0F, -11.0F, -4.0F);
      this.chest.addChild(this.villagerHead);
      this.arm1 = new ModelRenderer(this, 104, 0);
      this.arm1.addBox(-3.0F, 0.0F, -3.0F, 6, 16, 6);
      this.arm1.setRotationPoint(-11.0F, -8.0F, 2.0F);
      this.chest.addChild(this.arm1);
      this.arm2 = new ModelRenderer(this, 104, 0);
      this.arm2.mirror = true;
      this.arm2.addBox(-3.0F, 0.0F, -3.0F, 6, 16, 6);
      this.arm2.setRotationPoint(11.0F, -8.0F, 2.0F);
      this.chest.addChild(this.arm2);
      this.forearm1 = new ModelRenderer(this, 104, 22);
      this.forearm1.addBox(-3.0F, 0.0F, -3.0F, 6, 16, 6, 0.1F);
      this.forearm1.setRotationPoint(0.0F, 14.0F, 0.0F);
      this.arm1.addChild(this.forearm1);
      this.forearm2 = new ModelRenderer(this, 104, 22);
      this.forearm2.mirror = true;
      this.forearm2.addBox(-3.0F, 0.0F, -3.0F, 6, 16, 6, 0.1F);
      this.forearm2.setRotationPoint(0.0F, 14.0F, 0.0F);
      this.arm2.addChild(this.forearm2);
      this.leg1 = new ModelRenderer(this, 80, 0);
      this.leg1.addBox(-3.0F, 0.0F, -3.0F, 6, 11, 6);
      this.leg1.setRotationPoint(-5.0F, -2.0F, 0.0F);
      this.pelvis.addChild(this.leg1);
      this.leg2 = new ModelRenderer(this, 80, 0);
      this.leg2.mirror = true;
      this.leg2.addBox(-3.0F, 0.0F, -3.0F, 6, 11, 6);
      this.leg2.setRotationPoint(5.0F, -2.0F, 0.0F);
      this.pelvis.addChild(this.leg2);
      this.foreleg1 = new ModelRenderer(this, 80, 17);
      this.foreleg1.addBox(-3.0F, 0.0F, -3.0F, 6, 8, 6, 0.1F);
      this.foreleg1.setRotationPoint(0.0F, 9.5F, 0.0F);
      this.leg1.addChild(this.foreleg1);
      this.foreleg2 = new ModelRenderer(this, 80, 17);
      this.foreleg2.mirror = true;
      this.foreleg2.addBox(-3.0F, 0.0F, -3.0F, 6, 8, 6, 0.1F);
      this.foreleg2.setRotationPoint(0.0F, 9.5F, 0.0F);
      this.leg2.addChild(this.foreleg2);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      MutantZombie zombie = (MutantZombie)entity;
      this.setAngles();
      this.animTick = MutantCreatures.proxy.getPartialTicks();
      this.animate(zombie, f, f1, f2, f3, f4, f5);
      if (zombie.getVillager()) {
         this.head.showModel = false;
         this.villagerHead.showModel = true;
      } else {
         this.head.showModel = true;
         this.villagerHead.showModel = false;
      }

      this.pelvis.render(f5);
   }

   public void setAngles() {
      this.pelvis.rotationPointY = 10.0F;
      this.waist.rotateAngleX = 0.19634955F;
      this.chest.rotateAngleX = ((float)Math.PI / 6F);
      this.chest.rotateAngleY = 0.0F;
      this.head.rotateAngleX = -0.71994835F;
      this.head.rotateAngleY = 0.0F;
      this.head.rotateAngleZ = 0.0F;
      this.arm1.rotateAngleX = -0.32724923F;
      this.arm1.rotateAngleY = 0.0F;
      this.arm1.rotateAngleZ = ((float)Math.PI / 8F);
      this.arm2.rotateAngleX = -0.32724923F;
      this.arm2.rotateAngleY = 0.0F;
      this.arm2.rotateAngleZ = (-(float)Math.PI / 8F);
      this.forearm1.rotateAngleX = (-(float)Math.PI / 3F);
      this.forearm2.rotateAngleX = (-(float)Math.PI / 3F);
      this.leg1.rotateAngleX = (-(float)Math.PI / 4F);
      this.leg1.rotateAngleY = 0.0F;
      this.leg1.rotateAngleZ = 0.0F;
      this.leg2.rotateAngleX = (-(float)Math.PI / 4F);
      this.leg2.rotateAngleY = 0.0F;
      this.leg2.rotateAngleZ = 0.0F;
      this.foreleg1.rotateAngleX = ((float)Math.PI / 4F);
      this.foreleg2.rotateAngleX = ((float)Math.PI / 4F);
   }

   public void animate(MutantZombie zombie, float f, float f1, float f2, float f3, float f4, float f5) {
      float walkAnim1 = (MathHelper.sin((f - 0.7F) * 0.4F) + 0.7F) * f1;
      float walkAnim2 = -(MathHelper.sin((f + 0.7F) * 0.4F) - 0.7F) * f1;
      float walkAnim = MathHelper.sin(f * 0.4F) * f1;
      float breatheAnim = MathHelper.sin(f2 * 0.1F);
      float faceYaw = f3 * (float)Math.PI / 180.0F;
      float facePitch = f4 * (float)Math.PI / 180.0F;
      if (zombie.deathTick <= 0) {
         if (zombie.currentAttackID == 1) {
            this.animateMelee(zombie.animTick);
         }

         if (zombie.currentAttackID == 2) {
            this.animateRoar(zombie.animTick);
            float scale = 1.0F - MathHelper.clamp_float((float)zombie.animTick / 6.0F, 0.0F, 1.0F);
            walkAnim1 *= scale;
            walkAnim2 *= scale;
            walkAnim *= scale;
            facePitch *= scale;
         }

         if (zombie.currentAttackID == 3) {
            this.animateThrow(zombie);
            float scale = 1.0F - MathHelper.clamp_float((float)zombie.animTick / 3.0F, 0.0F, 1.0F);
            walkAnim1 *= scale;
            walkAnim2 *= scale;
            walkAnim *= scale;
            facePitch *= scale;
         }
      } else {
         this.animateDeath(zombie);
         float scale = 1.0F - MathHelper.clamp_float((float)zombie.deathTick / 6.0F, 0.0F, 1.0F);
         walkAnim1 *= scale;
         walkAnim2 *= scale;
         walkAnim *= scale;
         breatheAnim *= scale;
         faceYaw *= scale;
         facePitch *= scale;
      }

      ModelRenderer var10000 = this.chest;
      var10000.rotateAngleX += breatheAnim * 0.02F;
      var10000 = this.arm1;
      var10000.rotateAngleZ -= breatheAnim * 0.05F;
      var10000 = this.arm2;
      var10000.rotateAngleZ += breatheAnim * 0.05F;
      var10000 = this.head;
      var10000.rotateAngleX += facePitch * 0.6F;
      var10000 = this.head;
      var10000.rotateAngleY += faceYaw * 0.8F;
      var10000 = this.head;
      var10000.rotateAngleZ -= faceYaw * 0.2F;
      var10000 = this.chest;
      var10000.rotateAngleX += facePitch * 0.4F;
      var10000 = this.chest;
      var10000.rotateAngleY += faceYaw * 0.2F;
      var10000 = this.pelvis;
      var10000.rotationPointY += MathHelper.sin(f * 0.8F) * f1 * 0.5F;
      var10000 = this.chest;
      var10000.rotateAngleY -= walkAnim * 0.1F;
      var10000 = this.arm1;
      var10000.rotateAngleX -= walkAnim * 0.6F;
      var10000 = this.arm2;
      var10000.rotateAngleX += walkAnim * 0.6F;
      var10000 = this.leg1;
      var10000.rotateAngleX += walkAnim1 * 0.9F;
      var10000 = this.leg2;
      var10000.rotateAngleX += walkAnim2 * 0.9F;
      this.villagerHead.rotateAngleX = this.head.rotateAngleX;
      this.villagerHead.rotateAngleY = this.head.rotateAngleY;
      this.villagerHead.rotateAngleZ = this.head.rotateAngleZ;
   }

   protected void animateMelee(int fullTick) {
      this.arm1.rotateAngleZ = 0.0F;
      this.arm2.rotateAngleZ = 0.0F;
      if (fullTick < 8) {
         float tick = ((float)fullTick + this.animTick) / 8.0F;
         float f = -MathHelper.sin(tick * (float)Math.PI / 2.0F);
         float f1 = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         ModelRenderer var10000 = this.waist;
         var10000.rotateAngleX += f * 0.2F;
         var10000 = this.chest;
         var10000.rotateAngleX += f * 0.2F;
         var10000 = this.arm1;
         var10000.rotateAngleX += f * 2.3F;
         var10000 = this.arm1;
         var10000.rotateAngleZ += f1 * (float)Math.PI / 8.0F;
         var10000 = this.arm2;
         var10000.rotateAngleX += f * 2.3F;
         var10000 = this.arm2;
         var10000.rotateAngleZ -= f1 * (float)Math.PI / 8.0F;
         var10000 = this.forearm1;
         var10000.rotateAngleX += f * 0.8F;
         var10000 = this.forearm2;
         var10000.rotateAngleX += f * 0.8F;
      } else if (fullTick < 12) {
         float tick = ((float)(fullTick - 8) + this.animTick) / 4.0F;
         float f = -MathHelper.cos(tick * (float)Math.PI / 2.0F);
         float f1 = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var17 = this.waist;
         var17.rotateAngleX += f * 0.9F + 0.7F;
         var17 = this.chest;
         var17.rotateAngleX += f * 0.9F + 0.7F;
         var17 = this.arm1;
         var17.rotateAngleX += f * 0.2F - 2.1F;
         var17 = this.arm1;
         var17.rotateAngleZ += f1 * 0.3F;
         var17 = this.arm2;
         var17.rotateAngleX += f * 0.2F - 2.1F;
         var17 = this.arm2;
         var17.rotateAngleZ -= f1 * 0.3F;
         var17 = this.forearm1;
         var17.rotateAngleX += f * 1.0F + 0.2F;
         var17 = this.forearm2;
         var17.rotateAngleX += f * 1.0F + 0.2F;
      } else if (fullTick < 16) {
         ModelRenderer var25 = this.waist;
         var25.rotateAngleX += 0.7F;
         var25 = this.chest;
         var25.rotateAngleX += 0.7F;
         var25 = this.arm1;
         var25.rotateAngleX -= 2.1F;
         var25 = this.arm1;
         var25.rotateAngleZ += 0.3F;
         var25 = this.arm2;
         var25.rotateAngleX -= 2.1F;
         var25 = this.arm2;
         var25.rotateAngleZ -= 0.3F;
         var25 = this.forearm1;
         var25.rotateAngleX += 0.2F;
         var25 = this.forearm2;
         var25.rotateAngleX += 0.2F;
      } else if (fullTick < 24) {
         float tick = ((float)(fullTick - 16) + this.animTick) / 8.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         ModelRenderer var33 = this.waist;
         var33.rotateAngleX += f * 0.7F;
         var33 = this.chest;
         var33.rotateAngleX += f * 0.7F;
         var33 = this.arm1;
         var33.rotateAngleX -= f * 2.1F;
         var33 = this.arm1;
         var33.rotateAngleZ += f * -0.09269908F + ((float)Math.PI / 8F);
         var33 = this.arm2;
         var33.rotateAngleX -= f * 2.1F;
         var33 = this.arm2;
         var33.rotateAngleZ -= f * -0.09269908F + ((float)Math.PI / 8F);
         var33 = this.forearm1;
         var33.rotateAngleX += f * 0.2F;
         var33 = this.forearm2;
         var33.rotateAngleX += f * 0.2F;
      } else {
         ModelRenderer var41 = this.arm1;
         var41.rotateAngleZ += ((float)Math.PI / 8F);
         var41 = this.arm2;
         var41.rotateAngleZ += (-(float)Math.PI / 8F);
      }

   }

   protected void animateRoar(int fullTick) {
      if (fullTick < 10) {
         float tick = ((float)fullTick + this.animTick) / 10.0F;
         float f = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         float f1 = MathHelper.sin(tick * (float)Math.PI * (float)Math.PI / 8.0F);
         ModelRenderer var10000 = this.waist;
         var10000.rotateAngleX += f * 0.2F;
         var10000 = this.chest;
         var10000.rotateAngleX += f * 0.4F;
         var10000 = this.chest;
         var10000.rotateAngleY += f1 * 0.06F;
         var10000 = this.head;
         var10000.rotateAngleX += f * 0.8F;
         var10000 = this.arm1;
         var10000.rotateAngleX -= f * 1.2F;
         var10000 = this.arm1;
         var10000.rotateAngleZ += f * 0.6F;
         var10000 = this.arm2;
         var10000.rotateAngleX -= f * 1.2F;
         var10000 = this.arm2;
         var10000.rotateAngleZ -= f * 0.6F;
         var10000 = this.forearm1;
         var10000.rotateAngleX -= f * 0.8F;
         var10000 = this.forearm2;
         var10000.rotateAngleX -= f * 0.8F;
      } else if (fullTick < 15) {
         float tick = ((float)(fullTick - 10) + this.animTick) / 5.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         float f1 = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var24 = this.waist;
         var24.rotateAngleX += f * 0.39634955F - 0.19634955F;
         var24 = this.chest;
         var24.rotateAngleX += f * 0.6F - 0.2F;
         var24 = this.head;
         var24.rotateAngleX += f * 1.0F - 0.2F;
         var24 = this.arm1;
         var24.rotateAngleX -= f * 2.2F - 1.0F;
         var24 = this.arm1;
         var24.rotateAngleY += f1 * 0.4F;
         var24 = this.arm1;
         var24.rotateAngleZ += 0.6F;
         var24 = this.arm2;
         var24.rotateAngleX -= f * 2.2F - 1.0F;
         var24 = this.arm2;
         var24.rotateAngleY -= f1 * 0.4F;
         var24 = this.arm2;
         var24.rotateAngleZ -= 0.6F;
         var24 = this.forearm1;
         var24.rotateAngleX -= f * 1.0F - 0.2F;
         var24 = this.forearm2;
         var24.rotateAngleX -= f * 1.0F - 0.2F;
         var24 = this.leg1;
         var24.rotateAngleY += f1 * 0.3F;
         var24 = this.leg2;
         var24.rotateAngleY -= f1 * 0.3F;
      } else if (fullTick < 75) {
         ModelRenderer var37 = this.waist;
         var37.rotateAngleX -= 0.19634955F;
         var37 = this.chest;
         var37.rotateAngleX -= 0.2F;
         var37 = this.head;
         var37.rotateAngleX -= 0.2F;
         this.addRotation(this.arm1, 1.0F, 0.4F, 0.6F);
         this.addRotation(this.arm2, 1.0F, -0.4F, -0.6F);
         var37 = this.forearm1;
         var37.rotateAngleX += 0.2F;
         var37 = this.forearm2;
         var37.rotateAngleX += 0.2F;
         var37 = this.leg1;
         var37.rotateAngleY += 0.3F;
         var37 = this.leg2;
         var37.rotateAngleY -= 0.3F;
      } else if (fullTick < 90) {
         float tick = ((float)(fullTick - 75) + this.animTick) / 15.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         ModelRenderer var44 = this.waist;
         var44.rotateAngleX -= f * 0.69634956F - 0.5F;
         var44 = this.chest;
         var44.rotateAngleX -= f * 0.7F - 0.5F;
         var44 = this.head;
         var44.rotateAngleX -= f * 0.6F - 0.4F;
         this.addRotation(this.arm1, f * 2.6F - 1.6F, f * 0.4F, f * 0.99269915F - ((float)Math.PI / 8F));
         this.addRotation(this.arm2, f * 2.6F - 1.6F, -f * 0.4F, -f * 0.99269915F + ((float)Math.PI / 8F));
         var44 = this.forearm1;
         var44.rotateAngleX += f * -0.6F + 0.8F;
         var44 = this.forearm2;
         var44.rotateAngleX += f * -0.6F + 0.8F;
         var44 = this.leg1;
         var44.rotateAngleY += f * 0.3F;
         var44 = this.leg2;
         var44.rotateAngleY -= f * 0.3F;
      } else if (fullTick < 110) {
         ModelRenderer var51 = this.waist;
         var51.rotateAngleX += 0.5F;
         var51 = this.chest;
         var51.rotateAngleX += 0.5F;
         var51 = this.head;
         var51.rotateAngleX += 0.4F;
         this.addRotation(this.arm1, -1.6F, 0.0F, (-(float)Math.PI / 8F));
         this.addRotation(this.arm2, -1.6F, 0.0F, ((float)Math.PI / 8F));
         var51 = this.forearm1;
         var51.rotateAngleX += 0.8F;
         var51 = this.forearm2;
         var51.rotateAngleX += 0.8F;
      } else {
         float tick = ((float)(fullTick - 110) + this.animTick) / 10.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         ModelRenderer var56 = this.waist;
         var56.rotateAngleX += f * 0.5F;
         var56 = this.chest;
         var56.rotateAngleX += f * 0.5F;
         var56 = this.head;
         var56.rotateAngleX += f * 0.4F;
         this.addRotation(this.arm1, f * -1.6F, 0.0F, f * -(float)Math.PI / 8.0F);
         this.addRotation(this.arm2, f * -1.6F, 0.0F, f * (float)Math.PI / 8.0F);
         var56 = this.forearm1;
         var56.rotateAngleX += f * 0.8F;
         var56 = this.forearm2;
         var56.rotateAngleX += f * 0.8F;
      }

      if (fullTick >= 10 && fullTick < 75) {
         float tick = ((float)(fullTick - 10) + this.animTick) / 65.0F;
         float f = MathHelper.sin(tick * (float)Math.PI * 8.0F);
         float f1 = MathHelper.sin(tick * (float)Math.PI * 8.0F + ((float)Math.PI / 4F));
         ModelRenderer var61 = this.head;
         var61.rotateAngleY += f * 0.5F - f1 * 0.2F;
         var61 = this.head;
         var61.rotateAngleZ -= f * 0.5F;
         var61 = this.chest;
         var61.rotateAngleY += f1 * 0.06F;
      }

   }

   protected void animateThrow(MutantZombie zombie) {
      if (zombie.animTick < 3) {
         float tick = ((float)zombie.animTick + this.animTick) / 3.0F;
         float f = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var10000 = this.chest;
         var10000.rotateAngleX -= f * 0.4F;
         var10000 = this.arm1;
         var10000.rotateAngleX -= f * 1.8F;
         var10000 = this.arm1;
         var10000.rotateAngleZ -= f * (float)Math.PI / 8.0F;
         var10000 = this.arm2;
         var10000.rotateAngleX -= f * 1.8F;
         var10000 = this.arm2;
         var10000.rotateAngleZ += f * (float)Math.PI / 8.0F;
      } else if (zombie.animTick < 5) {
         ModelRenderer var19 = this.chest;
         var19.rotateAngleX -= 0.4F;
         --this.arm1.rotateAngleX;
         this.arm1.rotateAngleZ = 0.0F;
         --this.arm2.rotateAngleX;
         this.arm2.rotateAngleZ = 0.0F;
      } else if (zombie.animTick < 8) {
         float tick = ((float)(zombie.animTick - 5) + this.animTick) / 3.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         float f1 = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var20 = this.waist;
         var20.rotateAngleX += f1 * 0.2F;
         var20 = this.chest;
         var20.rotateAngleX -= f * 0.6F - 0.2F;
         var20 = this.arm1;
         var20.rotateAngleX -= f * 2.2F - 0.4F;
         var20 = this.arm1;
         var20.rotateAngleZ -= f * (float)Math.PI / 8.0F;
         var20 = this.arm2;
         var20.rotateAngleX -= f * 2.2F - 0.4F;
         var20 = this.arm2;
         var20.rotateAngleZ += f * (float)Math.PI / 8.0F;
         var20 = this.forearm1;
         var20.rotateAngleX -= f1 * 0.4F;
         var20 = this.forearm2;
         var20.rotateAngleX -= f1 * 0.4F;
      } else if (zombie.animTick < 10) {
         ModelRenderer var28 = this.waist;
         var28.rotateAngleX += 0.2F;
         var28 = this.chest;
         var28.rotateAngleX += 0.2F;
         var28 = this.arm1;
         var28.rotateAngleX += 0.4F;
         var28 = this.arm2;
         var28.rotateAngleX += 0.4F;
         var28 = this.forearm1;
         var28.rotateAngleX -= 0.4F;
         var28 = this.forearm2;
         var28.rotateAngleX -= 0.4F;
      } else if (zombie.animTick < 15) {
         float tick = ((float)(zombie.animTick - 10) + this.animTick) / 5.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         float f1 = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var34 = this.waist;
         var34.rotateAngleX += f * 0.39634955F - 0.19634955F;
         var34 = this.chest;
         var34.rotateAngleX += f * 0.8F - 0.6F;
         var34 = this.arm1;
         var34.rotateAngleX += f * 3.0F - 2.6F;
         var34 = this.arm2;
         var34.rotateAngleX += f * 3.0F - 2.6F;
         var34 = this.forearm1;
         var34.rotateAngleX -= f * 0.4F;
         var34 = this.forearm2;
         var34.rotateAngleX -= f * 0.4F;
         var34 = this.leg1;
         var34.rotateAngleX += f1 * 0.6F;
         var34 = this.leg2;
         var34.rotateAngleX += f1 * 0.6F;
      } else if (zombie.throwHitTick == -1) {
         ModelRenderer var42 = this.waist;
         var42.rotateAngleX -= 0.19634955F;
         var42 = this.chest;
         var42.rotateAngleX -= 0.6F;
         var42 = this.arm1;
         var42.rotateAngleX -= 2.6F;
         var42 = this.arm2;
         var42.rotateAngleX -= 2.6F;
         var42 = this.leg1;
         var42.rotateAngleX += 0.6F;
         var42 = this.leg2;
         var42.rotateAngleX += 0.6F;
      } else if (zombie.throwHitTick < 5) {
         float tick = ((float)zombie.throwHitTick + this.animTick) / 3.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         float f1 = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var48 = this.waist;
         var48.rotateAngleX -= f * 0.39634955F - 0.2F;
         var48 = this.chest;
         var48.rotateAngleX -= f * 0.8F - 0.2F;
         this.addRotation(this.arm1, -(f * 2.2F + 0.4F), -f1 * (float)Math.PI / 8.0F, f1 * 0.4F);
         this.addRotation(this.arm2, -(f * 2.2F + 0.4F), f1 * (float)Math.PI / 8.0F, -f1 * 0.4F);
         var48 = this.forearm1;
         var48.rotateAngleX += f1 * 0.2F;
         var48 = this.forearm2;
         var48.rotateAngleX += f1 * 0.2F;
         var48 = this.leg1;
         var48.rotateAngleX += f * 0.8F - 0.2F;
         var48 = this.leg2;
         var48.rotateAngleX += f * 0.8F - 0.2F;
      } else if (zombie.throwFinishTick == -1) {
         ModelRenderer var54 = this.waist;
         var54.rotateAngleX += 0.2F;
         var54 = this.chest;
         var54.rotateAngleX += 0.2F;
         this.addRotation(this.arm1, -0.4F, (-(float)Math.PI / 8F), 0.4F);
         this.addRotation(this.arm2, -0.4F, ((float)Math.PI / 8F), -0.4F);
         var54 = this.forearm1;
         var54.rotateAngleX += 0.2F;
         var54 = this.forearm2;
         var54.rotateAngleX += 0.2F;
         var54 = this.leg1;
         var54.rotateAngleX -= 0.2F;
         var54 = this.leg2;
         var54.rotateAngleX -= 0.2F;
      } else if (zombie.throwFinishTick < 10) {
         float tick = ((float)zombie.throwFinishTick + this.animTick) / 10.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         ModelRenderer var60 = this.waist;
         var60.rotateAngleX += f * 0.2F;
         var60 = this.chest;
         var60.rotateAngleX += f * 0.2F;
         this.addRotation(this.arm1, -f * 0.4F, -f * (float)Math.PI / 8.0F, f * 0.4F);
         this.addRotation(this.arm1, -f * 0.4F, f * (float)Math.PI / 8.0F, -f * 0.4F);
         var60 = this.forearm1;
         var60.rotateAngleX += f * 0.2F;
         var60 = this.forearm2;
         var60.rotateAngleX += f * 0.2F;
         var60 = this.leg1;
         var60.rotateAngleX -= f * 0.2F;
         var60 = this.leg2;
         var60.rotateAngleX -= f * 0.2F;
      }

   }

   protected void animateDeath(MutantZombie zombie) {
      if (zombie.deathTick <= 20) {
         float tick = ((float)zombie.deathTick + this.animTick - 1.0F) / 20.0F;
         float f = MathHelper.sin(tick * (float)Math.PI / 2.0F);
         ModelRenderer var10000 = this.pelvis;
         var10000.rotationPointY += f * 28.0F;
         var10000 = this.head;
         var10000.rotateAngleX -= f * (float)Math.PI / 10.0F;
         var10000 = this.head;
         var10000.rotateAngleY += f * (float)Math.PI / 5.0F;
         var10000 = this.chest;
         var10000.rotateAngleX -= f * (float)Math.PI / 12.0F;
         var10000 = this.waist;
         var10000.rotateAngleX -= f * (float)Math.PI / 10.0F;
         var10000 = this.arm1;
         var10000.rotateAngleX -= f * (float)Math.PI / 2.0F;
         var10000 = this.arm1;
         var10000.rotateAngleY += f * (float)Math.PI / 2.8F;
         var10000 = this.arm2;
         var10000.rotateAngleX -= f * (float)Math.PI / 2.0F;
         var10000 = this.arm2;
         var10000.rotateAngleY -= f * (float)Math.PI / 2.8F;
         var10000 = this.leg1;
         var10000.rotateAngleX += f * (float)Math.PI / 6.0F;
         var10000 = this.leg1;
         var10000.rotateAngleZ += f * (float)Math.PI / 12.0F;
         var10000 = this.leg2;
         var10000.rotateAngleX += f * (float)Math.PI / 6.0F;
         var10000 = this.leg2;
         var10000.rotateAngleZ -= f * (float)Math.PI / 12.0F;
      } else if (zombie.deathTick <= zombie.maxDeathTime() - 40) {
         ModelRenderer var18 = this.pelvis;
         var18.rotationPointY += 28.0F;
         var18 = this.head;
         var18.rotateAngleX -= ((float)Math.PI / 10F);
         var18 = this.head;
         var18.rotateAngleY += ((float)Math.PI / 5F);
         var18 = this.chest;
         var18.rotateAngleX -= 0.2617994F;
         var18 = this.waist;
         var18.rotateAngleX -= ((float)Math.PI / 10F);
         --this.arm1.rotateAngleX;
         ++this.arm1.rotateAngleY;
         --this.arm2.rotateAngleX;
         --this.arm2.rotateAngleY;
         var18 = this.leg1;
         var18.rotateAngleX += ((float)Math.PI / 6F);
         var18 = this.leg1;
         var18.rotateAngleZ += 0.2617994F;
         var18 = this.leg2;
         var18.rotateAngleX += ((float)Math.PI / 6F);
         var18 = this.leg2;
         var18.rotateAngleZ -= 0.2617994F;
      } else {
         float tick = ((float)(40 - (zombie.maxDeathTime() - zombie.deathTick)) + this.animTick) / 40.0F;
         float f = MathHelper.cos(tick * (float)Math.PI / 2.0F);
         ModelRenderer var27 = this.pelvis;
         var27.rotationPointY += f * 28.0F;
         var27 = this.head;
         var27.rotateAngleX -= f * (float)Math.PI / 10.0F;
         var27 = this.head;
         var27.rotateAngleY += f * (float)Math.PI / 5.0F;
         var27 = this.chest;
         var27.rotateAngleX -= f * (float)Math.PI / 12.0F;
         var27 = this.waist;
         var27.rotateAngleX -= f * (float)Math.PI / 10.0F;
         var27 = this.arm1;
         var27.rotateAngleX -= f * (float)Math.PI / 2.0F;
         var27 = this.arm1;
         var27.rotateAngleY += f * (float)Math.PI / 2.8F;
         var27 = this.arm2;
         var27.rotateAngleX -= f * (float)Math.PI / 2.0F;
         var27 = this.arm2;
         var27.rotateAngleY -= f * (float)Math.PI / 2.8F;
         var27 = this.leg1;
         var27.rotateAngleX += f * (float)Math.PI / 6.0F;
         var27 = this.leg1;
         var27.rotateAngleZ += f * (float)Math.PI / 12.0F;
         var27 = this.leg2;
         var27.rotateAngleX += f * (float)Math.PI / 6.0F;
         var27 = this.leg2;
         var27.rotateAngleZ -= f * (float)Math.PI / 12.0F;
      }

   }

   public void addRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX += x;
      model.rotateAngleY += y;
      model.rotateAngleZ += z;
   }
}
