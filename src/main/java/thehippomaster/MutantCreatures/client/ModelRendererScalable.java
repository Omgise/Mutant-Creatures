package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelRendererScalable extends ModelRenderer {
   private float scale = 1.0F;

   public ModelRendererScalable(ModelBase par1ModelBase, String par2Str) {
      super(par1ModelBase, par2Str);
   }

   public ModelRendererScalable(ModelBase par1ModelBase) {
      super(par1ModelBase);
   }

   public ModelRendererScalable(ModelBase par1ModelBase, int par2, int par3) {
      super(par1ModelBase, par2, par3);
   }

   public void setScale(float f) {
      this.scale = f;
   }

   public void render(float par1) {
      if (!this.isHidden && this.showModel) {
         GL11.glPushMatrix();
         GL11.glScalef(this.scale, this.scale, this.scale);
         super.render(par1);
         GL11.glPopMatrix();
      }

   }

   public void renderWithRotation(float par1) {
      if (!this.isHidden && this.showModel) {
         GL11.glPushMatrix();
         GL11.glScalef(this.scale, this.scale, this.scale);
         super.renderWithRotation(par1);
         GL11.glPopMatrix();
      }

   }

   public void postRender(float par1) {
      if (!this.isHidden && this.showModel) {
         GL11.glPushMatrix();
         GL11.glScalef(this.scale, this.scale, this.scale);
         super.postRender(par1);
         GL11.glPopMatrix();
      }

   }
}
