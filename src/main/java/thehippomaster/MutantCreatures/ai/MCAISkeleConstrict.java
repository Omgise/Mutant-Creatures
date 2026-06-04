package thehippomaster.MutantCreatures.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.DamageSource;
import thehippomaster.AnimationAPI.AIAnimation;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.MutantSkeleton;

public class MCAISkeleConstrict extends AIAnimation {
   private MutantSkeleton mutantSkeleton;
   private EntityLivingBase attackTarget;

   public MCAISkeleConstrict(MutantSkeleton skeleton) {
      super(skeleton);
      this.mutantSkeleton = skeleton;
      this.attackTarget = null;
   }

   public boolean isAutomatic() {
      return true;
   }

   public int getAnimID() {
      return 4;
   }

   public int getDuration() {
      return 20;
   }

   public void startExecuting() {
      super.startExecuting();
      this.attackTarget = this.mutantSkeleton.getAttackTarget();
   }

   public void updateTask() {
      super.updateTask();
      if (this.mutantSkeleton.getAnimTick() == 6) {
         this.attackTarget.attackEntityFrom(DamageSource.causeMobDamage(this.mutantSkeleton), 9.0F);
         this.attackTarget.motionX = (double)((1.0F + this.mutantSkeleton.getRNG().nextFloat() * 0.4F) * (float)(this.mutantSkeleton.getRNG().nextBoolean() ? 1 : -1));
         this.attackTarget.motionY = (double)(0.4F + this.mutantSkeleton.getRNG().nextFloat() * 0.8F);
         this.attackTarget.motionZ = (double)((1.0F + this.mutantSkeleton.getRNG().nextFloat() * 0.4F) * (float)(this.mutantSkeleton.getRNG().nextBoolean() ? 1 : -1));
         this.mutantSkeleton.worldObj.playSoundAtEntity(this.mutantSkeleton, "random.explode", 0.5F, 0.8F + this.mutantSkeleton.getRNG().nextFloat() * 0.4F);
         if (this.attackTarget instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)this.attackTarget;
            MutantCreatures.sendPacketToAll(player, new S12PacketEntityVelocity(player));
         }
      }

   }
}
