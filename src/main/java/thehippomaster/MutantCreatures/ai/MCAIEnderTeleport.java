package thehippomaster.MutantCreatures.ai;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import thehippomaster.MutantCreatures.MutantEnderman;

public class MCAIEnderTeleport extends EntityAIBase {
   private MutantEnderman mutantEnderman;
   private EntityLivingBase attackTarget;

   public MCAIEnderTeleport(MutantEnderman enderman) {
      this.mutantEnderman = enderman;
      this.attackTarget = null;
      this.setMutexBits(5);
   }

   public boolean shouldExecute() {
      return this.mutantEnderman.currentAttackID == 4;
   }

   public void startExecuting() {
      this.mutantEnderman.animTick = 0;
      this.mutantEnderman.getNavigator().clearPathEntity();
      this.attackTarget = this.mutantEnderman.getAttackTarget();
      if (this.attackTarget != null) {
         this.mutantEnderman.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
      }

      float r = 3.0F;
      List list = this.mutantEnderman.worldObj.getEntitiesWithinAABBExcludingEntity(this.mutantEnderman, this.mutantEnderman.boundingBox.expand((double)r, (double)r, (double)r));

      for(int i = 0; i < list.size(); ++i) {
         Entity entity = (Entity)list.get(i);
         if (entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase)entity;
            living.attackEntityFrom(DamageSource.causeMobDamage(this.mutantEnderman), 4.0F);
            if (this.mutantEnderman.getRNG().nextInt(3) == 0) {
               living.addPotionEffect(new PotionEffect(Potion.blindness.id, 140 + this.mutantEnderman.getRNG().nextInt(60)));
            }

            double x = entity.posX - this.mutantEnderman.posX;
            double z = entity.posZ - this.mutantEnderman.posZ;
            double signX = x / Math.abs(x);
            double signZ = z / Math.abs(z);
            living.motionX = ((double)r * signX * (double)2.0F - x) * (double)0.2F;
            living.motionY = (double)0.2F;
            living.motionZ = ((double)r * signZ * (double)2.0F - z) * (double)0.2F;
         }
      }

      double oldX = this.mutantEnderman.posX;
      double oldY = this.mutantEnderman.posY;
      double oldZ = this.mutantEnderman.posZ;
      this.mutantEnderman.setPosition((double)this.mutantEnderman.teleX + (double)0.5F, (double)this.mutantEnderman.teleY, (double)this.mutantEnderman.teleZ + (double)0.5F);
      this.mutantEnderman.worldObj.playSoundEffect(oldX, oldY + (double)this.mutantEnderman.height / (double)2.0F, oldZ, "mob.endermen.portal", 1.0F, 1.0F);
      this.mutantEnderman.playSound("mob.endermen.portal", 1.0F, 1.0F);
      list = this.mutantEnderman.worldObj.getEntitiesWithinAABBExcludingEntity(this.mutantEnderman, this.mutantEnderman.boundingBox.expand((double)r, (double)r, (double)r));

      for(int i = 0; i < list.size(); ++i) {
         Entity entity = (Entity)list.get(i);
         if (entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase)entity;
            living.attackEntityFrom(DamageSource.causeMobDamage(this.mutantEnderman), 4.0F);
            if (this.mutantEnderman.getRNG().nextInt(3) == 0) {
               living.addPotionEffect(new PotionEffect(Potion.blindness.id, 140 + this.mutantEnderman.getRNG().nextInt(60)));
            }

            double x = entity.posX - this.mutantEnderman.posX;
            double z = entity.posZ - this.mutantEnderman.posZ;
            double signX = x / Math.abs(x);
            double signZ = z / Math.abs(z);
            living.motionX = ((double)r * signX * (double)2.0F - x) * (double)0.2F;
            living.motionY = (double)0.2F;
            living.motionZ = ((double)r * signZ * (double)2.0F - z) * (double)0.2F;
         }
      }

      this.mutantEnderman.setPosition(oldX, oldY, oldZ);
   }

   public boolean continueExecuting() {
      return this.shouldExecute() && this.mutantEnderman.animTick < 10;
   }

   public void resetTask() {
      this.mutantEnderman.sendAttackPacket(0);
      this.mutantEnderman.setPosition((double)this.mutantEnderman.teleX + (double)0.5F, (double)this.mutantEnderman.teleY, (double)this.mutantEnderman.teleZ + (double)0.5F);
      if (this.attackTarget != null) {
         this.mutantEnderman.setRevengeTarget(this.attackTarget);
      }

      this.mutantEnderman.prevPosX = this.mutantEnderman.posX;
      this.mutantEnderman.prevPosY = this.mutantEnderman.posY;
      this.mutantEnderman.prevPosZ = this.mutantEnderman.posZ;
   }
}
