package thehippomaster.MutantCreatures.ai;

import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import thehippomaster.MutantCreatures.MCHandler;
import thehippomaster.MutantCreatures.MutantEnderman;

public class MCAIEnderScream extends EntityAIBase {
   private MutantEnderman mutantEnderman;
   private Random rand;

   public MCAIEnderScream(MutantEnderman enderman) {
      this.mutantEnderman = enderman;
      this.rand = this.mutantEnderman.getRNG();
      this.setMutexBits(7);
   }

   public boolean shouldExecute() {
      if (this.mutantEnderman.getAttackTarget() != null && this.mutantEnderman.currentAttackID == 0) {
         return this.mutantEnderman.screamDelayTick > 0 ? false : this.getScreamChance();
      } else {
         return false;
      }
   }

   public void startExecuting() {
      this.mutantEnderman.animTick = 0;
      this.mutantEnderman.sendAttackPacket(5);
      this.mutantEnderman.getNavigator().clearPathEntity();
      if (this.mutantEnderman.worldObj.getWorldInfo().isRaining()) {
         this.mutantEnderman.worldObj.getWorldInfo().setRaining(false);
      }

   }

   public boolean continueExecuting() {
      return this.mutantEnderman.animTick < 165;
   }

   public void updateTask() {
      if (this.mutantEnderman.animTick == 40) {
         MCHandler.spawnParticlesAtEntity(4, this.mutantEnderman, 0);
         float f = 0.7F + this.mutantEnderman.getRNG().nextFloat() * 0.2F;
         this.mutantEnderman.worldObj.playSoundAtEntity(this.mutantEnderman, "MutantCreatures:mutantenderman.scream", 2.0F, f);
         List screamEntities = this.mutantEnderman.worldObj.getEntitiesWithinAABBExcludingEntity(this.mutantEnderman, this.mutantEnderman.boundingBox.expand((double)20.0F, (double)12.0F, (double)20.0F));

         for(int i = 0; i < screamEntities.size(); ++i) {
            Entity entity = (Entity)screamEntities.get(i);
            double dist = this.mutantEnderman.getDistanceSqToEntity(entity);
            if (dist > (double)400.0F) {
               screamEntities.remove(i);
               --i;
            } else {
               entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage((Entity)null, this.mutantEnderman), 4.0F);
               if (entity instanceof EntityLiving) {
                  EntityLiving living = (EntityLiving)entity;
                  living.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 120, 3));
                  if (this.rand.nextInt(2) != 0) {
                     living.addPotionEffect(new PotionEffect(Potion.poison.id, 120 + this.rand.nextInt(180), this.rand.nextInt(2)));
                  }

                  if (this.rand.nextInt(4) != 0) {
                     living.addPotionEffect(new PotionEffect(Potion.weakness.id, 300 + this.rand.nextInt(300), this.rand.nextInt(2)));
                  }

                  if (this.rand.nextInt(3) != 0) {
                     living.addPotionEffect(new PotionEffect(Potion.hunger.id, 120 + this.rand.nextInt(60), 10 + this.rand.nextInt(2)));
                  }

                  if (this.rand.nextInt(4) != 0) {
                     living.addPotionEffect(new PotionEffect(Potion.confusion.id, 120 + this.rand.nextInt(400), 0));
                  }
               }
            }
         }
      }

   }

   public void resetTask() {
      this.mutantEnderman.sendAttackPacket(0);
      this.mutantEnderman.screamDelayTick = 600;
   }

   private boolean getScreamChance() {
      int chance = 1200;
      if (this.mutantEnderman.isWet()) {
         chance = 500;
      }

      return this.rand.nextInt(chance) == 0;
   }
}
