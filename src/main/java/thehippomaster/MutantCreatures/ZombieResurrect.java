package thehippomaster.MutantCreatures;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class ZombieResurrect {
   private int posX;
   private int posY;
   private int posZ;
   private int blockID;
   private int metadata;
   private int tick;
   private EntityLivingBase target;
   private World worldObj;
   private Random rand;

   public ZombieResurrect(EntityLivingBase living, int x, int y, int z) {
      this.posX = x;
      this.posY = y;
      this.posZ = z;
      this.target = living;
      this.worldObj = living.worldObj;
      this.rand = living.getRNG();
      this.blockID = Block.getIdFromBlock(this.worldObj.getBlock(x, y, z));
      this.metadata = this.worldObj.getBlockMetadata(x, y, z);
      this.tick = 100 + this.rand.nextInt(40);
   }

   private boolean canResurrect() {
      int id = Block.getIdFromBlock(this.worldObj.getBlock(this.posX, this.posY, this.posZ));
      int data = this.worldObj.getBlockMetadata(this.posX, this.posY, this.posZ);
      return this.blockID == id && this.metadata == data;
   }

   public boolean update(MutantZombie mutantZombie) {
      if (!this.canResurrect()) {
         return false;
      } else {
         if (this.rand.nextInt(15) == 0) {
            this.worldObj.playAuxSFX(2001, this.posX, this.posY + 1, this.posZ, this.blockID);
         }

         --this.tick;
         if (!this.worldObj.isRemote && this.tick <= 0) {
            Zombie zombie = new Zombie(this.worldObj);
            zombie.leader = mutantZombie;
            if (mutantZombie.getVillager() && this.rand.nextInt(3) == 0) {
               zombie.setVillager(true);
            }

            zombie.playLivingSound();
            if (!this.target.isEntityAlive()) {
               this.target = null;
            }

            zombie.setAttackTarget(this.target);
            zombie.setRevengeTarget(this.target);
            zombie.setPosition((double)this.posX + (double)0.5F, (double)this.posY + (double)1.0F, (double)this.posZ + (double)0.5F);
            this.worldObj.spawnEntityInWorld(zombie);
            return false;
         } else {
            return true;
         }
      }
   }

   public static int getSuitableGround(World world, int x, int y, int z) {
      return getSuitableGround(world, x, y, z, 4, true);
   }

   public static int getSuitableGround(World world, int x, int y, int z, int range, boolean checkDay) {
      int i = y;

      while(Math.abs(y - i) <= range) {
         Block block = world.getBlock(x, i, z);
         Block block1 = world.getBlock(x, i + 1, z);
         if (block != Blocks.lava && block != Blocks.flowing_lava && block != Blocks.fire) {
            if (block != Blocks.water) {
               if (block == Blocks.air) {
                  --i;
                  continue;
               }

               if (block != Blocks.air && block1 == Blocks.air && block.getCollisionBoundingBoxFromPool(world, x, i, z) == null) {
                  --i;
               } else if (block != Blocks.air && block1 != Blocks.air && block1.getCollisionBoundingBoxFromPool(world, x, i + 1, z) != null) {
                  ++i;
                  continue;
               }
            }

            if (checkDay && world.isDaytime()) {
               float f = world.getLightBrightness(x, y + 1, z);
               if (f > 0.5F && world.canBlockSeeTheSky(x, y + 1, z) && world.rand.nextInt(3) != 0) {
                  return -1;
               }
            }

            return i;
         }

         return -1;
      }

      return -1;
   }
}
