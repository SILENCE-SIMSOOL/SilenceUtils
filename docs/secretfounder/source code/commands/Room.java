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

//		else {
//
//    PRIVATE CODE
//
//		}

	}
}
