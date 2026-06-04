package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thehippomaster.MutantCreatures.MutantSnowGolem;

@SideOnly(Side.CLIENT)
public class RenderMutantSnowGolem extends RenderLiving {
   private ModelMutantSnowGolem golemModel;
   private static final ResourceLocation texture = new ResourceLocation("MutantCreatures:textures/snowman.png");
   private static final ResourceLocation glowTexture = new ResourceLocation("MutantCreatures:textures/snowman_glow.png");

   public RenderMutantSnowGolem(float f) {
      super(new ModelMutantSnowGolem(), f);
      this.golemModel = (ModelMutantSnowGolem)this.mainModel;
      this.setRenderPassModel(this.golemModel);
   }

   public void renderMutantSnowGolem(MutantSnowGolem golem, double d, double d1, double d2, float f, float f1) {
      super.doRender(golem, d, d1, d2, f, f1);
   }

   protected void renderEquippedItems(MutantSnowGolem golem, float par2) {
      super.renderEquippedItems(golem, par2);
      if (golem.throwAttack && golem.throwTick < 7) {
         GL11.glEnable(32826);
         GL11.glPushMatrix();
         GL11.glTranslatef(0.4F, 0.0F, 0.0F);
         this.postRenderArm(0.0625F);
         GL11.glTranslatef(0.0F, 0.9F, 0.0F);
         float f = 0.8F;
         GL11.glScalef(-f, -f, f);
         int var4 = golem.getBrightnessForRender(par2);
         int var5 = var4 % 65536;
         int var6 = var4 / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var5, (float)var6);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.bindTexture(TextureMap.locationBlocksTexture);
         this.field_147909_c.renderBlockAsItem(Blocks.ice, 0, 1.0F);
         GL11.glPopMatrix();
         GL11.glDisable(32826);
      }

   }

   protected void postRenderArm(float scale) {
      this.golemModel.pelvis.postRender(scale);
      this.golemModel.abdomen.postRender(scale);
      this.golemModel.chest.postRender(scale);
      this.golemModel.arm1.postRender(scale);
      this.golemModel.arm1.getModel().postRender(scale);
      this.golemModel.forearm1.postRender(scale);
      this.golemModel.forearm1.getModel().postRender(scale);
   }

   protected int shouldRenderPass(MutantSnowGolem golem, int state, float animTick) {
      if (golem.isInvisible()) {
         GL11.glDepthMask(false);
      } else {
         GL11.glDepthMask(true);
      }

      if (state == 1) {
         GL11.glDisable(2896);
         this.bindTexture(glowTexture);
         float f = (float)golem.ticksExisted + animTick;
         float f1 = MathHelper.cos(f * 0.1F);
         float f2 = MathHelper.cos(f * 0.15F);
         char var5 = '\uf0f0';
         int var6 = var5 % 65536;
         int var7 = var5 / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6, (float)var7);
         GL11.glColor4f(1.0F, 0.8F + 0.05F * f2, 0.15F + 0.2F * f1, 1.0F);
         return 1;
      } else {
         if (state == 2) {
            GL11.glEnable(2896);
         }

         return -1;
      }
   }

   protected void passSpecialRender(EntityLivingBase living, double par2, double par4, double par6) {
      super.passSpecialRender(living, par2, par4, par6);
      MutantSnowGolem golem = (MutantSnowGolem)living;
      if (golem.owner != null) {
         this.func_147906_a(golem, golem.owner.getCommandSenderName(), par2, par4, par6, 64);
      }

   }

   protected void renderEquippedItems(EntityLivingBase living, float par2) {
      this.renderEquippedItems((MutantSnowGolem)living, par2);
   }

   protected int shouldRenderPass(EntityLivingBase living, int par2, float par3) {
      return this.shouldRenderPass((MutantSnowGolem)living, par2, par3);
   }

   protected int inheritRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3) {
      return -1;
   }

   public void doRender(EntityLiving living, double d, double d1, double d2, float f, float f1) {
      this.renderMutantSnowGolem((MutantSnowGolem)living, d, d1, d2, f, f1);
   }

   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.renderMutantSnowGolem((MutantSnowGolem)entity, d, d1, d2, f, f1);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return texture;
   }
}
