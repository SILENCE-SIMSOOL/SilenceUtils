package silence.simsool.mods.others.secretfounder.dungeon;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.input.Keyboard;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import silence.simsool.config.Config;
import silence.simsool.events.PacketEvent;
import silence.simsool.mods.others.secretfounder.SecretFounder;
import silence.simsool.mods.others.secretfounder.utils.MapUtils;
import silence.simsool.mods.others.secretfounder.utils.SFUtils;
import silence.simsool.utils.RenderUtils;

public class WaypointManager {
	
    public static boolean enabled = true;
    public static boolean allFound = false;
    public static int secretNum = 0;
    public static int completedSecrets = 0;
    public static Map<String, List<Boolean>> allSecretsMap = new HashMap<>();
    public static List<Boolean> secretsList = new ArrayList<>(Arrays.asList(new Boolean[10]));
    static long lastSneakTime = 0;

    Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (!Config.SecretFounder || !enabled || mc.thePlayer == null || mc.theWorld == null) return;

        String roomName = RoomManager.roomName;
        if (roomName.equals("undefined") || SecretFounder.roomsJson.get(roomName) == null || secretsList == null) return;
        if (SecretFounder.waypointsJson.get(roomName) != null) {
        	
            JsonArray secretsArray = SecretFounder.waypointsJson.get(roomName).getAsJsonArray();
            int arraySize = secretsArray.size();
            for(int i = 0; i < arraySize; i++) {
                JsonObject secretsObject = secretsArray.get(i).getAsJsonObject();
                boolean display = true;
                for(int j = 1; j <= secretNum; j++) {
                    if (!secretsList.get(j-1)) {
                        if (secretsObject.get("secretName").getAsString().substring(0,2).replaceAll("[\\D]", "").equals(String.valueOf(j))) {
                            display = false;
                            break;
                        }
                    }
                }
                if (!display) continue;
                if (allFound && !secretsObject.get("category").getAsString().equals("fairysoul")) continue;
                BlockPos relative = new BlockPos(secretsObject.get("x").getAsInt(), secretsObject.get("y").getAsInt(), secretsObject.get("z").getAsInt());
                BlockPos pos = MapUtils.relativeToActual(relative, RoomManager.roomDirection, RoomManager.roomCorner);
                Entity viewer = mc.getRenderViewEntity();
                boolean Bounding = false;
                String mode = secretsObject.get("category").getAsString(); Color color;
                switch (secretsObject.get("category").getAsString()) {
                    case "chest":
                        color = new Color(0, 255, 0, 215);
                        Bounding = false;
                        break;
                    case "item":
                        color = new Color(0, 148, 255, 185);
                        Bounding = false;
                        break;
                    case "bat":
                        color = new Color(40, 30, 20, 185);
                        Bounding = false;
                        break;
                    case "wither":
                        color = new Color(255, 255, 255, 185);
                        Bounding = false;
                        break;
                    case "redstone":
                        color = new Color(255, 85, 85, 185);
                        Bounding = false;
                        break;
                    case "lever":
                        color = new Color(255, 255, 85, 185);
                        Bounding = false;
                        break;
                    case "fairysoul":
                        color = new Color(255, 85, 255, 35);
                        Bounding = true;
                        break;
                    case "aotv":
                        color = new Color(61, 191, 220, 25);
                        break;
                    default:
                        color = new Color(190, 255, 252, 200);
                        break;
                }
                double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * event.partialTicks;
                double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * event.partialTicks;
                double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * event.partialTicks;
                double x = pos.getX() - viewerX; double y = pos.getY() - viewerY; double z = pos.getZ() - viewerZ;
                double distSq = x*x + y*y + z*z;
                GlStateManager.disableDepth();
                GlStateManager.disableCull();
                
                if (Bounding) {
                    if (mode.equals("fairysoul")) RenderUtils.drawFilledBoundingBox(new AxisAlignedBB(x + 0.2, y, z + 0.2, x + 0.8, y + 0.55, z + 0.8), color);
                }
                else if (!Bounding) {
                    if (mode.equals("bat")) RenderUtils.drawLineBoundingBox(new AxisAlignedBB(x + 0.3, y + 0.3, z + 0.3, x + 1 - 0.3, y + 1 - 0.3, z + 1 - 0.3), color, 3.0F);
                        else if (mode.equals("item")) RenderUtils.drawLineBoundingBox(new AxisAlignedBB(x + 0.25, y, z + 0.25, x + 0.75, y + 0.3, z + 0.75), color, 1.5F);
                        else if (mode.equals("chest")) RenderUtils.drawLineBoundingBox(new AxisAlignedBB(x + 0.05, y, z + 0.05, x + 0.95, y + 0.9, z + 0.95), color, 3.05F);
                        else if (mode.equals("lever")) RenderUtils.drawCurrentSeletectBox(pos, color, 3.25F, 0.0F, 0.0F, 0.0F, true, event.partialTicks);
                        else if (mode.equals("redstone") || mode.equals("wither")) RenderUtils.drawLineBoundingBox(new AxisAlignedBB(x + 0.2, y, z + 0.2, x + 0.8, y + 0.55, z + 0.8), color, 1.5F);
                        else if (mode.equals("aotv")) {
                            RenderUtils.drawCurrentSeletectBox(pos, new Color(115, 195, 220, 165), 1.25F, 0.0F, 0.0F, 0.0F, true, event.partialTicks);
                            RenderUtils.drawCurrentSeletectBox(pos, color, 0.0F, 0.0F, 0.0F, 0.0F, false, event.partialTicks);
                        }
                }
                GlStateManager.disableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.enableTexture2D();
                GlStateManager.enableDepth();
                GlStateManager.enableCull();
            }
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onReceiveActionBar(ClientChatReceivedEvent event) {
        if (!Config.SecretFounder || !SFUtils.inCatacombs || !enabled || event.type != 2) return;
        String[] actionBarSections = event.message.getUnformattedText().split(" {3,}");
        for (String section : actionBarSections) {
            if (section.contains("Secrets") && section.contains("/")) {
                String cleanedSection = StringUtils.stripControlCodes(section);
                String[] splitSecrets = cleanedSection.split("/");
                completedSecrets = Integer.parseInt(splitSecrets[0].replaceAll("[^0-9]", ""));
                int totalSecrets = Integer.parseInt(splitSecrets[1].replaceAll("[^0-9]", ""));
                allFound = (totalSecrets == secretNum && completedSecrets == secretNum);
                break;
            }
        }
    }

    @SubscribeEvent
    public void onClickSecret(PlayerInteractEvent event) {
        if (!Config.SecretFounder || !SFUtils.inCatacombs || !enabled || allFound || event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.world.getBlockState(event.pos).getBlock();
        if (block != Blocks.chest && block != Blocks.skull && block != Blocks.lever) return;
        String roomName = RoomManager.roomName;
        if (roomName.equals("undefined") || SecretFounder.roomsJson.get(roomName) == null || secretsList == null) return;
        if (SecretFounder.waypointsJson.get(roomName) != null) {
            JsonArray secretsArray = SecretFounder.waypointsJson.get(roomName).getAsJsonArray();
            int arraySize = secretsArray.size();
            for(int i = 0; i < arraySize; i++) {
                JsonObject secretsObject = secretsArray.get(i).getAsJsonObject();
                String category = secretsObject.get("category").getAsString();
                if (category.equals("chest") || category.equals("wither") || category.equals("lever") || category.equals("redstone")) {
                	BlockPos relative = new BlockPos(secretsObject.get("x").getAsInt(), secretsObject.get("y").getAsInt(), secretsObject.get("z").getAsInt());
                    BlockPos pos = MapUtils.relativeToActual(relative, RoomManager.roomDirection, RoomManager.roomCorner);
                    if (pos.equals(event.pos)) {
                        for(int j = 1; j <= secretNum; j++) {
                            if (secretsObject.get("secretName").getAsString().substring(0,2).replaceAll("[\\D]", "").equals(String.valueOf(j))) {
                                WaypointManager.secretsList.set(j-1, false);
                                WaypointManager.allSecretsMap.replace(roomName, WaypointManager.secretsList);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onReceivePacket(PacketEvent.ReceiveEvent event) {
        if (!Config.SecretFounder || !SFUtils.inCatacombs || !enabled || mc.theWorld == null || allFound) return;
        if (event.packet instanceof S0DPacketCollectItem) {
            S0DPacketCollectItem packet = (S0DPacketCollectItem) event.packet;
            Entity entity = mc.theWorld.getEntityByID(packet.getCollectedItemEntityID()); if (entity == null) return;
            if (entity instanceof EntityItem) {
                EntityItem item = (EntityItem) entity;
                entity = mc.theWorld.getEntityByID(packet.getEntityID()); if (entity == null) return;
                String name = item.getEntityItem().getDisplayName();
                if (name.contains("Decoy") || name.contains("Defuse Kit") || name.contains("Dungeon Chest Key") ||
                        name.contains("Healing VIII") || name.contains("Inflatable Jerry") || name.contains("Spirit Leap") ||
                        name.contains("Training Weights") || name.contains("Trap") || name.contains("Treasure Talisman")) {
                    if (!entity.getCommandSenderEntity().getName().equals(mc.thePlayer.getName())) return;
                    String roomName = RoomManager.roomName;
                    if (roomName.equals("undefined") || SecretFounder.roomsJson.get(roomName) == null || secretsList == null) return;
                    if (SecretFounder.waypointsJson.get(roomName) != null) {
                        JsonArray secretsArray = SecretFounder.waypointsJson.get(roomName).getAsJsonArray();
                        int arraySize = secretsArray.size();
                        for(int i = 0; i < arraySize; i++) {
                            JsonObject secretsObject = secretsArray.get(i).getAsJsonObject();
                            String category = secretsObject.get("category").getAsString();
                            if (category.equals("item") || category.equals("bat")) {
                                BlockPos relative = new BlockPos(secretsObject.get("x").getAsInt(), secretsObject.get("y").getAsInt(), secretsObject.get("z").getAsInt());
                                BlockPos pos = MapUtils.relativeToActual(relative, RoomManager.roomDirection, RoomManager.roomCorner);
                                if (entity.getDistanceSq(pos) <= 36D) {
                                    for(int j = 1; j <= secretNum; j++) {
                                        if (secretsObject.get("secretName").getAsString().substring(0,2).replaceAll("[\\D]", "").equals(String.valueOf(j))) {
                                            if (!WaypointManager.secretsList.get(j-1)) continue;
                                            WaypointManager.secretsList.set(j-1, false);
                                            WaypointManager.allSecretsMap.replace(roomName, WaypointManager.secretsList);
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onSneak(InputEvent.KeyInputEvent event) {
        if (!Config.SecretFounder || !SFUtils.inCatacombs || !enabled || mc.thePlayer == null) return;
        if (mc.gameSettings.keyBindSneak.isPressed() && Keyboard.getEventKeyState()) {
            if (System.currentTimeMillis() - lastSneakTime < 500) {
                String roomName = RoomManager.roomName;
                if (roomName.equals("undefined") || SecretFounder.roomsJson.get(roomName) == null || secretsList == null) return;
                if (SecretFounder.waypointsJson.get(roomName) != null) {
                    JsonArray secretsArray = SecretFounder.waypointsJson.get(roomName).getAsJsonArray();
                    int arraySize = secretsArray.size();
                    for(int i = 0; i < arraySize; i++) {
                        JsonObject secretsObject = secretsArray.get(i).getAsJsonObject();
                        String category = secretsObject.get("category").getAsString();
                        if (category.equals("chest") || category.equals("wither") || category.equals("item") || category.equals("bat")) {
                            BlockPos relative = new BlockPos(secretsObject.get("x").getAsInt(), secretsObject.get("y").getAsInt(), secretsObject.get("z").getAsInt());
                            BlockPos pos = MapUtils.relativeToActual(relative, RoomManager.roomDirection, RoomManager.roomCorner);
                            if (mc.thePlayer.getDistanceSq(pos) <= 16D) {
                                for(int j = 1; j <= secretNum; j++) {
                                    if (secretsObject.get("secretName").getAsString().substring(0,2).replaceAll("[\\D]", "").equals(String.valueOf(j))) {
                                        if (!WaypointManager.secretsList.get(j-1)) continue;
                                        WaypointManager.secretsList.set(j-1, false);
                                        WaypointManager.allSecretsMap.replace(roomName, WaypointManager.secretsList);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            lastSneakTime = System.currentTimeMillis();
        }
    }
}
