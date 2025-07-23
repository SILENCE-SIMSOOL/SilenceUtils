package silence.simsool.utils;

public class UClick {

	private static Minecraft mc = Minecraft.getMinecraft();

	public static void leftClick(int slot) {
		ClientHandler.sendPacket(new C0EPacketClickWindow(mc.thePlayer.openContainer.windowId, slot, 0, 0, null, (short)0));
	}
	
	public static void rightClick(int slot) {
		ClientHandler.sendPacket(new C0EPacketClickWindow(mc.thePlayer.openContainer.windowId, slot, 1, 0, null, (short)0));
	}
	
	public static void leftClickAndClose(int slot) {
		ClientHandler.sendPacket(new C0EPacketClickWindow(mc.thePlayer.openContainer.windowId, slot, 0, 0, null, (short)0));
		mc.thePlayer.closeScreen();
	}
	
	public static void rightClickAndClose(int slot) {
		ClientHandler.sendPacket(new C0EPacketClickWindow(mc.thePlayer.openContainer.windowId, slot, 1, 0, null, (short)0));
		mc.thePlayer.closeScreen();
	}

}
