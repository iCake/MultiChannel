package us.kpvpdev.multichannel.objects;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import us.kpvpdev.multichannel.MultiChannel;
import us.kpvpdev.multichannel.config.Config;
import us.kpvpdev.multichannel.config.Lang;

import java.util.ArrayList;
import java.util.List;

public class ChatChannel {

	private String name;
	private String password;
	private String owner;
	private ArrayList<String> members;

	public ChatChannel(String name, String owner, String password) {
		this.name = name;
		this.owner = owner;
		this.password = password;
		this.members = new ArrayList<String>();
	}

	public ChatChannel(String name, String owner, String password, List<String> members) {
		this.name = name;
		this.owner = owner;
		this.password = password;

		ArrayList<String> temp = new ArrayList<String>();
		for(String str : members) {
			temp.add(str);
		}

		this.members = temp;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public ArrayList<String> getMembers() {
		return members;
	}

	public void broadcast(String msg) {
		for(String str : members) {
			Player player = Bukkit.getPlayer(str);

			if(player != null) {
                player.sendMessage(Lang.colorize("&b") + msg);
			}
		}
	}

	public void sendMessage(Player player, String msg) {
		for(String str : getMembers()) {
			Player member = Bukkit.getPlayer(str);

			if(member != null) {
				member.sendMessage(Lang.CHANNEL_MESSAGE.replace("{channel}", getName()).replace("{player}", player.getDisplayName()).replace("{message}", msg));
			}
		}

		if(!MultiChannel.spying.isEmpty()) {
			for(String str : MultiChannel.spying) {
				Player spy = Bukkit.getPlayer(str);

				if(spy != null) {
                    spy.sendMessage(Lang.CHANNEL_MESSAGE.replace("{channel}", getName()).replace("{player}", player.getDisplayName()).replace("{message}", msg));
				}
			}
		}
	}

	public void saveData() {
		FileConfiguration config = Config.getConfig();
		config.set(name + ".owner", owner);
		config.set(name + ".members", members);
		config.set(name + ".password", password);
		Config.saveConfig();
	}

	public void remove() {
		FileConfiguration config = Config.getConfig();
		config.set(name, null);
		Config.saveConfig();

		MultiChannel.channels.remove(name);
	}

	public void addAll() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(members.contains(player.getName())) {
				MultiChannel.playerChannels.put(player.getName(), this);
			}
		}
	}

}