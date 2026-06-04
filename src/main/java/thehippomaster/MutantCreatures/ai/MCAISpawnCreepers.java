package thehippomaster.MutantCreatures.ai;

import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import thehippomaster.MutantCreatures.CreeperMinion;
import thehippomaster.MutantCreatures.MutantCreeper;

public class MCAISpawnCreepers extends EntityAIBase {
   public int maxSpawn;
   public float executeChance;
   private MutantCreeper mutantCreeper;
   private EntityLivingBase attackTarget;
   private Random rand;

   public MCAISpawnCreepers(MutantCreeper creeper, float chance, int spawn) {
      this.mutantCreeper = creeper;
      this.executeChance = chance;
      this.rand = this.mutantCreeper.getRNG();
      this.maxSpawn = spawn;
   }

   public boolean shouldExecute() {
      this.attackTarget = this.mutantCreeper.getAttackTarget();
      if (this.attackTarget != null && !(this.mutantCreeper.getDistanceSqToEntity(this.attackTarget) > (double)256.0F)) {
         if (this.mutantCreeper.aiCollideAttack.getAttackTick() <= 20 && this.mutantCreeper.onGround) {
            if (this.mutantCreeper.getChargingAttack()) {
               return false;
            } else {
               return this.rand.nextFloat() * 100.0F < this.executeChance;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void startExecuting() {
      this.mutantCreeper.aiCollideAttack.resetAttackTick();

      for(int i = (int)Math.ceil((double)this.mutantCreeper.getHealth() / this.mutantCreeper.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue() * (double)this.maxSpawn); i > 0; --i) {
         CreeperMinion creeper = new CreeperMinion(this.mutantCreeper.worldObj);
         double x = this.mutantCreeper.posX + (double)this.rand.nextFloat() - (double)this.rand.nextFloat();
         double y = this.mutantCreeper.posY + (double)(this.rand.nextFloat() * 0.5F);
         double z = this.mutantCreeper.posZ + (double)this.rand.nextFloat() - (double)this.rand.nextFloat();
         double xx = this.attackTarget.posX - this.mutantCreeper.posX;
         double yy = this.attackTarget.posY - this.mutantCreeper.posY;
         double zz = this.attackTarget.posZ - this.mutantCreeper.posZ;
         creeper.motionX = xx * (double)0.15F + (double)(this.rand.nextFloat() * 0.05F);
         creeper.motionY = yy * (double)0.15F + (double)(this.rand.nextFloat() * 0.05F);
         creeper.motionZ = zz * (double)0.15F + (double)(this.rand.nextFloat() * 0.05F);
         creeper.setPosition(x, y, z);
         creeper.setAttackTarget(this.attackTarget);
         creeper.setRevengeTarget(this.attackTarget);
         if (this.mutantCreeper.getCharged()) {
            creeper.setPowered(true);
         }

         this.mutantCreeper.worldObj.spawnEntityInWorld(creeper);
      }

   }

   public boolean continueExecuting() {
      return false;
   }
}
