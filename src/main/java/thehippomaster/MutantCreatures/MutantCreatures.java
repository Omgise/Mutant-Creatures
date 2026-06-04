package thehippomaster.MutantCreatures;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import thehippomaster.MutantCreatures.client.FXEnder;
import thehippomaster.MutantCreatures.item.ChemicalX;
import thehippomaster.MutantCreatures.item.CreeperShard;
import thehippomaster.MutantCreatures.item.CreeperStats;
import thehippomaster.MutantCreatures.item.EndersoulHand;
import thehippomaster.MutantCreatures.item.HulkHammer;
import thehippomaster.MutantCreatures.item.ItemSkeletonPart;
import thehippomaster.MutantCreatures.item.MCSpawnEgg;
import thehippomaster.MutantCreatures.item.SkeletonArmor;
import thehippomaster.MutantCreatures.packet.PacketCMOptions;
import thehippomaster.MutantCreatures.packet.PacketEHandPos;
import thehippomaster.MutantCreatures.packet.PacketEnderAttack;
import thehippomaster.MutantCreatures.packet.PacketEnderBlock;
import thehippomaster.MutantCreatures.packet.PacketEnderTPlayer;
import thehippomaster.MutantCreatures.packet.PacketEnderTeleport;
import thehippomaster.MutantCreatures.packet.PacketSkeleAttack;
import thehippomaster.MutantCreatures.packet.PacketSnowGolemAttack;
import thehippomaster.MutantCreatures.packet.PacketSnowGolemOwner;
import thehippomaster.MutantCreatures.packet.PacketSpawnParticles;
import thehippomaster.MutantCreatures.packet.PacketSpiderPigJump;
import thehippomaster.MutantCreatures.packet.PacketZombieAttack;

@Mod(
   modid = "MutantCreatures",
   name = "Mutant Creatures",
   version = "1.4.9"
)
public class MutantCreatures {
   @Instance("MutantCreatures")
   public static MutantCreatures instance;
   @SidedProxy(
      clientSide = "thehippomaster.MutantCreatures.client.ClientProxy",
      serverSide = "thehippomaster.MutantCreatures.CommonProxy"
   )
   public static CommonProxy proxy;
   public static MCHandler mcHandler;
   public static SimpleNetworkWrapper wrapper;
   public static Item creeperShard;
   public static Item creeperStats;
   public static Item hulkHammer;
   public static Item chemicalX;
   public static Item endersoulHand;
   public static Item skeletonPart;
   public static Item spawnEgg;
   public static Item skeleArmorHead;
   public static Item skeleArmorChest;
   public static Item skeleArmorLegs;
   public static Item skeleArmorBoots;
   public static BiomeGenBase[] suitableBiomes;
   public static Random rand = new Random();
   public static final MCCreativeTab creativeTab = new MCCreativeTab();
   private static int spawnrate;
   private static boolean spawnCreeper;
   private static boolean spawnZombie;
   private static boolean spawnEnderman;
   private static boolean spawnEndermanEnd;
   private static boolean spawnSkeleton;
   private static boolean spawnSpiderPig;
   public static final String[] fTimer;
   public static final String[] fThrower;
   public static final String[] fShowModel;
   public static final String[] fCubeList;
   public static final String[] fChildModels;
   public static final String[] fItemInUse;
   public static final String[] fItemInUseCount;
   public static final String[] fIsInWeb;

