package silence.simsool.mods.others.secretfounder.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.InflaterInputStream;
import silence.simsool.Main;
import silence.simsool.handlers.ScoreboardHandler;

public class SFUtils {
    private final static String resourcePath = "/assets/silenceutils/secretfounder/"; 
    public static boolean inCatacombs = false;
    private static final boolean DEV = false;

    public static void checkForCatacombs() {
        List<String> scoreboard = ScoreboardHandler.getSidebarLines();
        for (String s : scoreboard) {
            String sCleaned = ScoreboardHandler.cleanSB(s);
            if (sCleaned.contains("The Catacombs")) {
                inCatacombs = true;
                return;
            }
        }
    }
    
    public static List<Path> getAllPaths(String folderName) {
    	List<Path> paths = new ArrayList<>();
        try {
            URI uri = Main.class.getResource(resourcePath + folderName).toURI();
            Path Path;
            FileSystem fileSystem = null;
            if (uri.getScheme().equals("jar")) {
                fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                Path = fileSystem.getPath(resourcePath + folderName);
            } 
            else Path = Paths.get(uri);
            Stream<Path> walk = Files.walk(Path, 3);
            for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
                Path path = it.next();
                String name = path.getFileName().toString();
                if (name.endsWith(".skeleton")) paths.add(path);
            }
            if (fileSystem != null) fileSystem.close();
        } catch (URISyntaxException | IOException e) { e.printStackTrace(); }
        return paths;
    }

    public static HashMap<String, long[]> pathsToRoomData (String parentFolder, List<Path> allPaths) {
        HashMap<String, long[]> allRoomData = new HashMap<>();
        try {
            for (Path path : allPaths) {
                if (!path.getParent().getFileName().toString().equals(parentFolder)) continue;
                String name = path.getFileName().toString();
                if (DEV) {
	            InputStream input = new FileInputStream(path.toFile());
	            try (ObjectInputStream data = new ObjectInputStream(new InflaterInputStream(input))) {
		        long[] roomData = (long[]) data.readObject();
			allRoomData.put(name.substring(0, name.length() - 9), roomData);
			}
                }
                else {
                    InputStream input = Main.class.getResourceAsStream(path.toString());
                    ObjectInputStream data = new ObjectInputStream(new InflaterInputStream(input));
                    long[] roomData = (long[]) data.readObject();
                    allRoomData.put(name.substring(0, name.length() - 9), roomData);
                }
            }
        } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
        return allRoomData;
    }

    public static long shortToLong(short a, short b, short c, short d) {
        return ((long)((a << 16) | (b & 0xFFFF)) << 32) | (((c << 16) | (d & 0xFFFF)) & 0xFFFFFFFFL);
    }

    public static short[] longToShort(long l) {
        return new short[]{(short) (l >> 48), (short) (l >> 32), (short) (l >> 16), (short) (l)};
    }
}
