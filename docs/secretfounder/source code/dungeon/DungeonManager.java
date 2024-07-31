package silence.simsool.mods.others.secretfounder.dungeon;

import java.awt.Point;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import silence.simsool.config.Config;
import silence.simsool.mods.others.secretfounder.utils.MapUtils;
import silence.simsool.mods.others.secretfounder.utils.SFUtils;

public class DungeonManager {
	public static int gameStage = 0;
	public static Integer[][] map;
	public static Point[] entranceMapCorners;
	public static Point entrancePhysicalNWCorner;
	public static Integer mapId;
	long bloodTime = Long.MAX_VALUE;
	static int tickAmount = 0;
	boolean oddRun = true;

	Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
	public void onReceiveChat(ClientChatReceivedEvent event) {
		if (!SFUtils.inCatacombs) return;
		String message = event.message.getFormattedText();
		if (message.startsWith("§e[NPC] §bMort§f: §rHere, I found this map when I first entered the dungeon.§r")) gameStage = 2;
		else if (message.startsWith("§r§c[BOSS] The Watcher§r§f: You have proven yourself. You may pass.§r")) bloodTime = System.currentTimeMillis() + 5000;
		else if (System.currentTimeMillis() > bloodTime && ((message.startsWith("§r§c[BOSS] ") && !message.contains(" The Watcher§r§f:")) || message.startsWith("§r§4[BOSS] "))) {
			if (gameStage != 3) {
				gameStage = 3;
				RoomManager.resetCurrentRoom();
				RoomManager.roomName = "Boss Room";
				RoomManager.roomCategory = "General";
			}
		} else if (message.contains("§r§c☠ §r§eDefeated §r")) {
			gameStage = 4;
			RoomManager.resetCurrentRoom();
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (!SFUtils.inCatacombs || event.phase != TickEvent.Phase.START || mc.thePlayer == null) return;
		tickAmount++;
		int t = (Config.RoomDetectPerformance == 0) ? 10 : 8;
		if (Config.RoomDetectPerformance == 2) t = 6;
		if (tickAmount > t) {
			tickAmount = 0;
			if (gameStage == 0 || gameStage == 1) {
				if (gameStage == 0) gameStage = 1;
				if (MapUtils.mapExists()) gameStage = 2;
				else if (gameStage == 1 && entrancePhysicalNWCorner == null && mc.thePlayer.getPositionVector() != null) {
					if (!mc.thePlayer.getPositionVector().equals(new Vec3(0.0D, 0.0D, 0.0D))) entrancePhysicalNWCorner = MapUtils.getClosestNWPhysicalCorner(mc.thePlayer.getPositionVector());
				}
			}
		}
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
		gameStage = 0;
		mapId = null;
		map = null;
		entranceMapCorners = null;
		entrancePhysicalNWCorner = null;
		RoomManager.entranceMapNullCount = 0;
		bloodTime = Long.MAX_VALUE;
		if (RoomManager.stage2Executor != null) RoomManager.stage2Executor.shutdown();
		WaypointManager.allSecretsMap.clear();
		RoomManager.resetCurrentRoom();
	}
}
