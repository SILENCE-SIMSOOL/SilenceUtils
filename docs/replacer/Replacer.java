package silence.simsool.mods.others.replacer;

import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import silence.simsool.config.Config;
import silence.simsool.events.PacketEvent;

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