   @EventHandler
   public void preInit(FMLPreInitializationEvent e) {
      Configuration cfg = new Configuration(e.getSuggestedConfigurationFile());
      cfg.load();
      spawnrate = cfg.get("general", "Spawnrate", 10).getInt();
      spawnCreeper = cfg.get("general", "SpawnMutantCreeper", true).getBoolean(true);
      spawnZombie = cfg.get("general", "SpawnMutantZombie", true).getBoolean(true);
      spawnEnderman = cfg.get("general", "SpawnMutantEnderman", true).getBoolean(true);
      spawnEndermanEnd = cfg.get("general", "SpawnMutantEndermanInTheEnd", true).getBoolean(true);
      spawnSkeleton = cfg.get("general", "SpawnMutantSkeleton", true).getBoolean(true);
      spawnSpiderPig = cfg.get("general", "SpawnSpiderPig", true).getBoolean(true);
      cfg.save();
      mcHandler = new MCHandler();
      MinecraftForge.EVENT_BUS.register(mcHandler);
      FMLCommonHandler.instance().bus().register(mcHandler);
      spawnEgg = (new MCSpawnEgg()).setUnlocalizedName("mcSpawnEgg").setTextureName("spawn_egg");
      creeperShard = (new CreeperShard()).setUnlocalizedName("creeperShard");
      creeperStats = (new CreeperStats()).setUnlocalizedName("creeperStats");
      hulkHammer = (new HulkHammer()).setUnlocalizedName("hulkHammer");
      chemicalX = (new ChemicalX()).setUnlocalizedName("chemicalX");
      endersoulHand = (new EndersoulHand()).setUnlocalizedName("endersoulHand");
      skeletonPart = (new ItemSkeletonPart()).setUnlocalizedName("skeletonPart");
      skeleArmorHead = (new SkeletonArmor(3, 0)).setUnlocalizedName("skeleArmorHead");
      skeleArmorChest = (new SkeletonArmor(3, 1)).setUnlocalizedName("skeleArmorChest");
      skeleArmorLegs = (new SkeletonArmor(3, 2)).setUnlocalizedName("skeleArmorLegs");
      skeleArmorBoots = (new SkeletonArmor(3, 3)).setUnlocalizedName("skeleArmorBoots");
      GameRegistry.registerItem(spawnEgg, spawnEgg.getUnlocalizedName());
      GameRegistry.registerItem(creeperShard, creeperShard.getUnlocalizedName());
      GameRegistry.registerItem(creeperStats, creeperStats.getUnlocalizedName());
      GameRegistry.registerItem(hulkHammer, hulkHammer.getUnlocalizedName());
      GameRegistry.registerItem(chemicalX, chemicalX.getUnlocalizedName());
      GameRegistry.registerItem(endersoulHand, endersoulHand.getUnlocalizedName());
      GameRegistry.registerItem(skeletonPart, skeletonPart.getUnlocalizedName());
      GameRegistry.registerItem(skeleArmorHead, skeleArmorHead.getUnlocalizedName());
      GameRegistry.registerItem(skeleArmorChest, skeleArmorChest.getUnlocalizedName());
      GameRegistry.registerItem(skeleArmorLegs, skeleArmorLegs.getUnlocalizedName());
      GameRegistry.registerItem(skeleArmorBoots, skeleArmorBoots.getUnlocalizedName());
      GameRegistry.addRecipe(new ItemStack(creeperStats), new Object[]{" # ", "#X#", " # ", '#', Blocks.cobblestone, 'X', Items.gunpowder});
      GameRegistry.addRecipe(new ItemStack(chemicalX), new Object[]{" # ", "#X#", " # ", '#', Blocks.obsidian, 'X', Items.potionitem});
      GameRegistry.addRecipe(new ItemStack(skeletonPart, 1, 4), new Object[]{"# #", "X X", "X X", '#', new ItemStack(skeletonPart, 1, 3), 'X', new ItemStack(skeletonPart, 1, 0)});
      GameRegistry.addRecipe(new ItemStack(skeletonPart, 1, 5), new Object[]{"# #", "# #", "# #", '#', new ItemStack(skeletonPart, 1, 1)});
      GameRegistry.addRecipe(new ItemStack(skeleArmorChest), new Object[]{"#", "X", '#', new ItemStack(skeletonPart, 1, 4), 'X', new ItemStack(skeletonPart, 1, 5)});
      GameRegistry.addRecipe(new ItemStack(skeleArmorLegs), new Object[]{" # ", "X X", '#', new ItemStack(skeletonPart, 1, 2), 'X', new ItemStack(skeletonPart, 1, 0)});
      GameRegistry.addRecipe(new ItemStack(skeleArmorBoots), new Object[]{"# #", '#', new ItemStack(skeletonPart, 1, 0)});
   }

