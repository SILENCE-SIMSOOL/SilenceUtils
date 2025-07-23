package silence.simsool.mods.dungeon;

public class SpiritMask {

	long time = -1L;
	Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onChat(ChatEvent event) {
		if (Config.SpiritMaskAlert) {
	        if (event.chat.equals("Second Wind Activated! Your Spirit Mask saved your life!")) {
	        	ClientHandler.playSound("random.orb", 1, 2f);
	        	GuiManager.createTitle("§dSpirit Mask §fBroken", 17);
	        	ItemStack helmet = mc.thePlayer.getCurrentArmor(3);
	        	if (helmet != null) {
	        		for (String lore : helmet.getTooltip(mc.thePlayer, false)) {
	            		if (lore.startsWith("§5§o§8Cooldown: §a")) {
	            			lore = lore.replace("§5§o§8Cooldown: §a", "");
	            			lore = lore.replace("s", "");
	            			time = System.currentTimeMillis() + Integer.parseInt(lore) * 1000;
	            			return;
	            		}
	            	}
	        	}
	        	time = System.currentTimeMillis() + 30000;
	        }
		}
	}

	@SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
		time = -1L;
	}

	@SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
		if (Config.SpiritMaskAlert) {
			int x = Main.hudManager.getXHUDItem("Spirit Mask");
			int y = Main.hudManager.getYHUDItem("Spirit Mask");
			
			if (isRenderScreen()) {
				if (time > System.currentTimeMillis()) {
					int left = (int) (time - System.currentTimeMillis()) / 1000;
					Utils.textRender("Time Left: §d" + left + "s" , x + 23, y, true);
				} else Utils.textRender("Time Left: §aREADY" , x + 23, y, true);
			}
			
			if (isRenderScreen() || mc.currentScreen instanceof HUDConfigScreen) {
				GlStateManager.color(1, 1, 1);
				mc.getTextureManager().bindTexture(new ResourceLocation("silenceutils:texture/icons/spiritmask.png"));
				Gui.drawModalRectWithCustomSizedTexture(x, y - 9, 0, 0, 24, 24, 24, 24);
			}
		}
	}
	
	private boolean isRenderScreen() {
		return !Utils.isDebugScreen() && (Utils.isInDungeon() || IslandManager.getIsland().equals("Kuudra"));
	}

}
