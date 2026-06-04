package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thehippomaster.MutantCreatures.SGolemBlock;

@SideOnly(Side.CLIENT)
public class RenderSGolemBlock extends Render {
   public RenderSGolemBlock() {
      this.shadowSize = 0.5F;
   }

   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      SGolemBlock block = (SGolemBlock)entity;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d, (float)d1, (float)d2);
      GL11.glRotatef(entity.rotationYaw, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(((float)entity.ticksExisted + f1) * 20.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(((float)entity.ticksExisted + f1) * 12.0F, 0.0F, 0.0F, -1.0F);
      this.bindEntityTexture(entity);
      GL11.glDisable(2896);
      this.field_147909_c.setRenderBoundsFromBlock(Blocks.ice);
      int x = MathHelper.floor_double(entity.posX);
      int y = MathHelper.floor_double(entity.posY);
      int z = MathHelper.floor_double(entity.posZ);
      this.field_147909_c.renderBlockSandFalling(Blocks.ice, entity.worldObj, x, y, z, 0);
      GL11.glEnable(2896);
      GL11.glPopMatrix();
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return TextureMap.locationBlocksTexture;
   }
}
