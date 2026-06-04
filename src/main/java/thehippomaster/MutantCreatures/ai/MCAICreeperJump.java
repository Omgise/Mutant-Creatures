package thehippomaster.MutantCreatures.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import thehippomaster.MutantCreatures.MutantCreeper;

public class MCAICreeperJump extends EntityAIBase {
   public float executeChance;
   public float jumpStrength;
   private MutantCreeper mutantCreeper;
   private EntityLivingBase attackTarget;

   public MCAICreeperJump(MutantCreeper creeper, float chance, float strength) {
      this.mutantCreeper = creeper;
      this.executeChance = chance;
      this.jumpStrength = strength;
   }

   public boolean shouldExecute() {
      this.attackTarget = this.mutantCreeper.getAttackTarget();
      if (this.attackTarget != null && !(this.mutantCreeper.getDistanceSqToEntity(this.attackTarget) > (double)1024.0F)) {
         if (this.mutantCreeper.aiCollideAttack.getAttackTick() <= 20 && this.mutantCreeper.onGround) {
            if (this.mutantCreeper.getChargingAttack()) {
               return false;
            } else {
               return this.mutantCreeper.getRNG().nextFloat() * 100.0F < this.executeChance;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void startExecuting() {
      this.mutantCreeper.setSuperJump(true);
      this.mutantCreeper.motionY = (double)this.jumpStrength;
      this.mutantCreeper.motionX = (this.attackTarget.posX - this.mutantCreeper.posX) * (double)0.2F;
      this.mutantCreeper.motionZ = (this.attackTarget.posZ - this.mutantCreeper.posZ) * (double)0.2F;
   }

   public boolean continueExecuting() {
      return false;
   }
}
