package thehippomaster.MutantCreatures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class MCExplosion extends Explosion {
   public boolean lessenDamage = true;
   public boolean useEntity = true;
   public boolean destroyBlocks = true;
   public boolean lessenDrop = true;
   private int size = 16;
   private Random explosionRNG = new Random();
   private World worldObj;
   private Map<EntityPlayer, Vec3> playerKnockbackMap = new HashMap<EntityPlayer, Vec3>();
   private static final String __OBFID = "CL_00000134";

   public MCExplosion(World par1World, Entity par2Entity, double par3, double par5, double par7, float par9) {
      super((World)null, (Entity)null, (double)0.0F, (double)0.0F, (double)0.0F, 0.0F);
      this.worldObj = par1World;
      this.exploder = par2Entity;
      this.explosionSize = par9;
      this.explosionX = par3;
      this.explosionY = par5;
      this.explosionZ = par7;
   }

   public void doExplosionA() {
      float f = this.explosionSize;
      HashSet<ChunkPosition> hashset = new HashSet<ChunkPosition>();

      for(int i = 0; i < this.size; ++i) {
         for(int j = 0; j < this.size; ++j) {
            for(int k = 0; k < this.size; ++k) {
               if (i == 0 || i == this.size - 1 || j == 0 || j == this.size - 1 || k == 0 || k == this.size - 1) {
                  double d0 = (double)((float)i / ((float)this.size - 1.0F) * 2.0F - 1.0F);
                  double d1 = (double)((float)j / ((float)this.size - 1.0F) * 2.0F - 1.0F);
                  double d2 = (double)((float)k / ((float)this.size - 1.0F) * 2.0F - 1.0F);
                  double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                  d0 /= d3;
                  d1 /= d3;
                  d2 /= d3;
                  float f1 = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
                  double d5 = this.explosionX;
                  double d6 = this.explosionY;
                  double d7 = this.explosionZ;

                  for(float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F) {
                     int j1 = MathHelper.floor_double(d5);
                     int k1 = MathHelper.floor_double(d6);
                     int l1 = MathHelper.floor_double(d7);
                     Block block = this.worldObj.getBlock(j1, k1, l1);
                     if (block.getMaterial() != Material.air) {
                        float f3 = this.exploder != null ? this.exploder.func_145772_a(this, this.worldObj, j1, k1, l1, block) : block.getExplosionResistance(this.exploder, this.worldObj, j1, k1, l1, this.explosionX, this.explosionY, this.explosionZ);
                        f1 -= (f3 + 0.3F) * f2;
                     }

                     if (f1 > 0.0F && (this.exploder == null || this.exploder.func_145774_a(this, this.worldObj, j1, k1, l1, block, f1)) && this.destroyBlocks) {
                        hashset.add(new ChunkPosition(j1, k1, l1));
                     }

                     d5 += d0 * (double)f2;
                     d6 += d1 * (double)f2;
                     d7 += d2 * (double)f2;
                  }
               }
            }
         }
      }

      this.affectedBlockPositions.addAll(hashset);
      this.explosionSize *= 2.0F;
      int var31 = MathHelper.floor_double(this.explosionX - (double)this.explosionSize - (double)1.0F);
      int j = MathHelper.floor_double(this.explosionX + (double)this.explosionSize + (double)1.0F);
      int k = MathHelper.floor_double(this.explosionY - (double)this.explosionSize - (double)1.0F);
      int i2 = MathHelper.floor_double(this.explosionY + (double)this.explosionSize + (double)1.0F);
      int l = MathHelper.floor_double(this.explosionZ - (double)this.explosionSize - (double)1.0F);
      int j2 = MathHelper.floor_double(this.explosionZ + (double)this.explosionSize + (double)1.0F);
      List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, AxisAlignedBB.getBoundingBox((double)var31, (double)k, (double)l, (double)j, (double)i2, (double)j2));
      Vec3 vec3 = Vec3.createVectorHelper(this.explosionX, this.explosionY, this.explosionZ);

      for(int i1 = 0; i1 < list.size(); ++i1) {
         Entity entity = (Entity)list.get(i1);
         double d4 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double)this.explosionSize;
         if (d4 <= (double)1.0F) {
            double d5 = entity.posX - this.explosionX;
            double d6 = entity.posY + (double)entity.getEyeHeight() - this.explosionY;
            double d7 = entity.posZ - this.explosionZ;
            double d10 = (double)MathHelper.sqrt_double(d5 * d5 + d6 * d6 + d7 * d7);
            if (d10 != (double)0.0F) {
               d5 /= d10;
               d6 /= d10;
               d7 /= d10;
               double d9 = (double)this.worldObj.getBlockDensity(vec3, entity.boundingBox);
               double d11 = ((double)1.0F - d4) * d9;
               DamageSource source = DamageSource.setExplosionSource(this);
               if (this.useEntity && this.exploder != null && this.exploder instanceof EntityLivingBase) {
                  source = DamageSource.causeMobDamage((EntityLivingBase)this.exploder);
               }

               float power = 8.0F;
               if (this.lessenDamage) {
                  power = 6.0F;
               }

               entity.attackEntityFrom(source, (float)((int)((d11 * d11 + d11) / (double)2.0F * (double)power * (double)this.explosionSize + (double)1.0F)));
               double d8 = EnchantmentProtection.func_92092_a(entity, d11);
               entity.motionX += d5 * d8;
               entity.motionY += d6 * d8;
               entity.motionZ += d7 * d8;
               if (entity instanceof EntityPlayer) {
                  this.playerKnockbackMap.put((EntityPlayer)entity, Vec3.createVectorHelper(d5 * d11, d6 * d11, d7 * d11));
               }
            }
         }
      }

      this.explosionSize = f;
   }

   public void doExplosionB(boolean par1) {
      this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
      if (this.explosionSize >= 2.0F && this.isSmoking) {
         this.worldObj.spawnParticle("hugeexplosion", this.explosionX, this.explosionY, this.explosionZ, (double)1.0F, (double)0.0F, (double)0.0F);
      } else {
         this.worldObj.spawnParticle("largeexplode", this.explosionX, this.explosionY, this.explosionZ, (double)1.0F, (double)0.0F, (double)0.0F);
      }

      if (this.isSmoking) {
         for(Object posObj : this.affectedBlockPositions) {
            ChunkPosition chunkposition = (ChunkPosition)posObj;
            int i = chunkposition.chunkPosX;
            int j = chunkposition.chunkPosY;
            int k = chunkposition.chunkPosZ;
            Block block = this.worldObj.getBlock(i, j, k);
            if (par1) {
               double d0 = (double)((float)i + this.worldObj.rand.nextFloat());
               double d1 = (double)((float)j + this.worldObj.rand.nextFloat());
               double d2 = (double)((float)k + this.worldObj.rand.nextFloat());
               double d3 = d0 - this.explosionX;
               double d4 = d1 - this.explosionY;
               double d5 = d2 - this.explosionZ;
               double d6 = (double)MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
               d3 /= d6;
               d4 /= d6;
               d5 /= d6;
               double d7 = (double)0.5F / (d6 / (double)this.explosionSize + 0.1);
               d7 *= (double)(this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
               d3 *= d7;
               d4 *= d7;
               d5 *= d7;
               this.worldObj.spawnParticle("explode", (d0 + this.explosionX * (double)1.0F) / (double)2.0F, (d1 + this.explosionY * (double)1.0F) / (double)2.0F, (d2 + this.explosionZ * (double)1.0F) / (double)2.0F, d3, d4, d5);
               this.worldObj.spawnParticle("smoke", d0, d1, d2, d3, d4, d5);
            }

            if (block.getMaterial() != Material.air) {
               if (block.canDropFromExplosion(this) && (!this.lessenDrop || this.explosionRNG.nextFloat() < 0.2F)) {
                  block.dropBlockAsItemWithChance(this.worldObj, i, j, k, this.worldObj.getBlockMetadata(i, j, k), 1.0F / this.explosionSize, 0);
               }

               block.onBlockExploded(this.worldObj, i, j, k, this);
            }
         }
      }

      if (this.isFlaming) {
         for(Object posObj : this.affectedBlockPositions) {
            ChunkPosition chunkposition = (ChunkPosition)posObj;
            int i = chunkposition.chunkPosX;
            int j = chunkposition.chunkPosY;
            int k = chunkposition.chunkPosZ;
            Block block = this.worldObj.getBlock(i, j, k);
            Block block1 = this.worldObj.getBlock(i, j - 1, k);
            if (block.getMaterial() == Material.air && block1.getMaterial().isReplaceable() && this.explosionRNG.nextInt(3) == 0) {
               this.worldObj.setBlock(i, j, k, Blocks.fire);
            }
         }
      }

   }

   public void explode() {
      this.doExplosionA();
      this.doExplosionB(false);
      if (this.worldObj instanceof WorldServer) {
         for(Object playerObj : this.worldObj.playerEntities) {
            EntityPlayer player = (EntityPlayer)playerObj;
            if (player.getDistanceSq(this.explosionX, this.explosionY, this.explosionZ) < (double)4096.0F) {
               ((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S27PacketExplosion(this.explosionX, this.explosionY, this.explosionZ, this.explosionSize, this.affectedBlockPositions, (Vec3)this.getPlayerKnockbackMap().get(player)));
            }
         }
      }

   }

   public Map<EntityPlayer, Vec3> getPlayerKnockbackMap() {
      return this.playerKnockbackMap;
   }

   public EntityLivingBase getExplosivePlacedBy() {
      return this.exploder == null ? null : (this.exploder instanceof EntityTNTPrimed ? ((EntityTNTPrimed)this.exploder).getTntPlacedBy() : (this.exploder instanceof EntityLivingBase ? (EntityLivingBase)this.exploder : null));
   }
}
