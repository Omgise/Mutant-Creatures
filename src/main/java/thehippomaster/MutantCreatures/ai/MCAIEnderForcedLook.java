package thehippomaster.MutantCreatures.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.MutantEnderman;

public class MCAIEnderForcedLook extends EntityAIBase {
   private MutantEnderman mutantEnderman;
   private EntityLivingBase attackTarget;

   public MCAIEnderForcedLook(MutantEnderman enderman) {
      this.mutantEnderman = enderman;
      this.attackTarget = null;
      this.setMutexBits(7);
   }

   public boolean shouldExecute() {
      return this.mutantEnderman.currentAttackID == 3;
   }

   public void startExecuting() {
      this.mutantEnderman.animTick = 0;
      this.attackTarget = this.mutantEnderman.getAttackTarget();
      this.mutantEnderman.getNavigator().clearPathEntity();
      float f = 0.7F + this.mutantEnderman.getRNG().nextFloat() * 0.2F;
      this.mutantEnderman.worldObj.playSoundAtEntity(this.attackTarget, "MutantCreatures:mutantenderman.stare", 0.6F, f);
   }

   public boolean continueExecuting() {
      if (this.attackTarget != null && this.attackTarget.isEntityAlive()) {
         return this.attackTarget instanceof EntityPlayer && this.mutantEnderman.animTick < 100 ? MCAIEnderAttackPlayer.isLookingAtEnderman(this.mutantEnderman, (EntityPlayer)this.attackTarget) : false;
      } else {
         return false;
      }
   }

   public void updateTask() {
      this.mutantEnderman.getLookHelper().setLookPositionWithEntity(this.attackTarget, 45.0F, 45.0F);
   }

   public void resetTask() {
      this.mutantEnderman.sendAttackPacket(0);
      this.attackTarget.attackEntityFrom(DamageSource.causeMobDamage(this.mutantEnderman), 2.0F);
      this.attackTarget.addPotionEffect(new PotionEffect(Potion.blindness.id, 160 + this.mutantEnderman.getRNG().nextInt(140)));
      double x = this.mutantEnderman.posX - this.attackTarget.posX;
      double z = this.mutantEnderman.posZ - this.attackTarget.posZ;
      this.attackTarget.motionX = x * (double)0.1F;
      this.attackTarget.motionY = (double)0.3F;
      this.attackTarget.motionZ = z * (double)0.1F;
      EntityPlayerMP player = (EntityPlayerMP)this.attackTarget;
      MutantCreatures.sendPacketToAll(player, new S12PacketEntityVelocity(player));
      this.attackTarget = null;
   }
}
