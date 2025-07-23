package silence.simsool.mods.dungeon;

public class BloodroomAlert {
	
	int TIMER = 0;
	Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onChat(ChatEvent event) {
		if (Config.BloodroomAlert && event.chat.equals("[BOSS] The Watcher: Let's see how you can handle this.")) {
			TIMER = 60 - (Config.SetupPing / 50) - 4;
		}
	}
	
	@SubscribeEvent
	public void onServerTick(ServerTickEvent event) {
		if (Config.BloodroomAlert && TIMER > 0) {
			TIMER--;
			if (TIMER == 0) {
				GuiManager.createTitle(getMessage(), 20);
				ClientHandler.playSound("note.pling", 10.0f, 0.8f);
			}
		}
	}
	
	@SubscribeEvent
	public void onUnload(WorldEvent.Unload event) {
		TIMER = 0;
	}
	
	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
		if (Config.BloodroomAlert && TIMER > 0 && DungeonRoomManager.getType().equals("BLOOD")) {
			ScaledResolution sr = event.resolution;
			int x = sr.getScaledWidth();
			int y = sr.getScaledHeight();

			String text = "§e" + String.format("%." + Config.DecimalType + "f", (double) (TIMER / 20.0)) + "s";
			int textWidth = mc.fontRendererObj.getStringWidth(text);

			mc.fontRendererObj.drawString(text, (x - textWidth) / 2, ((y - mc.fontRendererObj.FONT_HEIGHT) / 2) + 20, 0xFFFFFF, true);
		}
	}
	
	private String getMessage() {
		return Config.BloodroomAlertMessage.replaceAll("&", "§");
	}

}
