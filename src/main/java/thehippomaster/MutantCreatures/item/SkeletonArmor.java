package thehippomaster.MutantCreatures.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import thehippomaster.MutantCreatures.MutantCreatures;

public class SkeletonArmor extends ItemArmor implements ISpecialArmor {
   public SkeletonArmor(int armor, int index) {
      super(ArmorMaterial.IRON, armor, index);
      this.setCreativeTab(MutantCreatures.creativeTab);
   }

   public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
      if (this.armorType == 1) {
         MutantCreatures.proxy.increaseBowSpeed(player);
      }

      if (this.armorType == 2) {
         player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 1, 1, true));
      }

      if (this.armorType == 3) {
         player.addPotionEffect(new PotionEffect(Potion.jump.id, 1, player.isSprinting() ? 1 : 0, true));
      }

   }

   public ISpecialArmor.ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
      double dmgRatio = 0.05;
      if (slot == 3) {
         dmgRatio = 0.05;
      }

      if (slot == 1) {
         dmgRatio = 0.075;
      }

      ISpecialArmor.ArmorProperties properties = new ISpecialArmor.ArmorProperties(0, dmgRatio, Integer.MAX_VALUE);
      properties.Slot = slot;
      return properties;
   }

   public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
      if (slot == 0) {
         return 5;
      } else if (slot == 1) {
         return 6;
      } else {
         return slot == 2 ? 6 : 3;
      }
   }

   public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
      stack.damageItem(damage, entity);
   }

   public boolean getIsRepairable(ItemStack stack, ItemStack stack1) {
      return false;
   }

   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
      int layer = slot == 2 ? 2 : 1;
      return "MutantCreatures:textures/skele_armor_" + layer + ".png";
   }

   @SideOnly(Side.CLIENT)
   public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
      return (ModelBiped)MutantCreatures.proxy.getSkeleArmorModel(entityLiving, itemStack, armorSlot);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister iconRegister) {
      this.itemIcon = iconRegister.registerIcon("MutantCreatures:SkeleArmor_" + this.armorType);
   }
}
