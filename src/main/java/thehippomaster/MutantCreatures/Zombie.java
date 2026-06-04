package thehippomaster.MutantCreatures;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class Zombie extends EntityZombie {
   public MutantZombie leader;

   public Zombie(World world) {
      super(world);
      this.setHealth(this.getMaxHealth() * (0.6F + 0.4F * this.rand.nextFloat()));
      this.leader = null;
      this.tasks.taskEntries = new ArrayList();
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIBreakDoor(this));
      this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, (double)1.0F, false));
      this.tasks.addTask(3, new EntityAIAttackOnCollide(this, (double)1.0F, true));
      this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, (double)1.0F));
      this.tasks.addTask(5, new EntityAIMoveThroughVillage(this, (double)1.0F, false));
      this.tasks.addTask(6, new EntityAIWander(this, (double)1.0F));
      this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(7, new EntityAILookIdle(this));
   }

   protected boolean hasSuitableLeader() {
      if (!this.leader.isEntityAlive()) {
         return false;
      } else {
         double d = this.getDistanceSqToEntity(this.leader);
         return !(d > (double)576.0F);
      }
   }

   protected void updateLeaderState() {
      int x = MathHelper.floor_double(this.leader.posX);
      int y = MathHelper.floor_double(this.leader.boundingBox.minY);
      int z = MathHelper.floor_double(this.leader.posZ);
      if (!this.hasHome()) {
         this.setHomeArea(x, y, z, 16);
      } else {
         this.getHomePosition().set(x, y, z);
      }

      if (this.getAttackTarget() == null) {
         EntityLivingBase living = this.leader.getAttackTarget();
         if (living != null) {
            this.setAttackTarget(living);
            this.setRevengeTarget(living);
         }
      }

   }

   public void onUpdate() {
      super.onUpdate();
      if (this.getAttackTarget() != null && !this.getAttackTarget().isEntityAlive()) {
         this.setAttackTarget((EntityLivingBase)null);
      }

      if (this.leader != null && !this.hasSuitableLeader()) {
         this.leader = null;
      }

      if (this.leader == null) {
         this.detachHome();
         List list = this.worldObj.getEntitiesWithinAABB(MutantZombie.class, this.boundingBox.expand((double)16.0F, (double)16.0F, (double)16.0F));
         if (list.size() > 0) {
            this.leader = (MutantZombie)list.get(this.rand.nextInt(list.size()));
         }
      } else {
         this.updateLeaderState();
      }

   }

   public boolean attackEntityFrom(DamageSource source, float f) {
      Entity entity = source.getEntity();
      return entity != null && entity instanceof MutantZombie ? super.attackEntityFrom(DamageSource.generic, f) : super.attackEntityFrom(source, f);
   }

   protected Item getDropItem() {
      return this.rand.nextInt(3) == 0 ? super.getDropItem() : null;
   }

   protected void dropRareDrop(int i) {
      if (!this.rand.nextBoolean()) {
         super.dropRareDrop(i);
      }
   }
}
