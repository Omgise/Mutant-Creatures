package thehippomaster.MutantCreatures.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import thehippomaster.MutantCreatures.MutantZombie;

public class MCAIZombieAttack extends EntityAIBase {
   private float dist;
   private float actualDistSq;
   private MutantZombie mutantZombie;
   private EntityLivingBase attackTarget;

   public MCAIZombieAttack(MutantZombie zombie, float f) {
      this.dist = f;
      this.actualDistSq = 0.0F;
      this.mutantZombie = zombie;
      this.attackTarget = null;
   }

   public boolean shouldExecute() {
      this.attackTarget = this.mutantZombie.getAttackTarget();
      if (this.attackTarget == null) {
         return false;
      } else {
         double x = this.attackTarget.posX - this.mutantZombie.posX - (double)((this.attackTarget.width + this.mutantZombie.width) / 2.0F);
         double y = this.attackTarget.posY - this.mutantZombie.posY - (double)((this.attackTarget.height + this.mutantZombie.height) / 2.0F);
         double z = this.attackTarget.posZ - this.mutantZombie.posZ - (double)((this.attackTarget.width + this.mutantZombie.width) / 2.0F);
         x = Math.max((double)0.0F, x);
         y = Math.max((double)0.0F, y);
         z = Math.max((double)0.0F, z);
         this.actualDistSq = (float)(x * x + y * y + z * z);
         return this.actualDistSq <= this.dist * this.dist;
      }
   }

   public void startExecuting() {
      this.mutantZombie.attackEntityAtDistSq(this.attackTarget, this.actualDistSq);
   }

   public boolean continueExecuting() {
      return false;
   }
}
