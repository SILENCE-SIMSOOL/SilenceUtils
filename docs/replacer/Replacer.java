package silence.simsool.mods.others.replacer;

public class Replacer {
	@SubscribeEvent
	public void onPacketReceive(PacketEvent.ReceiveEvent event) {
		if (Config.Replacer && event.packet instanceof S2FPacketSetSlot) {
			S2FPacketSetSlot packet = (S2FPacketSetSlot) event.packet;
			ItemStack item = packet.func_149174_e(); if (item == null) return;
			String iName = packet.func_149174_e().getDisplayName();
			if (iName.contains("Drill") || iName.contains("Gemstone Gauntlet") || iName.contains("Pickonimbus")) event.setCanceled(true);
		}
	}
}
