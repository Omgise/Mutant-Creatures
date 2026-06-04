package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import thehippomaster.AnimationAPI.client.ModelJoint;

@SideOnly(Side.CLIENT)
public class ModelSkeletonPart extends ModelBase {
   public ModelRenderer pelvis;
   public ModelRenderer waist;
   public SkeletonSpine[] spine;
   public ModelJoint head;
   public ModelRenderer jaw;
   public ModelJoint arm1;
   public ModelJoint arm2;
   public ModelJoint forearm1;
   public ModelJoint forearm2;
   public ModelJoint leg1;
   public ModelJoint leg2;
   public ModelJoint foreleg1;
   public ModelJoint foreleg2;
   public ModelRenderer shoulder1;
   public ModelRenderer shoulder2;

   public ModelSkeletonPart() {
      this.textureWidth = 128;
      this.textureHeight = 128;
      this.pelvis = new ModelRenderer(this, 0, 16);
      this.pelvis.addBox(-4.0F, -3.0F, -3.0F, 8, 6, 6);
      this.spine = new SkeletonSpine[3];

      for(int i = 0; i < this.spine.length; ++i) {
         this.spine[i] = new SkeletonSpine(this, true);
         this.boxList.remove(this.spine[i].middle);
      }

      this.head = new ModelJoint(this, 0, 0);
      this.head.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.4F);
      this.jaw = new ModelRenderer(this, 72, 0);
      this.jaw.addBox(-4.0F, -3.0F, -8.0F, 8, 3, 8, 0.7F);
      this.jaw.setRotationPoint(0.0F, 3.8F, 3.7F);
      this.head.addChild(this.jaw);
      this.arm1 = new ModelJoint(this, 0, 28);
      this.arm1.addBox(-2.0F, -6.0F, -2.0F, 4, 12, 4);
      this.arm2 = new ModelJoint(this, 0, 28);
      this.arm2.mirror = true;
      this.arm2.addBox(-2.0F, -6.0F, -2.0F, 4, 12, 4);
      this.forearm1 = new ModelJoint(this, 16, 28);
      this.forearm1.addBox(-2.0F, -7.0F, -2.0F, 4, 14, 4, -0.01F);
      this.forearm2 = new ModelJoint(this, 16, 28);
      this.forearm2.mirror = true;
      this.forearm2.addBox(-2.0F, -7.0F, -2.0F, 4, 14, 4, -0.01F);
      this.leg1 = new ModelJoint(this, 0, 28);
      this.leg1.addBox(-2.0F, -6.0F, -2.0F, 4, 12, 4);
      this.leg2 = new ModelJoint(this, 0, 28);
      this.leg2.mirror = true;
      this.leg2.addBox(-2.0F, -6.0F, -2.0F, 4, 12, 4);
      this.foreleg1 = new ModelJoint(this, 32, 28);
      this.foreleg1.addBox(-2.0F, -6.0F, -2.0F, 4, 12, 4);
      this.foreleg2 = new ModelJoint(this, 32, 28);
      this.foreleg2.mirror = true;
      this.foreleg2.addBox(-2.0F, -6.0F, -2.0F, 4, 12, 4);
      this.shoulder1 = new ModelRenderer(this, 28, 16);
      this.shoulder1.addBox(-4.0F, -1.5F, -3.0F, 8, 3, 6);
      this.shoulder2 = new ModelRenderer(this, 28, 16);
      this.shoulder2.mirror = true;
      this.shoulder2.addBox(-4.0F, -1.5F, -3.0F, 8, 3, 6);
   }

   public void setAngles() {
      this.jaw.rotateAngleX = 0.09817477F;

      for(int i = 0; i < this.spine.length; ++i) {
         this.spine[i].setAngles((float)Math.PI, i == 1);
      }

   }

   public ModelRenderer getSkeletonPart(int i) {
      return (ModelRenderer)this.boxList.get(i);
   }
}
