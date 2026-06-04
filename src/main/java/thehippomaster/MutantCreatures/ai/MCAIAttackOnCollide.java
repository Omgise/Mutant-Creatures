package thehippomaster.MutantCreatures.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class MCAIAttackOnCollide extends EntityAIBase {
   World worldObj;
   EntityCreature attacker;
   EntityLivingBase entityTarget;
   private int attackTick;
   private int maxAttackTick;
   public float moveSpeed;
   boolean longMemory;
   PathEntity entityPathEntity;
   Class classTarget;
   private int attackDelayCounter;

   public MCAIAttackOnCollide(EntityCreature par1EntityLiving, Class par2Class, float par3, boolean par4) {
      this(par1EntityLiving, par3, par4);
      this.classTarget = par2Class;
   }

   public MCAIAttackOnCollide(EntityCreature par1EntityLiving, float par2, boolean par3) {
      this.attackTick = 0;
      this.attackDelayCounter = 0;
      this.maxAttackTick = 20;
      this.attacker = par1EntityLiving;
      this.worldObj = par1EntityLiving.worldObj;
      this.moveSpeed = par2;
      this.longMemory = par3;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      EntityLivingBase var1 = this.attacker.getAttackTarget();
      if (var1 == null) {
         return false;
      } else if (this.classTarget != null && !this.classTarget.isAssignableFrom(var1.getClass())) {
         return false;
      } else {
         this.entityTarget = var1;
         this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(this.entityTarget);
         return this.entityPathEntity != null;
      }
   }

   public boolean continueExecuting() {
      EntityLivingBase var1 = this.attacker.getAttackTarget();
      return var1 == null ? false : (!var1.isEntityAlive() ? false : (!this.longMemory ? !this.attacker.getNavigator().noPath() : this.attacker.isWithinHomeDistance(MathHelper.floor_double(var1.posX), MathHelper.floor_double(var1.posY), MathHelper.floor_double(var1.posZ))));
   }

   public void startExecuting() {
      this.attacker.getNavigator().setPath(this.entityPathEntity, (double)this.moveSpeed);
      this.attackTick = 0;
   }

   public void resetTask() {
      this.entityTarget = null;
      this.attacker.getNavigator().clearPathEntity();
   }

   public void updateTask() {
      this.attacker.getLookHelper().setLookPositionWithEntity(this.entityTarget, 30.0F, 30.0F);
      if ((this.longMemory || this.attacker.getEntitySenses().canSee(this.entityTarget)) && --this.attackDelayCounter <= 0) {
         this.attackDelayCounter = 4 + this.attacker.getRNG().nextInt(7);
         this.attacker.getNavigator().tryMoveToEntityLiving(this.entityTarget, (double)this.moveSpeed);
      }

      this.attackTick = Math.max(this.attackTick - 1, 0);
      double var1 = (double)(this.attacker.width * 2.0F * this.attacker.width * 2.0F);
      if (this.attacker.getDistanceSq(this.entityTarget.posX, this.entityTarget.boundingBox.minY, this.entityTarget.posZ) <= var1 && this.attackTick <= 0) {
         this.attackTick = this.maxAttackTick;
         if (this.attacker.getHeldItem() != null) {
            this.attacker.swingItem();
         }

         this.attacker.attackEntityAsMob(this.entityTarget);
      }

   }

   public int getAttackTick() {
      return this.attackTick;
   }

   public void resetAttackTick() {
      this.attackTick = this.maxAttackTick;
   }

   public MCAIAttackOnCollide setMaxAttackTick(int max) {
      this.maxAttackTick = max;
      return this;
   }
}
