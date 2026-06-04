package thehippomaster.MutantCreatures.ai;

import net.minecraft.entity.ai.EntityAIBase;
import thehippomaster.MutantCreatures.CreeperMinion;

public class MCAICreeperSit extends EntityAIBase {
   private CreeperMinion creeperMinion;

   public MCAICreeperSit(CreeperMinion minion) {
      this.creeperMinion = minion;
      this.setMutexBits(5);
   }

   public boolean shouldExecute() {
      return this.creeperMinion.getSitting();
   }

   public void startExecuting() {
      this.creeperMinion.getNavigator().clearPathEntity();
   }
}
