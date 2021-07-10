package me.marius.mysql;

import me.marius.main.Main;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.*;
import java.util.UUID;

public class MySQL {

    private final Main plugin;
    public MySQL(Main plugin) { this.plugin = plugin; }

    private static Connection con;

    public boolean connect(){
        if(!isconnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + plugin.getConfigManager().host + ":" + plugin.getConfigManager().port + "/" + plugin.getConfigManager().database,
                        plugin.getConfigManager().username, plugin.getConfigManager().password);
                System.out.println("[ClanSystem] Die Verbindung zur MySQL-Datenbank wurde hergestellt!");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("[ClanSystem] Die Verbindung zur MySQL-Datenbank konnte nicht hergestellt werden!");
            }
        }
        return false;
    }

    public boolean isconnected(){ return (con == null ? false : true); }

    public void disconnect(){
        try {
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createTable(){
        if(!isconnected())
            if(!connect())
                return;

        try {
            PreparedStatement ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS ClanManager (TIMECREATED DATETIME, PLAYERID int, CLANID int, UUID VARCHAR(64), NAME VARCHAR(64), CLANNAME VARCHAR(64), CLANTAG VARCHAR(64))");
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("[ClanSystem] Es gab einen Fehler beim Erstellen des Tables!");
        }
    }

    public void createNewClan(int playerid, int clanid, String uuid, String playername, String clanname, String clantag) {
        if(!isconnected())
            if(!connect())
                return;

        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO ClanManager (TIMECREATED,PLAYERID,CLANID,UUID,NAME,CLANNAME,CLANTAG) VALUES (NOW(),?,?,?,?,?,?)");
            ps.setInt(1, playerid);
            ps.setInt(2, clanid);
            ps.setString(3, uuid);
            ps.setString(4, playername);
            ps.setString(5, clanname);
            ps.setString(6, clantag);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("[ClanSystem] Es gab einen Fehler beim Erstellen eines Clans! (Clanname: " + clanname + ", Clantag: " + clantag + ", ClanID:" + clanid + ")");
        }

    }

    public Integer getClanIDs() {
        if(!isconnected())
            if(!connect())
                return -1;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ClanManager ORDER BY CLANID DESC");
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt("CLANID");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return 0;
    }

    public Integer getPlayerID() {
        if(!isconnected())
            if(!connect())
                return -1;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ClanManager ORDER BY PLAYERID DESC");
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return rs.getInt("PLAYERID");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return 0;
    }

    public boolean isPlayerInClan(UUID uuid) {
        if(!isconnected())
            if(!connect())
                return false;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT UUID FROM ClanManager WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public boolean isClanAlreadyExisting(String clanname, String clantag) {
        if(!isconnected())
            if(!connect())
                return false;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ClanManager WHERE Clanname = ? AND Clantag = ?");
            ps.setString(1, clanname);
            ps.setString(2, clantag);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                if(rs.getString("Clanname").equals(clanname))
                        return true;
                if(rs.getString("Clantag").equals(clantag))
                    return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

}
