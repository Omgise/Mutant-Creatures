package thehippomaster.MutantCreatures.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import thehippomaster.MutantCreatures.MutantCreatures;

public class CreeperStats extends Item {
   public CreeperStats() {
      this.setCreativeTab(MutantCreatures.creativeTab);
   }

   public void registerIcons(IIconRegister iconRegister) {
      this.itemIcon = iconRegister.registerIcon("MutantCreatures:CreeperStats");
   }
}
