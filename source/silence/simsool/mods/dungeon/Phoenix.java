package silence.simsool.mods.dungeon;

public class Phoenix {

	long time = -1L;
	Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onChat(ChatEvent event) {
		if (Config.PhoenixAlert) {
			if (event.chat.equals("Your Phoenix Pet saved you from certain death!")) {
				ClientHandler.playSound("random.orb", 1f, 2f);
				GuiManager.createTitle("§cPhoenix §fDead", 17);
				time = System.currentTimeMillis() + 60000;
			}
		}
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
		time = -1L;
	}

	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
		if (Config.PhoenixAlert) {
			int x = Main.hudManager.getXHUDItem("Phoenix");
			int y = Main.hudManager.getYHUDItem("Phoenix");

			if (isRenderScreen()) {
				if (time > System.currentTimeMillis()) {
					int left = (int) (time - System.currentTimeMillis()) / 1000;
					Utils.textRender("Time Left: §d" + left + "s", x + 23, y, true);
				} else Utils.textRender("Time Left: §aREADY", x + 23, y, true);
			}

			if (isRenderScreen() || mc.currentScreen instanceof HUDConfigScreen) {
				GlStateManager.color(1, 1, 1);
				mc.getTextureManager().bindTexture(new ResourceLocation("silenceutils:texture/icons/phoenix.png"));
				Gui.drawModalRectWithCustomSizedTexture(x, y - 9, 0, 0, 24, 24, 24, 24);
			}
		}
	}

	private boolean isRenderScreen() {
		return !Utils.isDebugScreen() && (Utils.isInDungeon() || IslandManager.getIsland().equals("Kuudra"));
	}

}
