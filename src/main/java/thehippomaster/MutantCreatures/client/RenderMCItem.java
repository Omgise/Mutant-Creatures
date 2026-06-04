package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderMCItem extends Render {
   private int itemDamage;
   private Item theItem;
   private IIcon itemIcon;

   public RenderMCItem(Item item, int damage) {
      this.theItem = item;
      this.itemDamage = damage;
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      if (this.itemIcon == null) {
         this.itemIcon = this.theItem.getIconFromDamage(this.itemDamage);
      }

      GL11.glPushMatrix();
      GL11.glTranslatef((float)par2, (float)par4, (float)par6);
      GL11.glEnable(32826);
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      this.bindEntityTexture(par1Entity);
      Tessellator var10 = Tessellator.instance;
      this.renderIcon(var10, this.itemIcon);
      GL11.glDisable(32826);
      GL11.glPopMatrix();
   }

   private void renderIcon(Tessellator par1Tessellator, IIcon icon) {
      float var3 = icon.getMinU();
      float var4 = icon.getMaxU();
      float var5 = icon.getMinV();
      float var6 = icon.getMaxV();
      float var7 = 1.0F;
      float var8 = 0.5F;
      float var9 = 0.25F;
      GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      par1Tessellator.startDrawingQuads();
      par1Tessellator.setNormal(0.0F, 1.0F, 0.0F);
      par1Tessellator.addVertexWithUV((double)(0.0F - var8), (double)(0.0F - var9), (double)0.0F, (double)var3, (double)var6);
      par1Tessellator.addVertexWithUV((double)(var7 - var8), (double)(0.0F - var9), (double)0.0F, (double)var4, (double)var6);
      par1Tessellator.addVertexWithUV((double)(var7 - var8), (double)(var7 - var9), (double)0.0F, (double)var4, (double)var5);
      par1Tessellator.addVertexWithUV((double)(0.0F - var8), (double)(var7 - var9), (double)0.0F, (double)var3, (double)var5);
      par1Tessellator.draw();
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return TextureMap.locationItemsTexture;
   }
}
