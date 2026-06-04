package thehippomaster.MutantCreatures;

import java.util.ArrayList;
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
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.ai.MCAIAttackOnCollide;
import thehippomaster.MutantCreatures.ai.MCAIZombieAttack;
import thehippomaster.MutantCreatures.ai.MCAIZombieMelee;
import thehippomaster.MutantCreatures.ai.MCAIZombieRoar;
import thehippomaster.MutantCreatures.ai.MCAIZombieThrow;
import thehippomaster.MutantCreatures.packet.PacketZombieAttack;

public class MutantZombie extends EntityMob {
   public int currentAttackID;
   public int animTick;
   public int throwHitTick;
   public int throwFinishTick;
   public int deathTick;
   public int vanishTick;
   public ArrayList<ZombieChunk> meleeGroundList;
   public ArrayList<ZombieResurrect> resurrectList;
   private EntityLivingBase killer;

   public MutantZombie(World world) {
      super(world);
      this.experienceValue = 30;
      this.animTick = 0;
      this.throwHitTick = -1;
      this.throwFinishTick = -1;
      this.deathTick = 0;
      this.vanishTick = 0;
      this.ignoreFrustumCheck = true;
      this.meleeGroundList = new ArrayList();
      this.resurrectList = new ArrayList();
      this.killer = null;
      this.setSize(1.8F, 2.6F);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(0, new MCAIZombieAttack(this, 3.0F));
      this.tasks.addTask(1, new MCAIZombieMelee(this));
      this.tasks.addTask(1, new MCAIZombieRoar(this, 0.35F));
      this.tasks.addTask(1, new MCAIZombieThrow(this));
      this.tasks.addTask(2, (new MCAIAttackOnCollide(this, EntityPlayer.class, 1.2F, false)).setMaxAttackTick(0));
      this.tasks.addTask(3, (new MCAIAttackOnCollide(this, 1.2F, true)).setMaxAttackTick(0));
      this.tasks.addTask(4, new EntityAIWander(this, (double)1.0F));
      this.tasks.addTask(5, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, 0, false));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, false));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)150.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.26);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte)0);
      this.dataWatcher.addObject(17, (byte)0);
      this.dataWatcher.addObject(20, (byte)3);
      this.dataWatcher.addObject(21, (byte)(this.rand.nextInt(8) == 0 ? 1 : 0));
   }

   public void setThrowAttackHit(boolean flag) {
      this.dataWatcher.updateObject(16, (byte)(flag ? 1 : 0));
   }

   public void setThrowAttackFinish(boolean flag) {
      this.dataWatcher.updateObject(17, (byte)(flag ? 1 : 0));
   }

   public void decrementLives() {
      this.dataWatcher.updateObject(20, (byte)(this.getLives() - 1));
   }

   public void setVillager() {
      this.dataWatcher.updateObject(21, (byte)1);
   }

   public boolean getThrowAttackHit() {
      return this.dataWatcher.getWatchableObjectByte(16) == 1;
   }

   public boolean getThrowAttackFinish() {
      return this.dataWatcher.getWatchableObjectByte(17) == 1;
   }

   public int getLives() {
      return this.dataWatcher.getWatchableObjectByte(20);
   }

   public boolean getVillager() {
      return this.dataWatcher.getWatchableObjectByte(21) == 1;
   }

   protected boolean isAIEnabled() {
      return true;
   }

   public int getTotalArmorValue() {
      return 3;
   }

   public int getMaxSpawnedInChunk() {
      return 1;
   }

   protected void fall(float f) {
   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEAD;
   }

   protected void fixRotation() {
      float f;
      for(f = this.rotationYawHead - this.renderYawOffset; f < -180.0F; f += 360.0F) {
      }

      while(f >= 180.0F) {
         f -= 360.0F;
      }

      float f1 = 0.1F;
      if (this.currentAttackID == 1) {
         f1 = 0.2F;
      }

      this.renderYawOffset += f * f1;
   }

   protected void updateAnimation() {
      if (this.currentAttackID != 0) {
         ++this.animTick;
      }

      if (this.currentAttackID == 3) {
         if (this.getThrowAttackHit()) {
            if (this.throwHitTick == -1) {
               this.throwHitTick = 0;
            }

            ++this.throwHitTick;
         }

         if (this.getThrowAttackFinish()) {
            if (this.throwFinishTick == -1) {
               this.throwFinishTick = 0;
            }

            ++this.throwFinishTick;
         }
      } else {
         this.throwHitTick = -1;
         this.throwFinishTick = -1;
      }

   }

   protected void updateMeleeGrounds() {
      if (!this.meleeGroundList.isEmpty()) {
         ZombieChunk chunk = (ZombieChunk)this.meleeGroundList.remove(0);
         if (chunk.spawnParticles) {
            int id = Block.getIdFromBlock(this.worldObj.getBlock(chunk.posX, chunk.posY, chunk.posZ));
            int data = this.worldObj.getBlockMetadata(chunk.posX, chunk.posY, chunk.posZ);
            this.worldObj.playAuxSFX(2001, chunk.posX, chunk.posY + 1, chunk.posZ, id + (data << 12));
         }

         AxisAlignedBB box = AxisAlignedBB.getBoundingBox((double)chunk.posX, (double)(chunk.posY + 1), (double)chunk.posZ, (double)(chunk.posX + 1), (double)(chunk.posY + 2), (double)(chunk.posZ + 1));
         if (chunk.first) {
            double addScale = this.rand.nextDouble() * (double)0.75F;
            box.expand((double)0.25F + addScale, (double)0.25F + addScale * (double)0.5F, (double)0.25F + addScale);
         }

         for(Entity entity : MutantCreatures.getCollidingEntities(this, this.worldObj, box)) {
            if (entity instanceof EntityLivingBase) {
               EntityLivingBase living = (EntityLivingBase)entity;
               living.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(6 + this.rand.nextInt(3)));
               if (this.rand.nextInt(5) == 0) {
                  living.addPotionEffect(new PotionEffect(Potion.hunger.id, 160, 1));
               }
            }
         }
      }

   }

   public void onUpdate() {
      super.onUpdate();
      this.fixRotation();
      this.updateAnimation();
      if (this.getAttackTarget() != null && !this.getAttackTarget().isEntityAlive()) {
         this.setAttackTarget((EntityLivingBase)null);
      }

      if (this.deathTick <= 0 && this.ticksExisted % 100 == 0 && !this.worldObj.isDaytime()) {
         this.heal(2.0F);
      }

      this.updateMeleeGrounds();

      for(int i = this.resurrectList.size() - 1; i >= 0; --i) {
         ZombieResurrect zr = (ZombieResurrect)this.resurrectList.get(i);
         if (!zr.update(this)) {
            this.resurrectList.remove(zr);
         }
      }

      if (this.getHealth() > 0.0F) {
         this.deathTick = 0;
         this.vanishTick = 0;
      }

   }

   protected void jump() {
      super.jump();
      this.motionY += (double)0.06F;
   }

   public boolean interact(EntityPlayer player) {
      if (this.deathTick > 0) {
         ItemStack stack = player.inventory.getCurrentItem();
         if (stack != null && (stack.getItem() == Items.flint_and_steel || stack.getItem() == Items.fire_charge)) {
            this.setFire(8);
         }
      }

      return super.interact(player);
   }

   public void attackEntityAtDistSq(EntityLivingBase living, float f) {
      if (!this.worldObj.isRemote) {
         if (this.currentAttackID == 0 && this.onGround && this.rand.nextInt(20) == 0) {
            this.sendAttackPacket(1);
         }

         if (this.currentAttackID == 0 && f < 1.0F && this.rand.nextInt(125) == 0) {
            this.sendAttackPacket(3);
         }
      }

   }

   public boolean attackEntityAsMob(Entity entity) {
      if (!this.worldObj.isRemote) {
         if (this.currentAttackID == 0 && this.rand.nextInt(5) == 0) {
            this.sendAttackPacket(3);
         }

         if (this.currentAttackID == 0 && this.onGround) {
            this.sendAttackPacket(1);
         }
      }

      return true;
   }

   public boolean attackEntityFrom(DamageSource source, float f) {
      Entity entity = source.getEntity();
      return entity != null && this.currentAttackID == 3 && entity == this.getAttackTarget() ? false : super.attackEntityFrom(source, f);
   }

   public void knockBack(Entity entity, float dmg, double d, double d1) {
   }

   public int maxDeathTime() {
      return 140;
   }

   public int maxVanishTime() {
      return 100;
   }

   public void onDeath(DamageSource source) {
      super.onDeath(source);
      Entity entity = source.getEntity();
      if (!this.worldObj.isRemote && entity != null && entity instanceof EntityLivingBase) {
         this.killer = (EntityLivingBase)entity;
      }

   }

   protected void onDeathUpdate() {
      ++this.deathTick;
      if (this.isBurning()) {
         ++this.vanishTick;
      } else {
         this.vanishTick = Math.max(0, this.vanishTick - 1);
      }

      if (this.deathTick >= this.maxDeathTime()) {
         float f = (float)(this.deathTick - this.vanishTick);
         int h = (int)(40.0F * f / (float)this.maxDeathTime());
         this.deathTime = 0;
         this.deathTick = 0;
         this.vanishTick = 0;
         this.extinguish();
         if (!this.worldObj.isRemote) {
            this.decrementLives();
         }

         this.setHealth((float)h);
         if (!this.worldObj.isRemote && this.killer != null) {
            this.setAttackTarget(this.killer);
            this.setRevengeTarget(this.killer);
         }
      }

      if (this.vanishTick >= this.maxVanishTime() || this.getLives() <= 0 && this.deathTick > 25) {
         if (!this.worldObj.isRemote) {
            if ((this.recentlyHit > 0 || this.isPlayer()) && !this.isChild()) {
               int i = this.getExperiencePoints(this.attackingPlayer);

               while(i > 0) {
                  int k = EntityXPOrb.getXPSplit(i);
                  i -= k;
                  this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, k));
               }
            }

            this.dropItem(MutantCreatures.hulkHammer, 1);
            MCHandler.spawnFlamesAtEntity(this, 30);
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
      tagcompound.setInteger("deathTick", this.deathTick);
      tagcompound.setInteger("vanishTick", this.vanishTick);
      tagcompound.setBoolean("villager", this.getVillager());
   }

   public void readEntityFromNBT(NBTTagCompound tagcompound) {
      super.readEntityFromNBT(tagcompound);
      this.deathTick = tagcompound.getInteger("deathTick");
      this.vanishTick = tagcompound.getInteger("vanishTick");
      if (tagcompound.getBoolean("villager")) {
         this.setVillager();
      }

   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   protected String getLivingSound() {
      return "MutantCreatures:mutantzombie.living";
   }

   protected String getHurtSound() {
      return "MutantCreatures:mutantzombie.snarl";
   }

   protected String getDeathSound() {
      return "MutantCreatures:mutantzombie.snarl";
   }

   public void sendAttackPacket(int id) {
      if (!MutantCreatures.isEffectiveClient()) {
         this.currentAttackID = id;
         MutantCreatures.wrapper.sendToAll(new PacketZombieAttack(id, this));
      }
   }
}
