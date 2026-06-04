package thehippomaster.MutantCreatures.item;

import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.MutantCreatures;

public class CreeperShard extends Item {
   public CreeperShard() {
      this.iconString = "MutantCreatures:CreeperShard";
      this.setMaxStackSize(1);
      this.setMaxDamage(32);
      this.setFull3D();
      this.setCreativeTab(MutantCreatures.creativeTab);
   }

   public boolean hasEffect(ItemStack stack) {
      return stack.getItemDamage() == 0;
   }

   public Multimap getItemAttributeModifiers() {
      Multimap multimap = super.getItemAttributeModifiers();
      multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(Item.field_111210_e, "Weapon modifier", (double)2.0F, 0));
      return multimap;
   }

   public boolean hitEntity(ItemStack stack, EntityLivingBase living, EntityLivingBase livingPlayer) {
      EntityPlayer player = (EntityPlayer)livingPlayer;
      double x = living.posX - player.posX;
      double y = living.posY - player.posY;
      double z = living.posZ - player.posZ;
      double d = Math.sqrt(x * x + y * y + z * z);
      living.motionX = x / d * (double)0.9F;
      living.motionY = y / d * (double)0.2F + (double)0.3F;
      living.motionZ = z / d * (double)0.9F;
      player.worldObj.playSoundAtEntity(player, "random.explode", 0.3F, 0.8F + player.getRNG().nextFloat() * 0.4F);
      if (player.getRNG().nextInt(4) == 0) {
         player.addPotionEffect(new PotionEffect(Potion.poison.id, 80 + player.getRNG().nextInt(40), 0));
      }

      int damage = stack.getItemDamage();
      if (damage > 0) {
         stack.setItemDamage(damage - 1);
      }

      return true;
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      int maxDmg = this.getMaxDamage();
      int dmg = stack.getItemDamage();
      if (!world.isRemote) {
         float damage = 5.0F * (float)(maxDmg - dmg) / 32.0F;
         if (dmg == 0) {
            damage += 2.0F;
         }

         world.createExplosion(player, player.posX, player.posY + (double)1.0F, player.posZ, damage, true);
      }

      player.swingItem();
      stack.setItemDamage(maxDmg);
      return stack;
   }
}
