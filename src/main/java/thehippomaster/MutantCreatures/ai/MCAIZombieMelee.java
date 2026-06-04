package thehippomaster.MutantCreatures.ai;

import java.util.ArrayList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.MutantZombie;
import thehippomaster.MutantCreatures.ZombieChunk;
import thehippomaster.MutantCreatures.ZombieResurrect;

public class MCAIZombieMelee extends EntityAIBase {
   private double dirX = (double)-1.0F;
   private double dirZ = (double)-1.0F;
   private MutantZombie mutantZombie;
   private EntityLivingBase attackTarget;

   public MCAIZombieMelee(MutantZombie zombie) {
      this.mutantZombie = zombie;
      this.attackTarget = null;
      this.setMutexBits(7);
   }

   public boolean shouldExecute() {
      this.attackTarget = this.mutantZombie.getAttackTarget();
      if (this.attackTarget != null && this.mutantZombie.onGround) {
         return this.mutantZombie.currentAttackID == 1;
      } else {
         return false;
      }
   }

   public void startExecuting() {
      this.mutantZombie.animTick = 0;
      this.mutantZombie.getNavigator().clearPathEntity();
      this.mutantZombie.worldObj.playSoundAtEntity(this.mutantZombie, "MutantCreatures:mutantzombie.gruntA", 0.3F, 0.8F + this.mutantZombie.getRNG().nextFloat() * 0.4F);
   }

   public boolean continueExecuting() {
      return this.mutantZombie.deathTick <= 0 && this.mutantZombie.animTick < 25;
   }

   public void updateTask() {
      if (this.mutantZombie.animTick < 8) {
         this.mutantZombie.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
      }

      if (this.mutantZombie.animTick == 8) {
         double x = this.attackTarget.posX - this.mutantZombie.posX;
         double z = this.attackTarget.posZ - this.mutantZombie.posZ;
         double d = Math.sqrt(x * x + z * z);
         this.dirX = x / d;
         this.dirZ = z / d;
      }

      if (this.mutantZombie.animTick == 12) {
         int x = MathHelper.floor_double(this.mutantZombie.posX + this.dirX * (double)2.0F);
         int y = MathHelper.floor_double(this.mutantZombie.boundingBox.minY);
         int z = MathHelper.floor_double(this.mutantZombie.posZ + this.dirZ * (double)2.0F);
         int x1 = MathHelper.floor_double(this.mutantZombie.posX + this.dirX * (double)8.0F);
         int z1 = MathHelper.floor_double(this.mutantZombie.posZ + this.dirZ * (double)8.0F);
         addLinePositions(this.mutantZombie.worldObj, this.mutantZombie.meleeGroundList, x, z, x1, z1, y);
         this.mutantZombie.worldObj.playSoundAtEntity(this.mutantZombie, "random.explode", 0.5F, 0.8F + this.mutantZombie.getRNG().nextFloat() * 0.4F);
      }

   }

   public void resetTask() {
      this.mutantZombie.sendAttackPacket(0);
      this.dirX = (double)-1.0F;
      this.dirZ = (double)-1.0F;
   }

   public static void addLinePositions(World world, ArrayList<ZombieChunk> list, int x1, int z1, int x2, int z2, int y) {
      int deltaX = x2 - x1;
      int deltaZ = z2 - z1;
      int xStep = deltaX < 0 ? -1 : 1;
      int zStep = deltaZ < 0 ? -1 : 1;
      deltaX = Math.abs(deltaX);
      deltaZ = Math.abs(deltaZ);
      int x = x1;
      int z = z1;
      int deltaX2 = deltaX * 2;
      int deltaZ2 = deltaZ * 2;
      ZombieChunk chunk = addPoint(world, list, x1, y, z1);
      if (chunk != null) {
         chunk.setFirst(true);
      }

      if (deltaX2 >= deltaZ2) {
         int error = deltaX;

         for(int i = 0; i < deltaX; ++i) {
            x += xStep;
            error += deltaZ2;
            if (error > deltaX2) {
               z += zStep;
               error -= deltaX2;
            }

            addPoint(world, list, x, y, z);
         }
      } else {
         int error = deltaZ;

         for(int i = 0; i < deltaZ; ++i) {
            z += zStep;
            error += deltaX2;
            if (error > deltaZ2) {
               x += xStep;
               error -= deltaZ2;
            }

            addPoint(world, list, x, y, z);
         }
      }

   }

   public static ZombieChunk addPoint(World world, ArrayList<ZombieChunk> list, int x, int y, int z) {
      y = ZombieResurrect.getSuitableGround(world, x, y, z, 3, false);
      ZombieChunk chunk = null;
      if (y != -1) {
         list.add(chunk = new ZombieChunk(x, y, z));
      }

      if (world.rand.nextInt(2) == 0) {
         list.add((new ZombieChunk(x, y + 1, z)).setParticles(false));
      }

      return chunk;
   }
}
