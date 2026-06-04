package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thehippomaster.MutantCreatures.SkeletonShot;

@SideOnly(Side.CLIENT)
public class RenderSkeletonShot extends Render {
   private ModelSkeletonArrow arrowModel = new ModelSkeletonArrow();
   private static final ResourceLocation texture = new ResourceLocation("MutantCreatures:textures/skeletonarrow.png");

   public void renderSkeletonShot(SkeletonShot shot, double d, double d1, double d2, float f, float f1) {
      GL11.glPushMatrix();
      GL11.glEnable(2977);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glTranslatef((float)d, (float)d1, (float)d2);
      this.bindEntityTexture(shot);

      for(int i = 0; i < shot.getClones(); ++i) {
         GL11.glPushMatrix();
         float scale = shot.getSpeed() - (float)i * 0.08F;
         double x = (shot.getTargetX() - shot.posX) * (double)((float)shot.ticksExisted + f1) * (double)scale;
         double y = (shot.getTargetY() - shot.posY) * (double)((float)shot.ticksExisted + f1) * (double)scale;
         double z = (shot.getTargetZ() - shot.posZ) * (double)((float)shot.ticksExisted + f1) * (double)scale;
         GL11.glTranslatef((float)x, (float)y, (float)z);
         GL11.glRotatef(shot.rotationYaw, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(shot.rotationPitch, 1.0F, 0.0F, 0.0F);
         GL11.glScalef(1.2F, 1.2F, 1.2F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F - (float)i * 0.08F);
         this.arrowModel.render(shot, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
         GL11.glPopMatrix();
      }

      GL11.glDisable(3042);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.renderSkeletonShot((SkeletonShot)entity, d, d1, d2, f, f1);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return texture;
   }
}
