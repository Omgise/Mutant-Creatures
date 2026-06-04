package thehippomaster.MutantCreatures.ai;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.DamageSource;
import thehippomaster.MutantCreatures.MutantSkeleton;

public class MCAISkeleMelee extends EntityAIBase {
   private MutantSkeleton mutantSkeleton;
   private EntityLivingBase attackTarget;

   public MCAISkeleMelee(MutantSkeleton skele) {
      this.mutantSkeleton = skele;
      this.attackTarget = null;
      this.setMutexBits(7);
   }

   public boolean shouldExecute() {
      this.attackTarget = this.mutantSkeleton.getAttackTarget();
      if (this.attackTarget != null && (this.mutantSkeleton.onGround || this.mutantSkeleton.isInWater())) {
         return this.mutantSkeleton.currentAttackID == 1;
      } else {
         return false;
      }
   }

   public void startExecuting() {
      this.mutantSkeleton.animTick = 0;
   }

   public boolean continueExecuting() {
      return this.mutantSkeleton.animTick < 14;
   }

   public void updateTask() {
      if (this.attackTarget.isEntityAlive()) {
         this.mutantSkeleton.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
      }

      if (this.mutantSkeleton.animTick == 3) {
         List list = this.mutantSkeleton.worldObj.getEntitiesWithinAABBExcludingEntity(this.mutantSkeleton, this.mutantSkeleton.boundingBox.expand((double)4.0F, (double)4.0F, (double)4.0F));

         for(int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity)list.get(i);
            if (entity instanceof EntityLivingBase) {
               double dist = (double)this.mutantSkeleton.getDistanceToEntity(entity);
               if (!(dist > (double)(2.3F + this.mutantSkeleton.getRNG().nextFloat() * 0.3F))) {
                  double x = this.mutantSkeleton.posX - entity.posX;
                  double z = this.mutantSkeleton.posZ - entity.posZ;

                  float rot;
                  for(rot = (float)(Math.atan2(z, x) * (double)180.0F / Math.PI) + 90.0F; rot > this.mutantSkeleton.rotationYawHead + 180.0F; rot -= 360.0F) {
                  }

                  while(rot <= this.mutantSkeleton.rotationYawHead - 180.0F) {
                     rot += 360.0F;
                  }

                  if (Math.abs(this.mutantSkeleton.rotationYawHead - rot) < 60.0F) {
                     entity.attackEntityFrom(DamageSource.causeMobDamage(this.mutantSkeleton), (float)(2 + this.mutantSkeleton.getRNG().nextInt(2)));
                     float power = 1.8F + (float)this.mutantSkeleton.getRNG().nextInt(5) * 0.15F;
                     entity.motionX = -x / dist * (double)power;
                     entity.motionY = Math.max((double)0.28F, entity.motionY);
                     entity.motionZ = -z / dist * (double)power;
                  }
               }
            }
         }

         this.mutantSkeleton.worldObj.playSoundAtEntity(this.mutantSkeleton, "random.bow", 1.0F, 1.0F / (this.mutantSkeleton.getRNG().nextFloat() * 0.4F + 1.2F));
      }

   }

   public void resetTask() {
      this.mutantSkeleton.sendAttackPacket(0);
   }
}
