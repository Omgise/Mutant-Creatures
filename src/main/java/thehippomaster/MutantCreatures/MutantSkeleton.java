package thehippomaster.MutantCreatures;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.IAnimatedEntity;
import thehippomaster.MutantCreatures.ai.MCAIAttackOnCollide;
import thehippomaster.MutantCreatures.ai.MCAISkeleConstrict;
import thehippomaster.MutantCreatures.ai.MCAISkeleMelee;
import thehippomaster.MutantCreatures.ai.MCAISkeleMulti;
import thehippomaster.MutantCreatures.ai.MCAISkeleShoot;
import thehippomaster.MutantCreatures.packet.PacketSkeleAttack;

public class MutantSkeleton extends EntityMob implements IAnimatedEntity {
   public int currentAttackID;
   public int animTick;

   public MutantSkeleton(World world) {
      super(world);
      this.experienceValue = 30;
      this.animTick = 0;
      this.ignoreFrustumCheck = true;
      this.setSize(1.2F, 3.2F);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, new MCAISkeleMelee(this));
      this.tasks.addTask(2, new MCAISkeleShoot(this));
      this.tasks.addTask(2, new MCAISkeleMulti(this));
      this.tasks.addTask(2, new MCAISkeleConstrict(this));
      this.tasks.addTask(3, (new MCAIAttackOnCollide(this, EntityPlayer.class, 1.1F, false)).setMaxAttackTick(20));
      this.tasks.addTask(4, (new MCAIAttackOnCollide(this, 1.1F, true)).setMaxAttackTick(20));
      this.tasks.addTask(5, new EntityAIWander(this, (double)1.0F));
      this.tasks.addTask(6, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)160.0F);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue((double)96.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.27);
   }

   protected boolean isAIEnabled() {
      return true;
   }

   public int getMaxSpawnedInChunk() {
      return 1;
   }

   protected void fall(float f) {
   }

   public void setAnimID(int id) {
      this.currentAttackID = id;
   }

   public void setAnimTick(int tick) {
      this.animTick = tick;
   }

   public int getAnimID() {
      return this.currentAttackID;
   }

   public int getAnimTick() {
      return this.animTick;
   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEAD;
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.currentAttackID != 0) {
         ++this.animTick;
      }

      if (this.getAttackTarget() != null && !this.getAttackTarget().isEntityAlive()) {
         this.setAttackTarget((EntityLivingBase)null);
      }

      if (this.getHealth() > 0.0F && this.ticksExisted % 100 == 0 && !this.worldObj.isDaytime()) {
         this.heal(2.0F);
      }

   }

   public void onDeath(DamageSource source) {
      super.onDeath(source);
      if (!this.worldObj.isRemote) {
         List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)3.0F, (double)2.0F, (double)3.0F));

         for(int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity)list.get(i);
            if (entity instanceof EntityLivingBase) {
               entity.attackEntityFrom(DamageSource.causeMobDamage(this), 7.0F);
            }
         }

         for(int i = 0; i < 18; ++i) {
            int j = i;
            if (i >= 3) {
               j = i + 1;
            }

            if (j >= 4) {
               ++j;
            }

            if (j >= 5) {
               ++j;
            }

            if (j >= 6) {
               ++j;
            }

            if (j >= 9) {
               ++j;
            }

            if (j >= 10) {
               ++j;
            }

            if (j >= 11) {
               ++j;
            }

            if (j >= 12) {
               ++j;
            }

            if (j >= 15) {
               ++j;
            }

            if (j >= 16) {
               ++j;
            }

            if (j >= 17) {
               ++j;
            }

            if (j >= 18) {
               ++j;
            }

            if (j >= 20) {
               ++j;
            }

            SkeletonPart part = new SkeletonPart(this.worldObj, this, j);
            part.motionX += (double)(this.rand.nextFloat() * 0.8F * 2.0F - 0.8F);
            part.motionY += (double)(this.rand.nextFloat() * 0.25F + 0.1F);
            part.motionZ += (double)(this.rand.nextFloat() * 0.8F * 2.0F - 0.8F);
            this.worldObj.spawnEntityInWorld(part);
         }
      }

      this.deathTime = 19;
      this.onDeathUpdate();
   }

   public boolean attackEntityAsMob(Entity entity) {
      if (!this.worldObj.isRemote && this.currentAttackID == 0 && (this.onGround || this.isInWater())) {
         if (this.rand.nextInt(4) != 0) {
            this.sendAttackPacket(1);
         } else {
            this.sendAttackPacket(4);
         }
      }

      return true;
   }

   public void knockBack(Entity entity, float dmg, double d, double d1) {
   }

   public boolean getCanSpawnHere() {
      return super.getCanSpawnHere() && MutantCreatures.getRandomSpawnChance();
   }

   public float getBrightness(float f) {
      float f1 = ((float)this.hurtTime - f) / (float)this.maxHurtTime;
      return super.getBrightness(f) * f1;
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   protected String getLivingSound() {
      return "MutantCreatures:mutantskeleton.living";
   }

   protected String getHurtSound() {
      return "MutantCreatures:mutantskeleton.hurt";
   }

   protected String getDeathSound() {
      return "MutantCreatures:mutantskeleton.death";
   }

   protected void playStepSound(int par1, int par2, int par3, Block par4) {
      this.playSound("MutantCreatures:mutantskeleton.step", 0.15F, 1.0F);
   }

   public void sendAttackPacket(int id) {
      if (!MutantCreatures.isEffectiveClient()) {
         this.currentAttackID = id;
         MutantCreatures.wrapper.sendToAll(new PacketSkeleAttack(id, this));
      }
   }
}
