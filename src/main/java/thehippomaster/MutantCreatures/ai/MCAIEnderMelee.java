package thehippomaster.MutantCreatures.ai;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.DamageSource;
import thehippomaster.MutantCreatures.MutantEnderman;

public class MCAIEnderMelee extends EntityAIBase {
   private MutantEnderman mutantEnderman;

   public MCAIEnderMelee(MutantEnderman enderman) {
      this.mutantEnderman = enderman;
   }

   public boolean shouldExecute() {
      return this.mutantEnderman.currentAttackID == 1;
   }

   public void startExecuting() {
      this.mutantEnderman.animTick = 0;
   }

   public boolean continueExecuting() {
      return this.mutantEnderman.animTick < 10;
   }

   public void updateTask() {
      if (this.mutantEnderman.animTick == 3) {
         List list = this.mutantEnderman.worldObj.getEntitiesWithinAABBExcludingEntity(this.mutantEnderman, this.mutantEnderman.boundingBox.expand((double)4.0F, (double)4.0F, (double)4.0F));

         for(int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity)list.get(i);
            if (!(this.mutantEnderman.boundingBox.minY > entity.boundingBox.maxY)) {
               double dist = (double)this.mutantEnderman.getDistanceToEntity(entity);
               if (!(dist > (double)4.0F)) {
                  double x = this.mutantEnderman.posX - entity.posX;
                  double z = this.mutantEnderman.posZ - entity.posZ;

                  float rot;
                  for(rot = (float)(Math.atan2(z, x) * (double)180.0F / Math.PI) + 90.0F; rot > this.mutantEnderman.rotationYawHead + 180.0F; rot -= 360.0F) {
                  }

                  while(rot <= this.mutantEnderman.rotationYawHead - 180.0F) {
                     rot += 360.0F;
                  }

                  float dif = this.mutantEnderman.rotationYawHead - rot;
                  float maxDif = 3.0F + (1.0F - (float)dist / 4.0F) * 40.0F;
                  if (Math.abs(dif) < maxDif) {
                     boolean lower = this.mutantEnderman.getMeleeArm() >= 3;
                     int dmg = lower ? 8 : 10;
                     entity.attackEntityFrom(DamageSource.causeMobDamage(this.mutantEnderman), (float)dmg);
                     float power = 0.4F + this.mutantEnderman.getRNG().nextFloat() * 0.2F;
                     if (!lower) {
                        power += 0.2F;
                     }

                     entity.motionX = -x / dist * (double)power;
                     entity.motionY = (double)(power * 0.6F);
                     entity.motionZ = -z / dist * (double)power;
                  }
               }
            }
         }
      }

   }

   public void resetTask() {
      this.mutantEnderman.sendAttackPacket(0);
   }
}
