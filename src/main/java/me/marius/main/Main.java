//This file was created in 2021

package me.marius.main;

import me.marius.commands.ClanCommand;
import me.marius.config.ConfigManager;
import me.marius.mysql.MySQL;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {

    private Main plugin;
    private ConfigManager cm;
    private MySQL mySQL;

    public void onEnable() {

        plugin = this;
        cm = new ConfigManager(this);
        mySQL = new MySQL(this);

        cm.register();

        mySQL.connect();
        mySQL.createTable();

        getProxy().getPluginManager().registerCommand(this, new ClanCommand(this, cm, mySQL));
        //Bukkit.getPluginManager().registerEvents(new LISTENER(), this);

// -------------------------------
        System.out.println("----------[ClanSystem]----------");
        System.out.println("Plugin aktiviert");
        System.out.println("Version: 1.0");
        System.out.println("Author: einMarius");
        System.out.println("----------[ClanSystem]----------");
// -------------------------------
    }

    public void onDisable() {

        cm.saveCfg();

        mySQL.disconnect();

// -------------------------------
        System.out.println("----------[ClanSystem]----------");
        System.out.println("Plugin deaktiviert");
        System.out.println("Version: 1.0");
        System.out.println("Author: einMarius");
        System.out.println("----------[ClanSystem]----------");
// -------------------------------

    }

    public Main getPlugin() { return plugin; }

    public ConfigManager getConfigManager() { return cm; }
}