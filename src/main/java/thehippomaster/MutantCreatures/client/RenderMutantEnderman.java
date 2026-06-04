package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thehippomaster.MutantCreatures.MutantEnderman;

@SideOnly(Side.CLIENT)
public class RenderMutantEnderman extends RenderLiving {
   private boolean teleportAttack = false;
   private ModelMutantEnderman endermanModel;
   private ModelEnderman cloneModel;
   private static final ResourceLocation texture = new ResourceLocation("MutantCreatures:textures/enderman.png");
   private static final ResourceLocation blankTexture = new ResourceLocation("MutantCreatures:textures/blank.png");
   private static final ResourceLocation glowTexture = new ResourceLocation("MutantCreatures:textures/enderman_glow.png");
   private static final ResourceLocation cloneTexture = new ResourceLocation("MutantCreatures:textures/enderman_clone.png");
   private static final ResourceLocation shuffleTexture = new ResourceLocation("MutantCreatures:textures/enderman_shuffle.png");

   public RenderMutantEnderman(float f) {
      super(new ModelMutantEnderman(), f);
      this.endermanModel = (ModelMutantEnderman)this.mainModel;
      this.cloneModel = new ModelEnderman();
      this.cloneModel.isAttacking = true;
      this.setRenderPassModel(this.endermanModel);
   }

   public void renderMutantEnderman(MutantEnderman enderman, double d, double d1, double d2, float f, float f1) {
      this.teleportAttack = false;
      double addX = (double)0.0F;
      double addZ = (double)0.0F;
      this.mainModel = (ModelBase)(enderman.currentAttackID == 6 ? this.cloneModel : this.endermanModel);
      this.setRenderPassModel(this.mainModel);
      boolean forcedLook = enderman.currentAttackID == 3;
      boolean scream = enderman.currentAttackID == 5;
      boolean telesmash = enderman.currentAttackID == 7 && enderman.animTick < 18;
      boolean death = enderman.currentAttackID == 10;
      if (forcedLook || scream || telesmash || death) {
         double scale = 0.03;
         if (enderman.animTick >= 40 && !death) {
            scale *= (double)0.5F;
         }

         if (death) {
            if (enderman.animTick < 80) {
               scale = (double)0.02F;
            } else {
               scale = (double)0.05F;
            }
         }

         addX = enderman.getRNG().nextGaussian() * scale;
         addZ = enderman.getRNG().nextGaussian() * scale;
      }

      super.doRender(enderman, d + addX, d1, d2 + addZ, f, f1);
      if (enderman.currentAttackID == 4) {
         this.teleportAttack = true;
         double x = (double)enderman.teleX + (double)0.5F;
         double y = (double)enderman.teleY;
         double z = (double)enderman.teleZ + (double)0.5F;
         RenderManager var10003 = this.renderManager;
         double var10002 = x - RenderManager.renderPosX;
         RenderManager var10004 = this.renderManager;
         double var25 = y - RenderManager.renderPosY;
         RenderManager var10005 = this.renderManager;
         super.doRender(enderman, var10002, var25, z - RenderManager.renderPosZ, f, f1);
      }

   }

   protected void renderEndermanModel(MutantEnderman enderman, float par2, float par3, float par4, float par5, float par6, float par7) {
      if (enderman.deathTick > 80) {
         float blendFactor = (float)(enderman.deathTick - 80) / (float)(enderman.maxDeathTick() - 80);
         GL11.glDepthFunc(515);
         GL11.glEnable(3008);
         GL11.glAlphaFunc(516, blendFactor);
         this.bindTexture(shuffleTexture);
         this.mainModel.render(enderman, par2, par3, par4, par5, par6, par7);
         GL11.glAlphaFunc(516, 0.1F);
         GL11.glDepthFunc(514);
      }

      this.bindEntityTexture(enderman);
      this.mainModel.render(enderman, par2, par3, par4, par5, par6, par7);
      GL11.glDepthFunc(515);
   }

