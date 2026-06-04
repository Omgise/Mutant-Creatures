package thehippomaster.MutantCreatures.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import thehippomaster.MutantCreatures.MutantEnderman;

public class MCAIEnderAttackPlayer extends EntityAITarget {
   private float maxAngryDistance;
   private EntityLivingBase attackTarget;

   public MCAIEnderAttackPlayer(MutantEnderman enderman, float dist1, boolean par3) {
      super(enderman, par3);
      this.maxAngryDistance = dist1;
      this.attackTarget = null;
      this.setMutexBits(1);
   }

   public boolean shouldExecute() {
      if (this.taskOwner.getAttackTarget() != null) {
         return false;
      } else {
         EntityPlayer player = this.taskOwner.worldObj.getClosestVulnerablePlayerToEntity(this.taskOwner, (double)64.0F);
         if (player != null) {
            if (player.capabilities.disableDamage) {
               return false;
            }

            ItemStack stack = player.inventory.armorInventory[3];
            if (this.taskOwner.isWet() || stack != null && stack.getItem() == Item.getItemFromBlock(Blocks.pumpkin) || this.taskOwner.getRNG().nextInt(2000) == 0) {
               boolean flag = this.taskOwner.getDistanceSqToEntity(player) < (double)(this.maxAngryDistance * this.maxAngryDistance);
               if (flag) {
                  this.taskOwner.setRevengeTarget(player);
                  return false;
               }
            }
         }

         this.attackTarget = player;
         boolean flag = this.isSuitableTarget(player, false) && isLookingAtEnderman(this.taskOwner, player);
         if (flag) {
            ((MutantEnderman)this.taskOwner).sendAttackPacket(3);
         }

         return flag;
      }
   }

   public void startExecuting() {
      this.taskOwner.setAttackTarget(this.attackTarget);
      super.startExecuting();
   }

   public void resetTask() {
      this.attackTarget = null;
      super.resetTask();
   }

   public static boolean isLookingAtEnderman(EntityLiving taskOwner, EntityPlayer player) {
      Vec3 playerVec = player.getLookVec().normalize();
      double y = taskOwner.boundingBox.minY + ((double)taskOwner.height - 1.8) - (player.posY + (double)player.getEyeHeight());
      Vec3 targetVec = Vec3.createVectorHelper(taskOwner.posX - player.posX, y, taskOwner.posZ - player.posZ);
      double length = targetVec.lengthVector();
      targetVec = targetVec.normalize();
      double d = playerVec.dotProduct(targetVec);
      return d > (double)1.0F - 0.08 / length ? player.canEntityBeSeen(taskOwner) : false;
   }
}
