package thehippomaster.MutantCreatures;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EndersoulFragment extends Entity {
   public int explodeTick;
   public int tameTime;
   public float[][] stickRotations;
   public EntityPlayer owner;

   public EndersoulFragment(World world) {
      super(world);
      this.explodeTick = 20 + this.rand.nextInt(20);
      this.ignoreFrustumCheck = true;
      this.stickRotations = new float[8][3];

      for(int i = 0; i < this.stickRotations.length; ++i) {
         for(int j = 0; j < this.stickRotations[i].length; ++j) {
            this.stickRotations[i][j] = this.rand.nextFloat() * 2.0F * (float)Math.PI;
         }
      }

      this.owner = null;
      this.setSize(0.75F, 0.75F);
   }

   protected void entityInit() {
      this.dataWatcher.addObject(16, (byte)0);
   }

   public void tame() {
      this.dataWatcher.updateObject(16, (byte)1);
   }

   public boolean getTamed() {
      return this.dataWatcher.getWatchableObjectByte(16) == 1;
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   public boolean canBePushed() {
      return !this.isDead;
   }

   protected void move() {
      if (this.ridingEntity == null) {
         this.prevPosX = this.posX;
         this.prevPosY = this.posY;
         this.prevPosZ = this.posZ;
         if (this.owner == null && this.motionY > (double)-0.05F) {
            this.motionY = Math.max((double)-0.05F, this.motionY - (double)0.1F);
         }

         this.moveEntity(this.motionX, this.motionY, this.motionZ);
         this.motionX *= 0.9;
         this.motionY *= 0.9;
         this.motionZ *= 0.9;
      }
   }

   protected void explode() {
      if (!this.worldObj.isRemote) {
         if (this.explodeTick == 0) {
            MCHandler.spawnParticlesAtEntity(5, this, 0);
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)5.0F, (double)5.0F, (double)5.0F));

            for(int i = 0; i < list.size(); ++i) {
               Entity entity = (Entity)list.get(i);
               if (!(entity instanceof EndersoulFragment) && !(entity instanceof EntityItem) && !(entity instanceof MutantEnderman) && !(this.getDistanceSqToEntity(entity) > (double)25.0F)) {
                  boolean protectedPlayer = isProtectedPlayer(entity);
                  boolean hitChance = this.rand.nextInt(3) != 0;
                  if (protectedPlayer) {
                     hitChance = this.rand.nextInt(3) == 0;
                  }

                  if (hitChance) {
                     entity.attackEntityFrom(DamageSource.causeThrownDamage(this, (Entity)(this.owner == null ? this : this.owner)), 1.0F);
                  }

                  if (!protectedPlayer) {
                     double x = entity.posX - this.posX;
                     double z = entity.posZ - this.posZ;
                     double d = Math.sqrt(x * x + z * z);
                     entity.motionX = (double)0.8F * x / d;
                     entity.motionY = (double)(this.rand.nextFloat() * 0.6F - 0.1F);
                     entity.motionZ = (double)0.8F * z / d;
                     if (entity instanceof EntityPlayerMP) {
                        EntityPlayerMP player = (EntityPlayerMP)entity;
                        MutantCreatures.sendPacketToAll(player, new S12PacketEntityVelocity(player));
                     }
                  }
               }
            }
         } else if (this.explodeTick <= -1) {
            this.setDead();
         }

      }
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.owner != null && !this.owner.isEntityAlive()) {
         this.owner = null;
      }

      if (!this.worldObj.isRemote) {
         if (!this.getTamed()) {
            --this.explodeTick;
            if (this.explodeTick <= 0) {
               this.explode();
            }
         }

         if (this.owner != null) {
            float scale = 0.05F;
            this.motionX += (this.owner.posX - this.posX) * (double)scale;
            this.motionY += (this.owner.posY + (double)(this.owner.height / 3.0F) - this.posY) * (double)scale;
            this.motionZ += (this.owner.posZ - this.posZ) * (double)scale;
         }
      }

      this.move();
   }

   public boolean interactFirst(EntityPlayer player) {
      if (!this.worldObj.isRemote && !this.getTamed()) {
         this.tame();
      }

      this.owner = player;
      if (!this.worldObj.isRemote && this.getTamed() && this.owner == player && player.isSneaking()) {
         this.owner = null;
      }

      return super.interactFirst(player);
   }

   public boolean attackEntityFrom(DamageSource source, float f) {
      if (!this.worldObj.isRemote && this.explodeTick > 0) {
         this.explodeTick = 0;
      }

      return true;
   }

   protected void writeEntityToNBT(NBTTagCompound tagcompound) {
      tagcompound.setBoolean("tamed", this.getTamed());
   }

   protected void readEntityFromNBT(NBTTagCompound tagcompound) {
      if (tagcompound.getBoolean("tamed")) {
         this.tame();
      }

   }

   public static boolean isProtectedPlayer(Entity entity) {
      if (!(entity instanceof EntityPlayer)) {
         return false;
      } else {
         EntityPlayer player = (EntityPlayer)entity;
         ItemStack stack = player.inventory.armorInventory[3];
         return stack != null && stack.getItem() == Item.getItemFromBlock(Blocks.pumpkin);
      }
   }
}
