package us.kpvpdev.multichannel.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import us.kpvpdev.multichannel.MultiChannel;
import us.kpvpdev.multichannel.objects.ChatChannel;

import java.io.File;
import java.io.IOException;

public class Config {

    private static FileConfiguration config;
    private static File configFile;

    public static void loadConfig() {
        saveConfig();

        FileConfiguration config = getConfig();

        for(String str : config.getConfigurationSection("").getKeys(false).toArray(new String[0])) {
            ChatChannel channel = new ChatChannel(str, config.getString(str + ".owner"), config.getString(str + ".password"), config.getStringList(str + ".members"));

            if(!MultiChannel.channels.containsKey(channel.getName())) {
                channel.addAll();
                MultiChannel.channels.put(channel.getName(), channel);
            }
        }
    }

    public static FileConfiguration getConfig() {
        if(config == null) {
            reloadConfig();
        }

        return config;
    }

    public static void reloadConfig() {
        if(configFile == null) {
            configFile = new File(MultiChannel.getInstance().getDataFolder(), "channels.yml");
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