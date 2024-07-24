package silence.simsool.mods.others.secretfounder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.lwjgl.input.Keyboard;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import gg.essential.universal.UChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import silence.simsool.Main;
import silence.simsool.config.Config;
import silence.simsool.mods.others.secretfounder.commands.Room;
import silence.simsool.mods.others.secretfounder.dungeon.DungeonManager;
import silence.simsool.mods.others.secretfounder.dungeon.RoomManager;
import silence.simsool.mods.others.secretfounder.dungeon.WaypointManager;
import silence.simsool.mods.others.secretfounder.utils.SFUtils;

public class SecretFounder {
	
    public static String PREFIX = "§8[§5Secret§dFounder§8]§f";
    public static JsonObject roomsJson;
    public static JsonObject waypointsJson;
    public static HashMap<String,HashMap<String,long[]>> ROOM_DATA = new HashMap<>();
    static int tickAmount = 1;
    public static final KeyBinding Keybind_Toggle = new KeyBinding("Toggle SecretFounder", Keyboard.KEY_H, "Silence Utils");
    
    private static Minecraft mc = Minecraft.getMinecraft();
    
    public static void unload() {
    	EventBus e = MinecraftForge.EVENT_BUS;
    	e.unregister(new SecretFounder());
    	e.unregister(new DungeonManager());
    	e.unregister(new RoomManager());
    	e.unregister(new WaypointManager());
    }
    public static void load() {
    	EventBus e = MinecraftForge.EVENT_BUS;
    	ClientCommandHandler.instance.registerCommand(new Room());
    	e.register(new SecretFounder());
    	e.register(new DungeonManager());
    	e.register(new RoomManager());
    	e.register(new WaypointManager());
    	
    	try (BufferedReader roomsReader = new BufferedReader(new InputStreamReader(mc.getResourceManager()
                .getResource(new ResourceLocation(Main.MODID, "secretfounder/dungeonrooms.json")).getInputStream()));
            BufferedReader waypointsReader = new BufferedReader(new InputStreamReader(mc.getResourceManager()
                .getResource(new ResourceLocation(Main.MODID, "secretfounder/secretlocations.json")).getInputStream())) ) {
            Gson gson = new Gson();
            roomsJson = gson.fromJson(roomsReader, JsonObject.class);
            waypointsJson = gson.fromJson(waypointsReader, JsonObject.class);
            loadData();
        } catch (IOException f) { f.printStackTrace(); }
    }
    
    public static void loadData() {
    	try {
            List<Path> paths = SFUtils.getAllPaths("catacombs");
            final ExecutorService ex = Executors.newFixedThreadPool(4);
            Future<HashMap<String, long[]>> f1x1 = ex.submit(() -> SFUtils.pathsToRoomData("1x1", paths));
            Future<HashMap<String, long[]>> f1x2 = ex.submit(() -> SFUtils.pathsToRoomData("1x2", paths));
            Future<HashMap<String, long[]>> f1x3 = ex.submit(() -> SFUtils.pathsToRoomData("1x3", paths));
            Future<HashMap<String, long[]>> f1x4 = ex.submit(() -> SFUtils.pathsToRoomData("1x4", paths));
            Future<HashMap<String, long[]>> f2x2 = ex.submit(() -> SFUtils.pathsToRoomData("2x2", paths));
            Future<HashMap<String, long[]>> fLShape = ex.submit(() -> SFUtils.pathsToRoomData("L-shape", paths));
            Future<HashMap<String, long[]>> fPuzzle = ex.submit(() -> SFUtils.pathsToRoomData("Puzzle", paths));
            Future<HashMap<String, long[]>> fTrap = ex.submit(() -> SFUtils.pathsToRoomData("Trap", paths));
            ROOM_DATA.put("1x1", f1x1.get());
            ROOM_DATA.put("1x2", f1x2.get());
            ROOM_DATA.put("1x3", f1x3.get());
            ROOM_DATA.put("1x4", f1x4.get());
            ROOM_DATA.put("2x2", f2x2.get());
            ROOM_DATA.put("L-shape", fLShape.get());
            ROOM_DATA.put("Puzzle", fPuzzle.get());
            ROOM_DATA.put("Trap", fTrap.get());
            ex.shutdown();
        } catch (ExecutionException | InterruptedException e) { e.printStackTrace(); }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Config.SecretFounder || event.phase != TickEvent.Phase.START) return;
        tickAmount++;
        int t = (Config.RoomDetectPerformance == 0) ? 60 : 45;
        if (Config.RoomDetectPerformance == 2) t = 30;
        if (tickAmount % t == 0) {
            if (mc.thePlayer != null) {
                SFUtils.checkForCatacombs();
                tickAmount = 0;
            }
        }
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
    	if (!Config.SecretFounder || !SFUtils.inCatacombs || mc.thePlayer == null || mc.theWorld == null || !Keybind_Toggle.isPressed() || !Keyboard.getEventKeyState()) return;
        WaypointManager.enabled = !WaypointManager.enabled;
        UChat.chat(PREFIX + " Toggle Status: " + (WaypointManager.enabled ? "§aENABLE" : "§cDISABLE") );
    }
}
