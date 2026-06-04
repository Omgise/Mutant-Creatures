package thehippomaster.MutantCreatures;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.item.ChemicalX;

public class SkullSpirit extends Entity {
   public EntityLivingBase target;
   private int startTick;
   private int grabTick;

   public SkullSpirit(World world) {
      this(world, (EntityLiving)null);
   }

   public SkullSpirit(World world, EntityLiving living) {
      super(world);
      this.startTick = 15;
      this.grabTick = 80 + this.rand.nextInt(40);
      this.target = living;
      this.noClip = true;
      this.setSize(0.1F, 0.1F);
   }

   protected void entityInit() {
      this.dataWatcher.addObject(16, (byte)0);
   }

   protected void setGrabbed(boolean flag) {
      this.dataWatcher.updateObject(16, (byte)(flag ? 1 : 0));
   }

   protected boolean getGrabbed() {
      return this.dataWatcher.getWatchableObjectByte(16) == 1;
   }

   public void onUpdate() {
      if (this.target != null && this.target.isEntityAlive() && !(this.target instanceof EntityPlayer)) {
         if (this.getGrabbed()) {
            if (!this.worldObj.isRemote) {
               --this.grabTick;
               this.target.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
               this.target.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
               if (this.grabTick <= 0) {
                  float yaw = this.target.rotationYaw;
                  this.target.setPosition(this.posX, (double)0.0F, this.posZ);
                  this.target.setDead();
                  this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 2.0F, false);
                  EntityLivingBase living = ChemicalX.getMutantOf(this.target);
                  if (living != null && this.rand.nextInt(2) == 0) {
                     living.rotationYaw = yaw;
                     living.setPosition(this.posX, this.posY, this.posZ);
                     this.worldObj.spawnEntityInWorld(living);
                  }

                  this.setDead();
               }
            }

            this.setPosition(this.target.posX, this.target.posY, this.target.posZ);
            if (this.rand.nextInt(8) == 0) {
               this.target.attackEntityFrom(DamageSource.magic, 0.0F);
            }

            MutantCreatures.proxy.spawnSkullParticles(this, false);
         } else {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionX = (double)0.0F;
            this.motionY = (double)0.0F;
            this.motionZ = (double)0.0F;
            if (this.startTick-- >= 0) {
               this.motionY += (double)(0.3F * (float)this.startTick / 15.0F);
            }

            double x = this.target.posX - this.posX;
            double y = this.target.posY - this.posY;
            double z = this.target.posZ - this.posZ;
            double d = Math.sqrt(x * x + y * y + z * z);
            this.motionX += x / d * (double)0.2F;
            this.motionY += y / d * (double)0.2F;
            this.motionZ += z / d * (double)0.2F;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            if (!this.worldObj.isRemote && this.getDistanceSqToEntity(this.target) < (double)1.0F) {
               this.setGrabbed(true);
            }

            MutantCreatures.proxy.spawnSkullParticles(this, true);
         }
      } else {
         this.setDead();
      }
   }

   public Random getRNG() {
      return this.rand;
   }

   public void writeEntityToNBT(NBTTagCompound tagCompound) {
      tagCompound.setFloat("posX", (float)this.posX);
      tagCompound.setFloat("posY", (float)this.posY);
      tagCompound.setFloat("posZ", (float)this.posZ);
   }

   public void readEntityFromNBT(NBTTagCompound tagCompound) {
      this.posX = (double)tagCompound.getFloat("posX");
      this.posY = (double)tagCompound.getFloat("posY");
      this.posZ = (double)tagCompound.getFloat("posZ");
   }

   @SideOnly(Side.CLIENT)
   public float getShadowSize() {
      return 0.0F;
   }
}
