package thehippomaster.MutantCreatures;

import cpw.mods.fml.relauncher.ReflectionHelper;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.ai.MCAIAttackOnCollide;
import thehippomaster.MutantCreatures.ai.MCAISpiderPigJump;
import thehippomaster.MutantCreatures.packet.PacketSpiderPigJump;

public class SpiderPig extends EntityCreature implements IAnimals, IEntityOwnable {
   public int lastJumpTick = 0;
   public int chargingTick = 0;
   public int exhaustAmount = 0;
   public boolean prevPlayerJumping = false;
   public boolean chargeExhausted = false;
   public MCAISpiderPigJump aiJump;
   public ArrayList<BlockCoord> webList;
   public MCSGTargetSelector mobSelector;

   public SpiderPig(World world) {
      super(world);
      this.ignoreFrustumCheck = true;
      this.webList = new ArrayList();
      this.mobSelector = new MCSGTargetSelector(this);
      this.setSize(1.4F, 1.2F);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, (new MCAIAttackOnCollide(this, EntityPlayer.class, 1.1F, false)).setMaxAttackTick(15));
      this.tasks.addTask(3, (new MCAIAttackOnCollide(this, 1.1F, true)).setMaxAttackTick(15));
      this.tasks.addTask(4, this.aiJump = new MCAISpiderPigJump(this, 8.0F));
      this.tasks.addTask(5, new EntityAIWander(this, (double)1.0F));
      this.tasks.addTask(6, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, CreeperMinion.class, 0, true, false, this.mobSelector));
      this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, true));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPig.class, 0, true, false, this.mobSelector));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntitySpider.class, 0, true, false, this.mobSelector));
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(18, "");
      this.dataWatcher.addObject(19, (byte)0);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue((double)4.0F);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue((double)48.0F);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)50.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue((double)0.25F);
   }

   protected boolean isAIEnabled() {
      return true;
   }

   public void setOwner(String name) {
      this.dataWatcher.updateObject(18, name);
   }

   public void setOwner(EntityPlayer player) {
      this.setOwner(player.getCommandSenderName());
   }

   public String getOwnerName() {
      return this.dataWatcher.getWatchableObjectString(18);
   }

   public String func_152113_b() {
      return this.getOwnerName();
   }

   public EntityLivingBase getOwner() {
      return this.getOwnerPlayer();
   }

   public EntityPlayer getOwnerPlayer() {
      return this.worldObj.getPlayerEntityByName(this.getOwnerName());
   }

   public boolean getTamed() {
      return !this.getOwnerName().equals("");
   }

   protected boolean canDespawn() {
      return !this.getTamed();
   }

   public Team getTeam() {
      if (this.getTamed()) {
         EntityLivingBase entitylivingbase = this.getOwnerPlayer();
         if (entitylivingbase != null) {
            return entitylivingbase.getTeam();
         }
      }

      return super.getTeam();
   }

   public boolean isOnSameTeam(EntityLivingBase living) {
      if (this.getTamed()) {
         EntityLivingBase owner = this.getOwnerPlayer();
         if (living == owner) {
            return true;
         }

         if (owner != null) {
            return owner.isOnSameTeam(living);
         }
      }

      return super.isOnSameTeam(living);
   }

   public void setBesideClimbableBlock(boolean flag) {
      byte b0 = this.dataWatcher.getWatchableObjectByte(19);
      if (flag) {
         b0 = (byte)(b0 | 1);
      } else {
         b0 = (byte)(b0 & -2);
      }

      this.dataWatcher.updateObject(19, b0);
   }

   public boolean isBesideClimbableBlock() {
      return (this.dataWatcher.getWatchableObjectByte(19) & 1) != 0;
   }

   public boolean isOnLadder() {
      return this.isBesideClimbableBlock();
   }

   public void setInWeb() {
   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.ARTHROPOD;
   }

   public boolean isPotionApplicable(PotionEffect effect) {
      return effect.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(effect);
   }

   protected void fall(float f) {
   }

   public void moveEntityWithHeading(float strafe, float forward) {
      if (this.riddenByEntity == null) {
         this.stepHeight = 0.5F;
         super.moveEntityWithHeading(strafe, forward);
      } else {
         this.stepHeight = 1.0F;
         this.prevRotationYaw = this.rotationYaw = this.rotationYawHead = this.riddenByEntity.rotationYaw;
         this.prevRotationPitch = this.rotationPitch = this.riddenByEntity.rotationPitch * 0.4F;
         this.setRotation(this.rotationYaw, this.rotationPitch);

         while(this.renderYawOffset > this.rotationYawHead + 180.0F) {
            this.renderYawOffset -= 360.0F;
         }

         while(this.renderYawOffset < this.rotationYawHead - 180.0F) {
            this.renderYawOffset += 360.0F;
         }

         this.renderYawOffset += (this.rotationYawHead - this.renderYawOffset) * 0.3F;
         EntityLivingBase rider = (EntityLivingBase)this.riddenByEntity;
         EntityPlayer player = null;
         if (rider instanceof EntityPlayer) {
            player = (EntityPlayer)rider;
         }

         float speed = (float)this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue();
         if (!this.worldObj.isRemote) {
            if (!this.getNavigator().noPath()) {
               this.getNavigator().clearPathEntity();
            }

            forward = rider.moveForward * 0.8F;
            strafe = rider.moveStrafing * 0.6F;
            this.setAIMoveSpeed(speed);
            super.moveEntityWithHeading(strafe, forward);
         } else {
            if (player != null) {
               boolean jumping = MutantCreatures.proxy.updateSpiderPigRider(this, player);
               if (!jumping && this.prevPlayerJumping) {
                  MutantCreatures.wrapper.sendToServer(new PacketSpiderPigJump(this));
               }

               this.prevPlayerJumping = jumping;
            }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d0 = this.posX - this.prevPosX;
            double d1 = this.posZ - this.prevPosZ;
            float f4 = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4.0F;
            if (f4 > 1.0F) {
               f4 = 1.0F;
            }

            this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
         }

      }
   }

   protected void updateWebList(boolean onlyCheckSize) {
      if (!onlyCheckSize) {
         for(int i = 0; i < this.webList.size(); ++i) {
            BlockCoord coord = (BlockCoord)this.webList.get(i);
            Block block = this.worldObj.getBlock(coord.x, coord.y, coord.z);
            if (block.getMaterial() != Material.web) {
               this.webList.remove(i);
               --i;
            } else {
               --coord.timeLeft;
            }
         }

         if (!this.webList.isEmpty()) {
            BlockCoord first = (BlockCoord)this.webList.get(0);
            if (first.timeLeft < 0) {
               this.webList.remove(0);
               this.worldObj.playAuxSFX(2001, first.x, first.y, first.z, Block.getIdFromBlock(Blocks.web));
               this.worldObj.setBlock(first.x, first.y, first.z, Blocks.air, 0, 3);
            }
         }
      }

      while(this.webList.size() > 12) {
         BlockCoord coord = (BlockCoord)this.webList.remove(0);
         this.worldObj.playAuxSFX(2001, coord.x, coord.y, coord.z, Block.getIdFromBlock(Blocks.web));
         this.worldObj.setBlock(coord.x, coord.y, coord.z, Blocks.air, 0, 3);
      }

   }

   protected void updateChargeState() {
      if (this.exhaustAmount >= 120) {
         this.chargeExhausted = true;
      }

      if (this.exhaustAmount <= 0) {
         this.chargeExhausted = false;
      }

      this.exhaustAmount = Math.max(0, this.exhaustAmount - 1);
      if (this.chargingTick > 0) {
         ArrayList<Entity> list = MutantCreatures.getCollidingEntities(this, this.worldObj, this.boundingBox);
         float damage = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();

         for(Entity entity : list) {
            if (entity != this.riddenByEntity) {
               entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage * 0.5F);
            }
         }
      }

      this.chargingTick = Math.max(0, this.chargingTick - 1);
   }

   public void onUpdate() {
      super.onUpdate();
      if (!this.worldObj.isRemote) {
         this.setBesideClimbableBlock(this.isCollidedHorizontally);
         this.lastJumpTick = Math.max(0, this.lastJumpTick - 1);
         if (this.aiJump.jumpTick > 10 && this.onGround) {
            this.aiJump.running = false;
         }

         this.updateWebList(false);
         this.updateChargeState();
         if (this.getTamed() && this.ticksExisted % 600 == 0) {
            this.heal(1.0F);
         }
      }

   }

   public boolean attackEntityAsMob(Entity entity) {
      super.attackEntityAsMob(entity);
      this.aiJump.running = false;
      boolean dealDamage = true;
      if (this.rand.nextInt(2) == 0) {
         double dx = entity.posX - entity.prevPosX;
         double dz = entity.posZ - entity.prevPosZ;
         int x = (int)(entity.posX + dx * (double)0.5F);
         int y = MathHelper.floor_double(this.boundingBox.minY);
         int z = (int)(entity.posZ + dz * (double)0.5F);
         Material material = this.worldObj.getBlock(x, y, z).getMaterial();
         if (!material.isSolid() && !material.isLiquid() && material != Material.web) {
            this.worldObj.setBlock(x, y, z, Blocks.web, 0, 3);
            this.webList.add(new BlockCoord(x, y, z));
            this.updateWebList(true);
            this.motionY = Math.max((double)0.25F, this.motionY);
            this.fallDistance = 0.0F;
         } else {
            dealDamage = true;
         }
      }

      if (dealDamage) {
         float damage = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
         boolean spiderType = entity instanceof EntitySpider || entity instanceof SpiderPig;
         if ((Boolean)ReflectionHelper.getPrivateValue(Entity.class, entity, MutantCreatures.fIsInWeb) && !spiderType) {
            damage += 4.0F;
         }

         entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
      }

      return false;
   }

   public void onKillEntity(EntityLivingBase living) {
      if (!this.worldObj.isRemote) {
         if (living instanceof CreeperMinion) {
            CreeperMinion minion = (CreeperMinion)living;
            if (minion.getTamed() && this.getHealth() <= 8.0F) {
               this.setOwner(minion.getOwner());
            }

            minion.setDead();
         }

         boolean killedPrey = living instanceof EntityPig || living instanceof EntitySpider;
         if (killedPrey && MutantCreatures.isSpiderPigEnabled()) {
            living.setDead();
            SpiderPig pig = new SpiderPig(this.worldObj);
            pig.setPositionAndRotation(living.posX, living.posY, living.posZ, living.rotationYaw, 0.0F);
            this.worldObj.spawnEntityInWorld(pig);
         }
      }

   }

   public boolean attackEntityFrom(DamageSource source, float dmg) {
      return source.getEntity() != null && source.getEntity() == this.riddenByEntity ? false : super.attackEntityFrom(source, dmg);
   }

   public void setDead() {
      super.setDead();
      if (!this.worldObj.isRemote && !this.webList.isEmpty()) {
         for(BlockCoord coord : this.webList) {
            Block block = this.worldObj.getBlock(coord.x, coord.y, coord.z);
            if (block.getMaterial() == Material.web) {
               this.worldObj.playAuxSFX(2001, coord.x, coord.y, coord.z, Block.getIdFromBlock(Blocks.web));
               this.worldObj.setBlock(coord.x, coord.y, coord.z, Blocks.air, 0, 3);
            }
         }
      }

   }

   public boolean interact(EntityPlayer player) {
      boolean correctPlayer = player.getCommandSenderName().equalsIgnoreCase(this.getOwnerName());
      if (!this.worldObj.isRemote && correctPlayer) {
         ItemStack stack = player.inventory.getCurrentItem();
         if (stack != null) {
            Item item = stack.getItem();
            boolean fullHealth = this.getHealth() >= this.getMaxHealth();
            if (item == Items.porkchop || item == Items.spider_eye) {
               if (!fullHealth) {
                  this.heal((float)(4 + this.rand.nextInt(5)));
                  if (!player.capabilities.isCreativeMode) {
                     --stack.stackSize;
                  }

                  MCHandler.spawnParticlesAtEntity(0, this, 1);
               }

               return false;
            }
         }

         if (this.riddenByEntity == null) {
            player.rotationYaw = this.rotationYawHead;
            player.mountEntity(this);
         }
      }

      return super.interact(player);
   }

   public boolean getCanSpawnHere() {
      return super.getCanSpawnHere() && MutantCreatures.getRandomSpawnChance();
   }

   public void writeEntityToNBT(NBTTagCompound tagcompound) {
      super.writeEntityToNBT(tagcompound);
      tagcompound.setString("owner", this.getOwnerName());
      if (!this.webList.isEmpty()) {
         NBTTagCompound tag = new NBTTagCompound();
         tag.setShort("length", (short)this.webList.size());

         for(int i = 0; i < this.webList.size(); ++i) {
            BlockCoord coord = (BlockCoord)this.webList.get(i);
            int[] coords = new int[]{coord.x, coord.y, coord.z};
            tag.setIntArray("coord" + i, coords);
            tag.setInteger("time" + i, coord.timeLeft);
         }

         tagcompound.setTag("webList", tag);
      }

   }

   public void readEntityFromNBT(NBTTagCompound tagcompound) {
      super.readEntityFromNBT(tagcompound);
      this.setOwner(tagcompound.getString("owner"));
      if (tagcompound.hasKey("webList")) {
         NBTTagCompound tag = (NBTTagCompound)tagcompound.getTag("webList");
         int length = tag.getShort("length");

         for(int i = 0; i < length; ++i) {
            int[] coords = tag.getIntArray("coord" + i);
            int time = tag.getInteger("time" + i);
            BlockCoord coord = new BlockCoord(coords, time);
            this.webList.add(coord);
         }
      }

   }

   protected String getLivingSound() {
      return "mob.pig.say";
   }

   protected String getHurtSound() {
      return "mob.pig.death";
   }

   protected String getDeathSound() {
      return "mob.zombiepig.zpighurt";
   }

   protected void playStepSound(int i, int j, int k, Block block) {
      this.playSound("mob.spider.step", 0.15F, 1.0F);
   }

   public static class BlockCoord {
      public int x;
      public int y;
      public int z;
      public int timeLeft;

      public BlockCoord(int x, int y, int z) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.timeLeft = 1200;
      }

      public BlockCoord(int[] coords, int time) {
         this.x = coords[0];
         this.y = coords[1];
         this.z = coords[2];
         this.timeLeft = time;
      }
   }

   public static class MCSGTargetSelector implements IEntitySelector {
      private SpiderPig spiderpig;

      public MCSGTargetSelector(SpiderPig pig) {
         this.spiderpig = pig;
      }

      public boolean isEntityApplicable(Entity entity) {
         if (this.spiderpig.getTamed()) {
            return false;
         } else {
            return entity instanceof CreeperMinion ? ((CreeperMinion)entity).getTamed() : true;
         }
      }
   }
}
