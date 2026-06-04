package thehippomaster.MutantCreatures;

import cpw.mods.fml.relauncher.ReflectionHelper;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.packet.PacketEHandPos;

public class EnderBlock extends EntityThrowable {
   private static final double grabbedDistance = 1.6;

   public EnderBlock(World world) {
      super(world);
      this.setSize(1.0F, 1.0F);
   }

   public EnderBlock(World world, MutantEnderman enderman, int id) {
      super(world, enderman);
      this.setBlockID(enderman.heldBlock[id]);
      this.setBlockData(enderman.heldBlockData[id]);
      boolean outer = id <= 2;
      boolean right = (id & 1) == 1;
      EntityLivingBase living = enderman.getAttackTarget();
      Vec3 forward = MutantCreatures.getDirVector(this.rotationYaw, outer ? 2.7F : 1.4F);
      Vec3 strafe = MutantCreatures.getDirVector(this.rotationYaw + (right ? 90.0F : -90.0F), outer ? 2.2F : 2.0F);
      this.posX += forward.xCoord + strafe.xCoord;
      this.posY += (double)((outer ? 2.8F : 1.1F) - enderman.getEyeHeight());
      this.posZ += forward.zCoord + strafe.zCoord;
      if (living != null) {
         this.setThrowableHeading(living.posX - this.posX, living.posY - this.posY, living.posZ - this.posZ, 1.4F, 1.0F);
      }

      this.setSize(1.0F, 1.0F);
   }

