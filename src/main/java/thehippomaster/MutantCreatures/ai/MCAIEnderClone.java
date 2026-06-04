package thehippomaster.MutantCreatures.ai;

import java.util.ArrayList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import thehippomaster.MutantCreatures.EndermanClone;
import thehippomaster.MutantCreatures.MCHandler;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.MutantEnderman;

public class MCAIEnderClone extends EntityAIBase {
   private MutantEnderman mutantEnderman;
   private EntityLivingBase attackTarget;
   private ArrayList<EndermanClone> cloneList;

   public MCAIEnderClone(MutantEnderman enderman) {
      this.mutantEnderman = enderman;
      this.attackTarget = null;
      this.cloneList = new ArrayList();
   }

   public boolean shouldExecute() {
      if (this.mutantEnderman.getAttackTarget() == null) {
         return false;
      } else if (this.mutantEnderman.heldBlock[1] == 0 && this.mutantEnderman.heldBlock[2] == 0) {
         if (this.mutantEnderman.currentAttackID == 6) {
            return true;
         } else {
            return this.mutantEnderman.currentAttackID == 0 && this.mutantEnderman.getRNG().nextInt(300) == 0;
         }
      } else {
         return false;
      }
   }

   public void startExecuting() {
      this.mutantEnderman.animTick = 0;
      this.mutantEnderman.sendAttackPacket(6);
      this.attackTarget = this.mutantEnderman.getAttackTarget();
      MCHandler.spawnParticlesAtEntity(4, this.mutantEnderman, 0);
      MCHandler.spawnParticlesAtEntity(4, this.mutantEnderman, 0);
      this.mutantEnderman.worldObj.playSoundEffect(this.mutantEnderman.posX, this.mutantEnderman.posY + (double)this.mutantEnderman.height / (double)2.0F, this.mutantEnderman.posZ, "mob.endermen.portal", 1.0F, 1.0F);

      for(int i = 0; i < 7; ++i) {
         EndermanClone enderman = new EndermanClone(this.mutantEnderman.worldObj);
         double x = this.attackTarget.posX + (double)((enderman.getRNG().nextFloat() - 0.5F) * 24.0F);
         double z = this.attackTarget.posZ + (double)((enderman.getRNG().nextFloat() - 0.5F) * 24.0F);
         double y = this.attackTarget.posY + (double)8.0F;
         enderman.setAttackTarget(this.attackTarget);
         enderman.setRevengeTarget(this.attackTarget);
         if (MutantCreatures.teleportTo(enderman, x, y, z)) {
            this.mutantEnderman.worldObj.spawnEntityInWorld(enderman);
            this.cloneList.add(enderman);
         }
      }

      EndermanClone enderman = new EndermanClone(this.mutantEnderman.worldObj);
      double preX = this.mutantEnderman.posX;
      double preY = this.mutantEnderman.posY;
      double preZ = this.mutantEnderman.posZ;
      double x = this.attackTarget.posX + (double)((this.mutantEnderman.getRNG().nextFloat() - 0.5F) * 24.0F);
      double z = this.attackTarget.posZ + (double)((this.mutantEnderman.getRNG().nextFloat() - 0.5F) * 24.0F);
      double y = this.attackTarget.posY + (double)8.0F;
      enderman.setRevengeTarget(this.attackTarget);
      MutantCreatures.teleportTo(this.mutantEnderman, x, y, z);
      if (MutantCreatures.teleportTo(enderman, preX, preY, preZ)) {
         this.mutantEnderman.worldObj.spawnEntityInWorld(enderman);
         this.cloneList.add(enderman);
      }

      MutantCreatures.removeAttackers(this.mutantEnderman);
   }

   public boolean continueExecuting() {
      if (this.attackTarget != null && this.attackTarget.isEntityAlive()) {
         return !this.cloneList.isEmpty() && this.mutantEnderman.animTick < 600;
      } else {
         return false;
      }
   }

   public void updateTask() {
      for(int i = this.cloneList.size() - 1; i >= 0; --i) {
         EndermanClone clone = (EndermanClone)this.cloneList.get(i);
         if (!clone.isEntityAlive()) {
            this.cloneList.remove(i);
         }
      }

   }

   public void resetTask() {
      this.mutantEnderman.sendAttackPacket(0);

      for(EndermanClone enderman : this.cloneList) {
         if (enderman.isEntityAlive()) {
            enderman.setDead();
         }
      }

      this.cloneList.clear();
      MCHandler.spawnParticlesAtEntity(4, this.mutantEnderman, 0);
      this.mutantEnderman.worldObj.playSoundEffect(this.mutantEnderman.posX, this.mutantEnderman.posY + (double)this.mutantEnderman.height / (double)2.0F, this.mutantEnderman.posZ, "mob.endermen.portal", 1.0F, 1.0F);
   }
}