   @EventHandler
   public void init(FMLInitializationEvent e) {
      NetworkRegistry.INSTANCE.registerGuiHandler(this, mcHandler);
      wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("MutantCreatures");
      wrapper.registerMessage(PacketSpawnParticles.Handler.class, PacketSpawnParticles.class, 0, Side.CLIENT);
      wrapper.registerMessage(PacketCMOptions.Handler.class, PacketCMOptions.class, 1, Side.SERVER);
      wrapper.registerMessage(PacketZombieAttack.Handler.class, PacketZombieAttack.class, 2, Side.CLIENT);
      wrapper.registerMessage(PacketSnowGolemAttack.Handler.class, PacketSnowGolemAttack.class, 3, Side.CLIENT);
      wrapper.registerMessage(PacketSnowGolemOwner.Handler.class, PacketSnowGolemOwner.class, 4, Side.CLIENT);
      wrapper.registerMessage(PacketEnderAttack.Handler.class, PacketEnderAttack.class, 5, Side.CLIENT);
      wrapper.registerMessage(PacketEnderBlock.Handler.class, PacketEnderBlock.class, 6, Side.CLIENT);
      wrapper.registerMessage(PacketEnderTeleport.Handler.class, PacketEnderTeleport.class, 7, Side.CLIENT);
      wrapper.registerMessage(PacketEnderTPlayer.Handler.class, PacketEnderTPlayer.class, 8, Side.CLIENT);
      wrapper.registerMessage(PacketEHandPos.Handler.class, PacketEHandPos.class, 9, Side.SERVER);
      wrapper.registerMessage(PacketSkeleAttack.Handler.class, PacketSkeleAttack.class, 10, Side.CLIENT);
      wrapper.registerMessage(PacketSpiderPigJump.Handler.class, PacketSpiderPigJump.class, 11, Side.SERVER);
      EntityRegistry.registerModEntity(MutantCreeper.class, "MutantCreeper", 1, this, 80, 3, true);
      EntityRegistry.registerModEntity(CreeperMinion.class, "CreeperMinion", 2, this, 80, 3, true);
      EntityRegistry.registerModEntity(CreeperMinionEgg.class, "CreeperMinionEgg", 3, this, 160, 20, true);
      EntityRegistry.registerModEntity(MutantZombie.class, "MutantZombie", 4, this, 80, 3, true);
      EntityRegistry.registerModEntity(Zombie.class, "ZombieMinion", 5, this, 80, 3, true);
      EntityRegistry.registerModEntity(MutantEnderman.class, "MutantEnderman", 6, this, 80, 3, true);
      EntityRegistry.registerModEntity(ChemicalXEntity.class, "Chemical X", 7, this, 160, 10, true);
      EntityRegistry.registerModEntity(SkullSpirit.class, "SkullSpirit", 8, this, 160, 20, false);
      EntityRegistry.registerModEntity(MutantSnowGolem.class, "MutantSnowGolem", 9, this, 80, 3, true);
      EntityRegistry.registerModEntity(SGolemBlock.class, "SnowGolemBlock", 10, this, 64, 10, true);
      EntityRegistry.registerModEntity(EnderBlock.class, "EnderBlock", 11, this, 64, 100, true);
      EntityRegistry.registerModEntity(EndermanClone.class, "EndermanClone", 12, this, 80, 3, true);
      EntityRegistry.registerModEntity(EndersoulFragment.class, "EndersoulFragment", 13, this, 64, 10, true);
      EntityRegistry.registerModEntity(MutantSkeleton.class, "MutantSkeleton", 14, this, 80, 3, true);
      EntityRegistry.registerModEntity(SkeletonShot.class, "SkeletonShot", 15, this, 80, 3, true);
      EntityRegistry.registerModEntity(SkeletonPart.class, "SkeletonPart", 16, this, 64, 10, true);
      EntityRegistry.registerModEntity(SpiderPig.class, "SpiderPig", 17, this, 80, 3, true);
      MCSpawnEgg.addEgg(MutantCreeper.class, 5349438, 11013646);
      MCSpawnEgg.addEgg(CreeperMinion.class, 894731, 12040119);
      MCSpawnEgg.addEgg(MutantZombie.class, 7969893, 44975);
      MCSpawnEgg.addEgg(MutantEnderman.class, 1447446, 8860812);
      MCSpawnEgg.addEgg(MutantSnowGolem.class, 15073279, 16753434);
      MCSpawnEgg.addEgg(MutantSkeleton.class, 12698049, 6310217);
      MCSpawnEgg.addEgg(SpiderPig.class, 3419431, 15771042);
      if (spawnCreeper) {
         EntityRegistry.addSpawn(MutantCreeper.class, 1, 1, 1, EnumCreatureType.monster, suitableBiomes);
      }

      if (spawnZombie) {
         EntityRegistry.addSpawn(MutantZombie.class, 1, 1, 1, EnumCreatureType.monster, suitableBiomes);
      }

      if (spawnEnderman) {
         EntityRegistry.addSpawn(MutantEnderman.class, 1, 1, 1, EnumCreatureType.monster, suitableBiomes);
      }

      if (spawnEndermanEnd) {
         EntityRegistry.addSpawn(MutantEnderman.class, 1, 1, 1, EnumCreatureType.monster, new BiomeGenBase[]{BiomeGenBase.sky});
      }

      if (spawnSkeleton) {
         EntityRegistry.addSpawn(MutantSkeleton.class, 1, 1, 1, EnumCreatureType.monster, suitableBiomes);
      }

      if (spawnSpiderPig) {
         EntityRegistry.addSpawn(SpiderPig.class, 2, 1, 3, EnumCreatureType.monster, suitableBiomes);
      }

      proxy.registerRenderers();
   }

