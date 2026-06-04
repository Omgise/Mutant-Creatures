package thehippomaster.MutantCreatures.ai;

import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import thehippomaster.MutantCreatures.MutantSkeleton;
import thehippomaster.MutantCreatures.SkeletonShot;

public class MCAISkeleShoot extends EntityAIBase {
   private MutantSkeleton mutantSkeleton;
   private EntityLivingBase attackTarget;
   private Random rand;

   public MCAISkeleShoot(MutantSkeleton skele) {
      this.mutantSkeleton = skele;
      this.attackTarget = null;
      this.rand = this.mutantSkeleton.getRNG();
      this.setMutexBits(7);
   }

   public boolean shouldExecute() {
      this.attackTarget = this.mutantSkeleton.getAttackTarget();
      if (this.attackTarget != null && (this.mutantSkeleton.onGround || this.mutantSkeleton.isInWater())) {
         if (!this.mutantSkeleton.canEntityBeSeen(this.attackTarget)) {
            return false;
         } else if (this.mutantSkeleton.getDistanceSqToEntity(this.attackTarget) < (double)4.0F) {
            return false;
         } else {
            return this.mutantSkeleton.currentAttackID == 0 && this.rand.nextInt(12) == 0;
         }
      } else {
         return false;
      }
   }

   public void startExecuting() {
      this.mutantSkeleton.sendAttackPacket(2);
      this.mutantSkeleton.animTick = 0;
   }

   public boolean continueExecuting() {
      return this.mutantSkeleton.animTick < 32;
   }

   public void updateTask() {
      if (this.attackTarget.isEntityAlive()) {
         this.mutantSkeleton.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
      }

      if (this.mutantSkeleton.animTick == 26) {
         SkeletonShot shot = new SkeletonShot(this.mutantSkeleton.worldObj, this.mutantSkeleton, this.attackTarget);
         if (this.mutantSkeleton.hurtTime > 0) {
            shot.randomize((float)this.mutantSkeleton.hurtTime / 2.0F);
         }

         if (this.rand.nextInt(4) == 0) {
            shot.setPotionEffect(new PotionEffect(Potion.poison.id, 80 + this.rand.nextInt(60), 0));
         }

         if (this.rand.nextInt(4) == 0) {
            shot.setPotionEffect(new PotionEffect(Potion.hunger.id, 120 + this.rand.nextInt(60), 1));
         }

         if (this.rand.nextInt(4) == 0) {
            shot.setPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 120 + this.rand.nextInt(60), 1));
         }

         this.mutantSkeleton.worldObj.spawnEntityInWorld(shot);
         this.mutantSkeleton.worldObj.playSoundAtEntity(this.mutantSkeleton, "random.bow", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 1.2F) + 0.25F);
      }

   }

   public void resetTask() {
      this.mutantSkeleton.sendAttackPacket(0);
   }
}
