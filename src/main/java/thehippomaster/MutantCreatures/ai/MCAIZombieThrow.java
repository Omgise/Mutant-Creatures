package thehippomaster.MutantCreatures.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.DamageSource;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.MutantZombie;

public class MCAIZombieThrow extends EntityAIBase {
   public int hit = -1;
   public int finish = -1;
   private MutantZombie mutantZombie;
   private EntityLivingBase attackTarget;

   public MCAIZombieThrow(MutantZombie zombie) {
      this.mutantZombie = zombie;
      this.attackTarget = null;
      this.setMutexBits(7);
   }

   public boolean shouldExecute() {
      this.attackTarget = this.mutantZombie.getAttackTarget();
      if (this.attackTarget == null) {
         return false;
      } else {
         return this.mutantZombie.currentAttackID == 3;
      }
   }

   public void startExecuting() {
      this.mutantZombie.animTick = 0;
      this.mutantZombie.getNavigator().clearPathEntity();
      double x = this.attackTarget.posX - this.mutantZombie.posX;
      double z = this.attackTarget.posZ - this.mutantZombie.posZ;
      double d = Math.sqrt(x * x + z * z);
      this.attackTarget.motionX = x / d * (double)0.8F;
      this.attackTarget.motionY = (double)1.6F;
      this.attackTarget.motionZ = z / d * (double)0.8F;
      if (this.attackTarget instanceof EntityPlayerMP) {
         EntityPlayerMP player = (EntityPlayerMP)this.attackTarget;
         MutantCreatures.sendPacketToAll(player, new S12PacketEntityVelocity(player));
      }

   }

   public boolean continueExecuting() {
      return this.mutantZombie.currentAttackID == 3 && this.mutantZombie.deathTick <= 0 && this.finish < 10;
   }

   public void updateTask() {
      this.mutantZombie.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
      if (this.mutantZombie.animTick == 15) {
         double x = this.attackTarget.posX - this.mutantZombie.posX;
         double y = this.attackTarget.posY - this.mutantZombie.posY;
         double z = this.attackTarget.posZ - this.mutantZombie.posZ;
         double d = Math.sqrt(x * x + y * y + z * z);
         this.mutantZombie.motionX = x / d * (double)3.4F;
         this.mutantZombie.motionY = y / d * (double)1.4F;
         this.mutantZombie.motionZ = z / d * (double)3.4F;
      } else if (this.mutantZombie.animTick > 15) {
         double d1 = (double)(this.mutantZombie.width * 2.0F * this.mutantZombie.width * 2.0F);
         double d2 = this.mutantZombie.getDistanceSq(this.attackTarget.posX, this.attackTarget.boundingBox.minY, this.attackTarget.posZ);
         if (d2 < d1 && this.hit == -1) {
            this.hit = 0;
            this.mutantZombie.setThrowAttackHit(true);
            this.attackTarget.attackEntityFrom(DamageSource.causeMobDamage(this.mutantZombie), 12.0F);
            double x = this.attackTarget.posX - this.mutantZombie.posX;
            double z = this.attackTarget.posZ - this.mutantZombie.posZ;
            double d = Math.sqrt(x * x + z * z);
            this.attackTarget.motionX = x / d * (double)0.6F;
            this.attackTarget.motionY = (double)-1.2F;
            this.attackTarget.motionZ = z / d * (double)0.6F;
            if (this.attackTarget instanceof EntityPlayerMP) {
               EntityPlayerMP player = (EntityPlayerMP)this.attackTarget;
               MutantCreatures.sendPacketToAll(player, new S12PacketEntityVelocity(player));
            }

            this.mutantZombie.worldObj.playSoundAtEntity(this.mutantZombie, "MutantCreatures:mutantzombie.gruntB", 0.3F, 0.8F + this.mutantZombie.getRNG().nextFloat() * 0.4F);
         }

         if (this.hit >= 0) {
            ++this.hit;
         }

         if ((this.mutantZombie.onGround || this.mutantZombie.isInWater() || this.mutantZombie.handleLavaMovement()) && this.finish == -1) {
            this.finish = 0;
            this.mutantZombie.setThrowAttackFinish(true);
         }

         if (this.finish >= 0) {
            ++this.finish;
         }
      }

   }

   public void resetTask() {
      this.mutantZombie.sendAttackPacket(0);
      this.hit = -1;
      this.finish = -1;
      this.mutantZombie.setThrowAttackHit(false);
      this.mutantZombie.setThrowAttackFinish(false);
      this.mutantZombie.setRevengeTarget(this.attackTarget);
   }

   public EntityLivingBase getTarget() {
      return this.attackTarget;
   }
}
