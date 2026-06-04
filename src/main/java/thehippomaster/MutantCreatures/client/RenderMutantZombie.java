package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thehippomaster.MutantCreatures.MutantZombie;

@SideOnly(Side.CLIENT)
public class RenderMutantZombie extends RenderLiving {
   private static final ResourceLocation texture = new ResourceLocation("MutantCreatures:textures/zombie.png");

   public RenderMutantZombie(ModelBase base, float f) {
      super(base, f);
   }

   protected void renderMutantZombie(MutantZombie zombie, double d, double d1, double d2, float f, float f1) {
      super.doRender(zombie, d, d1, d2, f, f1);
   }

   public void doRender(EntityLiving living, double d, double d1, double d2, float f, float f1) {
      this.renderMutantZombie((MutantZombie)living, d, d1, d2, f, f1);
   }

   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.renderMutantZombie((MutantZombie)entity, d, d1, d2, f, f1);
   }

   protected void renderModel(EntityLivingBase living, float f, float f1, float f2, float f3, float f4, float f5) {
      MutantZombie zombie = (MutantZombie)living;
      ModelMutantZombie model = (ModelMutantZombie)this.mainModel;
      if (zombie.vanishTick > 0) {
         GL11.glEnable(2977);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F - ((float)zombie.vanishTick + model.animTick) / (float)zombie.maxVanishTime() * 0.6F);
      }

      super.renderModel(living, f, f1, f2, f3, f4, f5);
      if (zombie.vanishTick > 0) {
         GL11.glDisable(3042);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   protected void preRenderCallback(EntityLivingBase living, float f) {
      GL11.glScalef(1.3F, 1.3F, 1.3F);
   }

   protected void rotateCorpse(EntityLivingBase living, float par2, float par3, float par4) {
      GL11.glRotatef(180.0F - par3, 0.0F, 1.0F, 0.0F);
      MutantZombie zombie = (MutantZombie)living;
      int pitch = Math.min(20, zombie.deathTick);
      boolean reviving = false;
      if (zombie.deathTick > zombie.maxDeathTime() - 40) {
         pitch = zombie.maxDeathTime() - zombie.deathTick;
         reviving = true;
      }

      if (pitch > 0) {
         float f = ((float)pitch + par4 - 1.0F) / 20.0F * 1.6F;
         if (reviving) {
            f = ((float)pitch - par4) / 40.0F * 1.6F;
         }

         f = MathHelper.sqrt_float(f);
         if (f > 1.0F) {
            f = 1.0F;
         }

         GL11.glRotatef(f * this.getDeathMaxRotation(zombie), -1.0F, 0.0F, 0.0F);
      }

   }

   protected float getDeathMaxRotation(EntityLivingBase living) {
      return 80.0F;
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return texture;
   }
}
