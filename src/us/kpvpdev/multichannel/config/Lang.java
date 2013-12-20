package us.kpvpdev.multichannel.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import us.kpvpdev.multichannel.MultiChannel;

import java.io.File;
import java.io.IOException;

public class Lang {

	private static FileConfiguration config;
	private static File configFile;

	public static String COMMAND_USAGE = "§cUsage:§f /§t <create, join, leave, info, setpass, setowner, delete>";
	public static String ADMIN_COMMAND_USAGE = "§cAdmin usage:§f /§t <spy>";

	public static String LEAVE_CHANNEL_OWNER = "§cYou must assign somebody else as the owner of the group";
	public static String LEAVE_COMMAND_USAGE = "§cUsage:§f /§t setowner <member>";

	public static String CHANNEL_LEAVE_LAST_MEMBER = "§cYou were the last remaining member of§f §t §cchannel";
	public static String CHANNEL_LEAVE_EMPTY = "§cIt has been removed because it is empty";
	public static String CHANNEL_JOIN = "§t has joined the channel";
	public static String CHANNEL_INCORRECT_PASSWORD = "§cIncorrect password";
	public static String CHANNEL_REQUIRES_PASSWORD = "§cThis channel requires a §fpassword§c.";
	public static String CHANNEL_NAME = "§cChannel name:§f §t";
	public static String CHANNEL_OWNER = "§cChannel owner:§f §t";
	public static String CHANNEL_PASSWORD = "§cChannel password:§f §t";
	public static String CHANNEL_MEMBERS = "§cChannel members:§f §t";
	public static String CHANNEL_DELETED = "§cYou have deleted the §f§t §cchannel";
	public static String CHANNEL_DELETED_OWNER = "This channel has been deleted by the owner";
	public static String CHANNEL_DELETED_ADMIN = "This channel has been deleted by an admin";
	public static String CHANNEL_LEAVE = "§t has left the channel";
	public static String CHANNEL_NOT_OWNER = "§cYou must be channel owner to use this!";
	public static String CHANNEL_CREATED = "§cYou created the §f§t §cchannel";
	public static String CHANNEL_CREATED_TALK = "§cTo talk in this channel, use §f/chat";
	public static String CHANNEL_PASSWORD_UPDATED = "The channel password has been updated";
	public static String CHANNEL_NEW_OWNER = "The channel owner is now §t";

	public static String SPYING_ENABLED = "§cYou can now see messages from all channels!";
	public static String SPYING_DISABLED = "§cYou can no longer see all channel messages!";

	public static String CHANNEL_NOT_FOUND = "§cCouldn't find the§f §t §cchannel!";
	public static String NOT_IN_CHANNEL = "§c§t isn't in a channel!";
	public static String OTHER_NOT_IN_CHANNEL = "§cYou're currently not in a channel!";
	public static String ALREADY_IN_CHANNEL = "§cYou're already in a channel!";
	public static String PLAYER_NOT_IN_CHANNEL = "§cThat member isn't in your channel!";
	public static String NO_PERMS = "§cYou do not have permission to use this!";
	public static String SUBCOMMAND_NOT_RECOGINSED = "§cSubcommand not recognised";
	public static String PLAYER_NOT_FOUND = "§cPlayer not found!";

	public static void loadConfig() {
		saveConfig();

		FileConfiguration config = getConfig();
		COMMAND_USAGE = config.getString("Command.usage").replaceAll("&", "§");
		ADMIN_COMMAND_USAGE = config.getString("Command.adminUsage").replaceAll("&", "§");

		LEAVE_CHANNEL_OWNER = config.getString("Leave.channelOwner").replaceAll("&", "§");
		LEAVE_COMMAND_USAGE = config.getString("Leave.usage").replaceAll("&", "§");

		CHANNEL_LEAVE_LAST_MEMBER = config.getString("Channel.lastMemberLeave").replaceAll("&", "§");
		CHANNEL_LEAVE_EMPTY = config.getString("Channel.leaveEmpty").replaceAll("&", "§");
		CHANNEL_JOIN = config.getString("Channel.join").replaceAll("&", "§");
		CHANNEL_INCORRECT_PASSWORD = config.getString("Channel.incorrectPassword").replaceAll("&", "§");
		CHANNEL_REQUIRES_PASSWORD = config.getString("Channel.requiresPassword").replaceAll("&", "§");
		CHANNEL_NAME = config.getString("Channel.name").replaceAll("&", "§");
		CHANNEL_OWNER = config.getString("Channel.owner").replaceAll("&", "§");
		CHANNEL_PASSWORD = config.getString("Channel.password").replaceAll("&", "§");
		CHANNEL_MEMBERS = config.getString("Channel.members").replaceAll("&", "§");
		CHANNEL_DELETED = config.getString("Channel.deleted").replaceAll("&", "§");
		CHANNEL_DELETED_ADMIN = config.getString("Channel.deletedAdmin").replaceAll("&", "§");
		CHANNEL_DELETED_OWNER = config.getString("Channel.deletedOwner").replaceAll("&", "§");
		CHANNEL_LEAVE = config.getString("Channel.leave").replaceAll("&", "§");
		CHANNEL_NOT_OWNER = config.getString("Channel.notOwner").replaceAll("&", "§");
		CHANNEL_CREATED = config.getString("Channel.created").replaceAll("&", "§");
		CHANNEL_CREATED_TALK = config.getString("Channel.createdTalk").replaceAll("&", "§");
		CHANNEL_PASSWORD_UPDATED = config.getString("Channel.passwordUpdated").replaceAll("&", "§");
		CHANNEL_NEW_OWNER = config.getString("Channel.newOwner").replaceAll("&", "§");

		SPYING_ENABLED = config.getString("Spying.enabled").replaceAll("&", "§");
		SPYING_DISABLED = config.getString("Spying.disabled").replaceAll("&", "§");

		CHANNEL_NOT_FOUND = config.getString("Error.channelNotFound").replaceAll("&", "§");
		NOT_IN_CHANNEL = config.getString("Error.notInChannel").replaceAll("&", "§");
		OTHER_NOT_IN_CHANNEL = config.getString("Error.otherNotInChannel").replaceAll("&", "§");
		ALREADY_IN_CHANNEL = config.getString("Error.alreadyInChannel").replaceAll("&", "§");
		PLAYER_NOT_IN_CHANNEL = config.getString("Error.playerNotInChannel").replaceAll("&", "§");
		NO_PERMS = config.getString("Error.noPerms").replaceAll("&", "§");
		SUBCOMMAND_NOT_RECOGINSED = config.getString("Error.subcommandNotRecognised").replaceAll("&", "§");
		PLAYER_NOT_FOUND = config.getString("Error.playerNotFound").replaceAll("&", "§");
	}

	public static FileConfiguration getConfig() {
		if(config == null) {
			reloadConfig();
		}

		return config;
	}

	public static void reloadConfig() {
		if(configFile == null) {
			configFile = new File(MultiChannel.getInstance().getDataFolder(), "lang.yml");
		}

		config = YamlConfiguration.loadConfiguration(configFile);
		loadConfig();
	}

	public static void saveConfig() {
		if(config == null || configFile == null) {
			return;
		}

		try {
			config.save(configFile);
		} catch(IOException e) { }
	}

}