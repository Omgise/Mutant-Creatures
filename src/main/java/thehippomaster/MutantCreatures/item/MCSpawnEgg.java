package thehippomaster.MutantCreatures.item;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thehippomaster.MutantCreatures.CreeperMinion;
import thehippomaster.MutantCreatures.MutantCreatures;

public class MCSpawnEgg extends Item {
   @SideOnly(Side.CLIENT)
   private IIcon secondaryIcon;
   public static HashMap<Integer, EggColors> eggColorsMap = new HashMap();

   public MCSpawnEgg() {
      this.setHasSubtypes(true);
      this.setCreativeTab(MutantCreatures.creativeTab);
   }

   public String getItemStackDisplayName(ItemStack stack) {
      ModContainer container = FMLCommonHandler.instance().findContainerFor(MutantCreatures.instance);
      EntityRegistry.EntityRegistration registration = EntityRegistry.instance().lookupModSpawn(container, stack.getItemDamage());
      return "Spawn " + StatCollector.translateToLocal("entity.MutantCreatures." + registration.getEntityName() + ".name");
   }

   @SideOnly(Side.CLIENT)
   public int getColorFromItemStack(ItemStack stack, int i) {
      EggColors colors = (EggColors)eggColorsMap.get(stack.getItemDamage());
      if (colors == null) {
         return 16777215;
      } else {
         return i == 0 ? colors.primaryColor : colors.secondaryColor;
      }
   }

   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
      if (world.isRemote) {
         return true;
      } else {
         Block block = world.getBlock(x, y, z);
         x += Facing.offsetsXForSide[side];
         y += Facing.offsetsYForSide[side];
         z += Facing.offsetsZForSide[side];
         double addY = (double)0.0F;
         if (side == 1 && block != Blocks.air && block.getRenderType() == 11) {
            addY = (double)0.5F;
         }

         EntityLiving living = spawnMutantCreature(world, stack.getItemDamage(), (double)x + (double)0.5F, (double)y + addY, (double)z + (double)0.5F);
         if (living != null) {
            if (stack.hasDisplayName()) {
               living.setCustomNameTag(stack.getDisplayName());
            }

            if (living instanceof CreeperMinion) {
               CreeperMinion minion = (CreeperMinion)living;
               minion.setOwner(player.getCommandSenderName());
               minion.setSitting(true);
            }

            if (!player.capabilities.isCreativeMode) {
               --stack.stackSize;
            }
         }

         return true;
      }
   }

   public static EntityLiving spawnMutantCreature(World world, int id, double x, double y, double z) {
      if (world.isRemote) {
         return null;
      } else {
         ModContainer container = FMLCommonHandler.instance().findContainerFor(MutantCreatures.instance);
         EntityRegistry.EntityRegistration registration = EntityRegistry.instance().lookupModSpawn(container, id);
         Entity entity = createEntity(registration.getEntityClass(), world);
         if (entity != null && entity instanceof EntityLiving) {
            EntityLiving living = (EntityLiving)entity;
            living.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
            living.rotationYawHead = living.rotationYaw;
            living.renderYawOffset = living.rotationYaw;
            living.onSpawnWithEgg((IEntityLivingData)null);
            world.spawnEntityInWorld(living);
            living.playLivingSound();
            return living;
         } else {
            return null;
         }
      }
   }

   public static Entity createEntity(Class<? extends Entity> clazz, World world) {
      if (clazz == null) {
         return null;
      } else {
         Entity entity = null;

         try {
            entity = (Entity)clazz.getConstructor(World.class).newInstance(world);
         } catch (Exception e) {
            e.printStackTrace();
         }

         return entity;
      }
   }

   @SideOnly(Side.CLIENT)
   public boolean requiresMultipleRenderPasses() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamageForRenderPass(int par1, int par2) {
      return par2 > 0 ? this.secondaryIcon : Items.spawn_egg.getIconFromDamageForRenderPass(par1, par2);
   }

   public void getSubItems(Item par1, CreativeTabs creativeTabs, List list) {
      Iterator<Integer> iterator = eggColorsMap.keySet().iterator();

      while(iterator.hasNext()) {
         list.add(new ItemStack(par1, 1, (Integer)iterator.next()));
      }

   }

   public void registerIcons(IIconRegister iconRegister) {
      super.registerIcons(iconRegister);
      this.secondaryIcon = iconRegister.registerIcon("MutantCreatures:SpawnEgg");
   }

   public static void addEgg(Class<? extends Entity> clazz, int color1, int color2) {
      if (eggColorsMap.containsKey(clazz)) {
         throw new IllegalArgumentException("Mapping for that entity exists already!");
      } else {
         EntityRegistry.EntityRegistration registration = EntityRegistry.instance().lookupModSpawn(clazz, false);
         eggColorsMap.put(registration.getModEntityId(), new EggColors(color1, color2));
      }
   }

   private static class EggColors {
      private int primaryColor;
      private int secondaryColor;

      public EggColors(int color1, int color2) {
         this.primaryColor = color1;
         this.secondaryColor = color2;
      }
   }
}
