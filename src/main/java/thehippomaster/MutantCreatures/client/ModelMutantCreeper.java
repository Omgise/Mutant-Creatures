package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelMutantCreeper extends ModelBase {
   public ModelRenderer pelvis;
   public ModelRenderer body;
   public ModelRenderer neck;
   public ModelRenderer head;
   public ModelRenderer frleg;
   public ModelRenderer flleg;
   public ModelRenderer frforeleg;
   public ModelRenderer flforeleg;
   public ModelRenderer brleg;
   public ModelRenderer blleg;
   public ModelRenderer brforeleg;
   public ModelRenderer blforeleg;
   public static final float PI = (float)Math.PI;

   public ModelMutantCreeper() {
      this(0.0F);
   }

   public ModelMutantCreeper(float f) {
      this.textureWidth = 128;
      this.textureHeight = 64;
      this.pelvis = new ModelRenderer(this, 0, 0);
      this.pelvis.addBox(-5.0F, -14.0F, -4.0F, 10, 14, 8, f);
      this.pelvis.setRotationPoint(0.0F, 14.0F, -3.0F);
      this.body = new ModelRenderer(this, 36, 0);
      this.body.addBox(-4.5F, -14.0F, -3.5F, 9, 16, 7, f);
      this.body.setRotationPoint(0.0F, -12.0F, 0.0F);
      this.pelvis.addChild(this.body);
      this.neck = new ModelRenderer(this, 68, 0);
      this.neck.addBox(-4.0F, -14.0F, -3.0F, 8, 14, 6, f);
      this.neck.setRotationPoint(0.0F, -11.0F, 1.0F);
      this.body.addChild(this.neck);
      this.head = new ModelRenderer(this, 0, 22);
      this.head.addBox(-5.0F, -12.0F, -5.0F, 10, 12, 10, f);
      this.head.setRotationPoint(0.0F, -12.0F, 1.0F);
      this.neck.addChild(this.head);
      this.frleg = new ModelRenderer(this, 40, 24);
      this.frleg.addBox(-3.0F, -4.0F, -14.0F, 6, 4, 14, f);
      this.frleg.setRotationPoint(3.0F, 0.0F, 0.0F);
      this.pelvis.addChild(this.frleg);
      this.flleg = new ModelRenderer(this, 40, 24);
      this.flleg.mirror = true;
      this.flleg.addBox(-3.0F, -4.0F, -14.0F, 6, 4, 14, f);
      this.flleg.setRotationPoint(-3.0F, 0.0F, 0.0F);
      this.pelvis.addChild(this.flleg);
      this.frforeleg = new ModelRenderer(this, 96, 0);
      this.frforeleg.addBox(-3.5F, 0.0F, -4.0F, 7, 20, 8, f);
      this.frforeleg.setRotationPoint(0.0F, -4.0F, -14.0F);
      this.frleg.addChild(this.frforeleg);
      this.flforeleg = new ModelRenderer(this, 96, 0);
      this.flforeleg.mirror = true;
      this.flforeleg.addBox(-3.5F, 0.0F, -4.0F, 7, 20, 8, f);
      this.flforeleg.setRotationPoint(0.0F, -4.0F, -14.0F);
      this.flleg.addChild(this.flforeleg);
      this.brleg = new ModelRenderer(this, 0, 44);
      this.brleg.addBox(-2.0F, -4.0F, 0.0F, 4, 4, 14, f);
      this.brleg.setRotationPoint(2.0F, -2.0F, 4.0F);
      this.pelvis.addChild(this.brleg);
      this.blleg = new ModelRenderer(this, 0, 44);
      this.blleg.mirror = true;
      this.blleg.addBox(-2.0F, -4.0F, 0.0F, 4, 4, 14, f);
      this.blleg.setRotationPoint(-2.0F, -2.0F, 4.0F);
      this.pelvis.addChild(this.blleg);
      this.brforeleg = new ModelRenderer(this, 80, 28);
      this.brforeleg.addBox(-3.0F, 0.0F, -3.0F, 6, 18, 6, f);
      this.brforeleg.setRotationPoint(0.0F, -4.0F, 14.0F);
      this.brleg.addChild(this.brforeleg);
      this.blforeleg = new ModelRenderer(this, 80, 28);
      this.blforeleg.mirror = true;
      this.blforeleg.addBox(-3.0F, 0.0F, -3.0F, 6, 18, 6, f);
      this.blforeleg.setRotationPoint(0.0F, -4.0F, 14.0F);
      this.blleg.addChild(this.blforeleg);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      this.setAngles();
      this.setRotationAngles(f, f1, f2, f3, f4, f5);
      this.pelvis.render(f5);
   }

   public void setAngles() {
      this.pelvis.rotationPointY = 14.0F;
      this.pelvis.rotateAngleX = (-(float)Math.PI / 4F);
      this.body.rotateAngleX = 0.9424778F;
      this.body.rotateAngleY = 0.0F;
      this.neck.rotateAngleX = ((float)Math.PI / 3F);
      this.head.rotateAngleX = ((float)Math.PI / 6F);
      this.frleg.rotateAngleX = ((float)Math.PI / 10F);
      this.frleg.rotateAngleY = (-(float)Math.PI / 4F);
      this.frleg.rotateAngleZ = 0.0F;
      this.flleg.rotateAngleX = ((float)Math.PI / 10F);
      this.flleg.rotateAngleY = ((float)Math.PI / 4F);
      this.flleg.rotateAngleZ = 0.0F;
      this.frforeleg.rotateAngleX = -0.20943952F;
      this.frforeleg.rotateAngleY = ((float)Math.PI / 8F);
      this.flforeleg.rotateAngleX = -0.20943952F;
      this.flforeleg.rotateAngleY = (-(float)Math.PI / 8F);
      this.brleg.rotateAngleX = ((float)Math.PI / 3F);
      this.brleg.rotateAngleY = ((float)Math.PI / 5F);
      this.brleg.rotateAngleZ = 0.0F;
      this.blleg.rotateAngleX = ((float)Math.PI / 3F);
      this.blleg.rotateAngleY = (-(float)Math.PI / 5F);
      this.blleg.rotateAngleZ = 0.0F;
      this.brforeleg.rotateAngleX = 0.48332196F;
      this.blforeleg.rotateAngleX = 0.48332196F;
   }

   public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
      float breatheAnim = MathHelper.sin(f2 * 0.1F);
      float walkAnim1 = (MathHelper.sin(f * (float)Math.PI / 4.0F) + 0.4F) * f1;
      float walkAnim2 = (MathHelper.sin(f * (float)Math.PI / 4.0F + (float)Math.PI) + 0.4F) * f1;
      if (walkAnim1 < 0.0F) {
         walkAnim1 = 0.0F;
      }

      if (walkAnim2 < 0.0F) {
         walkAnim2 = 0.0F;
      }

      float walkAnim3 = MathHelper.sin(f * (float)Math.PI / 8.0F) * f1;
      float walkAnim4 = (MathHelper.sin(f * (float)Math.PI / 4.0F + ((float)Math.PI / 2F)) + 0.4F) * f1;
      float walkAnim5 = (MathHelper.sin(f * (float)Math.PI / 4.0F + ((float)Math.PI * 1.5F)) + 0.4F) * f1;
      if (walkAnim4 < 0.0F) {
         walkAnim4 = 0.0F;
      }

      if (walkAnim5 < 0.0F) {
         walkAnim5 = 0.0F;
      }

      float walkAnim6 = MathHelper.sin(f * (float)Math.PI / 8.0F + ((float)Math.PI / 2F)) * f1;
      float faceYaw = f3 / (180F / (float)Math.PI);
      float facePitch = f4 / (180F / (float)Math.PI);
      float f6 = faceYaw / 3.0F;
      float f7 = facePitch / 3.0F;
      ModelRenderer var10000 = this.pelvis;
      var10000.rotationPointY += MathHelper.sin(f * (float)Math.PI / 4.0F) * f1 * 0.5F;
      var10000 = this.body;
      var10000.rotateAngleX += breatheAnim * 0.02F;
      var10000 = this.body;
      var10000.rotateAngleX += f7;
      var10000 = this.body;
      var10000.rotateAngleY += f6;
      var10000 = this.neck;
      var10000.rotateAngleX += breatheAnim * 0.02F;
      var10000 = this.neck;
      var10000.rotateAngleX += f7;
      this.neck.rotateAngleY = f6;
      var10000 = this.head;
      var10000.rotateAngleX += breatheAnim * 0.02F;
      var10000 = this.head;
      var10000.rotateAngleX += f7;
      this.head.rotateAngleY = f6;
      var10000 = this.frleg;
      var10000.rotateAngleX -= walkAnim1 * 0.3F;
      var10000 = this.frleg;
      var10000.rotateAngleY += walkAnim3 * 0.2F;
      var10000 = this.frleg;
      var10000.rotateAngleZ += walkAnim3 * 0.2F;
      var10000 = this.flleg;
      var10000.rotateAngleX -= walkAnim2 * 0.3F;
      var10000 = this.flleg;
      var10000.rotateAngleY -= walkAnim3 * 0.2F;
      var10000 = this.flleg;
      var10000.rotateAngleZ -= walkAnim3 * 0.2F;
      var10000 = this.brleg;
      var10000.rotateAngleX += walkAnim5 * 0.3F;
      var10000 = this.brleg;
      var10000.rotateAngleY -= walkAnim6 * 0.2F;
      var10000 = this.brleg;
      var10000.rotateAngleZ -= walkAnim6 * 0.2F;
      var10000 = this.blleg;
      var10000.rotateAngleX += walkAnim4 * 0.3F;
      var10000 = this.blleg;
      var10000.rotateAngleY += walkAnim6 * 0.2F;
      var10000 = this.blleg;
      var10000.rotateAngleZ += walkAnim6 * 0.2F;
      if (this.onGround > -9990.0F) {
         float swingAnim = MathHelper.sin(this.onGround * (float)Math.PI);
         var10000 = this.body;
         var10000.rotateAngleX += swingAnim * (float)Math.PI / 3.0F;
         var10000 = this.neck;
         var10000.rotateAngleX -= swingAnim * (float)Math.PI / 4.0F;
      }

   }
}