   @EventHandler
   public void postInit(FMLPostInitializationEvent e) {
   }

   public static boolean isClient() {
      return FMLCommonHandler.instance().getSide().isClient();
   }

   public static boolean isEffectiveClient() {
      return FMLCommonHandler.instance().getEffectiveSide().isClient();
   }

   public static void onRenderTick() {
   }

   public static void onClientTick() {
   }

   public static void onServerTick() throws Exception {
      WorldServer[] worlds = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers;

      for(String name : HulkHammer.chunkList.keySet()) {
         ArrayList<ZombieChunk> chunkList = (ArrayList)HulkHammer.chunkList.get(name);
         EntityPlayer player = null;
         World worldObj = null;

         for(int i = 0; i < worlds.length; ++i) {
            player = worlds[i].getPlayerEntityByName(name);
            if (player != null) {
               worldObj = worlds[i];
               break;
            }
         }

         if (chunkList != null && !chunkList.isEmpty()) {
            while(chunkList.size() > 16) {
               chunkList.remove(0);
            }

            ZombieChunk chunk = (ZombieChunk)chunkList.remove(0);
            if (chunk.spawnParticles) {
               Block block = worldObj.getBlock(chunk.posX, chunk.posY, chunk.posZ);
               int data = worldObj.getBlockMetadata(chunk.posX, chunk.posY, chunk.posZ);
               worldObj.playAuxSFX(2001, chunk.posX, chunk.posY + 1, chunk.posZ, Block.getIdFromBlock(block) + (data << 12));
            }

            AxisAlignedBB box = AxisAlignedBB.getBoundingBox((double)chunk.posX, (double)(chunk.posY + 1), (double)chunk.posZ, (double)(chunk.posX + 1), (double)(chunk.posY + 2), (double)(chunk.posZ + 1));

            for(Entity entity : getCollidingEntities(player, worldObj, box)) {
               if (entity instanceof EntityLivingBase) {
                  EntityLivingBase living = (EntityLivingBase)entity;
                  living.attackEntityFrom(DamageSource.causeMobDamage(player), (float)(6 + rand.nextInt(3)));
               }
            }

            if (chunkList.isEmpty()) {
               HulkHammer.chunkList.remove(name);
            }
         }
      }

   }

   public static boolean getRandomSpawnChance() {
      int i = Math.max(1, spawnrate);
      i = Math.min(20, i);
      return rand.nextInt(50 / i) == 0;
   }

