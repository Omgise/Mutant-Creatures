package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thehippomaster.MutantCreatures.EndermanClone;

@SideOnly(Side.CLIENT)
public class RenderEndermanClone extends RenderLiving {
   private static final ResourceLocation blankTexture = new ResourceLocation("MutantCreatures:textures/blank.png");
   private static final ResourceLocation texture = new ResourceLocation("MutantCreatures:textures/enderman_clone.png");

   public RenderEndermanClone() {
      super(new ModelEnderman(), 0.5F);
      this.setRenderPassModel(this.mainModel);
      ((ModelEnderman)this.mainModel).isAttacking = true;
   }

   public void renderClone(EndermanClone clone, double d, double d1, double d2, float f, float f1) {
      super.doRender(clone, d, d1, d2, f, f1);
   }

   protected int renderGlowModel(EndermanClone clone, int state, float animTick) {
      if (clone.isInvisible()) {
         GL11.glDepthMask(false);
      } else {
         GL11.glDepthMask(true);
      }

      if (state == 0) {
         GL11.glDisable(2896);
         this.bindTexture(texture);
         GL11.glMatrixMode(5890);
         GL11.glLoadIdentity();
         float f = ((float)clone.ticksExisted + animTick) * 0.008F;
         GL11.glTranslatef(f, f, 0.0F);
         GL11.glMatrixMode(5888);
         GL11.glEnable(2977);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         int var5 = 61680;
         int var6 = var5 % 65536;
         int var7 = var5 / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6, (float)var7);
         GL11.glColor4f(0.9F, 0.3F, 1.0F, 1.0F);
         GL11.glEnable(2896);
         return 1;
      } else {
         if (state == 1) {
            GL11.glMatrixMode(5890);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(5888);
            GL11.glDisable(3042);
         }

         return -1;
      }
   }

   protected int shouldRenderPass(EntityLivingBase living, int par2, float par3) {
      return this.renderGlowModel((EndermanClone)living, par2, par3);
   }

   protected int inheritRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3) {
      return -1;
   }

   public void doRender(EntityLiving living, double d, double d1, double d2, float f, float f1) {
      this.renderClone((EndermanClone)living, d, d1, d2, f, f1);
   }

   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.doRender((EntityLiving)entity, d, d1, d2, f, f1);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return blankTexture;
   }
}
