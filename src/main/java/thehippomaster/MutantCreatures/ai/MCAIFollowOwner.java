package thehippomaster.MutantCreatures.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.CreeperMinion;

public class MCAIFollowOwner extends EntityAIBase {
   private float moveSpeed;
   private float minDistance;
   private float maxDistance;
   private CreeperMinion creeperMinion;
   private PathNavigate navigator;
   private EntityPlayer owner;

   public MCAIFollowOwner(CreeperMinion minion, float speed, float minDist, float maxDist) {
      this.moveSpeed = speed;
      this.minDistance = minDist;
      this.maxDistance = maxDist;
      if (this.maxDistance < 4.0F) {
         this.maxDistance = 4.0F;
      }

      this.creeperMinion = minion;
      this.navigator = minion.getNavigator();
      this.owner = null;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      if (this.owner == null) {
         this.owner = this.creeperMinion.worldObj.getPlayerEntityByName(this.creeperMinion.getOwner());
      }

      if (this.owner != null && !this.creeperMinion.getSitting()) {
         double d = this.creeperMinion.getDistanceSqToEntity(this.owner);
         return d > (double)(this.minDistance * this.minDistance);
      } else {
         return false;
      }
   }

   public boolean continueExecuting() {
      if (this.creeperMinion.getSitting()) {
         return false;
      } else {
         double d = this.creeperMinion.getDistanceSqToEntity(this.owner);
         float f = this.minDistance;
         if (this.creeperMinion.getAttackTarget() != null) {
            f = this.maxDistance - 4.0F;
         }

         return !this.navigator.noPath() && d > (double)(f * f);
      }
   }

   public void resetTask() {
      this.navigator.clearPathEntity();
   }

   public void updateTask() {
      this.creeperMinion.getLookHelper().setLookPositionWithEntity(this.owner, 10.0F, (float)this.creeperMinion.getVerticalFaceSpeed());
      if (!this.navigator.tryMoveToEntityLiving(this.owner, (double)this.moveSpeed)) {
         if (!(this.creeperMinion.getDistanceSqToEntity(this.owner) < (double)(this.maxDistance * this.maxDistance))) {
            int i = MathHelper.floor_double(this.owner.posX) - 2;
            int j = MathHelper.floor_double(this.owner.posZ) - 2;
            int k = MathHelper.floor_double(this.owner.boundingBox.minY);
            World world = this.creeperMinion.worldObj;

            for(int l = 0; l <= 4; ++l) {
               for(int i1 = 0; i1 <= 4; ++i1) {
                  if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && World.doesBlockHaveSolidTopSurface(world, i + l, k - 1, j + i1) && !world.getBlock(i + l, k, j + i1).isNormalCube() && !world.getBlock(i + l, k + 1, j + i1).isNormalCube()) {
                     this.creeperMinion.setPosition((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F));
                     this.navigator.clearPathEntity();
                     return;
                  }
               }
            }

         }
      }
   }
}
