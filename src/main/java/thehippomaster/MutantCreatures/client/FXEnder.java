package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class FXEnder extends EntityFX {
   private float fullScale;
   private boolean up;

   public FXEnder(World world, double x, double y, double z, double xx, double yy, double zz, boolean moveUp) {
      super(world, x, y, z, (double)0.0F, (double)0.0F, (double)0.0F);
      this.particleMaxAge = (int)(Math.random() * (double)15.0F) + 10;
      this.motionX *= (double)0.1F;
      this.motionY *= (double)0.1F;
      this.motionZ *= (double)0.1F;
      this.motionX += xx;
      this.motionY += yy;
      this.motionZ += zz;
      this.fullScale = this.particleScale = this.rand.nextFloat() * 0.4F + 2.4F;
      this.particleRed = this.particleGreen = this.particleBlue = this.rand.nextFloat() * 0.6F + 0.4F;
      this.particleGreen *= 0.3F;
      this.particleRed *= 0.9F;
      this.noClip = true;
      this.up = moveUp;
      this.setParticleTextureIndex((int)(Math.random() * (double)8.0F));
   }

   public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
      float scale = 1.0F - ((float)this.particleAge + par2) / (float)this.particleMaxAge;
      scale *= scale;
      scale = 1.0F - scale;
      this.particleScale = this.fullScale * scale;
      super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
   }

   public int getBrightnessForRender(float par1) {
      int i = super.getBrightnessForRender(par1);
      float f = (float)this.particleAge / (float)this.particleMaxAge;
      f = f * f * f * f;
      int i1 = i & 255;
      int i2 = i >> 16 & 255;
      i2 += (int)(f * 15.0F * 16.0F);
      if (i2 > 240) {
         i2 = 240;
      }

      return i1 | i2 << 16;
   }

   public float getBrightness(float par1) {
      float f = super.getBrightness(par1);
      float f1 = (float)this.particleAge / (float)this.particleMaxAge;
      f1 = f1 * f1 * f1 * f1;
      return f * (1.0F - f1) + f1;
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

      this.motionX *= 0.9;
      this.motionY *= 0.9;
      this.motionZ *= 0.9;
   }
}
