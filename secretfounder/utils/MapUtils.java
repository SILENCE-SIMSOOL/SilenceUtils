package silence.simsool.mods.others.secretfounder.utils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import net.minecraft.block.material.MapColor;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec4b;
import net.minecraft.world.storage.MapData;
import silence.simsool.mods.others.secretfounder.dungeon.DungeonManager;

public class MapUtils {
    static Minecraft mc = Minecraft.getMinecraft();
    public static boolean mapExists() {
        if (mc.thePlayer == null || mc.thePlayer.inventory == null || mc.thePlayer.inventory.getStackInSlot(8) == null) return false;
        ItemStack mapSlot = mc.thePlayer.inventory.getStackInSlot(8);
        if (mapSlot == null || mapSlot.getItem() != Items.filled_map || !mapSlot.hasDisplayName()) return false;
        return mapSlot.getDisplayName().contains("Magical Map");
    }

    public static Integer[][] updatedMap() {
        return updatedMap(DungeonManager.mapId);
    }

    public static Integer[][] updatedMap(int mapId) {
        return updatedMap((MapData) mc.theWorld.getMapStorage().loadData(MapData.class, "map_" + mapId));
    }

    public static Integer[][] updatedMap(ItemStack mapSlot) {
        return updatedMap(Items.filled_map.getMapData(mapSlot, mc.theWorld));
    }

    public static Integer[][] updatedMap(MapData mapData) {
        if(mapData == null) return null;
        Integer[][] map = new Integer[128][128];
        for (int i = 0; i < 16384; ++i) {
            int x = i % 128;
            int y = i / 128;
            int j = mapData.colors[i] & 255;
            int rgba;
            if (j / 4 == 0) rgba = (i + i / 128 & 1) * 8 + 16 << 24;
            else rgba = MapColor.mapColorArray[j / 4].getMapColor(j & 3);
            map[x][y] = rgba & 0x00FFFFFF;
        }
        return map;
    }

    public static Point[] entranceMapCorners(Integer[][] map) {
        if (map == null) return null;
        Point[] corners = new Point[2];
        for (int x = 0; x < 128; x++) {
            for (int y = 0; y < 128; y++) {
                if (map[x][y] != null && map[x][y] == 31744 && map[x][y-1] != null && map[x][y-1] == 0) {
                    if (map[x - 1][y] != null && map[x - 1][y] == 0) corners[0] = new Point(x, y);
                    else if (map[x + 1][y] != null && map[x + 1][y] == 0) corners[1] = new Point(x, y);
                }
            }
            if (corners[0] != null && corners[1] != null) break;
        }
        return corners;
    }

    public static Point getClosestNWMapCorner(Point mapPos, Point leftCorner, Point rightCorner) {
        int roomWidthAndGap = rightCorner.x - leftCorner.x + 1 + 4;
        Point origin = new Point(leftCorner.x % roomWidthAndGap, leftCorner.y % roomWidthAndGap);
        mapPos.x = mapPos.x + 2;
        mapPos.y = mapPos.y + 2;
        int x = mapPos.x - (mapPos.x % roomWidthAndGap) + origin.x;
        int y = mapPos.y - (mapPos.y % roomWidthAndGap) + origin.y;
        if (x > mapPos.x) x -= roomWidthAndGap;
        if (y > mapPos.y) y -= roomWidthAndGap;
        return new Point(x, y);
    }

    public static Point getClosestNWPhysicalCorner (Vec3 vectorPos) {
        Vec3 shiftedPos = vectorPos.addVector(0.5, 0, 0.5);
        shiftedPos = shiftedPos.addVector(8, 0, 8);
        int x = (int) (shiftedPos.xCoord - Math.floorMod((int) shiftedPos.xCoord, 32));
        int z = (int) (shiftedPos.zCoord - Math.floorMod((int) shiftedPos.zCoord, 32));
        return new Point(x - 8 , z - 8);
    }

