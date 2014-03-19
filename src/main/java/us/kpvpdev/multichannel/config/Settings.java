package us.kpvpdev.multichannel.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import us.kpvpdev.multichannel.MultiChannel;

import java.io.File;
import java.io.IOException;

public class Settings {

	private static FileConfiguration config;
	private static File configFile;

	public static boolean PERMS_PER_CHANNEL = false;
	public static boolean ADMINS_BYPASS_JOIN = false;
	public static boolean JOIN_MESSAGE = true;
	public static boolean LOG_TO_CONSOLE = true;

	public static void loadConfig() {
		saveConfig();

		FileConfiguration config = getConfig();
		PERMS_PER_CHANNEL = config.getBoolean("PermissionsPerChannel");
		ADMINS_BYPASS_JOIN = config.getBoolean("AdminsBypassJoinPassword");
		JOIN_MESSAGE = config.getBoolean("JoinMessages");
		LOG_TO_CONSOLE = config.getBoolean("LogMsgsToConsole");
	}

	public static FileConfiguration getConfig() {
		if(config == null) {
			reloadConfig();
		}

		return config;
	}

	public static void reloadConfig() {
		if(configFile == null) {
			configFile = new File(MultiChannel.getInstance().getDataFolder(), "settings.yml");
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
