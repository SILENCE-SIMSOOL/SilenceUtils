package silence.simsool.mods.misc;

public class InventoryButton {

	Button[] buttons = new Button[6];
	Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onGuiScreenInitGui(GuiEvent.InitGuiEvent.Post event) {
		if (Config.InventoryButton && event.type == GuiType.INVENTORY) {
			int width = event.width2;
			int height = event.height2;
			buttons[0] = new Button(width - 5,  height - 19, width + 10, height - 3, () -> UChat.say("/pet"));
			buttons[1] = new Button(width + 14, height - 19, width + 30, height - 3, () -> UChat.say("/equipment"));
			buttons[2] = new Button(width + 34, height - 19, width + 50, height - 3, () -> UChat.say("/wardrobe"));
			buttons[3] = new Button(width + 54, height - 19, width + 70, height - 3, () -> UChat.say((Config.InventoryButtonChestType == 0 ? "/bp " : "/ec ") + Config.InventoryButtonChestNumber));
			buttons[4] = new Button(width + 72, height - 19, width + 79, height - 12,() -> Config.InventoryButtonChestNumber = Config.InventoryButtonChestType == 0 ? Config.InventoryButtonChestNumber < 18 ? Config.InventoryButtonChestNumber + 1 : 1 : Config.InventoryButtonChestNumber < 9 ? Config.InventoryButtonChestNumber + 1 : 1);
			buttons[5] = new Button(width + 72, height - 10, width + 79, height - 3, () -> Config.InventoryButtonChestNumber = Config.InventoryButtonChestType == 0 ? Config.InventoryButtonChestNumber > 1 ? Config.InventoryButtonChestNumber - 1 : 18 : Config.InventoryButtonChestNumber > 1 ? Config.InventoryButtonChestNumber - 1 : 9);
		}
	}

	@SubscribeEvent
	public void onGuiScreenDrawScreen(GuiEvent.DrawScreenEvent.Post event) {
		if (Config.InventoryButton && event.type == GuiType.INVENTORY) {
			GlStateManager.pushMatrix();
			GlStateManager.alphaFunc(516, 0.01F);
			for (Button button : buttons) Gui.drawRect(button.left, button.top, button.right, button.bottom, Config.InventoryButtonColor.getRGB());

			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.color(2f, 2f, 2f);
			drawImage("pet", buttons[0], 1, 1, 0, 0, 14, 14);
			drawImage("equipment", buttons[1], 1, 1, 0, 0, 14, 14);
			drawImage("wardrobe", buttons[2], 0, 0, 0, 0, 16, 16);
			if (Config.InventoryButtonChestType == 0) drawImage("chest", buttons[3], 0, 0, 2, 4, 20, 20);
			else drawImage("enderchest", buttons[3], 0, 0, 0, 0, 16, 16);
			drawImage("up", buttons[4], 1, 1, 0, 0, 5, 5);
			drawImage("down", buttons[5], 1, 1, 0, 0, 5, 5);
			mc.fontRendererObj.drawString(String.valueOf(Config.InventoryButtonChestNumber), buttons[3].left + (17 - mc.fontRendererObj.getStringWidth(String.valueOf(Config.InventoryButtonChestNumber))) / 2, buttons[3].top + 4, Color.WHITE.getRGB(), true);
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}
	
	@SubscribeEvent
	public void onGuiScreenMouseInput(GuiEvent.MouseInputEvent.Pre event) {
		if (Config.InventoryButton && event.type == GuiType.INVENTORY) {
			int mouseX = event.mouseX;
			int mouseY = mc.displayHeight - event.mouseY;

			for (Button button : buttons) {
				if (Mouse.getEventButtonState()) {
					if (Utils.isInside(mouseX, mouseY, button.left, button.top, button.right, button.bottom)) {
						if (Mouse.getEventButton() == 0) {
							if (button == buttons[4] || button == buttons[5]) ClientHandler.playSound("random.click", 1.0F, 1.0F);
							button.runnable.run();
						}
						else if (button == buttons[3] && Mouse.getEventButton() == 1) {
							if (Config.InventoryButtonChestType == 0) Config.InventoryButtonChestType = 1;
							else Config.InventoryButtonChestType = 0;
							Config.InventoryButtonChestNumber = 1;
						}
					}
				}
			}
		}
	}

	private void drawImage(String name, Button button, int x, int y, int u, int v, int width, int height) {
		mc.getTextureManager().bindTexture(new ResourceLocation("silenceutils:texture/inventorybutton/" + name + ".png"));
		Gui.drawModalRectWithCustomSizedTexture(button.left + x, button.top + y, u, v, width, height, width, height);
	}

	private static class Button {
		public int left;
		public int top;
		public int right;
		public int bottom;
		public Runnable runnable;

		public Button(int left, int top, int right, int bottom, Runnable runnable) {
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
			this.runnable = runnable;
		}
	}

}
