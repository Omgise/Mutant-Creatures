package thehippomaster.MutantCreatures;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import thehippomaster.MutantCreatures.client.GuiCreeperMinion;
import thehippomaster.MutantCreatures.packet.PacketSpawnParticles;

public class MCHandler implements IGuiHandler {
   @SubscribeEvent
   public void onRenderTick(TickEvent.RenderTickEvent event) {
      if (event.phase == Phase.END) {
         MutantCreatures.onRenderTick();
      }

   }

   @SubscribeEvent
   public void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase == Phase.END) {
         MutantCreatures.onClientTick();
      }

   }

   @SubscribeEvent
   public void onServerTick(TickEvent.ServerTickEvent event) {
      if (event.phase == Phase.END) {
         try {
            MutantCreatures.onServerTick();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

   }

   @SubscribeEvent
   public void onPlayerShootArrow(ArrowLooseEvent event) {
      EntityPlayer player = event.entityPlayer;
      World world = player.worldObj;
      ItemStack bow = player.getCurrentEquippedItem();
      boolean inAir = !player.onGround && !player.isInWater() && !player.isInsideOfMaterial(Material.lava);
      boolean skeleArmorHead = player.getCurrentArmor(3) != null && player.getCurrentArmor(3).getItem() == MutantCreatures.skeleArmorHead;
      boolean infiniteArrows = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, bow) > 0;
      if (!player.worldObj.isRemote && skeleArmorHead && (infiniteArrows || player.inventory.hasItem(Items.arrow))) {
         event.setCanceled(true);
         float f = (float)event.charge / 20.0F;
         f = (f * f + f * 2.0F) / 3.0F;
         if (f > 1.0F) {
            f = 1.0F;
         }

         EntityArrow entityarrow = new EntityArrow(world, player, f * 2.0F);
         if (f == 1.0F && inAir) {
            entityarrow.setIsCritical(true);
         }

         int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, bow);
         if (k > 0) {
            entityarrow.setDamage(entityarrow.getDamage() + (double)k * (double)0.5F + (double)0.5F);
         }

         int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, bow);
         if (l > 0) {
            entityarrow.setKnockbackStrength(l);
         }

         if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, bow) > 0) {
            entityarrow.setFire(100);
         }

         entityarrow.setDamage(entityarrow.getDamage() * (inAir ? (double)2.0F : (double)0.5F));
         bow.damageItem(1, player);
         world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (player.getRNG().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
         if (infiniteArrows) {
            entityarrow.canBePickedUp = 2;
         } else if (!inAir || player.getRNG().nextBoolean()) {
            player.inventory.consumeInventoryItem(Items.arrow);
         }

         world.spawnEntityInWorld(entityarrow);
      }

   }

   @SubscribeEvent
   public void onPlayerToss(ItemTossEvent event) {
      if (!event.player.worldObj.isRemote) {
         ItemStack stack = event.entityItem.getEntityItem();
         boolean isHand = stack.getItem() == MutantCreatures.endersoulHand;
         if (stack.getItem() == Items.ender_eye || isHand) {
            World world = event.player.worldObj;
            List list = world.getEntitiesWithinAABB(EndersoulFragment.class, event.player.boundingBox.expand((double)8.0F, (double)8.0F, (double)8.0F));
            int count = 0;

            for(int i = 0; i < list.size(); ++i) {
               EndersoulFragment orb = (EndersoulFragment)list.get(i);
               if (orb.isEntityAlive() && orb.owner == event.player) {
                  ++count;
                  orb.setDead();
               }
            }

            if (count > 0) {
               spawnParticlesAtEntity(4, event.player, 0);
               int addDmg = count * 60;
               if (isHand) {
                  int dmg = stack.getItemDamage() - addDmg;
                  dmg = Math.max(dmg, 0);
                  stack.setItemDamage(dmg);
               } else {
                  ItemStack newStack = new ItemStack(MutantCreatures.endersoulHand, 1, MutantCreatures.endersoulHand.getMaxDamage() - addDmg);
                  event.entityItem.setEntityItemStack(newStack);
               }
            }
         }
      }

   }

   @SubscribeEvent
   public void onEntityJoined(EntityJoinWorldEvent event) {
      if (event.entity instanceof EntityVillager) {
         EntityVillager villager = (EntityVillager)event.entity;
         villager.tasks.addTask(1, new EntityAIAvoidEntity(villager, MutantZombie.class, 4.0F, (double)0.27F, (double)0.3F));
      }

   }

   public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
      return null;
   }

   public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
      return id == 0 ? new GuiCreeperMinion() : null;
   }

   public static void spawnHeartsAtEntity(Entity entity, int amount) {
      spawnParticlesAtEntity(0, entity, amount);
   }

   public static void spawnFlamesAtEntity(Entity entity, int amount) {
      spawnParticlesAtEntity(1, entity, amount);
   }

   public static void spawnSnowAtEntity(Entity entity, int amount) {
      spawnParticlesAtEntity(3, entity, amount);
   }

   public static void spawnParticlesAtEntity(int id, Entity entity, int amount) {
      MutantCreatures.wrapper.sendToAll(new PacketSpawnParticles(id, entity.getEntityId(), amount));
   }

   @SideOnly(Side.CLIENT)
   public static void handleEnderParticles(Entity entity) {
      if (entity instanceof MutantEnderman) {
         ((MutantEnderman)entity).spawnBigParticles();
      } else {
         MutantCreatures.spawnEnderParticles(entity);
      }

   }
}
