package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class SkeletonSpine {
   public ModelRenderer middle;
   public ModelRenderer[] side1;
   public ModelRenderer[] side2;

   public SkeletonSpine(ModelBase base) {
      this(base, false);
   }

   public SkeletonSpine(ModelBase base, boolean skeletonPart) {
      this.middle = new ModelRenderer(base, 50, 0);
      this.middle.addBox(-2.5F, -4.0F, -2.0F, 5, 4, 4, 0.5F);
      this.side1 = new ModelRenderer[3];
      this.side2 = new ModelRenderer[3];
      this.side1[0] = new ModelRendererScalable(base, 32, 12);
      this.side1[0].addBox(skeletonPart ? 0.0F : -6.0F, -2.0F, -2.0F, 6, 2, 2, 0.25F);
      if (!skeletonPart) {
         this.side1[0].setRotationPoint(-3.0F, -1.0F, 1.75F);
      }

      this.middle.addChild(this.side1[0]);
      this.side2[0] = new ModelRendererScalable(base, 32, 12);
      this.side2[0].mirror = true;
      this.side2[0].addBox(skeletonPart ? -6.0F : 0.0F, -2.0F, -2.0F, 6, 2, 2, 0.25F);
      if (!skeletonPart) {
         this.side2[0].setRotationPoint(3.0F, -1.0F, 1.75F);
      }

      this.middle.addChild(this.side2[0]);
      this.side1[1] = new ModelRenderer(base, 32, 12);
      this.side1[1].mirror = true;
      this.side1[1].addBox(-6.0F, -2.0F, -2.0F, 6, 2, 2, 0.2F);
      this.side1[1].setRotationPoint(skeletonPart ? -0.5F : -6.5F, 0.0F, 0.0F);
      this.side1[0].addChild(this.side1[1]);
      this.side2[1] = new ModelRenderer(base, 32, 12);
      this.side2[1].addBox(0.0F, -2.0F, -2.0F, 6, 2, 2, 0.2F);
      this.side2[1].setRotationPoint(skeletonPart ? 0.5F : 6.5F, 0.0F, 0.0F);
      this.side2[0].addChild(this.side2[1]);
      this.side1[2] = new ModelRenderer(base, 32, 12);
      this.side1[2].addBox(-6.0F, -2.0F, -2.0F, 6, 2, 2, 0.15F);
      this.side1[2].setRotationPoint(-6.4F, 0.0F, 0.0F);
      this.side1[1].addChild(this.side1[2]);
      this.side2[2] = new ModelRenderer(base, 32, 12);
      this.side2[2].mirror = true;
      this.side2[2].addBox(0.0F, -2.0F, -2.0F, 6, 2, 2, 0.15F);
      this.side2[2].setRotationPoint(6.4F, 0.0F, 0.0F);
      this.side2[1].addChild(this.side2[2]);
   }

   private void resetAngles(ModelRenderer... boxes) {
      for(ModelRenderer box : boxes) {
         box.rotateAngleX = 0.0F;
         box.rotateAngleY = 0.0F;
         box.rotateAngleZ = 0.0F;
      }

   }

   public void setAngles(float PI, boolean middleSpine) {
      this.resetAngles(this.middle);
      this.resetAngles(this.side1);
      this.resetAngles(this.side2);
      this.middle.rotateAngleX = PI / 18.0F;
      this.side1[0].rotateAngleY = -PI / 4.5F;
      this.side2[0].rotateAngleY = PI / 4.5F;
      this.side1[1].rotateAngleY = -PI / 3.0F;
      this.side2[1].rotateAngleY = PI / 3.0F;
      this.side1[2].rotateAngleY = -PI / 3.5F;
      this.side2[2].rotateAngleY = PI / 3.5F;
      if (middleSpine) {
         for(int i = 0; i < this.side1.length; ++i) {
            ModelRenderer var10000 = this.side1[i];
            var10000.rotateAngleY *= 0.98F;
            var10000 = this.side2[i];
            var10000.rotateAngleY *= 0.98F;
         }
      }

      ((ModelRendererScalable)this.side1[0]).setScale(1.0F);
      ((ModelRendererScalable)this.side2[0]).setScale(1.0F);
   }

   public void animate(float breatheAnim) {
      ModelRenderer var10000 = this.side1[1];
      var10000.rotateAngleY += breatheAnim * 0.02F;
      var10000 = this.side2[1];
      var10000.rotateAngleY -= breatheAnim * 0.02F;
   }
}
