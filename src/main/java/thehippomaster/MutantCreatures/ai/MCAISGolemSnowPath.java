package thehippomaster.MutantCreatures.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import thehippomaster.MutantCreatures.MutantSnowGolem;

public class MCAISGolemSnowPath extends EntityAIBase {
   private MutantSnowGolem snowGolem;

   public MCAISGolemSnowPath(MutantSnowGolem golem) {
      this.snowGolem = golem;
   }

   public boolean shouldExecute() {
      return this.snowGolem.onGround;
   }

   public void updateTask() {
      int x = MathHelper.floor_double(this.snowGolem.posX);
      int y = MathHelper.floor_double(this.snowGolem.boundingBox.minY);
      int z = MathHelper.floor_double(this.snowGolem.posZ);

      for(int i = -2; i <= 2; ++i) {
         for(int j = -2; j <= 2; ++j) {
            if (Math.abs(i) != 2 || Math.abs(j) != 2) {
               Block block = this.snowGolem.worldObj.getBlock(x + i, y, z + j);
               Block block1 = this.snowGolem.worldObj.getBlock(x + i, y - 1, z + j);
               Block block2 = this.snowGolem.worldObj.getBlock(x + i, y + 1, z + j);
               boolean placeSnow = block == Blocks.air && Blocks.snow_layer.canPlaceBlockAt(this.snowGolem.worldObj, x + i, y, z + j);
               if (block1 == Blocks.ice) {
                  placeSnow = false;
               }

               boolean placeIce = block1 == Blocks.water || block1 == Blocks.flowing_water;
               if (block == Blocks.flowing_water) {
                  this.snowGolem.worldObj.setBlock(x + i, y, z + j, Blocks.ice, 0, 3);
               }

               if (block2 == Blocks.flowing_water) {
                  this.snowGolem.worldObj.setBlock(x + i, y + 1, z + j, Blocks.ice, 0, 3);
               }

               if ((!placeSnow || (Math.abs(i) != 2 && Math.abs(j) != 2 || this.snowGolem.getRNG().nextInt(20) == 0) && (Math.abs(i) != 1 && Math.abs(j) != 1 || this.snowGolem.getRNG().nextInt(10) == 0)) && (!placeIce || (Math.abs(i) != 2 && Math.abs(j) != 2 || this.snowGolem.getRNG().nextInt(14) == 0) && (Math.abs(i) != 1 && Math.abs(j) != 1 || this.snowGolem.getRNG().nextInt(6) == 0))) {
                  if (placeSnow) {
                     this.snowGolem.worldObj.setBlock(x + i, y, z + j, Blocks.snow_layer, 0, 3);
                  }

                  if (placeIce) {
                     this.snowGolem.worldObj.setBlock(x + i, y - 1, z + j, Blocks.ice, 0, 3);
                  }
               }
            }
         }
      }

   }
}
