package thehippomaster.MutantCreatures;

import java.util.List;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.item.ChemicalX;

public class ChemicalXEntity extends EntityThrowable {
   public ChemicalXEntity(World world) {
      super(world);
   }

   public ChemicalXEntity(World world, EntityLivingBase living) {
      super(world, living);
   }

   public ChemicalXEntity(World world, double x, double y, double z) {
      super(world, x, y, z);
   }

   protected float getGravityVelocity() {
      return 0.05F;
   }

   protected float getInitialVelocity() {
      return 0.5F;
   }

   protected float getInaccuracy() {
      return -20.0F;
   }

   protected void onImpact(MovingObjectPosition mop) {
      if (!this.worldObj.isRemote) {
         List list = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, this.boundingBox.expand((double)12.0F, (double)8.0F, (double)12.0F));
         EntityLiving living = null;

         while(!list.isEmpty()) {
            living = (EntityLiving)list.remove(this.rand.nextInt(list.size()));
            if (!living.equals(this.getThrower()) && !ChemicalX.containsMutant(living)) {
               double dist = this.getDistanceSqToEntity(living);
               if (dist < (double)144.0F) {
                  break;
               }
            } else {
               living = null;
            }
         }

         if (mop.entityHit != null && mop.entityHit instanceof EntityLiving) {
            living = (EntityLiving)mop.entityHit;
         }

         if (living != null && living instanceof IBossDisplayData) {
            living = null;
         }

         if (living != null && ChemicalX.containsMutant(living)) {
            living = null;
         }

         if (living != null) {
            SkullSpirit spirit = new SkullSpirit(this.worldObj, living);
            spirit.setPosition(this.posX, this.posY, this.posZ);
            this.worldObj.spawnEntityInWorld(spirit);
         }
      }

      String iconName = "iconcrack_" + Item.getIdFromItem(MutantCreatures.chemicalX);

      for(int i = 5 + this.rand.nextInt(3); i >= 0; --i) {
         float x = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;
         float y = 0.1F + this.rand.nextFloat() * 0.1F;
         float z = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;
         this.worldObj.spawnParticle(iconName, this.posX, this.posY, this.posZ, (double)x, (double)y, (double)z);
      }

      MutantCreatures.proxy.spawnChemicalXParticles(this);
      float pitch = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
      this.worldObj.playSoundAtEntity(this, "dig.glass", 1.0F, pitch);
      this.setDead();
   }
}
