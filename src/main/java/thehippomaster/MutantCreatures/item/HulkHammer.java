package thehippomaster.MutantCreatures.item;

import com.google.common.collect.Multimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.ZombieChunk;
import thehippomaster.MutantCreatures.ai.MCAIZombieMelee;

public class HulkHammer extends Item {
   public static HashMap<String, ArrayList<ZombieChunk>> chunkList = new HashMap();

   public HulkHammer() {
      this.setMaxStackSize(1);
      this.setMaxDamage(64);
      this.setFull3D();
      this.setCreativeTab(MutantCreatures.creativeTab);
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldRotateAroundWhenRendering() {
      return true;
   }

   public Multimap getItemAttributeModifiers() {
      Multimap multimap = super.getItemAttributeModifiers();
      multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(Item.field_111210_e, "Weapon modifier", (double)2.0F, 0));
      return multimap;
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if (!world.isRemote) {
         ArrayList<ZombieChunk> list = new ArrayList();
         float temp = player.rotationPitch;
         player.rotationPitch = 0.0F;
         Vec3 vec = player.getLookVec();
         player.rotationPitch = temp;
         int x = MathHelper.floor_double(player.posX + vec.xCoord * (double)1.5F);
         int y = MathHelper.floor_double(player.boundingBox.minY);
         int z = MathHelper.floor_double(player.posZ + vec.zCoord * (double)1.5F);
         int x1 = MathHelper.floor_double(player.posX + vec.xCoord * (double)8.0F);
         int z1 = MathHelper.floor_double(player.posZ + vec.zCoord * (double)8.0F);
         MCAIZombieMelee.addLinePositions(world, list, x, z, x1, z1, y);
         addChunkAttack(player.getCommandSenderName(), list);
         world.playSoundAtEntity(player, "random.explode", 0.8F, 0.8F + player.getRNG().nextFloat() * 0.4F);
      }

      player.swingItem();
      stack.damageItem(1, player);
      return stack;
   }

   public static void addChunkAttack(String name, ArrayList<ZombieChunk> list) {
      ArrayList<ZombieChunk> chunks = (ArrayList)chunkList.get(name);
      if (chunks == null) {
         chunkList.put(name, list);
      } else {
         chunks.addAll(list);
         chunkList.put(name, chunks);
      }
   }

   public void registerIcons(IIconRegister iconRegister) {
      this.itemIcon = iconRegister.registerIcon("MutantCreatures:HulkHammer");
   }
}
