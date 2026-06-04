package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import thehippomaster.MutantCreatures.MutantSkeleton;

@SideOnly(Side.CLIENT)
public class RenderMutantSkeleton extends RenderLiving {
   private static final ResourceLocation texture = new ResourceLocation("MutantCreatures:textures/skeleton.png");

   public RenderMutantSkeleton(float f) {
      super(new ModelMutantSkeleton(), f);
   }

   public void renderMutantSkeleton(MutantSkeleton skele, double d, double d1, double d2, float f, float f1) {
      super.doRender(skele, d, d1, d2, f, f1);
   }

   public void doRender(EntityLiving living, double d, double d1, double d2, float f, float f1) {
      this.renderMutantSkeleton((MutantSkeleton)living, d, d1, d2, f, f1);
   }

   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.renderMutantSkeleton((MutantSkeleton)entity, d, d1, d2, f, f1);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return texture;
   }
}
