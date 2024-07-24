package silence.simsool.mods.others.secretfounder.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.*;
import java.util.List;

public class RoomUtils {
    public static final double DEG_TO_RAD = Math.PI / 180.0;
    public static final double RAD_TO_DEG = 180.0 / Math.PI;
    
    static Minecraft mc = Minecraft.getMinecraft();
    static EntityPlayerSP player = mc.thePlayer;

    public static Vec3 getVectorFromRotation(float yaw, float pitch) {
        float f = MathHelper.cos(-yaw * (float) DEG_TO_RAD - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * (float) DEG_TO_RAD - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * (float) DEG_TO_RAD);
        float f3 = MathHelper.sin(-pitch * (float) DEG_TO_RAD);
        return new Vec3( f1 * f2, f3, f * f2);
    }

    public static List<Vec3> vectorsToRaytrace (int vectorQuantity) {
        List<Vec3> vectorList = new ArrayList<>();
        Vec3 eyes = new Vec3(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
        float aspectRatio = (float) mc.displayWidth / (float) mc.displayHeight;
        double fovV = mc.gameSettings.fovSetting * mc.thePlayer.getFovModifier();
        double fovH = Math.atan(aspectRatio * Math.tan(fovV * DEG_TO_RAD / 2)) * 2 * RAD_TO_DEG;

        float verticalSpacing = (float) (fovV * 0.8 / vectorQuantity);
        float horizontalSpacing = (float) (fovH * 0.9 / vectorQuantity);
        float playerYaw = player.rotationYaw;
        float playerPitch = player.rotationPitch;

        if (mc.gameSettings.thirdPersonView == 2) {
            playerYaw = playerYaw + 180.0F;
            playerPitch = -playerPitch;
        }

        for (float h = (float) -(vectorQuantity - 1) / 2; h <= (float) (vectorQuantity - 1) / 2; h++) {
            for (float v = (float) -(vectorQuantity - 1) / 2; v <= (float) (vectorQuantity - 1) / 2; v++) {
                float yaw = h * horizontalSpacing;
                float pitch = v * verticalSpacing;
                float yawScaled = yaw  * ((playerPitch*playerPitch/8100)+1) / (Math.abs(v/(vectorQuantity))+1);
                Vec3 direction = getVectorFromRotation(yawScaled + playerYaw, pitch + playerPitch);
                vectorList.add(eyes.addVector(direction.xCoord * 64, direction.yCoord * 64, direction.zCoord * 64));
            }
        }
        return vectorList;
    }

    public static HashSet<Integer> whitelistedBlocks = new HashSet<>(Arrays.asList(
            //These are the blocks which are stored in the ".skeleton" files
            100, //Stone
            103, //Diorite
            104, //Polished Diorite
            105, //Andesite
            106, //Polished Andesite
            200, //Grass
            300, //Dirt
            301, //Coarse Dirt
            400, //Cobblestone
            700, //Bedrock
            1800, //Oak Leaves
            3507, //Gray Wool
            4300, //Double Stone Slab
            4800, //Mossy Cobblestone
            8200, //Clay
            9800, //Stone Bricks
            9801, //Mossy Stone Bricks
            9803, //Chiseled Stone Bricks
            15907, //Gray Stained Clay
            15909, //Cyan Stained Clay
            15915 //Black Stained Clay
    ));

    public static boolean blockPartOfDoorway(BlockPos blockToCheck) {
        if (blockToCheck.getY() < 66 || blockToCheck.getY() > 73) return false;
        int relX = Math.floorMod((blockToCheck.getX() - 8), 32);
        int relZ = Math.floorMod((blockToCheck.getZ() - 8), 32);
        if (relX >= 13 && relX <= 17) {
            if (relZ <= 2) return true;
            if (relZ >= 28) return true;
        }
        if (relZ >= 13 && relZ <= 17) {
            if (relX <= 2) return true;
            if (relX >= 28) return true;
        }
        return false;
    }
}
