package thehippomaster.MutantCreatures.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import thehippomaster.MutantCreatures.MutantCreatures;

public class ItemSkeletonPart extends Item {
   public static final String[] partNames = new String[]{"limb", "rib", "pelvis", "shoulder", "arms", "ribcage"};
   @SideOnly(Side.CLIENT)
   public static IIcon[] icons;

   public ItemSkeletonPart() {
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      this.setCreativeTab(MutantCreatures.creativeTab);
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int dmg) {
      return icons[dmg];
   }

   public String getUnlocalizedName(ItemStack stack) {
      return super.getUnlocalizedName() + "." + partNames[stack.getItemDamage()];
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List) {
      for(int j = 0; j < partNames.length; ++j) {
         par3List.add(new ItemStack(item, 1, j));
      }

   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister iconRegister) {
      icons = new IIcon[partNames.length];

      for(int i = 0; i < icons.length; ++i) {
         icons[i] = iconRegister.registerIcon("MutantCreatures:SkeletonPart_" + partNames[i]);
      }

   }
}
