package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import thehippomaster.MutantCreatures.CreeperMinion;

@SideOnly(Side.CLIENT)
public class ModelCreeperMinion extends ModelCreeper {
   private boolean sitting;
   public static final float PI = (float)Math.PI;

   public ModelCreeperMinion() {
      this(0.0F);
   }

   public ModelCreeperMinion(float f) {
      super(f);
      this.sitting = false;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      if (entity instanceof CreeperMinion) {
         CreeperMinion minion = (CreeperMinion)entity;
         this.sitting = minion.getSitting();
      } else {
         this.sitting = false;
      }

      super.render(entity, f, f1, f2, f3, f4, f5);
   }

   public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
      super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.head.rotationPointY = 4.0F;
      this.body.rotationPointY = 4.0F;
      this.leg1.setRotationPoint(-2.0F, 16.0F, 4.0F);
      this.leg2.setRotationPoint(2.0F, 16.0F, 4.0F);
      this.leg3.setRotationPoint(-2.0F, 16.0F, -4.0F);
      this.leg4.setRotationPoint(2.0F, 16.0F, -4.0F);
      if (this.sitting) {
         ModelRenderer var10000 = this.head;
         var10000.rotationPointY += 8.0F;
         var10000 = this.body;
         var10000.rotationPointY += 8.0F;
         var10000 = this.leg1;
         var10000.rotationPointY += 6.0F;
         var10000 = this.leg1;
         var10000.rotationPointZ -= 2.0F;
         var10000 = this.leg2;
         var10000.rotationPointY += 6.0F;
         var10000 = this.leg2;
         var10000.rotationPointZ -= 2.0F;
         var10000 = this.leg3;
         var10000.rotationPointY += 6.0F;
         var10000 = this.leg3;
         var10000.rotationPointZ += 2.0F;
         var10000 = this.leg4;
         var10000.rotationPointY += 6.0F;
         var10000 = this.leg4;
         var10000.rotationPointZ += 2.0F;
         this.leg1.rotateAngleX = ((float)Math.PI / 2F);
         this.leg2.rotateAngleX = ((float)Math.PI / 2F);
         this.leg3.rotateAngleX = (-(float)Math.PI / 2F);
         this.leg4.rotateAngleX = (-(float)Math.PI / 2F);
      }

   }
}