   public static void sendPacketToAll(EntityPlayer player, Packet packet) {
      if (!isEffectiveClient()) {
         ((EntityPlayerMP)player).mcServer.getConfigurationManager().sendPacketToAllPlayers(packet);
      }
   }

   @SideOnly(Side.CLIENT)
   public static boolean renderSmallEgg(RenderBlocks render, Block block, int x, int y, int z) {
      float fx = 0.0625F;
      float fy1 = 0.9375F;
      float fy2 = 1.0F;
      render.setRenderBounds((double)(0.5F - fx), (double)fy1, (double)(0.5F - fx), (double)(0.5F + fx), (double)fy2, (double)(0.5F + fx));
      render.renderStandardBlock(block, x, y, z);
      fx = 0.125F;
      fy1 = 0.5F;
      fy2 = 0.9375F;
      render.setRenderBounds((double)(0.5F - fx), (double)fy1, (double)(0.5F - fx), (double)(0.5F + fx), (double)fy2, (double)(0.5F + fx));
      render.renderStandardBlock(block, x, y, z);
      fx = 0.1875F;
      fy1 = 0.5625F;
      fy2 = 0.8125F;
      render.setRenderBounds((double)(0.5F - fx), (double)fy1, (double)(0.5F - fx), (double)(0.5F + fx), (double)fy2, (double)(0.5F + fx));
      render.renderStandardBlock(block, x, y, z);
      render.setRenderBounds((double)0.0F, (double)0.0F, (double)0.0F, (double)1.0F, (double)1.0F, (double)1.0F);
      return true;
   }

   @SideOnly(Side.CLIENT)
   public static void spawnParticlesAtEntity(Entity entity, String name, int amount) {
      for(int i = 0; i < amount; ++i) {
         double posX = entity.posX + (double)(rand.nextFloat() * entity.width * 2.0F) - (double)entity.width;
         double posY = entity.posY + (double)0.5F + (double)(rand.nextFloat() * entity.height);
         double posZ = entity.posZ + (double)(rand.nextFloat() * entity.width * 2.0F) - (double)entity.width;
         double x = rand.nextGaussian() * 0.02;
         double y = rand.nextGaussian() * 0.02;
         double z = rand.nextGaussian() * 0.02;
         entity.worldObj.spawnParticle(name, posX, posY, posZ, x, y, z);
      }

   }

   @SideOnly(Side.CLIENT)
   public static void spawnEnderParticles(Entity entity) {
      spawnEnderParticles(entity, 256, 1.8F);
   }

   @SideOnly(Side.CLIENT)
   public static void spawnEnderParticles(Entity entity, int amount, float speed) {
      EffectRenderer renderer = Minecraft.getMinecraft().effectRenderer;

      for(int i = 0; i < amount; ++i) {
         float f = (rand.nextFloat() - 0.5F) * speed;
         float f1 = (rand.nextFloat() - 0.5F) * speed;
         float f2 = (rand.nextFloat() - 0.5F) * speed;
         double tempX = entity.posX + (double)((rand.nextFloat() - 0.5F) * entity.width);
         double tempY = entity.posY + (double)((rand.nextFloat() - 0.5F) * entity.height) + (double)0.5F;
         double tempZ = entity.posZ + (double)((rand.nextFloat() - 0.5F) * entity.width);
         renderer.addEffect(new FXEnder(entity.worldObj, tempX, tempY, tempZ, (double)f, (double)f1, (double)f2, true));
      }

   }

   public static ArrayList<Entity> getCollidingEntities(Entity entity, World world, AxisAlignedBB box) {
      ArrayList<Entity> list = new ArrayList();
      List entities = world.getEntitiesWithinAABBExcludingEntity(entity, box.expand((double)4.0F, (double)4.0F, (double)4.0F));

      for(int i = 0; i < entities.size(); ++i) {
         Entity entity1 = (Entity)entities.get(i);
         AxisAlignedBB box1 = entity1.boundingBox;
         if (box1 != null && box.intersectsWith(box1)) {
            list.add(entity1);
         }
      }

      return list;
   }

