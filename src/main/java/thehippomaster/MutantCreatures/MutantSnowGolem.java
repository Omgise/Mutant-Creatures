package thehippomaster.MutantCreatures;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.ai.MCAISGolemSnowPath;
import thehippomaster.MutantCreatures.ai.MCAISGolemSwimJump;
import thehippomaster.MutantCreatures.ai.MCAISGolemThrow;
import thehippomaster.MutantCreatures.packet.PacketSnowGolemAttack;
import thehippomaster.MutantCreatures.packet.PacketSnowGolemOwner;

public class MutantSnowGolem extends EntityGolem implements IRangedAttackMob {
   public int throwTick = 0;
   public boolean throwAttack;
   public EntityPlayer owner;
   protected MCAISGolemThrow aiThrow;
   public static MCSGTargetSelector mobSelector = new MCSGTargetSelector();

   public MutantSnowGolem(World world) {
      super(world);
      this.ignoreFrustumCheck = true;
      this.throwAttack = false;
      this.setSize(1.05F, 2.3F);
      this.tasks.addTask(0, new MCAISGolemSnowPath(this));
      this.tasks.addTask(1, new MCAISGolemSwimJump(this));
      this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, (double)1.1F));
      this.tasks.addTask(3, new EntityAIArrowAttack(this, (double)1.1F, 30, 12.0F));
      this.tasks.addTask(4, this.aiThrow = new MCAISGolemThrow(this));
      this.tasks.addTask(5, new EntityAIWander(this, (double)1.0F));
      this.tasks.addTask(6, new EntityAILookIdle(this));
      this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, true, false, mobSelector));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)80.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.26);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte)0);
   }

   public void setSwimJump(boolean flag) {
      this.dataWatcher.updateObject(16, (byte)(flag ? 1 : 0));
   }

   public boolean getSwimJump() {
      return this.dataWatcher.getWatchableObjectByte(16) == 1;
   }

   public boolean isAIEnabled() {
      return true;
   }

   public int getMaxSpawnedInChunk() {
      return 1;
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.owner != null) {
         int x = MathHelper.floor_double(this.owner.posX);
         int y = MathHelper.floor_double(this.owner.boundingBox.minY);
         int z = MathHelper.floor_double(this.owner.posZ);
         this.setHomeArea(x, y, z, this.getAttackTarget() == null ? 8 : 16);
      } else {
         this.detachHome();
      }

      if (MutantCreatures.isClient() && this.getSwimJump()) {
         MutantCreatures.spawnParticlesAtEntity(this, "blockcrack_80_0", 6);
         MutantCreatures.spawnParticlesAtEntity(this, "splash", 6);
      }

      if (this.throwAttack) {
         ++this.throwTick;
      }

      if (!this.worldObj.isRemote && this.getAttackTarget() == null) {
         if (this.throwAttack) {
            this.sendAttackPacket(0, false);
         }

         this.throwAttack = false;
         this.throwTick = 0;
      }

      EntityLivingBase target = this.getAttackTarget();
      if (target != null) {
         if (!target.isEntityAlive()) {
            this.setAttackTarget((EntityLivingBase)null);
         }

         if (target instanceof MutantSnowGolem || target instanceof EntityPlayer) {
            this.setAttackTarget((EntityLivingBase)null);
         }
      }

   }

   public boolean interact(EntityPlayer player) {
      if (!this.worldObj.isRemote) {
         this.setOwner(player.equals(this.owner) ? null : player);
      }

      return super.interact(player);
   }

   public void attackEntityWithRangedAttack(EntityLivingBase living, float f) {
      if (!this.throwAttack) {
         this.aiThrow.startAttack();
      }

   }

   public boolean attackEntityFrom(DamageSource source, float dmg) {
      Entity entity = source.getEntity();
      Entity sourceDamage = source.getSourceOfDamage();
      if (sourceDamage != null && sourceDamage instanceof EntitySnowball) {
         this.heal(2.0F);
         if (!this.worldObj.isRemote) {
            MCHandler.spawnHeartsAtEntity(this, 1);
            MCHandler.spawnSnowAtEntity(this, 10);
         }

         return true;
      } else {
         if (!this.worldObj.isRemote) {
            MCHandler.spawnSnowAtEntity(this, 30);
         }

         return super.attackEntityFrom(source, dmg);
      }
   }

   public void onDeath(DamageSource source) {
      super.onDeath(source);
      if (!this.worldObj.isRemote) {
         this.dropItem(Item.getItemFromBlock(Blocks.lit_pumpkin), 1);
         this.dropItem(Items.snowball, 32 + this.rand.nextInt(16));
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
   }

   public void readEntityFromNBT(NBTTagCompound tagcompound) {
      super.readEntityFromNBT(tagcompound);
   }

   public void sendAttackPacket(int id, boolean attack) {
      if (!MutantCreatures.isEffectiveClient()) {
         MutantCreatures.wrapper.sendToAll(new PacketSnowGolemAttack(id, this, attack));
      }
   }

   public void setOwner(EntityPlayer player) {
      if (!MutantCreatures.isEffectiveClient()) {
         this.owner = player;
         MutantCreatures.wrapper.sendToAll(new PacketSnowGolemOwner(this, player));
      }
   }

   public static class MCSGTargetSelector implements IEntitySelector {
      public boolean isEntityApplicable(Entity entity) {
         if (entity instanceof CreeperMinion) {
            CreeperMinion minion = (CreeperMinion)entity;
            if (minion.getTamed()) {
               return false;
            }
         }

         return entity instanceof IMob;
      }
   }
}
