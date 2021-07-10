package me.marius.commands;

import me.marius.config.ConfigManager;
import me.marius.main.Main;
import me.marius.mysql.MySQL;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ClanCommand extends Command {

    private final ConfigManager configManager;
    private final MySQL mySQL;
    private final Main plugin;

    public ClanCommand(Main plugin, ConfigManager configManager, MySQL mySQL){
        super("clan");

        this.plugin = plugin;
        this.configManager = configManager;
        this.mySQL = mySQL;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            if(args.length == 3) {

                //CLAN CREATE
                if(args[0].equalsIgnoreCase("create")){
                    String clanname = args[1];
                    String clantag = args[2];

                    if(!mySQL.isPlayerInClan(player.getUniqueId())) {
                        if(!mySQL.isClanAlreadyExisting(clanname, clantag)) {
                            if (clanname.length() > 2 && clanname.length() <= 20 && clantag.length() > 2 && clantag.length() < 5) {
                                ProxyServer.getInstance().getScheduler().runAsync(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        int playerid = mySQL.getPlayerID();
                                        int clanid = mySQL.getClanIDs();
                                        playerid++;
                                        clanid++;
                                        mySQL.createNewClan(playerid, clanid, player.getUniqueId().toString(), player.getName(), clanname, clantag);
                                        player.sendMessage(configManager.prefix + "Dein Clan wurde erstellt.");
                                    }
                                });
                            } else
                                player.sendMessage(configManager.prefix + "Es war nicht möglich den Clan zu erstellen! §8[§cName§8/§cKürzel zu lang§8]");
                        }else
                            player.sendMessage(configManager.prefix + "Der Clanname oder Kürzel existiert bereits!");
                    } else
                        player.sendMessage(configManager.prefix + "Du bist bereits in einem Clan!");


                } else {
                    player.sendMessage(configManager.prefix + "HELP MESSAGE");
                }



            } else if(args.length == 2) {

                //CLAN INVITE
                if(args[0].equalsIgnoreCase("invite")) {
                    ProxiedPlayer invitedPlayer = ProxyServer.getInstance().getPlayer(args[1]);
                    if (invitedPlayer != null) {
                        if (!mySQL.isPlayerInClan(invitedPlayer.getUniqueId())) {

                            player.sendMessage("Spieler eingeladen!");
                            invitedPlayer.sendMessage("Einladung erhalten!");

                        } else
                            player.sendMessage(configManager.prefix + "Der Spieler ist bereits in einem Clan!");
                    } else
                        player.sendMessage(configManager.prefix + "Der Spieler ist nicht online!");


                } else {
                    player.sendMessage("HELP MESSAGE");
                }

            } else
                player.sendMessage("HELP MESSAGE");
        }else
            commandSender.sendMessage("§4Must be a player!");
    }
}
