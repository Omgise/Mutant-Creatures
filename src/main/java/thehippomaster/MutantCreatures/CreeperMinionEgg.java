package thehippomaster.MutantCreatures;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CreeperMinionEgg extends Entity {
   public int health = this.getMaxHealth();
   public int age;
   public float rotationRoll;
   protected int recentlyHit;
   @SideOnly(Side.CLIENT)
   protected double velX;
   @SideOnly(Side.CLIENT)
   protected double velY;
   @SideOnly(Side.CLIENT)
   protected double velZ;

   public CreeperMinionEgg(World world) {
      super(world);
      int minToTick = 1200;
      this.age = (60 + this.rand.nextInt(40)) * minToTick;
      this.rotationRoll = 0.0F;
      this.preventEntitySpawning = true;
      this.setSize(0.5625F, 0.75F);
   }

   protected void entityInit() {
      this.dataWatcher.addObject(2, "");
   }

   public void setOwner(String name) {
      this.dataWatcher.updateObject(2, name);
   }

   public String getOwner() {
      return this.dataWatcher.getWatchableObjectString(2);
   }

   public double getYOffset() {
      return this.ridingEntity != null && this.ridingEntity instanceof EntityPlayer ? (double)-1.0625F : (double)this.yOffset;
   }

   public double getMountedYOffset() {
      return (double)this.height;
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   public AxisAlignedBB getCollisionBox(Entity entity) {
      return entity.boundingBox;
   }

   public AxisAlignedBB getBoundingBox() {
      return this.ridingEntity != null ? null : this.boundingBox;
   }

   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   public boolean canBePushed() {
      return !this.isDead;
   }

   public float getShadowSize() {
      return 0.0F;
   }

   public int getMaxHealth() {
      return 8;
   }

   @SideOnly(Side.CLIENT)
   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int i) {
      super.setPositionAndRotation2(x, y, z, yaw, pitch, i);
      this.motionX = this.velX;
      this.motionY = this.velY;
      this.motionZ = this.velZ;
   }

   @SideOnly(Side.CLIENT)
   public void setVelocity(double x, double y, double z) {
      super.setVelocity(x, y, z);
      this.velX = x;
      this.velY = y;
      this.velZ = z;
   }

   protected void move() {
      if (this.ridingEntity == null) {
         this.prevPosX = this.posX;
         this.prevPosY = this.posY;
         this.prevPosZ = this.posZ;
         this.motionY -= (double)0.04F;
         this.moveEntity(this.motionX, this.motionY, this.motionZ);
         this.motionX *= (double)0.98F;
         this.motionY *= (double)0.98F;
         this.motionZ *= (double)0.98F;
         if (this.onGround) {
            this.motionX *= (double)0.7F;
            this.motionZ *= (double)0.7F;
         }

      }
   }

   public void hatchEgg() {
      CreeperMinion minion = new CreeperMinion(this.worldObj);
      minion.setOwner(this.getOwner());
      minion.setSitting(true);
      minion.setHealth(minion.getMaxHealth());
      minion.setPosition(this.posX, this.posY, this.posZ);
      this.worldObj.spawnEntityInWorld(minion);
      this.worldObj.playSoundAtEntity(minion, "MutantCreatures:mutantcreeper.egghatch", 0.7F, 0.9F + this.rand.nextFloat() * 0.1F);
      int x = MathHelper.floor_double(this.posX);
      int y = MathHelper.floor_double(this.boundingBox.minY);
      int z = MathHelper.floor_double(this.posZ);
      this.setDead();
   }

   public void onUpdate() {
      super.onUpdate();
      this.move();
      if (!this.worldObj.isRemote) {
         --this.age;
         if (this.health < this.getMaxHealth() && this.ticksExisted - this.recentlyHit > 80 && this.ticksExisted % 20 == 0) {
            ++this.health;
         }

         if (this.age <= 0 && this.ridingEntity == null) {
            this.hatchEgg();
         }
      }

   }

   public static void entityMountEntity(Entity entity, Entity entity1) {
      if (entity != null && !entity.worldObj.isRemote) {
         entity.mountEntity(entity1);
      }
   }

   protected void removeTopEgg() {
      if (this.riddenByEntity != null && this.riddenByEntity instanceof CreeperMinionEgg) {
         CreeperMinionEgg egg = (CreeperMinionEgg)this.riddenByEntity;
         egg.removeTopEgg();
      } else {
         this.mountEntity((Entity)null);
      }
   }

   protected void mountEgg(CreeperMinionEgg egg) {
      if (egg.riddenByEntity == null) {
         this.mountEntity(egg);
      } else if (!(egg.riddenByEntity instanceof CreeperMinionEgg)) {
         entityMountEntity(egg.riddenByEntity, (Entity)null);
         this.mountEntity(egg);
      } else {
         CreeperMinionEgg eggRiding = (CreeperMinionEgg)egg.riddenByEntity;
         this.mountEgg(eggRiding);
      }
   }

   public boolean interactFirst(EntityPlayer player) {
      boolean mount = false;
      Entity entity = player.riddenByEntity;
      if (!this.worldObj.isRemote) {
         if (entity == null) {
            this.mountEntity(player);
            mount = true;
         } else if (entity instanceof CreeperMinionEgg) {
            if (entity == this) {
               this.removeTopEgg();
            } else {
               this.mountEgg((CreeperMinionEgg)entity);
               mount = true;
            }
         }

         this.worldObj.playSoundAtEntity(player, "random.pop", 0.7F, (mount ? 0.6F : 0.3F) + this.rand.nextFloat() * 0.1F);
      }

      return super.interactFirst(player);
   }

   public boolean attackEntityFrom(DamageSource source, float f) {
      if (this.ridingEntity != null) {
         return false;
      } else {
         Entity entity = source.getEntity();
         if (entity == null || !(entity instanceof MutantCreeper) && !(entity instanceof CreeperMinion)) {
            if (source.isExplosion()) {
               if (!this.worldObj.isRemote) {
                  this.age = (int)((float)this.age - f * 80.0F);
               }

               if (!this.worldObj.isRemote) {
                  MCHandler.spawnHeartsAtEntity(this, (int)(f / 2.0F));
               }

               return false;
            } else {
               this.recentlyHit = this.ticksExisted;
               this.motionY = (double)0.2F;
               if (!this.worldObj.isRemote) {
                  this.health = (int)((float)this.health - f);
               }

               if (this.health <= 0) {
                  if (!this.worldObj.isRemote) {
                     this.worldObj.createExplosion((Entity)null, this.posX, this.posY, this.posZ, 0.0F, true);
                     if (this.rand.nextInt(3) == 0) {
                        this.dropItem(MutantCreatures.creeperShard, 1);
                     } else {
                        for(int j = 5 + this.rand.nextInt(6); j > 0; --j) {
                           this.dropItem(Items.gunpowder, 1);
                        }
                     }
                  }

                  this.setDead();
               }

               return true;
            }
         } else {
            return false;
         }
      }
   }

   protected void writeEntityToNBT(NBTTagCompound tagcompound) {
      tagcompound.setInteger("health", this.health);
      tagcompound.setInteger("age", this.age);
      tagcompound.setString("owner", this.getOwner());
   }

   protected void readEntityFromNBT(NBTTagCompound tagcompound) {
      this.health = tagcompound.getInteger("health");
      this.age = tagcompound.getInteger("age");
      this.setOwner(tagcompound.getString("owner"));
   }
}
