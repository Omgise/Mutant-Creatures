package thehippomaster.MutantCreatures.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.MathHelper;
import thehippomaster.MutantCreatures.MCHandler;
import thehippomaster.MutantCreatures.MutantCreeper;

public class MCAICreeperCharge extends EntityAIBase {
   public int chargeTime = 0;
   public int hitCount;
   public float executeChance;
   public boolean lightningEffect;
   private MutantCreeper mutantCreeper;
   private EntityLivingBase attackTarget;

   public MCAICreeperCharge(MutantCreeper creeper, float f) {
      this.hitCount = 3 + creeper.getRNG().nextInt(3);
      this.executeChance = f;
      this.lightningEffect = false;
      this.mutantCreeper = creeper;
      this.setMutexBits(7);
   }

   public boolean shouldExecute() {
      this.attackTarget = this.mutantCreeper.getAttackTarget();
      if (this.attackTarget != null && this.mutantCreeper.onGround) {
         if (this.mutantCreeper.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue() - (double)this.mutantCreeper.getHealth() < (double)20.0F) {
            return false;
         } else {
            double d = this.mutantCreeper.getDistanceSqToEntity(this.attackTarget);
            if (!(d < (double)25.0F) && !(d > (double)1024.0F)) {
               return this.mutantCreeper.getRNG().nextFloat() * 100.0F < this.executeChance;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   public void startExecuting() {
      this.mutantCreeper.setChargingAttack(true);
      this.mutantCreeper.getNavigator().clearPathEntity();
      if (this.mutantCreeper.getRNG().nextInt(6) == 0) {
         this.lightningEffect = true;
      }

   }

   public boolean continueExecuting() {
      double d = this.mutantCreeper.getDistanceSqToEntity(this.attackTarget);
      if (this.lightningEffect && d < (double)25.0F) {
         return false;
      } else {
         return this.chargeTime < this.getMaxChargeTime() && this.hitCount > 0;
      }
   }

   public void updateTask() {
      int i = this.chargeTime % 20;
      if (i == 0 || i == 20) {
         this.mutantCreeper.worldObj.playSoundAtEntity(this.mutantCreeper, "MutantCreatures:mutantcreeper.chime", 0.6F, 0.7F + this.mutantCreeper.getRNG().nextFloat() * 0.6F);
      }

      ++this.chargeTime;
   }

   public void resetTask() {
      this.doEffect();
      this.chargeTime = 0;
      this.hitCount = 4 + this.mutantCreeper.getRNG().nextInt(3);
      this.mutantCreeper.setChargingAttack(false);
      this.lightningEffect = false;
      this.mutantCreeper.setAttackTarget(this.attackTarget);
   }

   public int getMaxChargeTime() {
      return 100;
   }

   public void doEffect() {
      int x = MathHelper.floor_double(this.mutantCreeper.posX);
      int y = MathHelper.floor_double(this.mutantCreeper.boundingBox.minY);
      int z = MathHelper.floor_double(this.mutantCreeper.posZ);
      double d = this.mutantCreeper.getDistanceSqToEntity(this.attackTarget);
      if (this.lightningEffect && d < (double)25.0F && this.mutantCreeper.worldObj.canBlockSeeTheSky(x, y, z)) {
         this.mutantCreeper.worldObj.spawnEntityInWorld(new EntityLightningBolt(this.mutantCreeper.worldObj, this.mutantCreeper.posX, this.mutantCreeper.posY, this.mutantCreeper.posZ));
      } else {
         if (this.chargeTime >= this.getMaxChargeTime()) {
            this.mutantCreeper.heal(30.0F);
            MCHandler.spawnHeartsAtEntity(this.mutantCreeper, 15);
         }

      }
   }
}
