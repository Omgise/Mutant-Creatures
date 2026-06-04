package thehippomaster.MutantCreatures.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;

public class MCAIHurtByTarget extends EntityAIHurtByTarget {
   private int tick;
   private EntityLivingBase attackTarget;

   public MCAIHurtByTarget(EntityCreature living) {
      this(living, false);
   }

   public MCAIHurtByTarget(EntityCreature living, boolean flag) {
      super(living, flag);
      this.tick = 40;
   }

   public boolean shouldExecute() {
      this.attackTarget = this.taskOwner.getAttackTarget();
      return this.isSuitableTarget(this.attackTarget, true);
   }

   public boolean continueExecuting() {
      EntityLivingBase target = this.taskOwner.getAttackTarget();
      return target != null && target != this.attackTarget && this.tick <= 0 ? false : super.continueExecuting();
   }

   public void updateTask() {
      super.updateTask();
      if (this.tick > 0) {
         --this.tick;
      }

   }

   public void resetTask() {
      super.resetTask();
      this.tick = 40;
   }
}
