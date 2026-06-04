package thehippomaster.MutantCreatures;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.ai.MCAIAttackOnCollide;
import thehippomaster.MutantCreatures.ai.MCAIEnderAttackPlayer;
import thehippomaster.MutantCreatures.ai.MCAIEnderClone;
import thehippomaster.MutantCreatures.ai.MCAIEnderForcedLook;
import thehippomaster.MutantCreatures.ai.MCAIEnderMelee;
import thehippomaster.MutantCreatures.ai.MCAIEnderScream;
import thehippomaster.MutantCreatures.ai.MCAIEnderTeleSmash;
import thehippomaster.MutantCreatures.ai.MCAIEnderTeleport;
import thehippomaster.MutantCreatures.ai.MCAIEnderThrowBlock;
import thehippomaster.MutantCreatures.client.FXEnder;
import thehippomaster.MutantCreatures.packet.PacketEnderAttack;
import thehippomaster.MutantCreatures.packet.PacketEnderBlock;
import thehippomaster.MutantCreatures.packet.PacketEnderTeleport;

public class MutantEnderman extends EntityMob {
   public int currentAttackID;
   public int animTick;
   public int hasTargetTick;
   public int preTargetTick;
   public int hasTarget;
   public int teleX;
   public int teleY;
   public int teleZ;
   public int screamDelayTick;
   public int deathTick;
   public int[] heldBlock;
   public int[] heldBlockData;
   public int[] heldBlockTick;
   public boolean triggerThrowBlock;
   protected int blockFrenzy;
   protected IAttributeInstance moveSpeed;
   protected List screamEntities;
   protected List deathEntities;
   protected MCAIAttackOnCollide aiAttackPlayer;
   protected MCAIAttackOnCollide aiAttack;
   protected MCAIEnderClone aiClone;
   private int dirty;
   private long preTargetA;
   private long preTargetB;
   private static ArrayList<Integer> carriableBlocks = new ArrayList();

