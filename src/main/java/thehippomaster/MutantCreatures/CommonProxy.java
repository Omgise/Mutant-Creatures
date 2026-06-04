package thehippomaster.MutantCreatures;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CommonProxy {
   public void registerRenderers() {
   }

   public boolean isClient() {
      return false;
   }

   public World getWorldClient() {
      return null;
   }

   public EntityPlayer getClientPlayer() {
      return null;
   }

   public float getPartialTicks() {
      return 0.0F;
   }

   public void spawnChemicalXParticles(ChemicalXEntity chemicalX) {
   }

   public void spawnSkullParticles(SkullSpirit spirit, boolean flag) {
   }

   public void renderEnderHand() {
   }

   public void increaseBowSpeed(EntityPlayer player) {
      if (!MutantCreatures.isClient()) {
         ItemStack bow = (ItemStack)ReflectionHelper.getPrivateValue(EntityPlayer.class, player, MutantCreatures.fItemInUse);
         int count = (Integer)ReflectionHelper.getPrivateValue(EntityPlayer.class, player, MutantCreatures.fItemInUseCount);
         if (bow != null && bow.getItem().getItemUseAction(bow) == EnumAction.bow && count > 4) {
            player.setItemInUse((ItemStack)null, 0);
            player.setItemInUse(bow, count - 3);
         }

      }
   }

   public Object getSkeleArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
      return null;
   }

   public boolean updateSpiderPigRider(SpiderPig pig, EntityPlayer rider) {
      return false;
   }
}
