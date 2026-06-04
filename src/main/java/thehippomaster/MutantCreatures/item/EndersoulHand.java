package thehippomaster.MutantCreatures.item;

import com.google.common.collect.Multimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import org.lwjgl.opengl.GL11;
import thehippomaster.MutantCreatures.EnderBlock;
import thehippomaster.MutantCreatures.MCHandler;
import thehippomaster.MutantCreatures.MutantCreatures;

public class EndersoulHand extends Item implements IItemRenderer {
   private static final ResourceLocation texture = new ResourceLocation("MutantCreatures:textures/endersoulhand.png");
   private static final ArrayList<Block> prohibitedBlocks = new ArrayList();

   public EndersoulHand() {
      this.setMaxStackSize(1);
      this.setMaxDamage(240);
      this.setFull3D();
      this.setCreativeTab(MutantCreatures.creativeTab);
   }

   @SideOnly(Side.CLIENT)
   public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
      return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
      boolean thirdPerson = type == ItemRenderType.EQUIPPED;
      if (thirdPerson || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
         GL11.glPushMatrix();
         TextureManager engine = RenderManager.instance.renderEngine;
         if (engine != null) {
            engine.bindTexture(texture);
         }

         if (RenderManager.instance.playerViewY == 180.0F) {
            thirdPerson = true;
         }

         if (thirdPerson) {
            GL11.glScalef(1.0F, 1.0F, -1.0F);
         }

         GL11.glRotatef(thirdPerson ? 90.0F : 30.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(30.0F, 1.0F, 0.0F, 0.0F);
         if (thirdPerson) {
            GL11.glTranslatef(0.0F, -0.6F, 0.5F);
         } else {
            GL11.glTranslatef(0.7F, -0.8F, 0.5F);
         }

         if (!thirdPerson) {
            GL11.glScalef(1.2F, 1.2F, 1.2F);
         }

         GL11.glDisable(2896);
         GL11.glMatrixMode(5890);
         GL11.glLoadIdentity();
         EntityLivingBase living = (EntityLivingBase)data[1];
         float partialTick = MutantCreatures.proxy.getPartialTicks();
         float add = ((float)living.ticksExisted + partialTick) * 0.008F;
         GL11.glTranslatef(add, add, 0.0F);
         GL11.glMatrixMode(5888);
         GL11.glEnable(2977);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         int var5 = 61680;
         int var6 = var5 % 65536;
         int var7 = var5 / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6, (float)var7);
         GL11.glColor4f(0.9F, 0.3F, 1.0F, 1.0F);
         MutantCreatures.proxy.renderEnderHand();
         GL11.glMatrixMode(5890);
         GL11.glLoadIdentity();
         GL11.glMatrixMode(5888);
         GL11.glDisable(3042);
         GL11.glEnable(2896);
         GL11.glPopMatrix();
      }

   }

   public Multimap getItemAttributeModifiers() {
      Multimap multimap = super.getItemAttributeModifiers();
      multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(Item.field_111210_e, "Weapon modifier", (double)6.0F, 0));
      return multimap;
   }

   public boolean hitEntity(ItemStack stack, EntityLivingBase living, EntityLivingBase player) {
      stack.damageItem(1, player);
      return true;
   }

   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int side, float f, float f1, float f2) {
      if (player.isSneaking()) {
         return false;
      } else {
         if (!world.isRemote) {
            if (!world.canMineBlock(player, i, j, k)) {
               return false;
            }

            if (!player.canPlayerEdit(i, j, k, side, stack)) {
               return false;
            }

            Block block = world.getBlock(i, j, k);
            if (prohibitedBlocks.contains(block)) {
               return false;
            }

            if (block instanceof BlockContainer) {
               return false;
            }

            int data = world.getBlockMetadata(i, j, k);
            world.setBlock(i, j, k, Blocks.air, 0, 3);
            world.spawnEntityInWorld(new EnderBlock(world, player, block, data));
         }

         return true;
      }
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if (!player.isSneaking()) {
         return stack;
      } else {
         MovingObjectPosition mop = this.getMOPFromPlayer(world, player, 128.0F);
         if (mop == null) {
            return stack;
         } else {
            if (mop.typeOfHit == MovingObjectType.BLOCK) {
               int x = mop.blockX;
               int y = mop.blockY;
               int z = mop.blockZ;
               x += Facing.offsetsXForSide[mop.sideHit];
               y += Facing.offsetsYForSide[mop.sideHit];
               z += Facing.offsetsZForSide[mop.sideHit];
               Block block = world.getBlock(x, y - 1, z);
               if (block != Blocks.air || !block.getMaterial().isSolid()) {
                  Block block1 = world.getBlock(mop.blockX, mop.blockY + 1, mop.blockZ);
                  Block block2 = world.getBlock(mop.blockX, mop.blockY + 2, mop.blockZ);
                  Block block3 = world.getBlock(mop.blockX, mop.blockY + 3, mop.blockZ);
                  if (block1 == Blocks.air) {
                     x = mop.blockX;
                     y = mop.blockY + 1;
                     z = mop.blockZ;
                  } else if (block2 == Blocks.air) {
                     x = mop.blockX;
                     y = mop.blockY + 2;
                     z = mop.blockZ;
                  } else if (block3 == Blocks.air) {
                     x = mop.blockX;
                     y = mop.blockY + 3;
                     z = mop.blockZ;
                  }
               }

               if (!world.isRemote) {
                  world.playSoundEffect(player.posX, player.posY + (double)player.height / (double)2.0F, player.posZ, "mob.endermen.portal", 1.0F, 1.0F);
               }

               player.setPosition((double)x + (double)0.5F, (double)y, (double)z + (double)0.5F);
               player.fallDistance = 0.0F;
               if (!world.isRemote) {
                  float r = 2.0F;
                  List list = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand((double)r, (double)r, (double)r));

                  for(int i = 0; i < list.size(); ++i) {
                     Entity entity = (Entity)list.get(i);
                     if (entity instanceof EntityLiving) {
                        EntityLivingBase living = (EntityLiving)entity;
                        living.attackEntityFrom(DamageSource.causePlayerDamage(player), 4.0F);
                        if (living instanceof EntityPlayer) {
                           living.addPotionEffect(new PotionEffect(Potion.blindness.id, 100));
                        }

                        double mx = entity.posX - player.posX;
                        double mz = entity.posZ - player.posZ;
                        double signX = mx / Math.abs(mx);
                        double signZ = mz / Math.abs(mz);
                        living.motionX = ((double)r * signX * (double)2.0F - mx) * (double)0.2F;
                        living.motionY = (double)0.2F;
                        living.motionZ = ((double)r * signZ * (double)2.0F - mz) * (double)0.2F;
                     }
                  }

                  world.playSoundEffect(player.posX, player.posY + (double)player.height / (double)2.0F, player.posZ, "mob.endermen.portal", 1.0F, 1.0F);
                  MCHandler.spawnParticlesAtEntity(4, player, 0);
                  MutantCreatures.sendPacketToAll(player, new S0BPacketAnimation(player, 0));
               }

               stack.damageItem(4, player);
            }

            return stack;
         }
      }
   }

   public MovingObjectPosition getMOPFromPlayer(World world, EntityPlayer player, float maxDist) {
      float f = 1.0F;
      float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
      float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
      double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double)f;
      double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double)f + 1.62 - (double)player.yOffset;
      double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)f;
      Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
      float f3 = MathHelper.cos(-f2 * ((float)Math.PI / 180F) - (float)Math.PI);
      float f4 = MathHelper.sin(-f2 * ((float)Math.PI / 180F) - (float)Math.PI);
      float f5 = -MathHelper.cos(-f1 * ((float)Math.PI / 180F));
      float f6 = MathHelper.sin(-f1 * ((float)Math.PI / 180F));
      float f7 = f4 * f5;
      float f8 = f3 * f5;
      double d3 = (double)maxDist;
      Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
      return world.func_147447_a(vec3, vec31, false, true, false);
   }

   public void registerIcons(IIconRegister iconRegister) {
      this.itemIcon = iconRegister.registerIcon("MutantCreatures:EndersoulHand");
   }

   static {
      prohibitedBlocks.add(Blocks.bedrock);
      prohibitedBlocks.add(Blocks.obsidian);
      prohibitedBlocks.add(Blocks.redstone_wire);
      prohibitedBlocks.add(Blocks.tripwire);
      prohibitedBlocks.add(Blocks.iron_bars);
      prohibitedBlocks.add(Blocks.rail);
      prohibitedBlocks.add(Blocks.detector_rail);
      prohibitedBlocks.add(Blocks.golden_rail);
      prohibitedBlocks.add(Blocks.powered_repeater);
      prohibitedBlocks.add(Blocks.powered_comparator);
      prohibitedBlocks.add(Blocks.daylight_detector);
      prohibitedBlocks.add(Blocks.vine);
      prohibitedBlocks.add(Blocks.wheat);
      prohibitedBlocks.add(Blocks.pumpkin_stem);
      prohibitedBlocks.add(Blocks.melon_stem);
      prohibitedBlocks.add(Blocks.waterlily);
      prohibitedBlocks.add(Blocks.carrots);
      prohibitedBlocks.add(Blocks.potatoes);
      prohibitedBlocks.add(Blocks.cocoa);
   }
}
