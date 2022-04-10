package me.Zcamt.zstaffcore.utils;

import me.Zcamt.zstaffcore.ZStaffCore;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {

    private static File configFile;
    public static Configuration config;

    public static String host, database, username, password, port;


    public static boolean staffchatEnabled, stafflistEnabled, reportSystemEnabled;
    public static String staffchatPrefix;
    public static String stafflistHeader, stafflistFooter;

    public static int reportCooldown;


    public static void reload() {
        configFile = new File(ZStaffCore.getInstance().getDataFolder(), "config.yml");

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            throw new RuntimeException("Unable to able to load config!", e);
        }
        host = config.getString("Database.Host");
        database = config.getString("Database.Databasename");
        username = config.getString("Database.Username");
        password = config.getString("Database.Password");
        port = config.getString("Database.Port");

        staffchatEnabled = config.getBoolean("StaffChat.Enabled");
        stafflistEnabled = config.getBoolean("StaffList.Enabled");
        reportSystemEnabled = config.getBoolean("Report.Enabled");

        staffchatPrefix = config.getString("StaffChat.Prefix");
        stafflistHeader = config.getString("StaffList.Settings.Header");
        stafflistFooter = config.getString("StaffList.Settings.Footer");

        reportCooldown = config.getInt("Report.Cooldown");

    }
}
