package thehippomaster.MutantCreatures.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import thehippomaster.MutantCreatures.MutantZombie;
import thehippomaster.MutantCreatures.ZombieResurrect;

public class MCAIZombieRoar extends EntityAIBase {
   public float chance;
   private MutantZombie mutantZombie;
   private EntityLivingBase attackTarget;
   private Random rand;

   public MCAIZombieRoar(MutantZombie zombie, float f) {
      this.chance = f;
      this.mutantZombie = zombie;
      this.attackTarget = null;
      this.rand = this.mutantZombie.getRNG();
      this.setMutexBits(7);
   }

   public boolean shouldExecute() {
      this.attackTarget = this.mutantZombie.getAttackTarget();
      if (this.attackTarget != null && this.mutantZombie.onGround) {
         double d = this.mutantZombie.getDistanceSqToEntity(this.attackTarget);
         if (d < (double)16.0F) {
            return false;
         } else {
            return this.rand.nextFloat() * 100.0F < this.chance;
         }
      } else {
         return false;
      }
   }

   public void startExecuting() {
      this.mutantZombie.animTick = 0;
      this.mutantZombie.sendAttackPacket(2);
      this.mutantZombie.getNavigator().clearPathEntity();
   }

   public boolean continueExecuting() {
      return this.mutantZombie.deathTick <= 0 && this.mutantZombie.animTick < 120;
   }

   public void updateTask() {
      if (this.mutantZombie.animTick < 75) {
         this.mutantZombie.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
      }

      if (this.mutantZombie.animTick == 10) {
         float f = 0.7F + this.rand.nextFloat() * 0.2F;
         this.mutantZombie.worldObj.playSoundAtEntity(this.mutantZombie, "MutantCreatures:mutantzombie.roar", 0.8F, f);
         List list = this.mutantZombie.worldObj.getEntitiesWithinAABBExcludingEntity(this.mutantZombie, this.mutantZombie.boundingBox.expand((double)12.0F, (double)8.0F, (double)12.0F));

         for(int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity)list.get(i);
            if (!(this.mutantZombie.getDistanceSqToEntity(entity) > (double)196.0F)) {
               double x = entity.posX - this.mutantZombie.posX;
               double z = entity.posZ - this.mutantZombie.posZ;
               double d = Math.sqrt(x * x + z * z);
               entity.motionX = x / d * (double)0.7F;
               entity.motionY = (double)0.3F;
               entity.motionZ = z / d * (double)0.7F;
               if (entity instanceof EntityLivingBase) {
                  entity.attackEntityFrom(DamageSource.generic, (float)(2 + this.rand.nextInt(2)));
               }
            }
         }
      }

      if (this.mutantZombie.animTick >= 20 && this.mutantZombie.animTick < 80 && this.mutantZombie.animTick % 10 == 0) {
         int x = MathHelper.floor_double(this.mutantZombie.posX);
         int y = MathHelper.floor_double(this.mutantZombie.boundingBox.minY);
         int z = MathHelper.floor_double(this.mutantZombie.posZ);
         x += (1 + this.rand.nextInt(8)) * (this.rand.nextBoolean() ? 1 : -1);
         z += (1 + this.rand.nextInt(8)) * (this.rand.nextBoolean() ? 1 : -1);
         y = ZombieResurrect.getSuitableGround(this.mutantZombie.worldObj, x, y - 1, z);
         if (y != -1) {
            EntityLivingBase target = this.getRandomVulnerablePlayer((double)16.0F);
            if (this.mutantZombie.animTick == 20 || this.mutantZombie.getRNG().nextInt(4) == 0 || target == null) {
               EntityLivingBase var18 = this.attackTarget;
            }

            this.mutantZombie.resurrectList.add(new ZombieResurrect(this.attackTarget, x, y, z));
         }
      }

   }

   public void resetTask() {
      this.mutantZombie.sendAttackPacket(0);
      this.mutantZombie.setRevengeTarget(this.attackTarget);
   }

   private EntityPlayer getRandomVulnerablePlayer(double dist) {
      ArrayList<EntityPlayer> playerList = new ArrayList();
      List list = this.mutantZombie.worldObj.playerEntities;

      for(int i = 0; i < list.size(); ++i) {
         EntityPlayer player = (EntityPlayer)list.get(i);
         if (!(this.mutantZombie.getDistanceSqToEntity(player) > dist * dist) && !player.capabilities.disableDamage) {
            playerList.add(player);
         }
      }

      if (playerList.isEmpty()) {
         return null;
      } else {
         return (EntityPlayer)playerList.get(this.mutantZombie.getRNG().nextInt(playerList.size()));
      }
   }
}
