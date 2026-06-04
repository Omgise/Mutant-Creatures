package thehippomaster.MutantCreatures.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import thehippomaster.MutantCreatures.SpiderPig;

public class MCAISpiderPigJump extends EntityAIBase {
   public int jumpTick = 0;
   public float jumpRange;
   public boolean running;
   private SpiderPig spiderPig;

   public MCAISpiderPigJump(SpiderPig pig, float range) {
      this.jumpRange = range;
      this.spiderPig = pig;
   }

   public boolean shouldExecute() {
      EntityLivingBase target = this.spiderPig.getAttackTarget();
      if (target != null && this.spiderPig.lastJumpTick <= 0) {
         if (!this.spiderPig.onGround && !this.spiderPig.isInWater()) {
            return false;
         } else {
            double distSq = this.spiderPig.getDistanceSqToEntity(target);
            return distSq < (double)(this.jumpRange * this.jumpRange) && this.spiderPig.getRNG().nextInt(8) == 0 || distSq < (double)6.25F;
         }
      } else {
         return false;
      }
   }

   public void startExecuting() {
      this.running = true;
      this.spiderPig.lastJumpTick = 15;
      EntityLivingBase target = this.spiderPig.getAttackTarget();
      double x = target.posX - this.spiderPig.posX;
      double y = target.posY - this.spiderPig.posY;
      double z = target.posZ - this.spiderPig.posZ;
      double d = (double)MathHelper.sqrt_double(x * x + y * y + z * z);
      double scale = (double)(2.0F + 0.2F * this.spiderPig.getRNG().nextFloat() * this.spiderPig.getRNG().nextFloat());
      this.spiderPig.motionX = x / d * scale;
      this.spiderPig.motionY = y / d * scale * (double)0.5F + 0.3;
      this.spiderPig.motionZ = z / d * scale;
   }

   public boolean continueExecuting() {
      return this.running && this.jumpTick < 40;
   }

   public void updateTask() {
      ++this.jumpTick;
   }

   public void resetTask() {
      this.jumpTick = 0;
   }
}
