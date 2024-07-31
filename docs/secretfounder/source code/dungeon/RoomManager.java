package silence.simsool.mods.others.secretfounder.dungeon;

import static silence.simsool.mods.others.secretfounder.dungeon.DungeonManager.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.gson.JsonObject;

import gg.essential.universal.UChat;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import silence.simsool.config.Config;
import silence.simsool.mods.others.secretfounder.SecretFounder;
import silence.simsool.mods.others.secretfounder.utils.MapUtils;
import silence.simsool.mods.others.secretfounder.utils.RoomUtils;
import silence.simsool.mods.others.secretfounder.utils.SFUtils;

public class RoomManager {

	static int stage2Ticks = 0;
	static ExecutorService stage2Executor;
	public static List<Point> currentMapSegments;
	public static List<Point> currentPhysicalSegments;
	public static String roomSize = "undefined";
	public static String roomColor = "undefined";
	public static String roomCategory = "undefined";
	public static String roomName = "undefined";
	public static String roomDirection = "undefined";
	public static Point roomCorner;
	public static HashSet<BlockPos> currentScannedBlocks = new HashSet<>();
	public static HashMap<BlockPos, Integer> blocksToCheck = new HashMap<>();
	public static int totalBlocksAvailableToCheck = 0;
	public static List<BlockPos> blocksUsed = new ArrayList<>();
	static Future<HashMap<String, List<String>>> futureUpdatePossibleRooms;
	public static HashMap<String, List<String>> possibleRooms;
	static long incompleteScan = 0;
	static long redoScan = 0;
	static int entranceMapNullCount = 0;
	static int tickAmount = 1;

	Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if ((Config.SecretFounder || Config.FixTeleport) && event.phase == TickEvent.Phase.START && SFUtils.inCatacombs) {
			if (gameStage == 2) {
				if (mapId == null && extractMapId() == null) return;
				stage2Ticks++;
				int t = (Config.RoomDetectPerformance == 0) ? 26 : 13;
				if (Config.RoomDetectPerformance == 2) t = 7;
				if (stage2Ticks > t) {
					stage2Ticks = 0;
					if (stage2Executor == null || stage2Executor.isTerminated()) stage2Executor = Executors.newSingleThreadExecutor();
					if (entranceMapCorners == null) {
						map = MapUtils.updatedMap();
						entranceMapCorners = MapUtils.entranceMapCorners(map);
					}
					else if (entranceMapCorners[0] == null || entranceMapCorners[1] == null) {
						entranceMapNullCount++;
						entranceMapCorners = null;
						if (entranceMapNullCount == 8) UChat.chat(SecretFounder.PREFIX + " Can't read your map. Try change texture pack.");
					}
					else if (entrancePhysicalNWCorner == null) {
						Point playerMarkerPos = MapUtils.playerMarkerPos(); if (playerMarkerPos == null) return;
						Point closestNWMapCorner = MapUtils.getClosestNWMapCorner(playerMarkerPos, entranceMapCorners[0], entranceMapCorners[1]);
						if (MapUtils.getMapColor(playerMarkerPos, map).equals("green") && MapUtils.getMapColor(closestNWMapCorner, map).equals("green")) {
							if (!mc.thePlayer.getPositionVector().equals(new Vec3(0.0D, 0.0D, 0.0D))) entrancePhysicalNWCorner = MapUtils.getClosestNWPhysicalCorner(mc.thePlayer.getPositionVector());
						} else UChat.chat(SecretFounder.PREFIX + " Can't read your map. Try go back into middle of the Entrance Room.");
					} 
					else {
						Point currentPhysicalCorner = MapUtils.getClosestNWPhysicalCorner(mc.thePlayer.getPositionVector());
						if (currentPhysicalSegments != null && !currentPhysicalSegments.contains(currentPhysicalCorner)) resetCurrentRoom();
						else if (incompleteScan != 0 && System.currentTimeMillis() > incompleteScan) {
							incompleteScan = 0;
							raytraceBlocks();
						} else if (redoScan != 0 && System.currentTimeMillis() > redoScan) {
							redoScan = 0;
							possibleRooms = null;
							raytraceBlocks();
						}
						if (currentPhysicalSegments == null || currentMapSegments == null || roomSize.equals("undefined") || roomColor.equals("undefined")) {
							updateCurrentRoom();
							if (!roomColor.equals("undefined")) {
								switch (roomColor) {
								case "brown":
								case "purple":
								case "orange":
									raytraceBlocks();
									break;
								case "yellow":
									roomName = "Miniboss Room";
									newRoom();
									break;
								case "green":
									roomName = "Entrance Room";
									newRoom();
									break;
								case "pink":
									roomName = "Fairy Room";
									newRoom();
									break;
								case "red":
									roomName = "Blood Room";
									newRoom();
									break;
								default:
									roomName = "undefined";
									break;
								}
							}
						}
					}
				}
				if (stage2Ticks > 2 && futureUpdatePossibleRooms != null && futureUpdatePossibleRooms.isDone()) {
					try {
						possibleRooms = futureUpdatePossibleRooms.get();
						futureUpdatePossibleRooms = null;
						TreeSet<String> possibleRoomsSet = new TreeSet<>();
						String tempDirection = "undefined";
						for (Map.Entry<String, List<String>> entry : possibleRooms.entrySet()) {
							List<String> possibleRoomList = entry.getValue();
							if (!possibleRoomList.isEmpty()) tempDirection = entry.getKey();
							possibleRoomsSet.addAll(possibleRoomList);
						}
						if (possibleRoomsSet.size() == 0) redoScan = System.currentTimeMillis() + 5000;
						else if (possibleRoomsSet.size() == 1) {
							roomName = possibleRoomsSet.first();
							roomDirection = tempDirection;
							roomCorner = MapUtils.getPhysicalCornerPos(roomDirection, currentPhysicalSegments);
							newRoom();
						} else
							incompleteScan = System.currentTimeMillis() + 1000;
					} catch (ExecutionException | InterruptedException e) { e.printStackTrace(); }
				}
			}

		}
	}

	void updateCurrentRoom() {
		map = MapUtils.updatedMap(); if (map == null) return;
		Point currentPhysicalCorner = MapUtils.getClosestNWPhysicalCorner(mc.thePlayer.getPositionVector());
		Point currentMapCorner = MapUtils.physicalToMapCorner(currentPhysicalCorner, entrancePhysicalNWCorner, entranceMapCorners[0], entranceMapCorners[1]);
		roomColor = MapUtils.getMapColor(currentMapCorner, map);
		if (roomColor.equals("undefined")) return;
		currentMapSegments = MapUtils.neighboringSegments(currentMapCorner, map, entranceMapCorners[0], entranceMapCorners[1], new ArrayList<>());
		currentPhysicalSegments = new ArrayList<>();
		for (Point mapCorner : currentMapSegments) currentPhysicalSegments.add(MapUtils.mapToPhysicalCorner(mapCorner, entrancePhysicalNWCorner, entranceMapCorners[0], entranceMapCorners[1]));
		roomSize = MapUtils.roomSize(currentMapSegments);
		roomCategory = MapUtils.roomCategory(roomSize, roomColor);
	}

	public static void resetCurrentRoom() {
		WaypointManager.allFound = false;
		currentPhysicalSegments = null;
		currentMapSegments = null;
		roomSize = "undefined";
		roomColor = "undefined";
		roomCategory = "undefined";
		roomName = "undefined";
		roomDirection = "undefined";
		roomCorner = null;
		currentScannedBlocks = new HashSet<>();
		blocksToCheck = new HashMap<>();
		totalBlocksAvailableToCheck = 0;
		blocksUsed = new ArrayList<>();
		futureUpdatePossibleRooms = null;
		possibleRooms = null;
		incompleteScan = 0;
		redoScan = 0;
		WaypointManager.secretNum = 0;
	}

	public static Integer extractMapId() {
		if (!MapUtils.mapExists()) return null;
		ItemStack mapSlot = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(8);
		return mapId = mapSlot.getMetadata();
	}

	public static void newRoom() {
		if (!roomName.equals("undefined") && !roomCategory.equals("undefined")) {
			if (SecretFounder.roomsJson.get(roomName) != null) {
				WaypointManager.secretNum = SecretFounder.roomsJson.get(roomName).getAsJsonObject().get("secrets").getAsInt();
				WaypointManager.allSecretsMap.putIfAbsent(roomName, new ArrayList<>(Collections.nCopies(WaypointManager.secretNum, true)));
			} else {
				WaypointManager.secretNum = 0;
				WaypointManager.allSecretsMap.putIfAbsent(roomName, new ArrayList<>(Collections.nCopies(0, true)));
			}
			WaypointManager.secretsList = WaypointManager.allSecretsMap.get(roomName);
		}
	}

	void raytraceBlocks() {
		long timeStart = System.currentTimeMillis();
		List<Vec3> vecList = RoomUtils.vectorsToRaytrace(24); // actually creates 24^2 = 576 raytrace vectors
		Vec3 eyes = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
		for (Vec3 vec : vecList) {
			MovingObjectPosition raytraceResult = mc.thePlayer.getEntityWorld().rayTraceBlocks(eyes, vec, false, false, true);
			if (raytraceResult != null && raytraceResult.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				BlockPos raytracedBlockPos = raytraceResult.getBlockPos();
				if (currentScannedBlocks.contains(raytracedBlockPos)) continue;
				currentScannedBlocks.add(raytracedBlockPos);
				if (!currentPhysicalSegments.contains(MapUtils.getClosestNWPhysicalCorner(raytracedBlockPos))) continue;
				if (RoomUtils.blockPartOfDoorway(raytracedBlockPos)) continue;
				IBlockState hitBlock = mc.theWorld.getBlockState(raytracedBlockPos);
				int identifier = Block.getIdFromBlock(hitBlock.getBlock()) * 100 + hitBlock.getBlock().damageDropped(hitBlock);
				if (RoomUtils.whitelistedBlocks.contains(identifier)) blocksToCheck.put(raytracedBlockPos, identifier);
			}
		}
		if (futureUpdatePossibleRooms == null && stage2Executor != null && !stage2Executor.isTerminated()) futureUpdatePossibleRooms = getPossibleRooms();
	}

	Future<HashMap<String, List<String>>> getPossibleRooms() {
		return stage2Executor.submit(() -> {
			long timeStart = System.currentTimeMillis();
			HashMap<String, List<String>> updatedPossibleRooms;
			List<String> possibleDirections;
			if (possibleRooms == null) {
				possibleDirections = MapUtils.possibleDirections(roomSize, currentMapSegments);
				updatedPossibleRooms = new HashMap<>();
				for (String direction : possibleDirections) updatedPossibleRooms.put(direction, new ArrayList<>(SecretFounder.ROOM_DATA.get(roomCategory).keySet()));
			}
			else {
				updatedPossibleRooms = possibleRooms;
				possibleDirections = new ArrayList<>(possibleRooms.keySet());
			}
			HashMap<String, Point> directionCorners = new HashMap<>();
			for (String direction : possibleDirections) directionCorners.put(direction, MapUtils.getPhysicalCornerPos(direction, currentPhysicalSegments));
			List<BlockPos> blocksChecked = new ArrayList<>();
			int doubleCheckedBlocks = 0;
			for (Map.Entry<BlockPos, Integer> entry : blocksToCheck.entrySet()) {
				BlockPos blockPos = entry.getKey();
				int combinedMatchingRooms = 0;
				for (String direction : possibleDirections) {
					BlockPos relative = MapUtils.actualToRelative(blockPos, direction, directionCorners.get(direction));
					long idToCheck = SFUtils.shortToLong((short) relative.getX(), (short) relative.getY(), (short) relative.getZ(), entry.getValue().shortValue());
					List<String> matchingRooms = new ArrayList<>();
					for (String roomName : updatedPossibleRooms.get(direction)) {
						int index = Arrays.binarySearch(SecretFounder.ROOM_DATA.get(roomCategory).get(roomName), idToCheck);
						if (index > -1) matchingRooms.add(roomName);
					}
					combinedMatchingRooms += matchingRooms.size();
					updatedPossibleRooms.put(direction, matchingRooms);
				}
				blocksChecked.add(blockPos);
				if (combinedMatchingRooms == 0) break;
				if (combinedMatchingRooms == 1) {
					if (doubleCheckedBlocks >= 10) break;
					doubleCheckedBlocks++;
				}
			}
			blocksUsed.addAll(blocksChecked);
			totalBlocksAvailableToCheck += blocksToCheck.size();
			blocksToCheck = new HashMap<>();
			long timeFinish = System.currentTimeMillis();
			return updatedPossibleRooms;
		});
	}
}