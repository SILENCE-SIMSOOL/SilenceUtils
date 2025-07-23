package silence.simsool.handlers;

public class ClientHandler {

	private static Minecraft mc = Minecraft.getMinecraft();

	public static void createTitle(String title, String lore, int fadeIn, int time, int fadeOut) {
		mc.addScheduledTask(() -> {
			Utils.createTitle(title, lore, fadeIn, time, fadeOut);
		});
	}

	public static void sendPacket(Packet packet) {
		mc.addScheduledTask(() -> {
			Utils.sendPacket(packet);
		});
	}

	public static void playSound(String sound, float volume, float pitch) {
		mc.addScheduledTask(() -> {
			Utils.playSound(sound, volume, pitch);
		});
	}

}
