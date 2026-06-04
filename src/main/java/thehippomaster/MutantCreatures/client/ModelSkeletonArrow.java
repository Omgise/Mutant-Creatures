package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelSkeletonArrow extends ModelBase {
   public ModelRenderer stick = new ModelRenderer(this, 0, 0);
   public ModelRenderer point1;
   public ModelRenderer point2;
   public ModelRenderer point3;
   public ModelRenderer point4;
   public static final float PI = (float)Math.PI;

   public ModelSkeletonArrow() {
      this.stick.addBox(-0.5F, -0.5F, -13.0F, 1, 1, 26);
      this.stick.setRotationPoint(0.0F, 24.0F, 0.0F);
      this.point1 = new ModelRenderer(this, 0, 0);
      this.point1.addBox(-3.0F, -0.5F, 0.0F, 3, 1, 1, 0.25F);
      this.point1.setRotationPoint(0.0F, 0.0F, -12.0F);
      this.stick.addChild(this.point1);
      this.point2 = new ModelRenderer(this, 0, 0);
      this.point2.addBox(0.0F, -0.5F, 0.0F, 3, 1, 1, 0.251F);
      this.point2.setRotationPoint(0.0F, 0.0F, -12.0F);
      this.stick.addChild(this.point2);
      this.point3 = new ModelRenderer(this, 0, 2);
      this.point3.addBox(-0.5F, -3.0F, 0.0F, 1, 3, 1, 0.25F);
      this.point3.setRotationPoint(0.0F, 0.0F, -13.0F);
      this.stick.addChild(this.point3);
      this.point4 = new ModelRenderer(this, 0, 2);
      this.point4.addBox(-0.5F, 0.0F, 0.0F, 1, 3, 1, 0.251F);
      this.point4.setRotationPoint(0.0F, 0.0F, -13.0F);
      this.stick.addChild(this.point4);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      this.point1.rotateAngleY = ((float)Math.PI / 4F);
      this.point2.rotateAngleY = (-(float)Math.PI / 4F);
      this.point3.rotateAngleX = (-(float)Math.PI / 4F);
      this.point4.rotateAngleX = ((float)Math.PI / 4F);
      this.stick.render(f5);
   }
}
