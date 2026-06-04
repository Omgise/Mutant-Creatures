package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thehippomaster.MutantCreatures.SkeletonPart;

@SideOnly(Side.CLIENT)
public class RenderSkeletonPart extends Render {
   private ModelSkeletonPart modelSkelePart = new ModelSkeletonPart();
   private static final ResourceLocation texture = new ResourceLocation("MutantCreatures:textures/skeleton.png");

   public RenderSkeletonPart() {
      for(int i = this.modelSkelePart.boxList.size() - 1; i >= 0; --i) {
         ModelRenderer renderer = (ModelRenderer)this.modelSkelePart.boxList.get(i);
         if (renderer.cubeList.isEmpty()) {
            this.modelSkelePart.boxList.remove(i);
         }
      }

   }

   public void renderSkeletonPart(SkeletonPart part, double d, double d1, double d2, float f, float f1) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d, (float)d1, (float)d2);
      float yaw = part.prevRotYaw + (part.rotYaw - part.prevRotYaw) * f1;
      float pitch = part.prevRotPitch + (part.rotPitch - part.prevRotPitch) * f1;
      GL11.glRotatef(yaw, 0.2F, 0.9F, -0.1F);
      GL11.glRotatef(pitch, 0.9F, 0.1F, 0.2F);
      GL11.glEnable(32826);
      GL11.glScalef(1.2F, -1.2F, -1.2F);
      this.bindEntityTexture(part);
      int bodyPart = part.getBodyPart();
      this.modelSkelePart.setAngles();
      ModelRenderer renderer = this.modelSkelePart.getSkeletonPart(bodyPart);
      renderer.render(0.0625F);
      GL11.glDisable(32826);
      GL11.glPopMatrix();
   }

   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.renderSkeletonPart((SkeletonPart)entity, d, d1, d2, f, f1);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return texture;
   }
}
