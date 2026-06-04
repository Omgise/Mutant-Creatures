package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class ModelEnderHand extends ModelBase {
   public ModelRenderer hand;
   public ModelRenderer[] finger;
   public ModelRenderer[] foreFinger;
   public ModelRenderer thumb;
   public static final float PI = (float)Math.PI;

   public ModelEnderHand() {
      this.textureWidth = 32;
      this.textureHeight = 32;
      this.finger = new ModelRenderer[3];
      this.foreFinger = new ModelRenderer[3];
      this.hand = new ModelRenderer(this);
      this.hand.setRotationPoint(0.0F, 17.5F, 0.0F);
      float fingerScale = 0.6F;

      for(int i = 0; i < this.finger.length; ++i) {
         this.finger[i] = new ModelRenderer(this, i * 4, 0);
         this.finger[i].addBox(-0.5F, 0.0F, -0.5F, 1, i == 1 ? 6 : 5, 1, fingerScale);
      }

      this.finger[0].setRotationPoint(-0.5F, 0.0F, -1.0F);
      this.finger[1].setRotationPoint(-0.5F, 0.0F, 0.0F);
      this.finger[2].setRotationPoint(-0.5F, 0.0F, 1.0F);

      for(int i = 0; i < this.foreFinger.length; ++i) {
         this.foreFinger[i] = new ModelRenderer(this, 1 + i * 5, 0);
         this.foreFinger[i].addBox(-0.5F, 0.0F, -0.5F, 1, i == 1 ? 6 : 5, 1, fingerScale - 0.01F);
         this.foreFinger[i].setRotationPoint(0.0F, 0.5F + (float)(i == 1 ? 6 : 5), 0.0F);
      }

      for(int i = 0; i < this.finger.length; ++i) {
         this.hand.addChild(this.finger[i]);
         this.finger[i].addChild(this.foreFinger[i]);
      }

      this.thumb = new ModelRenderer(this, 14, 0);
      this.thumb.addBox(-0.5F, 0.0F, -0.5F, 1, 5, 1, fingerScale);
      this.thumb.setRotationPoint(0.5F, 0.0F, -0.5F);
      this.hand.addChild(this.thumb);
   }

   private void resetAngles(ModelRenderer model) {
      model.rotateAngleX = 0.0F;
      model.rotateAngleY = 0.0F;
      model.rotateAngleZ = 0.0F;
   }

   public void setAngles() {
      this.resetAngles(this.hand);

      for(int i = 0; i < this.finger.length; ++i) {
         this.resetAngles(this.finger[i]);
         this.resetAngles(this.foreFinger[i]);
      }

      this.resetAngles(this.thumb);
      this.hand.rotateAngleY = (-(float)Math.PI / 8F);
      this.finger[0].rotateAngleX = -0.2617994F;
      this.finger[1].rotateAngleZ = 0.17453294F;
      this.finger[2].rotateAngleX = 0.2617994F;
      this.foreFinger[0].rotateAngleZ = -0.2617994F;
      this.foreFinger[1].rotateAngleZ = (-(float)Math.PI / 8F);
      this.foreFinger[2].rotateAngleZ = -0.2617994F;
      this.thumb.rotateAngleX = (-(float)Math.PI / 5F);
      this.thumb.rotateAngleZ = (-(float)Math.PI / 8F);
   }

   public void render() {
      this.setAngles();
      this.hand.render(0.0625F);
   }
}
