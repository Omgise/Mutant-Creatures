package thehippomaster.MutantCreatures.ai;

import java.util.ArrayList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import thehippomaster.MutantCreatures.MutantSkeleton;
import thehippomaster.MutantCreatures.SkeletonShot;

public class MCAISkeleMulti extends EntityAIBase {
   private MutantSkeleton mutantSkeleton;
   private EntityLivingBase attackTarget;
   private ArrayList<SkeletonShot> shotList;

   public MCAISkeleMulti(MutantSkeleton skeleton) {
      this.mutantSkeleton = skeleton;
      this.attackTarget = null;
      this.shotList = new ArrayList();
      this.setMutexBits(7);
   }

   public boolean shouldExecute() {
      this.attackTarget = this.mutantSkeleton.getAttackTarget();
      if (this.attackTarget != null && (this.mutantSkeleton.onGround || this.mutantSkeleton.isInWater())) {
         if (this.mutantSkeleton.getDistanceSqToEntity(this.attackTarget) < (double)16.0F) {
            return false;
         } else {
            return this.mutantSkeleton.currentAttackID == 0 && this.mutantSkeleton.getRNG().nextInt(26) == 0;
         }
      } else {
         return false;
      }
   }

   public void startExecuting() {
      this.mutantSkeleton.sendAttackPacket(3);
      this.mutantSkeleton.animTick = 0;
   }

   public boolean continueExecuting() {
      return this.mutantSkeleton.animTick < 30;
   }

   public void updateTask() {
      if (this.attackTarget.isEntityAlive()) {
         this.mutantSkeleton.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
      }

      if (this.mutantSkeleton.animTick == 10) {
         double x = this.attackTarget.posX - this.mutantSkeleton.posX;
         double z = this.attackTarget.posZ - this.mutantSkeleton.posZ;
         float scale = 0.06F + this.mutantSkeleton.getRNG().nextFloat() * 0.03F;
         this.mutantSkeleton.motionX = x * (double)scale;
         this.mutantSkeleton.motionY = (double)1.1F;
         this.mutantSkeleton.motionZ = z * (double)scale;
      }

      if (this.mutantSkeleton.animTick >= 24 && this.mutantSkeleton.animTick < 28) {
         if (!this.shotList.isEmpty()) {
            for(SkeletonShot shot : this.shotList) {
               this.mutantSkeleton.worldObj.spawnEntityInWorld(shot);
            }

            this.shotList.clear();
         }

         for(int i = 0; i < 6; ++i) {
            SkeletonShot shot = new SkeletonShot(this.mutantSkeleton.worldObj, this.mutantSkeleton, this.attackTarget);
            shot.setSpeed(1.2F - this.mutantSkeleton.getRNG().nextFloat() * 0.1F);
            shot.setClones(2);
            shot.randomize(3.0F);
            shot.setDamage(5 + this.mutantSkeleton.getRNG().nextInt(5));
            this.shotList.add(shot);
         }

         this.mutantSkeleton.worldObj.playSoundAtEntity(this.mutantSkeleton, "random.bow", 1.0F, 1.0F / (this.mutantSkeleton.getRNG().nextFloat() * 0.4F + 1.2F) + 0.25F);
      }

   }

   public void resetTask() {
      this.mutantSkeleton.sendAttackPacket(0);
      this.shotList.clear();
   }
}
