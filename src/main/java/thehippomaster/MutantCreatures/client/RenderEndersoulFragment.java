package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thehippomaster.MutantCreatures.EndersoulFragment;

@SideOnly(Side.CLIENT)
public class RenderEndersoulFragment extends Render {
   private ModelEndersoulFragment modelRod = new ModelEndersoulFragment();
   private static final ResourceLocation texture = new ResourceLocation("MutantCreatures:textures/endersoulorb.png");

   protected void renderSoulRod(EndersoulFragment orb, double d, double d1, double d2, float f, float f1) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d, (float)d1 - 1.9F, (float)d2);
      float scale = 1.6F;
      GL11.glScalef(scale, scale, scale);
      this.bindEntityTexture(orb);
      GL11.glDisable(2896);
      GL11.glMatrixMode(5890);
      GL11.glLoadIdentity();
      float add = ((float)orb.ticksExisted + f1) * 0.008F;
      GL11.glTranslatef(add, add, 0.0F);
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
      this.modelRod.render(orb);
      GL11.glMatrixMode(5890);
      GL11.glLoadIdentity();
      GL11.glMatrixMode(5888);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.renderSoulRod((EndersoulFragment)entity, d, d1, d2, f, f1);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return texture;
   }
}
