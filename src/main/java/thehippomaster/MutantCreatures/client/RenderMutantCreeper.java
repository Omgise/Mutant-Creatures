package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thehippomaster.MutantCreatures.MutantCreeper;

@SideOnly(Side.CLIENT)
public class RenderMutantCreeper extends RenderLiving {
   protected ModelMutantCreeper chargedModel = new ModelMutantCreeper(2.0F);
   private static final ResourceLocation texture = new ResourceLocation("MutantCreatures:textures/creeper.png");
   private static final ResourceLocation armorTexture = new ResourceLocation("textures/entity/creeper/creeper_armor.png");

   public RenderMutantCreeper(ModelBase model, float shadow) {
      super(model, shadow);
   }

   protected void renderMutantCreeper(MutantCreeper creeper, double d, double d1, double d2, float f, float f1) {
      super.doRender(creeper, d, d1, d2, f, f1);
   }

   protected int renderCreeperPassModel(MutantCreeper creeper, int i, float tick) {
      if (creeper.getCharged() && creeper.deathTime <= 0) {
         if (i == 1) {
            float f = (float)creeper.ticksExisted + tick;
            this.bindTexture(armorTexture);
            GL11.glMatrixMode(5890);
            GL11.glLoadIdentity();
            float f1 = f * 0.01F;
            GL11.glTranslatef(f1, f1, 0.0F);
            this.setRenderPassModel(this.chargedModel);
            GL11.glMatrixMode(5888);
            GL11.glEnable(3042);
            GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
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

   protected void preRenderMutantCreeper(MutantCreeper creeper, float f) {
      float scale = 1.2F;
      if (creeper.deathTime > 0) {
         float f1 = (float)creeper.deathTime / (float)creeper.maxDeathTime();
         scale -= f1 * 0.4F;
      }

      GL11.glScalef(scale, scale, scale);
   }

   protected int mutantCreeperColor(MutantCreeper creeper, float f, float f1) {
      int a = creeper.getExplosionColor();
      int r = 255;
      int g = 255;
      int b = 255;
      if (creeper.getCharged()) {
         r = 160;
         g = 180;
      }

      return a << 24 | r << 16 | g << 8 | b;
   }

   public void doRenderLiving(EntityLiving living, double d, double d1, double d2, float f, float f1) {
      this.renderMutantCreeper((MutantCreeper)living, d, d1, d2, f, f1);
   }

   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.doRenderLiving((EntityLiving)entity, d, d1, d2, f, f1);
   }

   protected int shouldRenderPass(EntityLivingBase living, int i, float f) {
      return this.renderCreeperPassModel((MutantCreeper)living, i, f);
   }

   protected void preRenderCallback(EntityLivingBase living, float f) {
      this.preRenderMutantCreeper((MutantCreeper)living, f);
   }

   protected int getColorMultiplier(EntityLivingBase living, float f, float f1) {
      return this.mutantCreeperColor((MutantCreeper)living, f, f1);
   }

   protected float getDeathMaxRotation(EntityLivingBase living) {
      return 0.0F;
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return texture;
   }
}
