package thehippomaster.MutantCreatures.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import thehippomaster.MutantCreatures.CreeperMinion;

public class MCAIAttackOwnerTarget extends EntityAITarget {
   private boolean ownerAttacks;
   private CreeperMinion creeperMinion;
   private EntityPlayer owner;
   private EntityLivingBase target;

   public MCAIAttackOwnerTarget(CreeperMinion minion, boolean ownerAttacks) {
      super(minion, false);
      this.ownerAttacks = ownerAttacks;
      this.creeperMinion = minion;
      this.owner = null;
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      if (this.owner == null) {
         this.owner = this.creeperMinion.worldObj.getPlayerEntityByName(this.creeperMinion.getOwner());
      }

      if (this.owner != null && !this.creeperMinion.getSitting()) {
         if (this.ownerAttacks) {
            this.target = this.owner.getLastAttacker();
            if (this.creeperMinion.getSitting()) {
               return false;
            }
         }

         if (this.target instanceof EntityTameable) {
            EntityTameable tameable = (EntityTameable)this.target;
            if (tameable.isTamed()) {
               return false;
            }
         }

         if (this.target instanceof CreeperMinion) {
            CreeperMinion minion = (CreeperMinion)this.target;
            if (minion.getTamed()) {
               return false;
            }
         }

         return this.isSuitableTarget(this.target, false);
      } else {
         return false;
      }
   }

   public void startExecuting() {
      this.creeperMinion.setAttackTarget(this.target);
      super.startExecuting();
   }
}
