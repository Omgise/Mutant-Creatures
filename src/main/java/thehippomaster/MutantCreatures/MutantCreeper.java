package thehippomaster.MutantCreatures;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.ai.MCAIAttackOnCollide;
import thehippomaster.MutantCreatures.ai.MCAICreeperCharge;
import thehippomaster.MutantCreatures.ai.MCAICreeperJump;
import thehippomaster.MutantCreatures.ai.MCAISpawnCreepers;

public class MutantCreeper extends EntityMob {
   public MCAIAttackOnCollide aiCollideAttack;
   public MCAICreeperCharge aiChargeAttack;
   private int swingProgressInt;
   private String killerName;

   public MutantCreeper(World world) {
      super(world);
      this.experienceValue = 30;
      this.killerName = "";
      this.setSize(2.0F, 2.4F);
      this.getNavigator().setAvoidsWater(true);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new MCAICreeperJump(this, 0.9F, 1.4F));
      this.tasks.addTask(1, new MCAISpawnCreepers(this, 0.6F, 4));
      this.tasks.addTask(1, this.aiChargeAttack = new MCAICreeperCharge(this, 0.7F));
      this.tasks.addTask(2, this.aiCollideAttack = (new MCAIAttackOnCollide(this, 1.3F, false)).setMaxAttackTick(20 + this.rand.nextInt(10)));
      this.tasks.addTask(3, new EntityAIWander(this, (double)1.0F));
      this.tasks.addTask(4, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, false));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityOcelot.class, 0, true));
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte)0);
      this.dataWatcher.addObject(17, (byte)0);
      this.dataWatcher.addObject(18, (byte)0);
      this.dataWatcher.addObject(19, (byte)0);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)120.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.26);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue((double)4.0F);
   }

   private void setSwinging(boolean flag) {
      this.dataWatcher.updateObject(16, (byte)(flag ? 1 : 0));
   }

   public void setCharged(boolean flag) {
      this.dataWatcher.updateObject(17, (byte)(flag ? 1 : 0));
   }

   public void setSuperJump(boolean flag) {
      this.dataWatcher.updateObject(18, (byte)(flag ? 1 : 0));
   }

   public void setChargingAttack(boolean flag) {
      this.dataWatcher.updateObject(19, (byte)(flag ? 1 : 0));
   }

   private boolean getSwinging() {
      return this.dataWatcher.getWatchableObjectByte(16) == 1;
   }

   public boolean getCharged() {
      return this.dataWatcher.getWatchableObjectByte(17) == 1;
   }

   public boolean getSuperJump() {
      return this.dataWatcher.getWatchableObjectByte(18) == 1;
   }

   public boolean getChargingAttack() {
      return this.dataWatcher.getWatchableObjectByte(19) == 1;
   }

   public boolean isAIEnabled() {
      return true;
   }

   public float getEyeHeight() {
      return this.height;
   }

   public int getMaxSpawnedInChunk() {
      return 1;
   }

   protected void fall(float f) {
   }

   protected void updateSwingState() {
      int swingSpeed = 6;
      if (this.getSwinging()) {
         ++this.swingProgressInt;
         if (this.swingProgressInt >= swingSpeed) {
            this.swingProgressInt = 0;
            this.setSwinging(false);
         }
      } else {
         this.swingProgressInt = 0;
      }

      this.swingProgress = (float)this.swingProgressInt / (float)swingSpeed;
   }

   protected void updateSuperJump(boolean wasOnGround) {
      if (!wasOnGround && this.onGround) {
         this.setSuperJump(false);
         this.aiCollideAttack.resetAttackTick();
         if (!this.worldObj.isRemote) {
            boolean grief = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
            MCExplosion explosion = new MCExplosion(this.worldObj, this, this.posX, this.posY, this.posZ, this.getCharged() ? 6.0F : 4.0F);
            explosion.destroyBlocks = grief;
            explosion.explode();
         }
      }

   }

   public void onUpdate() {
      boolean wasOnGround = this.onGround;
      super.onUpdate();
      this.isImmuneToFire = this.getCharged();
      this.updateSwingState();
      if (this.getSuperJump()) {
         this.updateSuperJump(wasOnGround);
      }

      float yawOffset = this.rotationYaw - this.renderYawOffset;
      if (Math.abs(yawOffset) > 3.0F) {
         this.renderYawOffset += yawOffset * 0.2F;
      }

      EntityLivingBase target = this.getAttackTarget();
      if (target != null && target.isDead) {
         this.setAttackTarget((EntityLivingBase)null);
      }

   }

   public boolean attackEntityAsMob(Entity entity) {
      this.swingHead();
      this.aiCollideAttack.resetAttackTick();
      boolean impact = true;
      if (entity instanceof EntityPlayer && ((EntityPlayer)entity).isBlocking()) {
         impact = false;
      }

      if (impact) {
         double x = entity.posX - this.posX;
         double y = entity.posY - this.posY;
         double z = entity.posZ - this.posZ;
         double d = Math.sqrt(x * x + y * y + z * z);
         entity.motionX = x / d * (double)0.5F;
         entity.motionY = y / d * (double)0.05F + (double)0.15F;
         entity.motionZ = z / d * (double)0.5F;
      }

      return super.attackEntityAsMob(entity);
   }

   public boolean attackEntityFrom(DamageSource source, float dmg) {
      if (source.isExplosion()) {
         float amount = dmg / 2.0F;
         this.heal(amount);
         if (!this.worldObj.isRemote) {
            MCHandler.spawnHeartsAtEntity(this, (int)(amount / 2.0F));
         }

         return true;
      } else {
         if (this.getChargingAttack()) {
            --this.aiChargeAttack.hitCount;
         }

         return super.attackEntityFrom(source, dmg);
      }
   }

   public void swingHead() {
      if (!this.getSwinging() || this.swingProgressInt >= 3 || this.swingProgressInt < 0) {
         this.swingProgressInt = -1;
         this.setSwinging(true);
      }

   }

   public void onStruckByLightning(EntityLightningBolt bolt) {
      this.setCharged(true);
   }

   public int maxDeathTime() {
      return 100;
   }

   @SideOnly(Side.CLIENT)
   public int getExplosionColor() {
      float f = (float)this.deathTime / (float)this.maxDeathTime();
      if (this.getChargingAttack()) {
         int i = this.ticksExisted % 20;
         f = i < 10 ? 0.6F : 0.0F;
      }

      return (int)(f * 255.0F);
   }

   public void onDeath(DamageSource source) {
      super.onDeath(source);
      Entity entity = source.getEntity();
      if (entity != null && entity instanceof EntityPlayer) {
         this.killerName = ((EntityPlayer)entity).getCommandSenderName();
      }

      this.worldObj.playSoundAtEntity(this, "MutantCreatures:mutantcreeper.fuse", 0.9F, 1.0F);
   }

   protected void onDeathUpdate() {
      ++this.deathTime;
      float f = this.getCharged() ? 12.0F : 8.0F;
      float f1 = f * 1.5F;
      List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)f1, (double)f1, (double)f1));

      for(int i = 0; i < list.size(); ++i) {
         Entity entity = (Entity)list.get(i);
         double x = this.posX - entity.posX;
         double y = this.posY - entity.posY;
         double z = this.posZ - entity.posZ;
         double d = Math.sqrt(x * x + y * y + z * z);
         float f2 = (float)this.deathTime / (float)this.maxDeathTime();
         entity.motionX += x / d * (double)f2 * 0.09;
         entity.motionY += y / d * (double)f2 * 0.09;
         entity.motionZ += z / d * (double)f2 * 0.09;
      }

      this.setPosition(this.posX + (double)(this.rand.nextFloat() * 0.2F) - (double)0.1F, this.posY, this.posZ + (double)(this.rand.nextFloat() * 0.2F) - (double)0.1F);
      if (this.deathTime >= this.maxDeathTime()) {
         if (!this.worldObj.isRemote) {
            boolean grief = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
            MCExplosion explosion = new MCExplosion(this.worldObj, this, this.posX, this.posY, this.posZ, f);
            explosion.destroyBlocks = grief;
            explosion.explode();
            if ((this.recentlyHit > 0 || this.isPlayer()) && !this.isChild()) {
               int i = this.getExperiencePoints(this.attackingPlayer);

               while(i > 0) {
                  int k = EntityXPOrb.getXPSplit(i);
                  i -= k;
                  this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, k));
               }
            }

            if (this.killerName != null) {
               CreeperMinionEgg egg = new CreeperMinionEgg(this.worldObj);
               egg.setOwner(this.killerName);
               egg.setPosition(this.posX, this.posY, this.posZ);
               this.worldObj.spawnEntityInWorld(egg);
            }
         }

         this.setDead();
      }

   }

   public boolean getCanSpawnHere() {
      return super.getCanSpawnHere() && MutantCreatures.getRandomSpawnChance();
   }

   public float getBrightness(float f) {
      float f1 = ((float)this.hurtTime - f) / (float)this.maxHurtTime;
      return super.getBrightness(f) * f1;
   }

   public void writeEntityToNBT(NBTTagCompound tagcompound) {
      super.writeEntityToNBT(tagcompound);
      tagcompound.setBoolean("charged", this.getCharged());
      tagcompound.setString("killer", this.killerName);
   }

   public void readEntityFromNBT(NBTTagCompound tagcompound) {
      super.readEntityFromNBT(tagcompound);
      this.setCharged(tagcompound.getBoolean("charged"));
      this.killerName = tagcompound.getString("killer");
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   protected String getLivingSound() {
      return "MutantCreatures:mutantcreeper.living";
   }

   protected String getHurtSound() {
      return "MutantCreatures:mutantcreeper.hurt";
   }

   protected String getDeathSound() {
      return null;
   }
}
