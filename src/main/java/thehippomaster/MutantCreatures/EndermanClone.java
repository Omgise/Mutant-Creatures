package thehippomaster.MutantCreatures;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.ai.MCAIAttackOnCollide;

public class EndermanClone extends EntityMob {
   public EndermanClone(World world) {
      super(world);
      this.experienceValue = this.rand.nextInt(2);
      this.stepHeight = 1.0F;
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new MCAIAttackOnCollide(this, EntityPlayer.class, 1.0F, false));
      this.tasks.addTask(2, new MCAIAttackOnCollide(this, 1.0F, true));
      this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
      this.setSize(0.6F, 2.9F);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)1.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue((double)1.0F);
   }

   protected boolean isAIEnabled() {
      return true;
   }

   public void setDead() {
      super.setDead();
      MCHandler.spawnParticlesAtEntity(4, this, 0);
   }

   public boolean attackEntityAsMob(Entity entity) {
      boolean flag = super.attackEntityAsMob(entity);
      if (!this.worldObj.isRemote && this.rand.nextInt(3) != 0) {
         double x = entity.posX + (double)((this.rand.nextFloat() - 0.5F) * 24.0F);
         double z = entity.posZ + (double)((this.rand.nextFloat() - 0.5F) * 24.0F);
         double y = entity.posY + (double)this.rand.nextInt(5) + (double)4.0F;
         MutantCreatures.teleportTo(this, x, y, z);
      }

      return flag;
   }

   public boolean attackEntityFrom(DamageSource source, float f) {
      if (!this.worldObj.isRemote) {
         this.worldObj.playSoundEffect(this.posX, this.posY + (double)this.height / (double)2.0F, this.posZ, "mob.endermen.portal", 1.0F, 1.0F);
         this.setDead();
      }

      return true;
   }

   public void readEntityFromNBT(NBTTagCompound tagcompound) {
      super.readEntityFromNBT(tagcompound);
      this.setDead();
   }

   public float getSoundVolume() {
      return 0.3F;
   }

   public String getLivingSound() {
      return "mob.endermen.idle";
   }

   public String getHurtSound() {
      return "mob.endermen.hit";
   }

   public String getDeathSound() {
      return null;
   }
}
