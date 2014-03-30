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

    /**
     * @return the name of a channel
     */
    public String getName() {
        return name;
    }

    /**
     * @return the password associated with the channel
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the channel.
     *
     * @param password 
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the owner of the channel
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the owner of the channel.
     *
     * @param owner 
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return all members of a channel
     */
    public ArrayList<String> getMembers() {
        return members;
    }

    /**
     * Broadcasts a message to all channel members.
     *
     * @param msg 
     */
    public void broadcast(String msg) {
        for(String str : members) {
            Player player = Bukkit.getPlayer(str);

            if(player != null) {
                player.sendMessage(Lang.colorize("&b") + msg);
            }
        }
    }

    /**
     * Sends a message to all members of a group from a Player.
     *
     * @param player
     * @param msg 
     */
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

    /**
     * Saves the data of the channel.
     */
    public void saveData() {
        FileConfiguration config = Config.getConfig();
        config.set(name + ".owner", owner);
        config.set(name + ".members", members);
        config.set(name + ".password", password);
        Config.saveConfig();
    }

    /**
     * In essence, permanently deletes a channel.
     */
    public void remove() {
        FileConfiguration config = Config.getConfig();
        config.set(name, null);
        Config.saveConfig();

        MultiChannel.channels.remove(name);
    }

    /**
     * Adds all members of a channel to the channel.
     */
    public void addAll() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(members.contains(player.getName())) {
                MultiChannel.playerChannels.put(player.getName(), this);
            }
        }
    }

}