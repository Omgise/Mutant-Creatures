package thehippomaster.MutantCreatures.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import thehippomaster.MutantCreatures.CreeperMinion;
import thehippomaster.MutantCreatures.MutantCreatures;
import thehippomaster.MutantCreatures.packet.PacketCMOptions;

@SideOnly(Side.CLIENT)
public class GuiCreeperMinion extends GuiScreen {
   private int xSize = 176;
   private int ySize = 166;
   private int guiX;
   private int guiY;
   private boolean editingName;
   private CreeperMinion creeperMinion;
   private String name;
   private boolean destroyBlocks;
   private boolean showName;
   private GuiButton nameButton;
   private GuiButton destroyBlocksButton;
   private GuiButton showNameButton;
   private GuiButton doneButton;
   private static final ResourceLocation texture = new ResourceLocation("MutantCreatures:textures/guiminion.png");

   private String getDestroyBlocks() {
      return this.destroyBlocks ? "Destroy Blocks: ON" : "Destroy Blocks: OFF";
   }

   private String getShowName() {
      return this.showName ? "Name: ON" : "Name: OFF";
   }

   public void initGui() {
      this.editingName = false;
      this.creeperMinion = CreeperMinion.minionObj;
      this.creeperMinion.setHealth((float)this.creeperMinion.getCurrentHealth());
      this.name = this.creeperMinion.getName();
      this.destroyBlocks = this.creeperMinion.getDestroyBlocks();
      this.showName = this.creeperMinion.getShowName();
      this.guiX = (this.width - this.xSize) / 2;
      this.guiY = (this.height - this.ySize) / 2;
      int buttonWidth = this.xSize / 2 - 10;
      this.nameButton = new GuiButton(0, this.guiX + 10, this.guiY + 10, this.xSize - 20, 20, this.name);
      this.destroyBlocksButton = new GuiButton(1, this.guiX + 8, this.guiY + this.ySize - 54, buttonWidth * 2 + 4, 20, this.getDestroyBlocks());
      this.showNameButton = new GuiButton(2, this.guiX + 8, this.guiY + this.ySize - 30, buttonWidth, 20, this.getShowName());
      this.doneButton = new GuiButton(3, this.guiX + this.xSize / 2 + 2, this.guiY + this.ySize - 30, buttonWidth, 20, "Done");
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.buttonList.add(this.nameButton);
      this.buttonList.add(this.destroyBlocksButton);
      this.buttonList.add(this.showNameButton);
      this.buttonList.add(this.doneButton);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      this.changeName();
   }

   public void updateScreen() {
      String s = this.name;
      if (this.editingName) {
         StringBuilder sb = new StringBuilder(s);
         sb.insert(0, ">").append("<");
         s = sb.toString();
      }

      this.nameButton.displayString = s;
   }

   private void changeName() {
      MutantCreatures.wrapper.sendToServer(new PacketCMOptions(0, this.creeperMinion, this.name));
   }

   protected void actionPerformed(GuiButton guibutton) {
      if (guibutton.enabled) {
         if (guibutton.id == 0) {
            this.editingName = !this.editingName;
            if (!this.editingName) {
               this.changeName();
            }
         } else if (guibutton.id == 1) {
            this.destroyBlocks = !this.destroyBlocks;
            MutantCreatures.wrapper.sendToServer(new PacketCMOptions(1, this.creeperMinion, this.destroyBlocks));
            this.destroyBlocksButton.displayString = this.getDestroyBlocks();
         } else if (guibutton.id == 2) {
            this.showName = !this.showName;
            MutantCreatures.wrapper.sendToServer(new PacketCMOptions(2, this.creeperMinion, this.showName));
            this.showNameButton.displayString = this.getShowName();
         } else if (guibutton.id == 3) {
            this.mc.thePlayer.closeScreen();
         }

      }
   }

   protected void keyTyped(char c, int i) {
      super.keyTyped(c, i);
      if (this.editingName) {
         if (i == 28) {
            this.editingName = false;
            this.changeName();
            return;
         }

         if (i == 14 && this.name.length() > 0) {
            this.name = this.name.substring(0, this.name.length() - 1);
         }

         if (ChatAllowedCharacters.isAllowedCharacter(c) && this.name.length() < 24) {
            this.name = this.name + c;
         }
      } else if (i == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
         this.mc.thePlayer.closeScreen();
      }

   }

   public void drawScreen(int i, int j, float f) {
      this.drawDefaultBackground();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture(texture);
      this.drawTexturedModalRect(this.guiX, this.guiY, 0, 0, this.xSize, this.ySize);
      int health = (int)(this.creeperMinion.getHealth() * 150.0F / this.creeperMinion.getMaxHealth());
      this.drawTexturedModalRect(this.guiX + 13, this.guiY + 36, 0, 166, health, 6);
      this.fontRendererObj.drawString("Health", this.guiX + 13, this.guiY + 54, 4210752);
      this.fontRendererObj.drawString("Explosion", this.guiX + 13, this.guiY + 74, 4210752);
      this.fontRendererObj.drawString("Blast Radius", this.guiX + 13, this.guiY + 94, 4210752);
      StringBuilder sb = new StringBuilder();
      sb.append(this.creeperMinion.getHealth() / 2.0F).append(" / ").append(this.creeperMinion.getMaxHealth() / 2.0F);
      this.drawCenteredString(this.fontRendererObj, sb.toString(), this.guiX + this.xSize / 2 + 38, this.guiY + 54, 16777215);
      this.drawCenteredString(this.fontRendererObj, this.creeperMinion.getContinuous() ? "Continuous" : "One-time", this.guiX + this.xSize / 2 + 38, this.guiY + 74, 16777215);
      int temp = (int)((this.creeperMinion.getBlastRadius() - 1.0F) * 10.0F);
      sb = (new StringBuilder()).append((float)temp / 10.0F);
      this.drawCenteredString(this.fontRendererObj, sb.toString(), this.guiX + this.xSize / 2 + 38, this.guiY + 94, 16777215);
      super.drawScreen(i, j, f);
   }
}
