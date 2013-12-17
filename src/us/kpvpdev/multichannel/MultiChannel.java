package us.kpvpdev.multichannel;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import us.kpvpdev.multichannel.commands.Channel;
import us.kpvpdev.multichannel.commands.Chat;
import us.kpvpdev.multichannel.config.Config;
import us.kpvpdev.multichannel.listeners.PlayerListener;
import us.kpvpdev.multichannel.objects.ChatChannel;

import java.util.ArrayList;
import java.util.HashMap;

public class MultiChannel extends JavaPlugin {

	public static HashMap<String, ChatChannel> channels = new HashMap<String, ChatChannel>();
	public static HashMap<String, ChatChannel> playerChannels = new HashMap<String, ChatChannel>();
	public static ArrayList<String> chatting = new ArrayList<String>();

	public void onEnable() {
		multichannel = this;
		Config.reloadConfig();

		getCommand("channel").setExecutor(new Channel());
		getCommand("chat").setExecutor(new Chat());
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
	}

	public void onDisable() {
		multichannel = null;

		channels.clear();
		channels = null;

		playerChannels.clear();
		playerChannels = null;

		chatting.clear();
		chatting = null;
	}

	private static MultiChannel multichannel;

	public static MultiChannel getInstance() {
		return multichannel;
	}

}