package me.marius.config;

import me.marius.main.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private Main plugin;
    public ConfigManager(Main plugin) { this.plugin = plugin;}

    public static File file;
    public Configuration config;

    public String prefix;

    public String host,
                database,
                username,
                password;
    public int port;

    public void register(){

        if(!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdir();

        file = new File(plugin.getDataFolder().getParent(), "ClanSystem/config.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

                config.set("Einstellungen.Prefix", "&8[&cClans&8] &7");

                config.set("MySQL.Host", "localhost");
                config.set("MySQL.Port", 3306);
                config.set("MySQL.Database", "clansystem");
                config.set("MySQL.Username", "root");
                config.set("MySQL.Password", "pP8nt2J9t5xpdUiN");

                saveCfg();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

            prefix = config.getString("Einstellungen.Prefix");
            prefix = ChatColor.translateAlternateColorCodes('&', prefix);

            host = config.getString("MySQL.Host");
            port = config.getInt("MySQL.Port");
            database = config.getString("MySQL.Database");
            username = config.getString("MySQL.Username");
            password = config.getString("MySQL.Password");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveCfg(){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            System.out.println("Es gab einen Fehler beim Speichern der config.yml!");
        }
    }

}
