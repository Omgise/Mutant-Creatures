package thehippomaster.MutantCreatures.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.MutantSnowGolem;

public class MCAISGolemSwimJump extends EntityAIBase {
   private int jumpTick = 20;
   private boolean waterReplaced = false;
   private MutantSnowGolem snowGolem;
   private ChunkCoordinates prevPos;

   public MCAISGolemSwimJump(MutantSnowGolem golem) {
      this.snowGolem = golem;
      this.prevPos = null;
      golem.getNavigator().setCanSwim(true);
      this.setMutexBits(4);
   }

   public boolean shouldExecute() {
      return this.snowGolem.isInWater();
   }

   public void startExecuting() {
      int x = MathHelper.floor_double(this.snowGolem.posX);
      int y = MathHelper.floor_double(this.snowGolem.boundingBox.minY) - 1;
      int z = MathHelper.floor_double(this.snowGolem.posZ);
      this.prevPos = new ChunkCoordinates(x, y, z);
      this.snowGolem.motionX = (double)((this.snowGolem.getRNG().nextFloat() - this.snowGolem.getRNG().nextFloat()) * 0.9F);
      this.snowGolem.motionY = (double)1.5F;
      this.snowGolem.motionZ = (double)((this.snowGolem.getRNG().nextFloat() - this.snowGolem.getRNG().nextFloat()) * 0.9F);
      this.snowGolem.attackEntityFrom(DamageSource.drown, 16.0F);
      this.snowGolem.setSwimJump(true);
   }

   public boolean continueExecuting() {
      return this.jumpTick > 0;
   }

   public void updateTask() {
      --this.jumpTick;
      if (!this.waterReplaced && !this.snowGolem.isInWater() && this.jumpTick < 17) {
         this.prevPos.posY = getWaterSurfaceHeight(this.snowGolem.worldObj, this.prevPos);
         if ((double)this.prevPos.posY > this.snowGolem.posY) {
            return;
         }

         for(int x = -2; x <= 2; ++x) {
            for(int y = -1; y <= 1; ++y) {
               for(int z = -2; z <= 2; ++z) {
                  if (y == 0 || Math.abs(x) != 2 && Math.abs(z) != 2) {
                     int posX = this.prevPos.posX + x;
                     int posY = this.prevPos.posY + y;
                     int posZ = this.prevPos.posZ + z;
                     Block block = this.snowGolem.worldObj.getBlock(posX, posY, posZ);
                     if (block == Blocks.air || block == Blocks.water || block == Blocks.flowing_water) {
                        if (y != 0) {
                           if ((Math.abs(x) == 1 || Math.abs(z) == 1) && this.snowGolem.getRNG().nextInt(4) == 0) {
                              continue;
                           }
                        } else if ((Math.abs(x) == 2 || Math.abs(z) == 2) && this.snowGolem.getRNG().nextInt(3) == 0) {
                           continue;
                        }

                        this.snowGolem.worldObj.setBlock(posX, posY, posZ, Blocks.ice, 0, 3);
                     }
                  }
               }
            }
         }

         Block topBlock = this.snowGolem.worldObj.getBlock(this.prevPos.posX, this.prevPos.posY + 2, this.prevPos.posZ);
         if (topBlock == Blocks.air) {
            this.snowGolem.worldObj.setBlock(this.prevPos.posX, this.prevPos.posY + 2, this.prevPos.posZ, Blocks.ice, 0, 3);
         }

         this.waterReplaced = true;
      }

   }

   public void resetTask() {
      this.jumpTick = 20;
      this.waterReplaced = false;
      this.snowGolem.setSwimJump(false);
   }

   public static int getWaterSurfaceHeight(World world, ChunkCoordinates coord) {
      int y = coord.posY;

      while(true) {
         Block block = world.getBlock(coord.posX, y + 1, coord.posZ);
         if (block != Blocks.water && block != Blocks.flowing_water) {
            return y;
         }

         ++y;
      }
   }
}
