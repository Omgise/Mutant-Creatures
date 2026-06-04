package thehippomaster.MutantCreatures;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class MCCreativeTab extends CreativeTabs {
   public MCCreativeTab() {
      super("mutantcreatures");
   }

   @SideOnly(Side.CLIENT)
   public Item getTabIconItem() {
      return MutantCreatures.chemicalX;
   }
}
