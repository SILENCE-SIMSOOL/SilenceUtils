package silence.simsool.mods.others.secretfounder.commands;

import java.util.Collections;
import java.util.List;

import gg.essential.universal.UChat;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import silence.simsool.mods.others.secretfounder.SecretFounder;
import silence.simsool.mods.others.secretfounder.dungeon.RoomManager;
import silence.simsool.mods.others.secretfounder.dungeon.WaypointManager;
import silence.simsool.mods.others.secretfounder.utils.SFUtils;

public class Room extends CommandBase {

	Minecraft mc = Minecraft.getMinecraft();

	@Override
	public String getCommandName() {
		return "room";
	}

	@Override
	public String getCommandUsage(ICommandSender arg0) {
		return "/" + getCommandName();
	}

	@Override
	public List<String> getCommandAliases() {
		return Collections.singletonList("rooms");
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length < 1) {
			UChat.chat(SecretFounder.PREFIX + " Your Room is §a" + RoomManager.roomName);
		}

//      else {
//        	
//        	if (args[0].equals("reload")) {
//                
//                List<Path> paths = Utils.getAllPaths("catacombs");
//                final ExecutorService executor = Executors.newFixedThreadPool(4);
//                Future<HashMap<String, long[]>> future1x1 = executor.submit(() -> Utils.pathsToRoomData("1x1", paths));
//                Future<HashMap<String, long[]>> future1x2 = executor.submit(() -> Utils.pathsToRoomData("1x2", paths));
//                Future<HashMap<String, long[]>> future1x3 = executor.submit(() -> Utils.pathsToRoomData("1x3", paths));
//                Future<HashMap<String, long[]>> future1x4 = executor.submit(() -> Utils.pathsToRoomData("1x4", paths));
//                Future<HashMap<String, long[]>> future2x2 = executor.submit(() -> Utils.pathsToRoomData("2x2", paths));
//                Future<HashMap<String, long[]>> futureLShape = executor.submit(() -> Utils.pathsToRoomData("L-shape", paths));
//                Future<HashMap<String, long[]>> futurePuzzle = executor.submit(() -> Utils.pathsToRoomData("Puzzle", paths));
//                Future<HashMap<String, long[]>> futureTrap = executor.submit(() -> Utils.pathsToRoomData("Trap", paths));
//                ConfigHandler.reloadConfig();
//
//                try (BufferedReader roomsReader = new BufferedReader(new InputStreamReader(new FileInputStream("D:/SILENCE/dungeonrooms.json")));
//                	BufferedReader waypointsReader = new BufferedReader(new InputStreamReader(new FileInputStream("D:/SILENCE/secretlocations.json")));
//                ) {
//                    Gson gson = new Gson();
//                    SecretFounder.roomsJson = gson.fromJson(roomsReader, JsonObject.class);
//                    SecretFounder.waypointsJson = gson.fromJson(waypointsReader, JsonObject.class);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    SecretFounder.ROOM_DATA.put("1x1", future1x1.get());
//                    SecretFounder.ROOM_DATA.put("1x2", future1x2.get());
//                    SecretFounder.ROOM_DATA.put("1x3", future1x3.get());
//                    SecretFounder.ROOM_DATA.put("1x4", future1x4.get());
//                    SecretFounder.ROOM_DATA.put("2x2", future2x2.get());
//                    SecretFounder.ROOM_DATA.put("L-shape", futureLShape.get());
//                    SecretFounder.ROOM_DATA.put("Puzzle", futurePuzzle.get());
//                    SecretFounder.ROOM_DATA.put("Trap", futureTrap.get());
//                } catch (ExecutionException | InterruptedException e) {
//                    e.printStackTrace();
//                }
//                executor.shutdown();
//                player.addChatMessage(new ChatComponentText("[SecretFounder]: Reloaded"));
//        		return;
//        	}
//        	
//        	switch (args[0].toLowerCase()) {
//        	
//	        	case "add":
//	                World world = mc.theWorld;
//	                if (!Utils.inCatacombs || DungeonManager.gameStage != 2 || RoomDetection.roomDirection.equals("undefined") || RoomDetection.roomCorner == null) {
//	                    player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED
//	                            + "Dungeon Rooms: Current dungeon room is undefined"));
//	                    return;
//	                }
//	                switch (args[1].toLowerCase()) {
//	                    case "chest":
//	                    case "wither":
//	                    case "lever":
//	                    case "bat":
//	                    case "item":
//	                    case "superboom":
//	                    case "tnt":
//	                    case "aotv":
//	                    	
//	                        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && mc.objectMouseOver.getBlockPos() != null) {
//	                            BlockPos viewingPos = MapUtils.actualToRelative(mc.objectMouseOver.getBlockPos(), RoomDetection.roomDirection, RoomDetection.roomCorner);
//	                            String TypeName = Character.toUpperCase(args[1].charAt(0)) + args[1].substring(1);
//	                            if (args[1].equals("tnt")) {
//	                            	player.addChatMessage(new ChatComponentText("{\n" +
//		                                    "  \"secretName\":\"# - Superboom\",\n" +
//		                                    "  \"category\":\"superboom\",\n" +
//		                                    "  \"x\":" + viewingPos.getX() + ",\n" +
//		                                    "  \"y\":" + viewingPos.getY() + ",\n" +
//		                                    "  \"z\":" + viewingPos.getZ() + "\n" +
//		                                    "}"));
//		                            Toolkit.getDefaultToolkit()
//		                                    .getSystemClipboard()
//		                                    .setContents(
//		                                            new StringSelection("{\n" +
//		                                            		"  \"secretName\":\"# - Superboom\",\n" +
//		        		                                    "  \"category\":\"superboom\",\n" +
//		        		                                    "  \"x\":" + viewingPos.getX() + ",\n" +
//		        		                                    "  \"y\":" + viewingPos.getY() + ",\n" +
//		        		                                    "  \"z\":" + viewingPos.getZ() + "\n" +
//		                                                    "}"),
//		                                            null
//		                                    );
//	                            }
//	                            player.addChatMessage(new ChatComponentText("{\n" +
//	                                    "  \"secretName\":\"# - " + TypeName + "\",\n" +
//	                                    "  \"category\":\"" + args[1] + "\",\n" +
//	                                    "  \"x\":" + viewingPos.getX() + ",\n" +
//	                                    "  \"y\":" + viewingPos.getY() + ",\n" +
//	                                    "  \"z\":" + viewingPos.getZ() + "\n" +
//	                                    "}"));
//	                            Toolkit.getDefaultToolkit()
//	                                    .getSystemClipboard()
//	                                    .setContents(
//	                                            new StringSelection("{\n" +
//	            	                                    "  \"secretName\":\"# - " + TypeName + "\",\n" +
//	            	                                    "  \"category\":\"" + args[1] + "\",\n" +
//	            	                                    "  \"x\":" + viewingPos.getX() + ",\n" +
//	            	                                    "  \"y\":" + viewingPos.getY() + ",\n" +
//	            	                                    "  \"z\":" + viewingPos.getZ() + "\n" +
//	                                                    "}"),
//	                                            null
//	                                    );
//	                        } else {
//	                            player.addChatMessage(new ChatComponentText("You are not looking at a Chest Secret"));
//	                        }
//	                        break;
//	                }
//	        }
//        }
	}
}
