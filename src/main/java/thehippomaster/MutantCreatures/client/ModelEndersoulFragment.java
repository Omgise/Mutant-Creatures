package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import thehippomaster.MutantCreatures.EndersoulFragment;

@SideOnly(Side.CLIENT)
public class ModelEndersoulFragment extends ModelBase {
   public ModelRenderer base = new ModelRenderer(this);
   public ModelRenderer[] sticks;

   public ModelEndersoulFragment() {
      this.base.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4);
      this.base.setRotationPoint(0.0F, 22.0F, 0.0F);
      this.sticks = new ModelRenderer[8];

      for(int i = 0; i < this.sticks.length; ++i) {
         this.sticks[i] = new ModelRenderer(this);
         if (i < this.sticks.length / 2) {
            this.sticks[i].addBox(-0.5F, -4.0F, -0.5F, 1, 8, 1);
         } else {
            this.sticks[i].addBox(-0.5F, -6.0F, -0.5F, 1, 10, 1, 0.15F);
         }

         this.base.addChild(this.sticks[i]);
      }

   }

   public void render(EndersoulFragment orb) {
      for(int i = 0; i < this.sticks.length; ++i) {
         this.sticks[i].rotateAngleX = orb.stickRotations[i][0];
         this.sticks[i].rotateAngleY = orb.stickRotations[i][1];
         this.sticks[i].rotateAngleZ = orb.stickRotations[i][2];
      }

      this.base.render(0.0625F);
   }
}
