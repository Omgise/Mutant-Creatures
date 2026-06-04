package thehippomaster.MutantCreatures.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import thehippomaster.MutantCreatures.MCHandler;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.MutantEnderman;
import thehippomaster.MutantCreatures.packet.PacketEnderTPlayer;

public class MCAIEnderTeleSmash extends EntityAIBase {
   private MutantEnderman mutantEnderman;
   private EntityLivingBase attackTarget;

   public MCAIEnderTeleSmash(MutantEnderman enderman) {
      this.mutantEnderman = enderman;
      this.attackTarget = null;
      this.setMutexBits(7);
   }

   public boolean shouldExecute() {
      if (this.mutantEnderman.currentAttackID != 7) {
         return false;
      } else {
         return this.mutantEnderman.getAttackTarget() != null;
      }
   }

   public void startExecuting() {
      this.mutantEnderman.animTick = 0;
      this.attackTarget = this.mutantEnderman.getAttackTarget();
      this.attackTarget.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 20, 5));
      this.attackTarget.addPotionEffect(new PotionEffect(Potion.confusion.id, 160 + this.attackTarget.getRNG().nextInt(160), 0));
   }

   public boolean continueExecuting() {
      return this.mutantEnderman.currentAttackID == 7 && this.mutantEnderman.animTick < 30;
   }

   public void updateTask() {
      if (this.mutantEnderman.animTick < 20) {
         this.mutantEnderman.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
      }

      if (this.mutantEnderman.animTick == 18) {
         if (this.attackTarget instanceof EntityPlayer) {
            this.sendTeleportPlayer(this.attackTarget);
         } else {
            double x = this.attackTarget.posX + (double)((this.attackTarget.getRNG().nextFloat() - 0.5F) * 14.0F);
            double y = this.attackTarget.posY + (double)this.attackTarget.getRNG().nextFloat() + (double)7.0F;
            double z = this.attackTarget.posZ + (double)((this.attackTarget.getRNG().nextFloat() - 0.5F) * 14.0F);
            this.attackTarget.setPosition(x, y, z);
         }

         this.attackTarget.attackEntityFrom(DamageSource.causeMobDamage(this.mutantEnderman), 6.0F);
      }

      if (this.mutantEnderman.animTick == 19 && !(this.attackTarget instanceof EntityPlayer)) {
         MCHandler.spawnParticlesAtEntity(4, this.attackTarget, 0);
         this.attackTarget.worldObj.playSoundAtEntity(this.attackTarget, "random.explode", 1.2F, 0.9F + this.attackTarget.getRNG().nextFloat() * 0.2F);
      }

   }

   public void resetTask() {
      this.mutantEnderman.sendAttackPacket(0);
   }

   private void sendTeleportPlayer(EntityLivingBase player) {
      MutantCreatures.wrapper.sendTo(new PacketEnderTPlayer(), (EntityPlayerMP)player);
   }
}