   public MutantEnderman(World world) {
      super(world);
      this.experienceValue = 35;
      this.hasTargetTick = this.preTargetTick = 0;
      this.hasTarget = 0;
      this.currentAttackID = 0;
      this.animTick = 0;
      this.blockFrenzy = 0;
      this.screamDelayTick = 0;
      this.deathTick = 0;
      this.dirty = -1;
      this.preTargetA = 0L;
      this.preTargetB = 0L;
      this.stepHeight = 1.4F;
      this.ignoreFrustumCheck = true;
      this.triggerThrowBlock = false;
      this.heldBlock = new int[5];
      this.heldBlockData = new int[5];
      this.heldBlockTick = new int[5];
      this.screamEntities = null;
      this.deathEntities = null;
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new MCAIEnderMelee(this));
      this.tasks.addTask(1, new MCAIEnderThrowBlock(this));
      this.tasks.addTask(1, new MCAIEnderForcedLook(this));
      this.tasks.addTask(1, new MCAIEnderTeleport(this));
      this.tasks.addTask(1, new MCAIEnderScream(this));
      this.tasks.addTask(1, this.aiClone = new MCAIEnderClone(this));
      this.tasks.addTask(1, new MCAIEnderTeleSmash(this));
      this.tasks.addTask(2, this.aiAttackPlayer = new MCAIAttackOnCollide(this, EntityPlayer.class, 1.2F, false));
      this.tasks.addTask(3, this.aiAttack = new MCAIAttackOnCollide(this, 1.2F, true));
      this.tasks.addTask(4, new EntityAIWander(this, (double)1.0F));
      this.tasks.addTask(5, new EntityAILookIdle(this));
      this.targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(1, new MCAIEnderAttackPlayer(this, 16.0F, true));
      this.setSize(1.2F, 4.8F);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)200.0F);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue((double)96.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3);
      this.moveSpeed = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue((double)6.0F);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte)0);
      this.dataWatcher.addObject(17, (byte)0);
      this.dataWatcher.addObject(18, (byte)0);
   }

   public void setMeleeArm(int id) {
      this.dataWatcher.updateObject(17, (byte)id);
   }

   public void setThrownBlock(int index) {
      this.dataWatcher.updateObject(18, (byte)index);
   }

   public boolean getHasTarget() {
      return this.dataWatcher.getWatchableObjectByte(16) == 1;
   }

   public int getMeleeArm() {
      return this.dataWatcher.getWatchableObjectByte(17);
   }

   public int getThrownBlock() {
      return this.dataWatcher.getWatchableObjectByte(18);
   }

   protected boolean isAIEnabled() {
      return true;
   }

   public float getEyeHeight() {
      return this.height;
   }

   public int getMaxSpawnedInChunk() {
      return 1;
   }

   public void setAttackTarget(EntityLivingBase living) {
      super.setAttackTarget(living);
      this.dataWatcher.updateObject(16, (byte)(living == null ? 0 : 1));
   }

   protected void updateTargetTick() {
      this.preTargetTick = this.hasTargetTick;
      if (this.getHasTarget()) {
         this.hasTarget = 20;
      }

      boolean emptyHanded = true;

      for(int i = 1; i < this.heldBlock.length; ++i) {
         if (this.heldBlock[i] > 0) {
            emptyHanded = false;
         }

         if (this.hasTarget > 0) {
            if (this.heldBlock[i] > 0) {
               this.heldBlockTick[i] = Math.min(10, this.heldBlockTick[i] + 1);
            }
         } else {
            this.heldBlockTick[i] = Math.max(0, this.heldBlockTick[i] - 1);
         }
      }

      if (this.hasTarget > 0) {
         this.hasTargetTick = Math.min(10, this.hasTargetTick + 1);
      } else if (emptyHanded) {
         this.hasTargetTick = Math.max(0, this.hasTargetTick - 1);
      } else if (!this.worldObj.isRemote) {
         for(int i = 1; i < this.heldBlock.length; ++i) {
            if (this.heldBlock[i] != 0 && this.heldBlockTick[i] == 0) {
               int x = MathHelper.floor_double(this.posX - (double)1.5F + this.rand.nextDouble() * (double)4.0F);
               int y = MathHelper.floor_double(this.posY - (double)0.5F + this.rand.nextDouble() * (double)2.5F);
               int z = MathHelper.floor_double(this.posZ - (double)1.5F + this.rand.nextDouble() * (double)4.0F);
               Block block = this.worldObj.getBlock(x, y, z);
               Block block1 = this.worldObj.getBlock(x, y - 1, z);
               if (block == Blocks.air && block1 != Blocks.air && block1.renderAsNormalBlock()) {
                  this.worldObj.setBlock(x, y, z, Block.getBlockById(this.heldBlock[i]), this.heldBlockData[i], 3);
                  this.sendHoldBlock(i, 0, 0);
               } else if (this.rand.nextInt(50) == 0) {
                  this.sendHoldBlock(i, 0, 0);
               }
            }
         }
      }

      this.hasTarget = Math.max(0, this.hasTarget - 1);
   }

   protected void updateScreamEntities() {
      this.screamDelayTick = Math.max(0, this.screamDelayTick - 1);
      if (this.currentAttackID == 5 && this.animTick >= 40 && this.animTick <= 160) {
         if (this.animTick == 160) {
            this.screamEntities = null;
         } else {
            if (this.screamEntities == null) {
               this.screamEntities = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)20.0F, (double)12.0F, (double)20.0F));
               List list = new ArrayList();
               list.addAll(this.screamEntities);
               this.screamEntities = list;
            }

            for(int i = 0; i < this.screamEntities.size(); ++i) {
               Entity entity = (Entity)this.screamEntities.get(i);
               if (this.getDistanceSqToEntity(entity) > (double)400.0F) {
                  this.screamEntities.remove(i);
                  --i;
               } else {
                  entity.rotationPitch += (this.rand.nextFloat() - 0.3F) * 6.0F;
               }
            }

         }
      }
   }

   public void onUpdate() {
      this.isJumping = false;
      super.onUpdate();
      if (this.getAttackTarget() != null && !this.getAttackTarget().isEntityAlive()) {
         this.setAttackTarget((EntityLivingBase)null);
      }

      if (this.currentAttackID != 0) {
         ++this.animTick;
      }

      this.updateTargetTick();
      this.updateScreamEntities();
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      double h = this.currentAttackID != 10 ? (double)this.height : (double)(this.height + 1.0F);
      double w = this.currentAttackID != 10 ? (double)this.width : (double)(this.width * 1.5F);
      boolean targetBlind = this.getAttackTarget() != null && this.getAttackTarget().getActivePotionEffect(Potion.blindness) != null;
      if (!targetBlind) {
         for(int i = 0; i < 3; ++i) {
            double x = this.posX + (this.rand.nextDouble() - (double)0.5F) * w;
            double y = this.posY + this.rand.nextDouble() * h - (double)0.25F;
            double z = this.posZ + (this.rand.nextDouble() - (double)0.5F) * w;
            this.worldObj.spawnParticle("portal", x, y, z, (this.rand.nextDouble() - (double)0.5F) * (double)2.0F, -this.rand.nextDouble(), (this.rand.nextDouble() - (double)0.5F) * (double)2.0F);
         }
      }

   }

   private void updateDirtyHands() {
      if (this.dirty >= 0) {
         ++this.dirty;
      }

      if (this.dirty >= 8) {
         this.dirty = -1;

         for(int i = 1; i < this.heldBlock.length; ++i) {
            if (this.heldBlock[i] > 0) {
               this.sendHoldBlock(i, this.heldBlock[i], this.heldBlockData[i]);
            }
         }

         if (this.preTargetA != 0L && this.preTargetB != 0L) {
            List list = this.worldObj.loadedEntityList;

            for(int i = 0; i < list.size(); ++i) {
               Entity entity = (Entity)list.get(i);
               if (entity instanceof EntityLivingBase) {
                  EntityLivingBase living = (EntityLivingBase)entity;
                  if (living.getPersistentID() != null && living.getPersistentID().getLeastSignificantBits() == this.preTargetA && living.getPersistentID().getMostSignificantBits() == this.preTargetB) {
                     this.setRevengeTarget(living);
                     break;
                  }
               }
            }

            this.preTargetA = 0L;
            this.preTargetB = 0L;
         }
      }

   }

   protected void updateBlockFrenzy() {
      this.blockFrenzy = Math.max(0, this.blockFrenzy - 1);
      if (this.getAttackTarget() != null && this.currentAttackID == 0) {
         if (this.blockFrenzy == 0 && this.rand.nextInt(600) == 0) {
            this.blockFrenzy = 200 + this.rand.nextInt(80);
         }

         if (this.blockFrenzy > 0 && this.rand.nextInt(8) == 0) {
            int x = MathHelper.floor_double(this.posX - (double)2.5F + this.rand.nextDouble() * (double)5.0F);
            int y = MathHelper.floor_double(this.posY - (double)0.5F + this.rand.nextDouble() * (double)3.0F);
            int z = MathHelper.floor_double(this.posZ - (double)2.5F + this.rand.nextDouble() * (double)5.0F);
            int id = Block.getIdFromBlock(this.worldObj.getBlock(x, y, z));
            int index = this.getFavorableHand();
            if (index != -1 && carriableBlocks.contains(id)) {
               this.sendHoldBlock(index, id, this.worldObj.getBlockMetadata(x, y, z));
               if (this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
                  this.worldObj.setBlock(x, y, z, Blocks.air, 0, 3);
               }
            }
         }
      }

   }

   protected void updateTeleport() {
      Entity entity = this.getAttackTarget();
      this.teleportByChance(entity == null ? 1600 : 800, entity);
      if (entity != null) {
         double d = this.getDistanceSqToEntity(entity);
         if (d > (double)1024.0F) {
            this.teleportByChance(10, entity);
         }
      }

   }

   protected void updateClone() {
      if (this.currentAttackID == 6) {
         this.aiAttackPlayer.moveSpeed = 1.0F;
         this.aiAttack.moveSpeed = 1.0F;
      } else {
         this.aiAttackPlayer.moveSpeed = 1.2F;
         this.aiAttack.moveSpeed = 1.2F;
      }

   }

   public void updateAITick() {
      super.updateAITick();
      if (this.isWet() && this.ticksExisted % 100 == 0) {
         this.attackEntityFrom(DamageSource.drown, 1.0F);
      }

      this.updateDirtyHands();
      this.updateBlockFrenzy();
      this.updateTeleport();
      this.updateClone();
   }

   public int getAvailableHand() {
      ArrayList<Integer> list = new ArrayList();

      for(int i = 1; i < this.heldBlock.length; ++i) {
         if (this.heldBlock[i] == 0) {
            list.add(i);
         }
      }

      if (list.isEmpty()) {
         return -1;
      } else {
         return (Integer)list.get(this.rand.nextInt(list.size()));
      }
   }

   public int getFavorableHand() {
      ArrayList<Integer> outer = new ArrayList();
      ArrayList<Integer> inner = new ArrayList();

      for(int i = 1; i < this.heldBlock.length; ++i) {
         if (this.heldBlock[i] == 0) {
            if (i <= 2) {
               outer.add(i);
            } else {
               inner.add(i);
            }
         }
      }

      if (outer.isEmpty() && inner.isEmpty()) {
         return -1;
      } else if (!outer.isEmpty()) {
         return (Integer)outer.get(this.rand.nextInt(outer.size()));
      } else {
         return (Integer)inner.get(this.rand.nextInt(inner.size()));
      }
   }

   public boolean attackEntityAsMob(Entity entity) {
      if (!this.worldObj.isRemote && this.currentAttackID == 0) {
         int i = this.getAvailableHand();
         if (!this.teleportByChance(6, entity)) {
            if (i != -1) {
               boolean allHandsFree = this.heldBlock[1] == 0 && this.heldBlock[2] == 0;
               if (allHandsFree && this.rand.nextInt(10) == 0) {
                  this.sendAttackPacket(6);
               } else if (allHandsFree && this.rand.nextInt(7) == 0) {
                  this.sendAttackPacket(7);
               } else {
                  this.setMeleeArm(i);
                  this.sendAttackPacket(1);
               }
            } else {
               this.triggerThrowBlock = true;
            }
         }
      }

      if (this.currentAttackID == 6) {
         boolean flag = super.attackEntityAsMob(entity);
         if (!this.worldObj.isRemote && this.rand.nextInt(2) == 0) {
            double x = entity.posX + (double)((this.rand.nextFloat() - 0.5F) * 24.0F);
            double z = entity.posZ + (double)((this.rand.nextFloat() - 0.5F) * 24.0F);
            double y = entity.posY + (double)this.rand.nextInt(5) + (double)4.0F;
            MutantCreatures.teleportTo(this, x, y, z);
         }

         if (flag) {
            this.heal(2.0F);
         }

         return flag;
      } else {
         return true;
      }
   }

   public boolean attackEntityFrom(DamageSource source, float f) {
      Entity entity = source.getEntity();
      if (entity != null && entity instanceof EntityDragon) {
         return false;
      } else if (this.currentAttackID != 4 && this.currentAttackID != 5) {
         if (!this.worldObj.isRemote) {
            if (this.currentAttackID == 6) {
               this.aiClone.resetTask();
            }

            boolean betterDodge = entity == null;
            if (source.isProjectile()) {
               betterDodge = true;
            }

            if (this.teleportByChance(betterDodge ? 3 : 6, entity)) {
               if (entity != null && entity instanceof EntityLivingBase) {
                  this.setRevengeTarget((EntityLivingBase)entity);
               }

               return false;
            }

            boolean betterTeleport = false;
            if (source == DamageSource.drown) {
               betterTeleport = true;
            }

            this.teleportByChance(betterTeleport ? 3 : 5, entity);
         }

         return super.attackEntityFrom(source, f);
      } else {
         return false;
      }
   }

   public boolean teleportByChance(int chance, Entity entity) {
      if (this.currentAttackID != 0) {
         return false;
      } else {
         chance = Math.max(1, chance);
         if (this.rand.nextInt(chance) == 0) {
            return entity == null ? this.teleportRandomly() : this.teleportToEntity(entity);
         } else {
            return false;
         }
      }
   }

   public boolean teleportRandomly() {
      if (this.currentAttackID != 0) {
         return false;
      } else {
         double radius = (double)24.0F;
         double x = this.posX + (this.rand.nextDouble() - (double)0.5F) * (double)2.0F * radius;
         double y = this.posY + (double)this.rand.nextInt((int)radius * 2) - radius;
         double z = this.posZ + (this.rand.nextDouble() - (double)0.5F) * (double)2.0F * radius;
         return this.sendTeleportPacket(x, y, z);
      }
   }

   public boolean teleportToEntity(Entity entity) {
      if (this.currentAttackID != 0) {
         return false;
      } else {
         double d = this.getDistanceSqToEntity(entity);
         double x = (double)0.0F;
         double y = (double)0.0F;
         double z = (double)0.0F;
         double radius = (double)16.0F;
         if (d < (double)100.0F) {
            x = entity.posX + (this.rand.nextDouble() - (double)0.5F) * (double)2.0F * radius;
            y = entity.posY + this.rand.nextDouble() * radius;
            z = entity.posZ + (this.rand.nextDouble() - (double)0.5F) * (double)2.0F * radius;
         } else {
            Vec3 vec = Vec3.createVectorHelper(this.posX - entity.posX, this.boundingBox.minY + (double)this.height / (double)2.0F - entity.posY + (double)entity.getEyeHeight(), this.posZ - entity.posZ);
            vec = vec.normalize();
            x = this.posX + (this.rand.nextDouble() - (double)0.5F) * (double)8.0F - vec.xCoord * radius;
            y = this.posY + (double)this.rand.nextInt(8) - vec.yCoord * radius;
            z = this.posZ + (this.rand.nextDouble() - (double)0.5F) * (double)8.0F - vec.zCoord * radius;
         }

         return this.sendTeleportPacket(x, y, z);
      }
   }

   public void knockBack(Entity entity, float dmg, double d, double d1) {
   }

   public int maxDeathTick() {
      return 280;
   }

   protected void onDeathUpdate() {
      ++this.deathTick;
      this.motionX = (double)0.0F;
      this.motionY = Math.min(this.motionY, (double)0.0F);
      this.motionZ = (double)0.0F;
      if (this.currentAttackID != 10) {
         this.sendAttackPacket(10);
      }

      if (this.deathTick == 80) {
         this.playSound("MutantCreatures:mutantenderman.death", this.getSoundVolume(), this.getSoundPitch());
      }

      if (!this.worldObj.isRemote) {
         if (this.deathTick >= 60 && this.deathTick < 80 && this.deathEntities == null) {
            this.deathEntities = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)10.0F, (double)8.0F, (double)10.0F));
            List temp = new ArrayList();
            temp.addAll(this.deathEntities);
            this.deathEntities = temp;
         }

         if (this.deathTick >= 60 && this.rand.nextInt(3) != 0) {
            EndersoulFragment orb = new EndersoulFragment(this.worldObj);
            orb.setPosition(this.posX, this.posY + (double)this.getEyeHeight() - (double)1.0F, this.posZ);
            orb.motionX = (double)((this.rand.nextFloat() - 0.5F) * 1.5F);
            orb.motionY = (double)((this.rand.nextFloat() - 0.5F) * 1.5F);
            orb.motionZ = (double)((this.rand.nextFloat() - 0.5F) * 1.5F);
            this.worldObj.spawnEntityInWorld(orb);
         }

         if (this.deathTick >= 80 && this.deathTick < this.maxDeathTick() - 20 && this.deathEntities != null) {
            for(int i = 0; i < this.deathEntities.size(); ++i) {
               Entity entity = (Entity)this.deathEntities.get(i);
               if (!(entity instanceof EndersoulFragment) && !(entity instanceof EntityItem) && !(entity instanceof MutantEnderman)) {
                  if (entity.fallDistance > 4.5F) {
                     entity.fallDistance = 4.5F;
                  }

                  if (!(this.getDistanceSqToEntity(entity) <= (double)64.0F)) {
                     boolean protectedPlayer = EndersoulFragment.isProtectedPlayer(entity);
                     if (protectedPlayer) {
                        this.deathEntities.remove(i);
                        --i;
                     } else {
                        double x = this.posX - entity.posX;
                        double z = this.posZ - entity.posZ;
                        double d = Math.sqrt(x * x + z * z);
                        entity.motionX = (double)0.8F * x / d;
                        if (this.posY + (double)4.0F > entity.posY) {
                           entity.motionY = Math.max(entity.motionY, (double)0.4F);
                        }

                        entity.motionZ = (double)0.8F * z / d;
                        if (entity instanceof EntityPlayerMP) {
                           EntityPlayerMP player = (EntityPlayerMP)entity;
                           MutantCreatures.sendPacketToAll(player, new S12PacketEntityVelocity(player));
                        }
                     }
                  }
               }
            }
         }

         if (this.deathTick >= 100 && this.deathTick < 150 && this.deathTick % 6 == 0) {
            Item item = Items.ender_pearl;
            if (this.rand.nextBoolean()) {
               item = Items.ender_eye;
            }

            EntityItem itemEntity = new EntityItem(this.worldObj, this.posX, this.posY + (double)(this.getEyeHeight() * 0.8F), this.posZ, new ItemStack(item, 1, 0));
            this.worldObj.spawnEntityInWorld(itemEntity);
         }
      }

      if (this.deathTick >= this.maxDeathTick()) {
         if (!this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && !this.isChild()) {
            int i = this.getExperiencePoints(this.attackingPlayer);

            while(i > 0) {
               int k = EntityXPOrb.getXPSplit(i);
               i -= k;
               this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, k));
            }
         }

         this.setDead();
      }

   }

   public boolean getCanSpawnHere() {
      if (this.rand.nextInt(3) == 0) {
         return false;
      } else if (this.worldObj.provider.dimensionId == 1 && this.rand.nextInt(2600) != 0) {
         return false;
      } else {
         return super.getCanSpawnHere() && MutantCreatures.getRandomSpawnChance();
      }
   }

   public float getBrightness(float f) {
      float f1 = ((float)this.hurtTime - f) / (float)this.maxHurtTime;
      return super.getBrightness(f) * f1;
   }

   public void writeEntityToNBT(NBTTagCompound tagcompound) {
      super.writeEntityToNBT(tagcompound);

      for(int i = 1; i < this.heldBlock.length; ++i) {
         tagcompound.setInteger("heldBlockID_" + i, this.heldBlock[i]);
         tagcompound.setInteger("heldBlockData_" + i, this.heldBlockData[i]);
      }

      EntityLivingBase target = this.getAttackTarget();
      if (target != null) {
         tagcompound.setLong("targetA", target.getPersistentID().getLeastSignificantBits());
         tagcompound.setLong("targetB", target.getPersistentID().getMostSignificantBits());
      }

   }

   public void readEntityFromNBT(NBTTagCompound tagcompound) {
      super.readEntityFromNBT(tagcompound);

      for(int i = 1; i < this.heldBlock.length; ++i) {
         this.heldBlock[i] = tagcompound.getInteger("heldBlockID_" + i);
         this.heldBlockData[i] = tagcompound.getInteger("heldBlockData_" + i);
         this.dirty = 0;
      }

      if (tagcompound.hasKey("targetA") && tagcompound.hasKey("targetB")) {
         this.preTargetA = tagcompound.getLong("targetA");
         this.preTargetB = tagcompound.getLong("targetB");
      }

   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   public String getLivingSound() {
      return "MutantCreatures:mutantenderman.living";
   }

   public String getHurtSound() {
      return "MutantCreatures:mutantenderman.hit";
   }

   public String getDeathSound() {
      return null;
   }

   public int getTalkInterval() {
      return 200;
   }

   public void sendAttackPacket(int id) {
      if (!MutantCreatures.isEffectiveClient()) {
         this.currentAttackID = id;
         MutantCreatures.wrapper.sendToAll(new PacketEnderAttack(id, this));
      }
   }

   public void sendHoldBlock(int blockIndex, int blockId, int blockData) {
      if (!MutantCreatures.isEffectiveClient()) {
         this.heldBlock[blockIndex] = blockId;
         this.heldBlockData[blockIndex] = blockData;
         this.heldBlockTick[blockIndex] = 0;
         MutantCreatures.wrapper.sendToAll(new PacketEnderBlock(this, blockId, blockIndex, blockData));
      }
   }

   public boolean sendTeleportPacket(double targetX, double targetY, double targetZ) {
      if (!MutantCreatures.isEffectiveClient() && this.currentAttackID == 0) {
         this.currentAttackID = 4;
         double oldX = this.posX;
         double oldY = this.posY;
         double oldZ = this.posZ;
         this.teleX = MathHelper.floor_double(targetX);
         this.teleY = MathHelper.floor_double(targetY);
         this.teleZ = MathHelper.floor_double(targetZ);
         this.posX = (double)this.teleX + (double)0.5F;
         this.posY = (double)this.teleY;
         this.posZ = (double)this.teleZ + (double)0.5F;
         boolean success = false;
         if (this.worldObj.blockExists(this.teleX, this.teleY, this.teleZ)) {
            boolean temp = false;

            while(!temp && this.teleY > 0) {
               Block block = this.worldObj.getBlock(this.teleX, this.teleY - 1, this.teleZ);
               if (block != Blocks.air && block.getMaterial().blocksMovement()) {
                  temp = true;
               } else {
                  --this.posY;
                  --this.teleY;
               }
            }

            if (temp) {
               this.setPosition(this.posX, this.posY, this.posZ);
               if (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(this.boundingBox)) {
                  success = true;
               }
            }
         }

         this.setPosition(oldX, oldY, oldZ);
         if (!success) {
            this.currentAttackID = 0;
            return false;
         } else {
            MutantCreatures.wrapper.sendToAll(new PacketEnderTeleport(this, this.teleX, this.teleY, this.teleZ));
            return true;
         }
      } else {
         return false;
      }
   }

   @SideOnly(Side.CLIENT)
   public void handleTeleport(int x, int y, int z) {
      this.currentAttackID = 4;
      this.animTick = 0;
      this.teleX = x;
      this.teleY = y;
      this.teleZ = z;
      this.spawnBigParticles();
   }

   @SideOnly(Side.CLIENT)
   public void spawnBigParticles() {
      this.spawnBigParticles(256, 1.8F);
   }

   @SideOnly(Side.CLIENT)
   public void spawnBigParticles(int temp, float speed) {
      EffectRenderer renderer = FMLClientHandler.instance().getClient().effectRenderer;
      if (this.currentAttackID == 4) {
         temp *= 2;
      }

      for(int i = 0; i < temp; ++i) {
         float f = (this.rand.nextFloat() - 0.5F) * speed;
         float f1 = (this.rand.nextFloat() - 0.5F) * speed;
         float f2 = (this.rand.nextFloat() - 0.5F) * speed;
         boolean flag = i < temp / 2;
         if (this.currentAttackID != 4) {
            flag = true;
         }

         boolean death = this.currentAttackID != 10;
         double h = death ? (double)this.height : (double)(this.height + 1.0F);
         double w = death ? (double)this.width : (double)(this.width * 1.5F);
         double tempX = (flag ? this.posX : (double)this.teleX) + (this.rand.nextDouble() - (double)0.5F) * w;
         double tempY = (flag ? this.posY : (double)this.teleY) + (this.rand.nextDouble() - (double)0.5F) * h + (double)(death ? 1.5F : 0.5F);
         double tempZ = (flag ? this.posZ : (double)this.teleZ) + (this.rand.nextDouble() - (double)0.5F) * w;
         renderer.addEffect(new FXEnder(this.worldObj, tempX, tempY, tempZ, (double)f, (double)f1, (double)f2, true));
      }

   }

   static {
      carriableBlocks.add(Block.getIdFromBlock(Blocks.stone));
      carriableBlocks.add(Block.getIdFromBlock(Blocks.grass));
      carriableBlocks.add(Block.getIdFromBlock(Blocks.dirt));
      carriableBlocks.add(Block.getIdFromBlock(Blocks.sand));
      carriableBlocks.add(Block.getIdFromBlock(Blocks.gravel));
      carriableBlocks.add(Block.getIdFromBlock(Blocks.yellow_flower));
      carriableBlocks.add(Block.getIdFromBlock(Blocks.red_flower));
      carriableBlocks.add(Block.getIdFromBlock(Blocks.brown_mushroom));
      carriableBlocks.add(Block.getIdFromBlock(Blocks.red_mushroom));
      carriableBlocks.add(Block.getIdFromBlock(Blocks.tnt));
      carriableBlocks.add(Block.getIdFromBlock(Blocks.cactus));
      carriableBlocks.add(Block.getIdFromBlock(Blocks.clay));
      carriableBlocks.add(Block.getIdFromBlock(Blocks.pumpkin));
      carriableBlocks.add(Block.getIdFromBlock(Blocks.melon_block));
      carriableBlocks.add(Block.getIdFromBlock(Blocks.mycelium));
   }
}
