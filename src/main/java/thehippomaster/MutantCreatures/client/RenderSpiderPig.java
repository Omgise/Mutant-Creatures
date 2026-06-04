package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thehippomaster.MutantCreatures.SpiderPig;

@SideOnly(Side.CLIENT)
public class RenderSpiderPig extends RenderLiving {
   private static final ResourceLocation texture = new ResourceLocation("MutantCreatures:textures/spiderpig.png");

   public RenderSpiderPig(float f) {
      super(new ModelSpiderPig(), f);
   }

   public void renderSpiderPig(SpiderPig pig, double d, double d1, double d2, float f, float f1) {
      super.doRender(pig, d, d1, d2, f, f1);
   }

   public void doRender(EntityLiving living, double d, double d1, double d2, float f, float f1) {
      this.renderSpiderPig((SpiderPig)living, d, d1, d2, f, f1);
   }

   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.renderSpiderPig((SpiderPig)entity, d, d1, d2, f, f1);
   }

   protected void preRenderCallback(EntityLivingBase living, float f) {
      float scale = 1.2F;
      GL11.glScalef(scale, scale, scale);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return texture;
   }
}
