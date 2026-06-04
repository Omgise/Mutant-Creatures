package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class FXSkull extends EntityFX {
   private float skullScale;
   private boolean up;
   private static final ResourceLocation particleTexture = new ResourceLocation("MutantCreatures:textures/particles.png");
   private static final ResourceLocation defaultParticleTexture = new ResourceLocation("textures/particle/particles.png");

   public FXSkull(World world, double x, double y, double z, double xx, double yy, double zz, boolean moveUp) {
      super(world, x, y, z, (double)0.0F, (double)0.0F, (double)0.0F);
      this.motionX *= (double)0.1F;
      this.motionY *= (double)0.1F;
      this.motionZ *= (double)0.1F;
      this.motionX += xx;
      this.motionY += yy;
      this.motionZ += zz;
      this.particleRed = this.particleGreen = this.particleBlue = 1.0F - (float)(Math.random() * 0.2);
      this.particleScale *= 1.0F;
      float scale = 0.4F + this.rand.nextFloat() * 0.6F;
      this.particleScale *= scale;
      this.skullScale = this.particleScale;
      this.particleMaxAge = (int)((double)8.0F / (Math.random() * 0.8 + 0.2));
      this.particleMaxAge = (int)((float)this.particleMaxAge * scale);
      this.noClip = true;
      this.up = moveUp;
      this.setParticleTextureIndex(0);
   }

   public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
      float timeScale = ((float)this.particleAge + par2) / (float)this.particleMaxAge * 32.0F;
      if (timeScale < 0.0F) {
         timeScale = 0.0F;
      }

      if (timeScale > 1.0F) {
         timeScale = 1.0F;
      }

      this.particleScale = this.skullScale * timeScale;
      TextureManager renderer = FMLClientHandler.instance().getClient().renderEngine;
      par1Tessellator.draw();
      renderer.bindTexture(particleTexture);
      par1Tessellator.startDrawingQuads();
      par1Tessellator.setBrightness(this.getBrightnessForRender(par2));
      par1Tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
      super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
      par1Tessellator.draw();
      renderer.bindTexture(defaultParticleTexture);
      par1Tessellator.startDrawingQuads();
   }

   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.setDead();
      }

      if (this.up) {
         this.motionY += 0.002;
      }

      this.moveEntity(this.motionX, this.motionY, this.motionZ);
      if (this.posY == this.prevPosY) {
         this.motionX *= 1.1;
         this.motionZ *= 1.1;
      }

      this.motionX *= (double)0.96F;
      this.motionY *= (double)0.96F;
      this.motionZ *= (double)0.96F;
   }
}
