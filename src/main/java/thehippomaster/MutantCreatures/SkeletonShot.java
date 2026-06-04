package thehippomaster.MutantCreatures;

import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class SkeletonShot extends Entity {
   private int shotDamage;
   private EntityLivingBase shooter;
   private ArrayList<Entity> pointedEntities;
   private PotionEffect potionEffect;

   public SkeletonShot(World world) {
      super(world);
      this.shotDamage = 10 + this.rand.nextInt(3);
      this.ignoreFrustumCheck = true;
      this.noClip = true;
      this.shooter = null;
      this.pointedEntities = new ArrayList();
      this.potionEffect = null;
   }

   public SkeletonShot(World world, EntityLivingBase shooter, EntityLivingBase target) {
      this(world);
      this.shooter = shooter;
      if (!world.isRemote) {
         this.setTargetX(target.posX);
         this.setTargetY(target.posY);
         this.setTargetZ(target.posZ);
      }

      double yPos = shooter.posY + (double)shooter.getEyeHeight();
      if (shooter instanceof MutantSkeleton) {
         yPos = shooter.posY + (double)(shooter.height * 0.4F);
      }

      this.setPosition(shooter.posX, yPos, shooter.posZ);
      double x = this.getTargetX() - this.posX;
      double y = this.getTargetY() - this.posY;
      double z = this.getTargetZ() - this.posZ;
      double d = Math.sqrt(x * x + z * z);
      this.rotationYaw = 180.0F + (float)Math.toDegrees(Math.atan2(x, z));
      this.rotationPitch = (float)Math.toDegrees(Math.atan2(y, d));
   }

   protected void entityInit() {
      this.dataWatcher.addObject(5, 0);
      this.dataWatcher.addObject(6, 0);
      this.dataWatcher.addObject(7, 0);
      this.dataWatcher.addObject(8, 12);
      this.dataWatcher.addObject(9, (byte)10);
   }

   public void setTargetX(double d) {
      this.dataWatcher.updateObject(5, (int)(d * (double)10000.0F));
   }

   public void setTargetY(double d) {
      this.dataWatcher.updateObject(6, (int)(d * (double)10000.0F));
   }

   public void setTargetZ(double d) {
      this.dataWatcher.updateObject(7, (int)(d * (double)10000.0F));
   }

   public void setSpeed(float f) {
      this.dataWatcher.updateObject(8, (int)(f * 10.0F));
   }

   public void setClones(int i) {
      this.dataWatcher.updateObject(9, (byte)i);
   }

   public double getTargetX() {
      return (double)this.dataWatcher.getWatchableObjectInt(5) / (double)10000.0F;
   }

   public double getTargetY() {
      return (double)this.dataWatcher.getWatchableObjectInt(6) / (double)10000.0F;
   }

   public double getTargetZ() {
      return (double)this.dataWatcher.getWatchableObjectInt(7) / (double)10000.0F;
   }

   public float getSpeed() {
      return (float)this.dataWatcher.getWatchableObjectInt(8) / 10.0F;
   }

   public int getClones() {
      return this.dataWatcher.getWatchableObjectByte(9);
   }

   public void randomize(float scale) {
      this.setTargetX(this.getTargetX() + (double)((this.rand.nextFloat() - 0.5F) * scale * 2.0F));
      this.setTargetY(this.getTargetY() + (double)((this.rand.nextFloat() - 0.5F) * scale * 2.0F));
      this.setTargetZ(this.getTargetZ() + (double)((this.rand.nextFloat() - 0.5F) * scale * 2.0F));
   }

   public void setDamage(int i) {
      this.shotDamage = i;
   }

   public void setPotionEffect(PotionEffect effect) {
      this.potionEffect = effect;
   }

   public void onUpdate() {
      super.onUpdate();
      double x = this.getTargetX() - this.posX;
      double y = this.getTargetY() - this.posY;
      double z = this.getTargetZ() - this.posZ;
      double d = Math.sqrt(x * x + z * z);
      this.rotationYaw = 180.0F + (float)Math.toDegrees(Math.atan2(x, z));
      if (this.rotationYaw > 360.0F) {
         this.rotationYaw -= 360.0F;
      }

      this.rotationPitch = (float)Math.toDegrees(Math.atan2(y, d));
      if (!this.worldObj.isRemote) {
         if (this.ticksExisted == 2) {
            this.hitEntities(0);
         }

         if (this.ticksExisted == 3) {
            this.hitEntities(32);
         }

         if (this.ticksExisted == 4) {
            this.handleEntities();
         }
      }

      if (this.ticksExisted > 10) {
         this.setDead();
      }

   }

   protected void hitEntities(int offset) {
      double targetX = this.getTargetX();
      double targetY = this.getTargetY();
      double targetZ = this.getTargetZ();
      double dist = this.getDistance(targetX, targetY, targetZ);
      double dx = (targetX - this.posX) / dist;
      double dy = (targetY - this.posY) / dist;
      double dz = (targetZ - this.posZ) / dist;

      for(int i = offset; i < offset + 32; ++i) {
         double x = this.posX + dx * (double)i * (double)0.5F;
         double y = this.posY + dy * (double)i * (double)0.5F;
         double z = this.posZ + dz * (double)i * (double)0.5F;
         AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y, z, x, y, z).expand(0.3, 0.3, 0.3);
         this.pointedEntities.addAll(MutantCreatures.getCollidingEntities(this.shooter, this.worldObj, box));
      }

   }

   protected void handleEntities() {
      this.pointedEntities.remove(this.shooter);
      this.pointedEntities.remove(this);
      ArrayList<Entity> list = new ArrayList();

      for(Entity entity : this.pointedEntities) {
         if (!list.contains(entity)) {
            list.add(entity);
         }
      }

      for(Entity entity : list) {
         entity.attackEntityFrom(DamageSource.causeMobDamage(this.shooter), (float)this.shotDamage);
         if (this.potionEffect != null && entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase)entity;
            living.addPotionEffect(this.potionEffect);
         }
      }

      this.pointedEntities.clear();
   }

   public void readEntityFromNBT(NBTTagCompound tagcompound) {
   }

   public void writeEntityToNBT(NBTTagCompound tagcompound) {
   }
}
