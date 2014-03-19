package us.kpvpdev.multichannel.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import us.kpvpdev.multichannel.MultiChannel;

import java.io.File;
import java.io.IOException;

public class Lang {

	private static FileConfiguration config;
	private static File configFile;

	public static String CHANNEL_MESSAGE = "&7[&b{channel}&7]&r {player} &7>&f {message}";
	public static String COMMAND_USAGE = "&cUsage:&f /{command} <create, join, leave, info, setpass, setowner, delete>";
	public static String ADMIN_COMMAND_USAGE = "&cAdmin usage:&f /{command} <spy, reload>";
	public static String CHAT_COMMAND_USAGE = "&cUsage:&f / {command} <toggle/t, msg>";


	public static String LEAVE_CHANNEL_OWNER = "&cYou must assign somebody else as the owner of the group";
	public static String LEAVE_COMMAND_USAGE = "&cUsage:&f /{command} setowner <member>";

	public static String CHANNEL_TOGGLE = "&cChannel chat&f {toggle}";
	public static String CHANNEL_LEAVE_LAST_MEMBER = "&cYou were the last remaining member of&f {channel} &cchannel";
	public static String CHANNEL_LEAVE_EMPTY = "&cIt has been removed because it is empty";
	public static String CHANNEL_JOIN = "{player} has joined the channel";
	public static String CHANNEL_INCORRECT_PASSWORD = "&cIncorrect password";
	public static String CHANNEL_REQUIRES_PASSWORD = "&cThis channel requires a &fpassword&c.";
	public static String CHANNEL_NAME = "&cChannel name:&f {name}";
	public static String CHANNEL_OWNER = "&cChannel owner:&f {owner}";
	public static String CHANNEL_PASSWORD = "&cChannel password:&f {password}";
	public static String CHANNEL_MEMBERS = "&cChannel members:&f {members}";
	public static String CHANNEL_DELETED = "&cYou have deleted the &f{channel} &cchannel";
	public static String CHANNEL_DELETED_OWNER = "This channel has been deleted by the owner";
	public static String CHANNEL_DELETED_ADMIN = "This channel has been deleted by an admin";
	public static String CHANNEL_LEAVE = "{player} has left the channel";
	public static String CHANNEL_NOT_OWNER = "&cYou must be channel owner to use this!";
	public static String CHANNEL_CREATED = "&cYou created the &f{channel} &cchannel";
	public static String CHANNEL_CREATED_TALK = "&cTo talk in this channel, use &f/chat";
	public static String CHANNEL_PASSWORD_UPDATED = "The channel password has been updated";
	public static String CHANNEL_NEW_OWNER = "The channel owner is now {owner}";

	public static String SPYING_ENABLED = "&cYou can now see messages from all channels!";
	public static String SPYING_DISABLED = "&cYou can no longer see all channel messages!";

	public static String CHANNEL_NOT_FOUND = "&cCouldn't find the&f {channel} &cchannel!";
	public static String NOT_IN_CHANNEL = "&c{player} isn't in a channel!";
	public static String OTHER_NOT_IN_CHANNEL = "&cYou're currently not in a channel!";
	public static String ALREADY_IN_CHANNEL = "&cYou're already in a channel!";
	public static String PLAYER_NOT_IN_CHANNEL = "&cThat member isn't in your channel!";
	public static String NO_PERMS = "&cYou do not have permission to use this!";
	public static String NO_PERMS_CHANNEL = "&cYou need the {channel} permission to join this channel!";
	public static String SUBCOMMAND_NOT_RECOGINSED = "&cSubcommand not recognised";
	public static String PLAYER_NOT_FOUND = "&cPlayer not found!";

	public static String RELOADED_CONFIGS = "&cReloaded all configs!";

	public static void loadConfig() {
		saveConfig();

		CHANNEL_MESSAGE = getFromConfig("Channel.message");
		COMMAND_USAGE = getFromConfig("Command.usage");
		ADMIN_COMMAND_USAGE = getFromConfig("Command.adminUsage");
		CHAT_COMMAND_USAGE = getFromConfig("Command.chatUsage");

		LEAVE_CHANNEL_OWNER = getFromConfig("Leave.channelOwner");
		LEAVE_COMMAND_USAGE = getFromConfig("Leave.usage");

		CHANNEL_TOGGLE = getFromConfig("Channel.toggle");
		CHANNEL_LEAVE_LAST_MEMBER = getFromConfig("Channel.lastMemberLeave");
		CHANNEL_LEAVE_EMPTY = getFromConfig("Channel.leaveEmpty");
		CHANNEL_JOIN = getFromConfig("Channel.join");
		CHANNEL_INCORRECT_PASSWORD = getFromConfig("Channel.incorrectPassword");
		CHANNEL_REQUIRES_PASSWORD = getFromConfig("Channel.requiresPassword");
		CHANNEL_NAME = getFromConfig("Channel.name");
		CHANNEL_OWNER = getFromConfig("Channel.owner");
		CHANNEL_PASSWORD = getFromConfig("Channel.password");
		CHANNEL_MEMBERS = getFromConfig("Channel.members");
		CHANNEL_DELETED = getFromConfig("Channel.deleted");
		CHANNEL_DELETED_ADMIN = getFromConfig("Channel.deletedAdmin");
		CHANNEL_DELETED_OWNER = getFromConfig("Channel.deletedOwner");
		CHANNEL_LEAVE = getFromConfig("Channel.leave");
		CHANNEL_NOT_OWNER = getFromConfig("Channel.notOwner");
		CHANNEL_CREATED = getFromConfig("Channel.created");
		CHANNEL_CREATED_TALK = getFromConfig("Channel.createdTalk");
		CHANNEL_PASSWORD_UPDATED = getFromConfig("Channel.passwordUpdated");
		CHANNEL_NEW_OWNER = getFromConfig("Channel.newOwner");

		SPYING_ENABLED = getFromConfig("Spying.enabled");
		SPYING_DISABLED = getFromConfig("Spying.disabled");

		CHANNEL_NOT_FOUND = getFromConfig("Error.channelNotFound");
		NOT_IN_CHANNEL = getFromConfig("Error.notInChannel");
		OTHER_NOT_IN_CHANNEL = getFromConfig("Error.otherNotInChannel");
		ALREADY_IN_CHANNEL = getFromConfig("Error.alreadyInChannel");
		PLAYER_NOT_IN_CHANNEL = getFromConfig("Error.playerNotInChannel");
		NO_PERMS = getFromConfig("Error.noPerms");
		NO_PERMS_CHANNEL = getFromConfig("Error.noPermsChannel");
		SUBCOMMAND_NOT_RECOGINSED = getFromConfig("Error.subcommandNotRecognised");
		PLAYER_NOT_FOUND = getFromConfig("Error.playerNotFound");

		RELOADED_CONFIGS = getFromConfig("Config.reloaded");
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

	private static String getFromConfig(String path) {
		FileConfiguration config = getConfig();
		return colorize(config.getString(path));
	}
    
	public static String colorize(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
}