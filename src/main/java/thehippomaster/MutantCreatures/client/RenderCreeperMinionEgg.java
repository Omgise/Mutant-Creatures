package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thehippomaster.MutantCreatures.MutantCreatures;

@SideOnly(Side.CLIENT)
public class RenderCreeperMinionEgg extends Render {
   protected RenderBlocks renderBlocks = new RenderBlocks();
   private static final ResourceLocation texture = new ResourceLocation("MutantCreatures:textures/blocks/CreeperEgg.png");

   public RenderCreeperMinionEgg() {
      this.renderBlocks.renderAllFaces = true;
      this.shadowSize = 0.4F;
   }

   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d, (float)d1, (float)d2);
      GL11.glScalef(1.5F, 1.5F, 1.5F);
      this.bindEntityTexture(entity);
      GL11.glDisable(2896);
      this.renderBlocks.blockAccess = entity.worldObj;
      Tessellator tess = Tessellator.instance;
      tess.startDrawingQuads();
      int x = MathHelper.floor_double(entity.posX);
      int y = MathHelper.floor_double(entity.posY);
      int z = MathHelper.floor_double(entity.posZ);
      tess.setTranslation((double)((float)(-x) - 0.5F), (double)((float)(-y) - 0.5F), (double)((float)(-z) - 0.5F));
      Block block = Blocks.bedrock;
      MutantCreatures.renderSmallEgg(this.renderBlocks, block, x, y, z);
      tess.setTranslation((double)0.0F, (double)0.0F, (double)0.0F);
      tess.draw();
      GL11.glEnable(2896);
      GL11.glPopMatrix();
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return texture;
   }
}