   public static Vec3 getDirVector(float rotation, float scale) {
      float rad = rotation * ((float)Math.PI / 180F);
      return Vec3.createVectorHelper((double)(-MathHelper.sin(rad) * scale), (double)0.0F, (double)(MathHelper.cos(rad) * scale));
   }

   public static boolean teleportTo(EntityLivingBase living, double x, double y, double z) {
      return teleportTo(living, x, y, z, true);
   }

   public static boolean teleportTo(EntityLivingBase living, double x, double y, double z, boolean changeY) {
      double oldX = living.posX;
      double oldY = living.posY;
      double oldZ = living.posZ;
      int teleX = MathHelper.floor_double(x);
      int teleY = MathHelper.floor_double(y);
      int teleZ = MathHelper.floor_double(z);
      boolean success = false;
      if (living.worldObj.blockExists(teleX, teleY, teleZ)) {
         boolean temp = false;

         while(!temp && teleY > 0) {
            Block block = living.worldObj.getBlock(teleX, teleY - 1, teleZ);
            if (block != Blocks.air && block.getMaterial().blocksMovement()) {
               temp = true;
            } else if (changeY) {
               --teleY;
            }
         }

         if (temp || !changeY) {
            living.setPosition(x, (double)teleY, z);
            if (living.worldObj.getCollidingBoundingBoxes(living, living.boundingBox).isEmpty() && !living.worldObj.isAnyLiquid(living.boundingBox)) {
               success = true;
            }
         }
      }

      if (!success) {
         living.setPosition(oldX, oldY, oldZ);
         return false;
      } else {
         return true;
      }
   }

   public static void removeAttackers(EntityLiving living) {
      List list = living.worldObj.getEntitiesWithinAABB(EntityLiving.class, living.boundingBox.expand((double)16.0F, (double)10.0F, (double)16.0F));

      for(int i = 0; i < list.size(); ++i) {
         EntityLiving attacker = (EntityLiving)list.get(i);
         if (attacker != living && attacker.getAttackTarget() == living) {
            attacker.setAttackTarget((EntityLivingBase)null);
            attacker.setRevengeTarget((EntityLivingBase)null);
         }
      }

   }

   static {
      suitableBiomes = new BiomeGenBase[]{BiomeGenBase.plains, BiomeGenBase.desert, BiomeGenBase.extremeHills, BiomeGenBase.forest, BiomeGenBase.taiga, BiomeGenBase.swampland, BiomeGenBase.river, BiomeGenBase.frozenOcean, BiomeGenBase.frozenRiver, BiomeGenBase.icePlains, BiomeGenBase.iceMountains, BiomeGenBase.beach, BiomeGenBase.desertHills, BiomeGenBase.forestHills, BiomeGenBase.taigaHills, BiomeGenBase.extremeHillsEdge, BiomeGenBase.jungle, BiomeGenBase.jungleHills, BiomeGenBase.jungleEdge, BiomeGenBase.stoneBeach, BiomeGenBase.stoneBeach, BiomeGenBase.birchForest, BiomeGenBase.birchForestHills, BiomeGenBase.roofedForest, BiomeGenBase.coldTaiga, BiomeGenBase.coldTaigaHills, BiomeGenBase.megaTaiga, BiomeGenBase.megaTaigaHills, BiomeGenBase.extremeHillsPlus, BiomeGenBase.savanna, BiomeGenBase.savannaPlateau, BiomeGenBase.mesa, BiomeGenBase.mesaPlateau, BiomeGenBase.mesaPlateau_F};
      fTimer = new String[]{"field_71428_T", "timer", "S"};
      fThrower = new String[]{"field_70192_c", "thrower", "g"};
      fShowModel = new String[]{"field_78806_j", "showModel", "j"};
      fCubeList = new String[]{"field_78804_l", "cubeList", "l"};
      fChildModels = new String[]{"field_78805_m", "childModels", "m"};
      fItemInUse = new String[]{"field_71074_e", "itemInUse", "f"};
      fItemInUseCount = new String[]{"field_71072_f", "itemInUseCount", "g"};
      fIsInWeb = new String[]{"field_70134_J", "isInWeb", "H"};
   }
}