   public EnderBlock(World world, EntityPlayer player, Block block, int data) {
      super(world, player);
      this.setPosition(this.posX, this.posY, this.posZ);
      this.setBlockID(Block.getIdFromBlock(block));
      this.setBlockData(data);
      this.setGrabbed(true);
      this.setThrowerID(player);
      this.setSize(1.0F, 1.0F);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, 0);
      this.dataWatcher.addObject(17, (byte)0);
      this.dataWatcher.addObject(18, (byte)0);
      this.dataWatcher.addObject(19, 0);
   }

   private void setBlockID(int id) {
      this.dataWatcher.updateObject(16, id);
   }

   private void setBlockData(int data) {
      this.dataWatcher.updateObject(17, (byte)data);
   }

   public void setGrabbed(boolean flag) {
      this.dataWatcher.updateObject(18, (byte)(flag ? 1 : 0));
   }

   private void setThrowerID(EntityLivingBase living) {
      this.dataWatcher.updateObject(19, living.getEntityId());
   }

   public int getBlockID() {
      return this.dataWatcher.getWatchableObjectInt(16);
   }

   public int getBlockData() {
      return this.dataWatcher.getWatchableObjectByte(17);
   }

   public boolean getGrabbed() {
      return this.dataWatcher.getWatchableObjectByte(18) == 1;
   }

   public EntityLivingBase getThrowerID() {
      int id = this.dataWatcher.getWatchableObjectInt(19);
      return (EntityLivingBase)this.worldObj.getEntityByID(id);
   }

   protected float getGravityVelocity() {
      if (this.getGrabbed()) {
         return 0.0F;
      } else {
         return this.getThrower() != null && this.getThrower() instanceof EntityPlayer ? 0.04F : 0.01F;
      }
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   public boolean canBeCollidedWith() {
      return this.getGrabbed() && !this.isDead;
   }

   public boolean canBePushed() {
      return this.getGrabbed() && !this.isDead;
   }

   public void onUpdate() {
      super.onUpdate();
      EntityLivingBase thrower = this.getThrower();
      if (this.getGrabbed()) {
         if (thrower == null) {
            thrower = this.getThrowerID();
            if (thrower != null) {
               ReflectionHelper.setPrivateValue(EntityThrowable.class, this, thrower, MutantCreatures.fThrower);
            }
         }

         if (thrower == null) {
            this.setGrabbed(false);
         } else {
            Vec3 vec = thrower.getLookVec();
            double x = thrower.posX + vec.xCoord * 1.6 - this.posX;
            double y = thrower.posY + (double)thrower.getEyeHeight() + vec.yCoord * 1.6 - this.posY;
            double z = thrower.posZ + vec.zCoord * 1.6 - this.posZ;
            float offset = 0.6F;
            this.motionX = x * (double)offset;
            this.motionY = y * (double)offset;
            this.motionZ = z * (double)offset;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            if (!this.worldObj.isRemote) {
               EntityPlayer player = (EntityPlayer)thrower;
               ItemStack stack = player.getCurrentEquippedItem();
               if (stack == null || stack.getItem() != MutantCreatures.endersoulHand) {
                  this.setGrabbed(false);
               }
            } else if (thrower == Minecraft.getMinecraft().thePlayer && this.ticksExisted % 20 == 0) {
               this.sendPositionPacket();
            }
         }
      }

   }

   public Vec3 getLookVec(EntityLiving living) {
      float PI = (float)Math.PI;
      float f1 = MathHelper.cos(-living.rotationYaw * ((float)Math.PI / 180F) - PI);
      float f2 = MathHelper.sin(-living.rotationYaw * ((float)Math.PI / 180F) - PI);
      float f3 = -MathHelper.cos(-living.rotationPitch * ((float)Math.PI / 180F));
      float f4 = MathHelper.sin(-living.rotationPitch * ((float)Math.PI / 180F));
      return Vec3.createVectorHelper((double)(f2 * f3), (double)f4, (double)(f1 * f3));
   }

   public boolean interactFirst(EntityPlayer player) {
      if (!this.worldObj.isRemote && this.getGrabbed() && this.getThrower() == player) {
         if (player.isSneaking()) {
            return super.interactFirst(player);
         }

         this.setGrabbed(false);
         this.moveTowardsHeading(player);
         ItemStack stack = player.getCurrentEquippedItem();
         if (stack != null && stack.getItem() == MutantCreatures.endersoulHand) {
            stack.damageItem(1, player);
         }

         MutantCreatures.sendPacketToAll(player, new S0BPacketAnimation(player, 0));
      }

      return super.interactFirst(player);
   }

   public void moveTowardsHeading(EntityLivingBase living) {
      this.rotationYaw = living.rotationYaw;
      this.rotationPitch = living.rotationPitch;
      float f = 0.4F;
      float PI = (float)Math.PI;
      this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * PI) * MathHelper.cos(this.rotationPitch / 180.0F * PI) * f);
      this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * PI) * f);
      this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * PI) * MathHelper.cos(this.rotationPitch / 180.0F * PI) * f);
      this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, 1.4F, 1.0F);
   }

   protected void onImpact(MovingObjectPosition mop) {
      if (!this.getGrabbed()) {
         EntityLivingBase thrower = this.getThrower();
         if (mop.typeOfHit == MovingObjectType.BLOCK) {
            Block block = this.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ);
            if (block.getCollisionBoundingBoxFromPool(this.worldObj, mop.blockX, mop.blockY, mop.blockZ) == null) {
               return;
            }
         }

         List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)2.0F, (double)2.0F, (double)2.0F));
         list.remove(thrower);

         for(int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity)list.get(i);
            if (!(this.getDistanceSqToEntity(entity) > (double)4.0F)) {
               double x = entity.posX - this.posX;
               double z = entity.posZ - this.posZ;
               double d = Math.sqrt(x * x + z * z);
               entity.motionX = x / d * (double)0.6F;
               entity.motionY = (double)0.2F;
               entity.motionZ = z / d * (double)0.6F;
               entity.attackEntityFrom(DamageSource.causeMobDamage(thrower), (float)(6 + this.rand.nextInt(3)));
            }
         }

         boolean hitEntity = mop.typeOfHit == MovingObjectType.ENTITY;
         if (hitEntity) {
            mop.entityHit.attackEntityFrom(DamageSource.causeMobDamage(thrower), 4.0F);
         }

         int x = MathHelper.floor_double(this.posX);
         int y = MathHelper.floor_double(this.posY);
         int z = MathHelper.floor_double(this.posZ);
         int id = this.getBlockID();
         int data = this.getBlockData();
         boolean grief = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
         if (thrower != null && thrower instanceof EntityPlayer) {
            grief = true;
         }

         boolean play = true;
         if (id >= 0) {
            Block block = Block.getBlockById(id);
            boolean fail = false;
            if (block == Blocks.air) {
               fail = true;
            } else if (block.canPlaceBlockAt(this.worldObj, x, y, z)) {
               if (!this.worldObj.isRemote && grief && !hitEntity) {
                  this.worldObj.setBlock(x, y, z, block, data, 3);
               }
            } else if (block.canPlaceBlockAt(this.worldObj, x, y + 1, z)) {
               if (!this.worldObj.isRemote && grief && !hitEntity) {
                  this.worldObj.setBlock(x, y + 1, z, block, data, 3);
               }

               this.worldObj.playAuxSFX(2001, x, y + 1, z, id + (data << 12));
               play = false;
            }

            if (!this.worldObj.isRemote && fail) {
               ItemStack stack = new ItemStack(block, 1, data);
               this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, stack));
            }
         }

         if (play) {
            this.worldObj.playAuxSFX(2001, x, y, z, id + (data << 12));
         }

         if (!hitEntity) {
            this.setDead();
         }

      }
   }

   public void writeEntityToNBT(NBTTagCompound tagcompound) {
      super.writeEntityToNBT(tagcompound);
      tagcompound.setShort("blockID", (short)this.getBlockID());
      tagcompound.setShort("blockData", (short)this.getBlockData());
   }

   public void readEntityFromNBT(NBTTagCompound tagcompound) {
      super.readEntityFromNBT(tagcompound);
      this.setBlockID(tagcompound.getShort("blockID"));
      this.setBlockData(tagcompound.getShort("blockData"));
   }

   private void sendPositionPacket() {
      if (MutantCreatures.isEffectiveClient()) {
         MutantCreatures.wrapper.sendToServer(new PacketEHandPos(this));
      }
   }
}
