package thehippomaster.MutantCreatures;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class SGolemBlock extends EntityThrowable {
   public SGolemBlock(World world) {
      super(world);
      this.setSize(1.0F, 1.0F);
   }

   public SGolemBlock(World world, MutantSnowGolem golem) {
      super(world, golem);
      this.rotationYaw = golem.rotationYaw;
      this.setSize(1.0F, 1.0F);
   }

   protected float getGravityVelocity() {
      return 0.06F;
   }

   protected void onImpact(MovingObjectPosition mop) {
      List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)2.5F, (double)2.0F, (double)2.5F));
      list.remove(this.getThrower());

      for(int i = 0; i < list.size(); ++i) {
         Entity entity = (Entity)list.get(i);
         if (!(this.getDistanceSqToEntity(entity) > (double)6.25F)) {
            entity.attackEntityFrom(DamageSource.causeMobDamage(this.getThrower()), (float)(4 + this.rand.nextInt(3)));
         }
      }

      if (mop.typeOfHit == MovingObjectType.ENTITY) {
         MCHandler.spawnParticlesAtEntity(2, this, 0);
         mop.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.getThrower()), 4.0F);
      } else {
         this.spawnIceParticles();
      }

      float pitch = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.8F;
      this.worldObj.playSoundAtEntity(this, "dig.glass", 0.8F, pitch);
      this.setDead();
   }

   public void spawnIceParticles() {
      for(int i = 0; i < 60; ++i) {
         double x = this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width;
         double y = this.posY + (double)0.5F + (double)(this.rand.nextFloat() * this.height);
         double z = this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width;
         double motx = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 3.0F);
         double moty = (double)(0.5F + this.rand.nextFloat() * 2.0F);
         double motz = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 3.0F);
         this.worldObj.spawnParticle("blockcrack_79_0", x, y, z, motx, moty, motz);
      }

   }

   public void writeEntityToNBT(NBTTagCompound tagcompound) {
      super.writeEntityToNBT(tagcompound);
   }

   public void readEntityFromNBT(NBTTagCompound tagcompound) {
      super.readEntityFromNBT(tagcompound);
   }
}
