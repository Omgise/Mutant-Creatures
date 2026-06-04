package thehippomaster.MutantCreatures.ai;

import java.util.ArrayList;
import net.minecraft.entity.ai.EntityAIBase;
import thehippomaster.MutantCreatures.EnderBlock;
import thehippomaster.MutantCreatures.MutantEnderman;

public class MCAIEnderThrowBlock extends EntityAIBase {
   private MutantEnderman mutantEnderman;

   public MCAIEnderThrowBlock(MutantEnderman enderman) {
      this.mutantEnderman = enderman;
   }

   public boolean shouldExecute() {
      if (this.mutantEnderman.currentAttackID != 0) {
         return false;
      } else if (!this.mutantEnderman.triggerThrowBlock && this.mutantEnderman.getRNG().nextInt(28) != 0) {
         return false;
      } else {
         int id = this.getThrowingHand();
         if (id == -1) {
            return false;
         } else {
            this.mutantEnderman.setThrownBlock(id);
            return true;
         }
      }
   }

   public void startExecuting() {
      this.mutantEnderman.animTick = 0;
      this.mutantEnderman.sendAttackPacket(2);
      this.mutantEnderman.triggerThrowBlock = false;
      int id = this.mutantEnderman.getThrownBlock();
      EnderBlock block = new EnderBlock(this.mutantEnderman.worldObj, this.mutantEnderman, id);
      this.mutantEnderman.worldObj.spawnEntityInWorld(block);
      this.mutantEnderman.sendHoldBlock(id, 0, 0);
   }

   public boolean continueExecuting() {
      return this.mutantEnderman.currentAttackID == 2 && this.mutantEnderman.animTick < 14;
   }

   public void resetTask() {
      this.mutantEnderman.sendAttackPacket(0);
      this.mutantEnderman.setThrownBlock(0);
      this.mutantEnderman.triggerThrowBlock = false;
   }

   private int getThrowingHand() {
      ArrayList<Integer> outer = new ArrayList();
      ArrayList<Integer> inner = new ArrayList();

      for(int i = 1; i < this.mutantEnderman.heldBlock.length; ++i) {
         if (this.mutantEnderman.heldBlock[i] != 0) {
            if (i <= 2) {
               outer.add(i);
            } else {
               inner.add(i);
            }
         }
      }

      if (outer.isEmpty() && inner.isEmpty()) {
         return -1;
      } else if (!inner.isEmpty()) {
         return (Integer)inner.get(this.mutantEnderman.getRNG().nextInt(inner.size()));
      } else {
         return (Integer)outer.get(this.mutantEnderman.getRNG().nextInt(outer.size()));
      }
   }
}