   protected void renderCarrying(MutantEnderman enderman, float par2) {
      super.renderEquippedItems(enderman, par2);
      GL11.glEnable(32826);

      for(int i = 1; i < enderman.heldBlock.length; ++i) {
         if (enderman.heldBlock[i] != 0) {
            GL11.glPushMatrix();
            this.postRenderArm(0.0625F, i);
            GL11.glTranslatef(0.0F, 1.2F, 0.0F);
            float var10000 = (float)enderman.ticksExisted;
            float var10001 = (float)i * 2.0F;
            ModelMutantEnderman var10002 = this.endermanModel;
            float tick = var10000 + var10001 * (float)Math.PI + par2;
            GL11.glRotatef(tick * 10.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(tick * 8.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(tick * 6.0F, 0.0F, 0.0F, 1.0F);
            float f = 0.75F;
            GL11.glScalef(-f, -f, f);
            int var4 = enderman.getBrightnessForRender(par2);
            int var5 = var4 % 65536;
            int var6 = var4 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var5, (float)var6);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.bindTexture(TextureMap.locationBlocksTexture);
            this.field_147909_c.renderBlockAsItem(Block.getBlockById(enderman.heldBlock[i]), enderman.heldBlockData[i], 1.0F);
            GL11.glPopMatrix();
         }
      }

      GL11.glDisable(32826);
   }

   protected void postRenderArm(float scale, int armID) {
      this.endermanModel.pelvis.postRender(scale);
      this.endermanModel.abdomen.postRender(scale);
      this.endermanModel.chest.postRender(scale);
      this.endermanModel.getArmFromID(armID).postRender(scale);
   }

   protected int renderGlowModels(MutantEnderman enderman, int state, float animTick) {
      if (enderman.isInvisible()) {
         GL11.glDepthMask(false);
      } else {
         GL11.glDepthMask(true);
      }

      if (state == 0 && enderman.currentAttackID != 6) {
         GL11.glDisable(2896);
         this.bindTexture(glowTexture);
         int var5 = 61680;
         int var6 = var5 % 65536;
         int var7 = var5 / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6, (float)var7);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         return 1;
      } else {
         if (state == 1 && enderman.currentAttackID != 6) {
            GL11.glEnable(2896);
         } else if (state != 2) {
            if (state == 3) {
               boolean teleport = enderman.currentAttackID == 4 && enderman.animTick < 10;
               boolean scream = enderman.currentAttackID == 5;
               boolean clone = enderman.currentAttackID == 6;
               if (teleport || scream || clone) {
                  GL11.glMatrixMode(5890);
                  GL11.glLoadIdentity();
                  GL11.glMatrixMode(5888);
                  GL11.glDisable(3042);
               }
            }
         } else {
            boolean teleport = enderman.currentAttackID == 4 && enderman.animTick < 10;
            boolean scream = enderman.currentAttackID == 5;
            boolean clone = enderman.currentAttackID == 6;
            if (teleport || scream || clone) {
               GL11.glDisable(2896);
               this.bindTexture(cloneTexture);
               GL11.glMatrixMode(5890);
               GL11.glLoadIdentity();
               float f = ((float)enderman.ticksExisted + animTick) * 0.008F;
               GL11.glTranslatef(f, f, 0.0F);
               GL11.glMatrixMode(5888);
               GL11.glEnable(2977);
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
               int var5 = 61680;
               int var6 = var5 % 65536;
               int var7 = var5 / 65536;
               OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6, (float)var7);
               float alpha = 1.0F;
               if (teleport) {
                  if (!this.teleportAttack && enderman.animTick >= 8) {
                     alpha -= ((float)(enderman.animTick - 8) + animTick) / 2.0F;
                  }

                  if (this.teleportAttack && enderman.animTick < 2) {
                     alpha = ((float)enderman.animTick + animTick) / 2.0F;
                  }
               }

               if (scream) {
                  if (enderman.animTick < 40) {
                     alpha = ((float)enderman.animTick + animTick) / 40.0F;
                  } else if (enderman.animTick >= 160) {
                     alpha = 1.0F - ((float)enderman.animTick + animTick) / 40.0F;
                  }
               }

               GL11.glColor4f(0.9F, 0.3F, 1.0F, alpha);
               GL11.glEnable(2896);
               float scale = 0.0F;
               if (teleport) {
                  scale = 1.2F + ((float)enderman.animTick + animTick) / 10.0F;
                  if (this.teleportAttack) {
                     scale = 2.2F - ((float)enderman.animTick + animTick) / 10.0F;
                  }
               }

               if (scream) {
                  if (enderman.animTick < 40) {
                     scale = 1.2F + ((float)enderman.animTick + animTick) / 40.0F;
                  } else if (enderman.animTick < 160) {
                     scale = 2.2F;
                  } else {
                     scale = 2.2F - ((float)enderman.animTick + animTick) / 10.0F;
                  }
               }

               if (clone) {
                  GL11.glScalef(1.25F, 1.25F, 1.25F);
               } else {
                  GL11.glScalef(scale, scale * 0.8F, scale);
               }

               return 1;
            }
         }

         return -1;
      }
   }

   protected void renderModel(EntityLivingBase living, float par2, float par3, float par4, float par5, float par6, float par7) {
      this.renderEndermanModel((MutantEnderman)living, par2, par3, par4, par5, par6, par7);
   }

   protected void renderEquippedItems(EntityLivingBase living, float par2) {
      this.renderCarrying((MutantEnderman)living, par2);
   }

   protected int shouldRenderPass(EntityLivingBase living, int par2, float par3) {
      return this.renderGlowModels((MutantEnderman)living, par2, par3);
   }

   protected int inheritRenderPass(EntityLivingBase living, int par2, float par3) {
      return -1;
   }

   public void doRender(EntityLiving living, double d, double d1, double d2, float f, float f1) {
      this.renderMutantEnderman((MutantEnderman)living, d, d1, d2, f, f1);
   }

   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.doRender((EntityLiving)entity, d, d1, d2, f, f1);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      MutantEnderman enderman = (MutantEnderman)entity;
      return enderman.currentAttackID == 6 ? blankTexture : texture;
   }
}
