package thehippomaster.MutantCreatures.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import thehippomaster.MutantCreatures.MutantSnowGolem;
import thehippomaster.MutantCreatures.SGolemBlock;

public class MCAISGolemThrow extends EntityAIBase {
   private MutantSnowGolem snowGolem;
   private EntityLivingBase attackTarget;

   public MCAISGolemThrow(MutantSnowGolem golem) {
      this.snowGolem = golem;
   }

   public void startAttack() {
      this.snowGolem.throwAttack = true;
      this.snowGolem.sendAttackPacket(0, true);
   }

   public boolean shouldExecute() {
      this.attackTarget = this.snowGolem.getAttackTarget();
      return this.attackTarget == null ? false : this.snowGolem.throwAttack;
   }

   public void startExecuting() {
      this.snowGolem.getNavigator().clearPathEntity();
   }

   public boolean continueExecuting() {
      return this.snowGolem.throwAttack && this.snowGolem.throwTick < this.maxThrowTick();
   }

   public void updateTask() {
      this.snowGolem.renderYawOffset = this.snowGolem.rotationYaw;
      if (this.snowGolem.throwTick == 7) {
         SGolemBlock block = new SGolemBlock(this.snowGolem.worldObj, this.snowGolem);
         ++block.posY;
         double x = this.attackTarget.posX - block.posX;
         double y = this.attackTarget.posY - block.posY;
         double z = this.attackTarget.posZ - block.posZ;
         double xz = Math.sqrt(x * x + z * z);
         block.setThrowableHeading(x, y + xz * (double)0.4F, z, 0.9F, 1.0F);
         this.snowGolem.worldObj.spawnEntityInWorld(block);
      }

   }

   public void resetTask() {
      this.snowGolem.throwAttack = false;
      this.snowGolem.throwTick = 0;
      this.snowGolem.sendAttackPacket(0, false);
   }

   public int maxThrowTick() {
      return 20;
   }
}
