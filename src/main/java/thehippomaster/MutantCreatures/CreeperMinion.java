package thehippomaster.MutantCreatures;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.ai.MCAIAttackOwnerTarget;
import thehippomaster.MutantCreatures.ai.MCAICreeperSit;
import thehippomaster.MutantCreatures.ai.MCAIFollowOwner;

public class CreeperMinion extends EntityCreeper {
   private int timeSinceIgnited;
   private int lastActiveTime;
   public static CreeperMinion minionObj;

   public CreeperMinion(World world) {
      super(world);
      this.setSize(0.2F, 0.8F);
      this.tasks.taskEntries = new ArrayList();
      this.targetTasks.taskEntries = new ArrayList();
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, new MCAICreeperSit(this));
      this.tasks.addTask(3, new EntityAICreeperSwell(this));
      this.tasks.addTask(4, new MCAIFollowOwner(this, 1.0F, 4.0F, 20.0F));
      this.tasks.addTask(5, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, (double)0.25F, (double)0.3F));
      this.tasks.addTask(6, new EntityAIAttackOnCollide(this, (double)1.0F, false));
      this.tasks.addTask(7, new EntityAIWander(this, (double)0.8F));
      this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(2, new MCAIAttackOwnerTarget(this, false));
      this.targetTasks.addTask(3, new MCAIAttackOwnerTarget(this, true));
      this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)4.0F);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(27, "");
      this.dataWatcher.addObject(19, (byte)0);
      this.dataWatcher.addObject(20, (byte)0);
      this.dataWatcher.addObject(21, (byte)1);
      this.dataWatcher.addObject(22, (byte)0);
      this.dataWatcher.addObject(23, 8);
      this.dataWatcher.addObject(24, 20);
      this.dataWatcher.addObject(25, "Creeper Minion");
      this.dataWatcher.addObject(26, 4);
   }

   public void setPowered(boolean flag) {
      this.dataWatcher.updateObject(17, (byte)(flag ? 1 : 0));
   }

   public void setOwner(String s) {
      this.dataWatcher.updateObject(27, s);
   }

   public void setSitting(boolean flag) {
      this.dataWatcher.updateObject(19, (byte)(flag ? 1 : 0));
   }

   public void setContinuous(boolean flag) {
      this.dataWatcher.updateObject(20, (byte)(flag ? 1 : 0));
   }

   public void setShowName(boolean flag) {
      this.dataWatcher.updateObject(21, (byte)(flag ? 1 : 0));
   }

   public void setDestroyBlocks(boolean flag) {
      this.dataWatcher.updateObject(22, (byte)(flag ? 1 : 0));
   }

   public void setBlastRadius(float f) {
      this.dataWatcher.updateObject(24, (int)(f * 10.0F));
   }

   public void setMaxHealth(int i) {
      this.dataWatcher.updateObject(23, i);
   }

   public void setName(String s) {
      this.dataWatcher.updateObject(25, s);
   }

   public void setCurrentHealth(int i) {
      this.dataWatcher.updateObject(26, i);
   }

   public String getOwner() {
      return this.dataWatcher.getWatchableObjectString(27);
   }

   public boolean getTamed() {
      String s = this.getOwner();
      return s != null && !s.isEmpty();
   }

   public boolean getSitting() {
      return this.dataWatcher.getWatchableObjectByte(19) == 1;
   }

   public boolean getContinuous() {
      return this.dataWatcher.getWatchableObjectByte(20) == 1;
   }

   public boolean getShowName() {
      return this.dataWatcher.getWatchableObjectByte(21) == 1;
   }

   public boolean getDestroyBlocks() {
      return this.dataWatcher.getWatchableObjectByte(22) == 1;
   }

   public int getMaxDataHealth() {
      return this.dataWatcher.getWatchableObjectInt(23);
   }

   public float getBlastRadius() {
      return (float)this.dataWatcher.getWatchableObjectInt(24) / 10.0F;
   }

   public String getName() {
      return this.dataWatcher.getWatchableObjectString(25);
   }

   public int getCurrentHealth() {
      return this.dataWatcher.getWatchableObjectInt(26);
   }

   public boolean isChild() {
      return true;
   }

   protected boolean canDespawn() {
      return !this.getTamed();
   }

   public void onUpdate() {
      if (this.getAttackTarget() != null && !this.getAttackTarget().isEntityAlive()) {
         this.setAttackTarget((EntityLivingBase)null);
      }

      int maxDataHealth = this.getMaxDataHealth();
      if (this.getTamed() && this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() != (double)maxDataHealth) {
         this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)maxDataHealth);
         this.setHealth(this.getMaxHealth());
      }

      if (this.isEntityAlive()) {
         this.lastActiveTime = this.timeSinceIgnited;
         int var1 = this.getCreeperState();
         if (var1 > 0 && this.timeSinceIgnited == 0) {
            this.worldObj.playSoundAtEntity(this, "creeper.primed", 1.0F, 0.5F);
         }

         this.timeSinceIgnited += var1;
         if (this.timeSinceIgnited < 0) {
            this.timeSinceIgnited = 0;
         }

         if (this.timeSinceIgnited >= 30) {
            this.timeSinceIgnited = 0;
            EntityLiving exploder = null;
            if (this.getContinuous()) {
               exploder = this;
            }

            if (!this.worldObj.isRemote) {
               MCExplosion explosion = new MCExplosion(this.worldObj, exploder, this.posX, this.posY, this.posZ, this.getBlastRadius() + (this.getPowered() ? 2.0F : 0.0F));
               boolean grief = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
               explosion.destroyBlocks = this.getTamed() ? this.getDestroyBlocks() : grief;
               explosion.explode();
            }

            if (!this.getContinuous()) {
               this.setDead();
            }

            this.setCreeperState(-30);
         }
      }

      super.onUpdate();
   }

   protected void updateAITick() {
      super.updateAITick();
      EntityLivingBase target = this.getAttackTarget();
      if (this.getTamed() && target instanceof EntityPlayer && ((EntityPlayer)target).getCommandSenderName().equals(this.getOwner())) {
         this.setAttackTarget((EntityLivingBase)null);
         this.setCreeperState(-1);
         PathNavigate nav = this.getNavigator();
         PathEntity path = nav.getPathToEntityLiving(target);
         if (path != null && path.isSamePath(nav.getPath())) {
            nav.setPath((PathEntity)null, (double)0.0F);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public float setCreeperFlashTime(float tick) {
      return ((float)this.lastActiveTime + (float)(this.timeSinceIgnited - this.lastActiveTime) * tick) / 28.0F;
   }

   public void setHealth(float f) {
      super.setHealth(f);
      if (!this.worldObj.isRemote) {
         this.setCurrentHealth((int)this.getHealth());
      }

   }

   public void heal(float f) {
      super.heal(f);
      if (!this.worldObj.isRemote) {
         this.setCurrentHealth((int)this.getHealth());
      }

   }

   protected void damageEntity(DamageSource source, float f) {
      super.damageEntity(source, f);
      if (!this.worldObj.isRemote) {
         this.setCurrentHealth((int)this.getHealth());
      }

   }

   public boolean interact(EntityPlayer player) {
      ItemStack stack = player.inventory.getCurrentItem();
      if (player.getCommandSenderName().equals(this.getOwner())) {
         if (stack != null) {
            Item item = stack.getItem();
            boolean takeItem = false;
            if (item == MutantCreatures.creeperStats) {
               minionObj = this;
               if (!this.worldObj.isRemote) {
                  this.setCurrentHealth((int)this.getHealth());
               }

               player.openGui(MutantCreatures.instance, 0, this.worldObj, 0, 0, 0);
               return false;
            }

            if (!this.worldObj.isRemote) {
               if (item == Item.getItemFromBlock(Blocks.tnt)) {
                  if (!this.getContinuous()) {
                     this.setContinuous(true);
                  } else {
                     this.setBlastRadius(this.getBlastRadius() + 0.2F);
                  }

                  takeItem = true;
               }

               if (item == Items.gunpowder) {
                  int i = 4;
                  if (this.getHealth() >= this.getMaxHealth()) {
                     this.setMaxHealth(this.getMaxDataHealth() + 1);
                     i = 1;
                  }

                  this.heal((float)i);
                  takeItem = true;
               }

               if (takeItem) {
                  if (!player.capabilities.isCreativeMode) {
                     --stack.stackSize;
                  }

                  MCHandler.spawnParticlesAtEntity(0, this, 1);
                  return false;
               }
            }
         }

         if (!this.worldObj.isRemote) {
            this.setSitting(!this.getSitting());
         }
      }

      return super.interact(player);
   }

   public boolean attackEntityFrom(DamageSource source, float f) {
      float dmg = f;
      Entity entity = source.getEntity();
      if (entity != null && entity instanceof EntityIronGolem && this.getTamed()) {
         return false;
      } else {
         if (source.isExplosion()) {
            if (this.getTamed()) {
               return false;
            }

            if (f >= 2.0F) {
               dmg = 2.0F;
            }
         }

         return super.attackEntityFrom(source, dmg);
      }
   }

   public float getBrightness(float f) {
      float f1 = ((float)this.hurtTime - f) / (float)this.maxHurtTime;
      return super.getBrightness(f) * f1;
   }

   public void writeEntityToNBT(NBTTagCompound tagcompound) {
      super.writeEntityToNBT(tagcompound);
      tagcompound.setInteger("maxHealth", this.getMaxDataHealth());
      tagcompound.setInteger("currentHealth", this.getCurrentHealth());
      tagcompound.setFloat("blastRadius", this.getBlastRadius());
      tagcompound.setBoolean("isSitting", this.getSitting());
      tagcompound.setBoolean("isContinuous", this.getContinuous());
      tagcompound.setBoolean("showName", this.getShowName());
      tagcompound.setBoolean("destroyBlocks", this.getDestroyBlocks());
      tagcompound.setString("owner", this.getOwner());
      tagcompound.setString("name", this.getName());
   }

   public void readEntityFromNBT(NBTTagCompound tagcompound) {
      super.readEntityFromNBT(tagcompound);
      this.setMaxHealth(tagcompound.getInteger("maxHealth"));
      this.setCurrentHealth(tagcompound.getInteger("currentHealth"));
      this.setHealth((float)this.getCurrentHealth());
      this.setBlastRadius(tagcompound.getFloat("blastRadius"));
      this.setSitting(tagcompound.getBoolean("isSitting"));
      this.setContinuous(tagcompound.getBoolean("isContinuous"));
      this.setShowName(tagcompound.getBoolean("showName"));
      this.setDestroyBlocks(tagcompound.getBoolean("destroyBlocks"));
      this.setOwner(tagcompound.getString("owner"));
      this.setName(tagcompound.getString("name"));
   }

   protected String getLivingSound() {
      return this.rand.nextBoolean() ? null : "mob.creeper.say";
   }
}
