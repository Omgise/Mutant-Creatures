package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Timer;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import thehippomaster.MutantCreatures.ChemicalXEntity;
import thehippomaster.MutantCreatures.CommonProxy;
import thehippomaster.MutantCreatures.CreeperMinion;
import thehippomaster.MutantCreatures.CreeperMinionEgg;
import thehippomaster.MutantCreatures.EnderBlock;
import thehippomaster.MutantCreatures.EndermanClone;
import thehippomaster.MutantCreatures.EndersoulFragment;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.MutantCreeper;
import thehippomaster.MutantCreatures.MutantEnderman;
import thehippomaster.MutantCreatures.MutantSkeleton;
import thehippomaster.MutantCreatures.MutantSnowGolem;
import thehippomaster.MutantCreatures.MutantZombie;
import thehippomaster.MutantCreatures.SGolemBlock;
import thehippomaster.MutantCreatures.SkeletonPart;
import thehippomaster.MutantCreatures.SkeletonShot;
import thehippomaster.MutantCreatures.SkullSpirit;
import thehippomaster.MutantCreatures.SpiderPig;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
   public static boolean animatedPlayer = false;
   private static Timer mcTimer;
   private static ModelEnderHand handModel = new ModelEnderHand();
   private static ModelBiped skullHelmet;

   public boolean isClient() {
      return true;
   }

   public void registerRenderers() {
      mcTimer = (Timer)ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), MutantCreatures.fTimer);
      MinecraftForgeClient.registerItemRenderer(MutantCreatures.endersoulHand, (IItemRenderer)MutantCreatures.endersoulHand);
      this.addRenderer(ChemicalXEntity.class, new RenderMCItem(MutantCreatures.chemicalX, 0));
      this.addRenderer(CreeperMinionEgg.class, new RenderCreeperMinionEgg());
      this.addRenderer(SGolemBlock.class, new RenderSGolemBlock());
      this.addRenderer(EnderBlock.class, new RenderEnderBlock());
      this.addRenderer(EndersoulFragment.class, new RenderEndersoulFragment());
      this.addRenderer(SkeletonShot.class, new RenderSkeletonShot());
      this.addRenderer(SkeletonPart.class, new RenderSkeletonPart());
      this.addRenderer(MutantCreeper.class, new RenderMutantCreeper(new ModelMutantCreeper(), 1.5F));
      this.addRenderer(CreeperMinion.class, new RenderCreeperMinion());
      this.addRenderer(MutantZombie.class, new RenderMutantZombie(new ModelMutantZombie(), 1.5F));
      this.addRenderer(MutantEnderman.class, new RenderMutantEnderman(1.2F));
      this.addRenderer(EndermanClone.class, new RenderEndermanClone());
      this.addRenderer(MutantSnowGolem.class, new RenderMutantSnowGolem(0.7F));
      this.addRenderer(MutantSkeleton.class, new RenderMutantSkeleton(1.2F));
      this.addRenderer(SpiderPig.class, new RenderSpiderPig(0.9F));
   }

   private void addRenderer(Class<? extends Entity> class1, Render renderer) {
      RenderingRegistry.registerEntityRenderingHandler(class1, renderer);
   }

   public World getWorldClient() {
      return FMLClientHandler.instance().getWorldClient();
   }

   public EntityPlayer getClientPlayer() {
      return FMLClientHandler.instance().getClientPlayerEntity();
   }

   public float getPartialTicks() {
      return mcTimer.renderPartialTicks;
   }

   public void spawnChemicalXParticles(ChemicalXEntity entity) {
      EffectRenderer renderer = FMLClientHandler.instance().getClient().effectRenderer;

      for(int i = MutantCreatures.rand.nextInt(5); i < 50; ++i) {
         float x = (MutantCreatures.rand.nextFloat() - 0.5F) * 1.2F;
         float y = MutantCreatures.rand.nextFloat() * 0.2F;
         float z = (MutantCreatures.rand.nextFloat() - 0.5F) * 1.2F;
         renderer.addEffect(new FXSkull(entity.worldObj, entity.posX, entity.posY, entity.posZ, (double)x, (double)y, (double)z, true));
      }

   }

   public void spawnSkullParticles(SkullSpirit spirit, boolean flag) {
      EffectRenderer renderer = FMLClientHandler.instance().getClient().effectRenderer;
      if (flag) {
         for(int i = 0; i < 16; ++i) {
            float xx = (spirit.getRNG().nextFloat() - 0.5F) * 1.2F;
            float yy = (spirit.getRNG().nextFloat() - 0.5F) * 1.2F;
            float zz = (spirit.getRNG().nextFloat() - 0.5F) * 1.2F;
            renderer.addEffect(new FXSkull(spirit.worldObj, spirit.posX + (double)xx, spirit.posY + (double)yy, spirit.posZ + (double)zz, (double)0.0F, (double)0.0F, (double)0.0F, true));
         }
      } else {
         for(int i = 0; i < 3; ++i) {
            double posX = spirit.target.posX + (double)(spirit.getRNG().nextFloat() * spirit.target.width * 2.0F) - (double)spirit.target.width;
            double posY = spirit.target.posY + (double)0.5F + (double)(spirit.getRNG().nextFloat() * spirit.target.height);
            double posZ = spirit.target.posZ + (double)(spirit.getRNG().nextFloat() * spirit.target.width * 2.0F) - (double)spirit.target.width;
            double x = spirit.getRNG().nextGaussian() * 0.02;
            double y = spirit.getRNG().nextGaussian() * 0.02;
            double z = spirit.getRNG().nextGaussian() * 0.02;
            renderer.addEffect(new FXSkull(spirit.worldObj, posX, posY, posZ, x, y, z, true));
         }
      }

   }

   public void renderEnderHand() {
      handModel.render();
   }

   public void increaseBowSpeed(EntityPlayer player) {
      ItemStack bow = player.getItemInUse();
      int count = player.getItemInUseCount();
      if (bow != null && bow.getItem().getItemUseAction(bow) == EnumAction.bow && count > 4) {
         player.setItemInUse((ItemStack)null, 0);
         player.setItemInUse(bow, count - 3);
      }

   }

   public Object getSkeleArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
      if (skullHelmet == null) {
         skullHelmet = new ModelBiped(0.5F);
         skullHelmet.bipedHead.cubeList.clear();
         skullHelmet.bipedHeadwear.cubeList.clear();
         skullHelmet.bipedBody.cubeList.clear();
         skullHelmet.bipedRightArm.cubeList.clear();
         skullHelmet.bipedLeftArm.cubeList.clear();
         skullHelmet.bipedRightLeg.cubeList.clear();
         skullHelmet.bipedLeftLeg.cubeList.clear();
         skullHelmet.bipedHead.addChild(ModelMutantSkeleton.createSkull(skullHelmet));
      }

      skullHelmet.isSneak = entityLiving.isSneaking();
      skullHelmet.isRiding = entityLiving.isRiding();
      return armorSlot == 0 ? skullHelmet : null;
   }

   public boolean updateSpiderPigRider(SpiderPig pig, EntityPlayer rider) {
      EntityPlayer clientPlayer = Minecraft.getMinecraft().thePlayer;
      if (rider != clientPlayer) {
         return false;
      } else {
         EntityPlayerSP player = (EntityPlayerSP)rider;
         return player.movementInput.jump;
      }
   }
}
