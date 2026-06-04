package thehippomaster.MutantCreatures;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class SkeletonPart extends Entity {
   public float prevRotYaw;
   public float prevRotPitch;
   public float rotYaw;
   public float rotPitch;
   private boolean yawPositive;
   private boolean pitchPositive;
   private MutantSkeleton owner;
   @SideOnly(Side.CLIENT)
   private double velocityX;
   @SideOnly(Side.CLIENT)
   private double velocityY;
   @SideOnly(Side.CLIENT)
   private double velocityZ;

   public SkeletonPart(World world, MutantSkeleton skeleton, int bodyPart) {
      this(world);
      this.owner = skeleton;
      this.setPosition(skeleton.posX, skeleton.posY + (double)(skeleton.height * (0.25F + this.rand.nextFloat() * 0.5F)), skeleton.posZ);
      this.setBodyPart(bodyPart);
   }

   public SkeletonPart(World world) {
      super(world);
      this.yOffset += 0.16F;
      this.prevRotYaw = this.rotYaw = this.rand.nextFloat() * 360.0F;
      this.prevRotPitch = this.rotPitch = this.rand.nextFloat() * 360.0F;
      this.ignoreFrustumCheck = true;
      this.yawPositive = this.rand.nextBoolean();
      this.pitchPositive = this.rand.nextBoolean();
      this.setSize(0.7F, 0.7F);
   }

   protected void entityInit() {
      this.dataWatcher.addObject(16, (byte)1);
   }

   public void setBodyPart(int i) {
      this.dataWatcher.updateObject(16, (byte)i);
   }

   public int getBodyPart() {
      return this.dataWatcher.getWatchableObjectByte(16);
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   public boolean canBePushed() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
      super.setPositionAndRotation2(par1, par3, par5, par7, par8, par9);
      this.motionX = this.velocityX;
      this.motionY = this.velocityY;
      this.motionZ = this.velocityZ;
   }

   @SideOnly(Side.CLIENT)
   public void setVelocity(double par1, double par3, double par5) {
      this.velocityX = this.motionX = par1;
      this.velocityY = this.motionY = par3;
      this.velocityZ = this.motionZ = par5;
   }

   protected void move() {
      if (this.ridingEntity == null) {
         this.prevPosX = this.posX;
         this.prevPosY = this.posY;
         this.prevPosZ = this.posZ;
         this.motionY -= 0.045;
         this.moveEntity(this.motionX, this.motionY, this.motionZ);
         this.motionX *= 0.96;
         this.motionY *= 0.96;
         this.motionZ *= 0.96;
         if (this.onGround) {
            this.motionX *= 0.7;
            this.motionY *= 0.7;
            this.motionZ *= 0.7;
         }

      }
   }

   public void onUpdate() {
      super.onUpdate();
      this.prevRotYaw = this.rotYaw;
      this.prevRotPitch = this.rotPitch;
      this.move();
      if (!this.onGround) {
         this.rotYaw += 10.0F * (float)(this.yawPositive ? 1 : -1);
         this.rotPitch += 15.0F * (float)(this.pitchPositive ? 1 : -1);

         for(Entity entity : MutantCreatures.getCollidingEntities(this, this.worldObj, this.boundingBox)) {
            if (entity instanceof EntityLivingBase) {
               DamageSource source = DamageSource.generic;
               if (this.owner != null) {
                  DamageSource.causeMobDamage(this.owner);
               }

               entity.attackEntityFrom(source, 4.0F + (float)this.rand.nextInt(4));
            }
         }
      }

   }

   public boolean interactFirst(EntityPlayer player) {
      if (!this.worldObj.isRemote) {
         int bodyPart = this.getBodyPart();
         ItemStack stack = null;
         if (bodyPart == 0) {
            stack = new ItemStack(MutantCreatures.skeletonPart, 1, 2);
         }

         if (bodyPart >= 1 && bodyPart < 19) {
            stack = new ItemStack(MutantCreatures.skeletonPart, 1, 1);
         }

         if (bodyPart == 19) {
            stack = new ItemStack(MutantCreatures.skeleArmorHead);
         }

         if (bodyPart >= 21 && bodyPart < 29) {
            stack = new ItemStack(MutantCreatures.skeletonPart, 1, 0);
         }

         if (bodyPart == 29 || bodyPart == 30) {
            stack = new ItemStack(MutantCreatures.skeletonPart, 1, 3);
         }

         EntityItem item = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, stack);
         item.delayBeforeCanPickup = 0;
         this.worldObj.spawnEntityInWorld(item);
      }

      this.setDead();
      return super.interactFirst(player);
   }

   public void writeEntityToNBT(NBTTagCompound tagcompound) {
      tagcompound.setShort("bodyPart", (short)this.getBodyPart());
      tagcompound.setFloat("rotYaw", this.rotYaw);
      tagcompound.setFloat("rotPitch", this.rotPitch);
   }

   public void readEntityFromNBT(NBTTagCompound tagcompound) {
      this.setBodyPart(tagcompound.getShort("bodyPart"));
      this.prevRotYaw = this.rotYaw = tagcompound.getFloat("rotYaw");
      this.prevRotPitch = this.rotPitch = tagcompound.getFloat("rotPitch");
   }
}
