package thehippomaster.MutantCreatures.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.ChemicalXEntity;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.MutantCreeper;
import thehippomaster.MutantCreatures.MutantEnderman;
import thehippomaster.MutantCreatures.MutantSkeleton;
import thehippomaster.MutantCreatures.MutantSnowGolem;
import thehippomaster.MutantCreatures.MutantZombie;

public class ChemicalX extends Item {
   private static HashMap<Class<? extends EntityLivingBase>, Class<? extends EntityLivingBase>> mcMap = new HashMap();

   public ChemicalX() {
      this.setCreativeTab(MutantCreatures.creativeTab);
   }

   @SideOnly(Side.CLIENT)
   public boolean hasEffect(ItemStack stack) {
      return true;
   }

   public EnumRarity getRarity(ItemStack stack) {
      return EnumRarity.epic;
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if (!player.capabilities.isCreativeMode) {
         --stack.stackSize;
      }

      world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
      if (!world.isRemote) {
         world.spawnEntityInWorld(new ChemicalXEntity(world, player));
      }

      return stack;
   }

   public void registerIcons(IIconRegister iconRegister) {
      this.itemIcon = iconRegister.registerIcon("MutantCreatures:ChemicalX");
   }

   public static EntityLivingBase getMutantOf(EntityLivingBase living) {
      if (!mcMap.containsKey(living.getClass())) {
         return null;
      } else {
         if (living instanceof EntitySkeleton) {
            EntitySkeleton skele = (EntitySkeleton)living;
            if (skele.getSkeletonType() == 1) {
               return null;
            }
         }

         String name = (String)EntityList.classToStringMapping.get(mcMap.get(living.getClass()));
         return (EntityLivingBase)EntityList.createEntityByName(name, living.worldObj);
      }
   }

   public static boolean containsMutant(EntityLivingBase living) {
      for(Class key : mcMap.keySet()) {
         if (living.getClass().isAssignableFrom((Class)mcMap.get(key))) {
            return true;
         }
      }

      return false;
   }

   static {
      mcMap.put(EntityCreeper.class, MutantCreeper.class);
      mcMap.put(EntityZombie.class, MutantZombie.class);
      mcMap.put(EntityEnderman.class, MutantEnderman.class);
      mcMap.put(EntitySnowman.class, MutantSnowGolem.class);
      mcMap.put(EntitySkeleton.class, MutantSkeleton.class);
   }
}