    public static Point getClosestNWPhysicalCorner (BlockPos blockPos) {
        return getClosestNWPhysicalCorner(new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
    }

    public static Point physicalToMapCorner(Point physicalClosestCorner, Point physicalLeftCorner, Point leftCorner, Point rightCorner) {
        int roomWidthAndGap = rightCorner.x - leftCorner.x + 1 + 4;
        int xShift = (physicalClosestCorner.x - physicalLeftCorner.x) / 32;
        int yShift = (physicalClosestCorner.y - physicalLeftCorner.y) / 32;
        int x = leftCorner.x + (roomWidthAndGap * xShift);
        int y = leftCorner.y + (roomWidthAndGap * yShift);
        return new Point(x, y);
    }

    public static Point mapToPhysicalCorner(Point mapCorner, Point physicalLeftCorner, Point leftCorner, Point rightCorner) {
        int roomWidthAndGap = rightCorner.x - leftCorner.x + 1 + 4;
        int xShift = (mapCorner.x - leftCorner.x) / roomWidthAndGap;
        int yShift = (mapCorner.y - leftCorner.y) / roomWidthAndGap;
        int x = physicalLeftCorner.x + (32 * xShift);
        int y = physicalLeftCorner.y + (32 * yShift);
        return new Point(x, y);
    }

    public static String getMapColor(Point point, Integer[][] map) {
        int x = point.x;
        int y = point.y;
        if (x < 0 || y < 0 || x > 127 || y > 127) return "undefined";
        if (map != null) {
            switch (map[x][y]) {
                case 7488283:
                    return "brown";
                case 11685080:
                    return "purple";
                case 15066419:
                    return "yellow";
                case 31744:
                    return "green";
                case 15892389:
                    return "pink";
                case 14188339:
                    return "orange";
                case 16711680:
                    return "red";
                default:
                    return "undefined";
            }
        }
        return "undefined";
    }

    public static List<Point> neighboringSegments(Point originCorner, Integer[][] map, Point leftCorner, Point rightCorner, List<Point> list) {
        if (!list.contains(originCorner)) list.add(originCorner);
        if (!getMapColor(originCorner, map).equals("brown")) return list;
        int roomWidth = rightCorner.x - leftCorner.x + 1;
        List<Point> pointsToCheck = new ArrayList<>();
        pointsToCheck.add(new Point(originCorner.x, originCorner.y - 1));
        pointsToCheck.add(new Point(originCorner.x, originCorner.y + roomWidth));
        pointsToCheck.add(new Point(originCorner.x - 1, originCorner.y));
        pointsToCheck.add(new Point(originCorner.x + roomWidth, originCorner.y));
        List<Point> pointsToTransform = new ArrayList<>();
        pointsToTransform.add(new Point(originCorner.x, originCorner.y - 1 - 4));
        pointsToTransform.add(new Point(originCorner.x, originCorner.y + roomWidth + 4));
        pointsToTransform.add(new Point(originCorner.x -1 - 4, originCorner.y));
        pointsToTransform.add(new Point(originCorner.x + roomWidth + 4, originCorner.y));
        for (int i = 0; i < 4; i++) {
            if (getMapColor(pointsToCheck.get(i), map).equals("brown")) {
                Point newCorner = getClosestNWMapCorner(pointsToTransform.get(i), leftCorner, rightCorner);
                if (!list.contains(newCorner)) {
                    list.add(newCorner);
                    list = neighboringSegments(newCorner, map, leftCorner, rightCorner, list);
                }
            }
        }
        return list;
    }

    public static String roomSize(List<Point> segments) {
        if (segments.size() == 1) return "1x1";
        if (segments.size() == 2) return "1x2";
        HashSet<Integer> x = new HashSet<>();
        HashSet<Integer> y = new HashSet<>();
        for(Point segment:segments) {
            x.add(segment.x);
            y.add(segment.y);
        }
        if (segments.size() == 3) {
            if (x.size() == 2 && y.size() == 2) return "L-shape";
            else return "1x3";
        }
        if (segments.size() == 4) {
            if (x.size() == 2 && y.size() == 2) return "2x2";
            else return "1x4";
        }
        return "undefined";
    }

    public static String roomCategory(String roomSize, String roomColor) {
        if (roomSize.equals("1x1")) {
            switch (roomColor) {
                case "brown":
                    return "1x1";
                case "purple":
                    return "Puzzle";
                case "orange":
                    return "Trap";
                case "green":
                case "red":
                case "pink":
                case "yellow":
                    return "General";
                default:
                    return "undefined";
            }
        } else {
            return roomSize;
        }
    }

    public static Point playerMarkerPos() {
        MapData mapData = (MapData) mc.theWorld.getMapStorage().loadData(MapData.class, "map_" + DungeonManager.mapId);
        if(mapData == null) return null;
        if (mapData.mapDecorations != null) {
            for (Map.Entry<String, Vec4b> entry : mapData.mapDecorations.entrySet()) {
                if (entry.getValue().func_176110_a() == 1) { //player marker
                    int x = entry.getValue().func_176112_b() / 2 + 64;
                    int y = entry.getValue().func_176113_c() / 2 + 64;
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    public static Point getPhysicalCornerPos(String direction, List<Point> currentPhysicalSegments) {
        TreeSet<Integer> xSet = new TreeSet<>(); //TreeSet removes duplicates and sorts increasing
        TreeSet<Integer> ySet = new TreeSet<>();
        for(Point segment:currentPhysicalSegments) {
            xSet.add(segment.x);
            ySet.add(segment.y);
        }
        switch (direction) {
            case "NW":
                return new Point(xSet.first(), ySet.first());
            case "NE":
                return new Point(xSet.last() + 30, ySet.first());
            case "SE":
                return new Point(xSet.last() + 30, ySet.last() + 30);
            case "SW":
                return new Point(xSet.first(), ySet.last() + 30);
        }
        return null;
    }

    public static List<String> possibleDirections(String roomSize, List<Point> currentRoomSegments) {
        List<String> directions = new ArrayList<>();
        if (roomSize.equals("1x1") || roomSize.equals("2x2")) {
            directions.add("NW");
            directions.add("NE");
            directions.add("SE");
            directions.add("SW");
        } else {
            TreeSet<Integer> xSet = new TreeSet<>();
            TreeSet<Integer> ySet = new TreeSet<>();
            for(Point segment:currentRoomSegments) {
                xSet.add(segment.x);
                ySet.add(segment.y);
            }
            if (roomSize.equals("L-shape")) {
                List<Integer> x = new ArrayList<>(xSet);
                List<Integer> y = new ArrayList<>(ySet);
                if (!currentRoomSegments.contains(new Point(x.get(0), y.get(0)))) directions.add("SW");
                else if (!currentRoomSegments.contains(new Point(x.get(0), y.get(1)))) directions.add("SE");
                else if (!currentRoomSegments.contains(new Point(x.get(1), y.get(0)))) directions.add("NW");
                else if (!currentRoomSegments.contains(new Point(x.get(1), y.get(1)))) directions.add("NE");
            } else if (roomSize.startsWith("1x")) {
                if (xSet.size() >= 2  && ySet.size() == 1) {
                    directions.add("NW");
                    directions.add("SE");
                } else if (xSet.size() == 1  && ySet.size() >= 2) {
                    directions.add("NE");
                    directions.add("SW");
                }
            }
        }
        return directions;
    }

    public static BlockPos actualToRelative(BlockPos actual, String cornerDirection, Point locationOfCorner) {
        double x = 0;
        double z = 0;
        switch (cornerDirection) {
            case "NW":
                x = actual.getX() - locationOfCorner.getX();
                z = actual.getZ() - locationOfCorner.getY();
                break;
            case "NE":
                x = actual.getZ() - locationOfCorner.getY();
                z = -(actual.getX() - locationOfCorner.getX());
                break;
            case "SE":
                x = -(actual.getX() - locationOfCorner.getX());
                z = -(actual.getZ() - locationOfCorner.getY());
                break;
            case "SW":
                x = -(actual.getZ() - locationOfCorner.getY());
                z = actual.getX() - locationOfCorner.getX();
                break;
        }
        return new BlockPos(x, actual.getY(), z);
    }

    public static BlockPos relativeToActual(BlockPos relative, String cornerDirection, Point locationOfCorner) {
        double x = 0;
        double z = 0;
        switch (cornerDirection) {
            case "NW":
                x = relative.getX() + locationOfCorner.getX();
                z = relative.getZ() + locationOfCorner.getY();
                break;
            case "NE":
                x = -(relative.getZ() - locationOfCorner.getX());
                z = relative.getX() + locationOfCorner.getY();
                break;
            case "SE":
                x = -(relative.getX() - locationOfCorner.getX());
                z = -(relative.getZ() - locationOfCorner.getY());
                break;
            case "SW":
                x = relative.getZ() + locationOfCorner.getX();
                z = -(relative.getX() - locationOfCorner.getY());
                break;
        }
        return new BlockPos(x, relative.getY(), z);
    }
    
}
