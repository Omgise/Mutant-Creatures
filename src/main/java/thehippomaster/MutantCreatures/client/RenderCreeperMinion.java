package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thehippomaster.MutantCreatures.CreeperMinion;

@SideOnly(Side.CLIENT)
public class RenderCreeperMinion extends RenderCreeper {
   private ModelCreeperMinion poweredModel;
   private static final ResourceLocation armorTexture = new ResourceLocation("textures/entity/creeper/creeper_armor.png");

   public RenderCreeperMinion() {
      this.shadowSize = 0.2F;
      this.mainModel = new ModelCreeperMinion();
      this.poweredModel = new ModelCreeperMinion(2.0F);
   }

   protected void preRenderCallback(EntityCreeper creeper, float par2) {
      float f = creeper.getCreeperFlashIntensity(par2);
      float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
      if (f < 0.0F) {
         f = 0.0F;
      }

      if (f > 1.0F) {
         f = 1.0F;
      }

      f *= f;
      f *= f;
      float f2 = (1.0F + f * 0.4F) * f1;
      float f3 = (1.0F + f * 0.1F) / f1;
      f2 *= 0.5F;
      f3 *= 0.5F;
      GL11.glScalef(f2, f3, f2);
   }

   protected int shouldRenderPass(EntityCreeper creeper, int i, float tick) {
      if (creeper.getPowered()) {
         if (i == 1) {
            float f = (float)creeper.ticksExisted + tick;
            this.bindTexture(armorTexture);
            GL11.glMatrixMode(5890);
            GL11.glLoadIdentity();
            float f1 = f * 0.01F;
            GL11.glTranslatef(f1, f1, 0.0F);
            this.setRenderPassModel(this.poweredModel);
            GL11.glMatrixMode(5888);
            GL11.glEnable(3042);
            float f3 = 0.5F;
            GL11.glColor4f(f3, f3, f3, 1.0F);
            GL11.glDisable(2896);
            GL11.glBlendFunc(1, 1);
            return 1;
         }

         if (i == 2) {
            GL11.glMatrixMode(5890);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(5888);
            GL11.glEnable(2896);
            GL11.glDisable(3042);
         }
      }

      return -1;
   }

   protected void passSpecialRender(EntityLivingBase living, double x, double y, double z) {
      CreeperMinion minion = (CreeperMinion)living;
      if (minion.getTamed() && minion.getShowName()) {
         this.func_147906_a(minion, minion.getName(), x, y, z, 40);
      }

   }
}
