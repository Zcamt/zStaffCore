package me.Zcamt.zstaffcore;

import co.aikar.commands.BungeeCommandManager;
import com.google.common.io.ByteStreams;
import me.Zcamt.zstaffcore.database.MySql;
import me.Zcamt.zstaffcore.utils.Config;
import me.Zcamt.zstaffcore.modules.core.commands.StaffcoreCommand;
import me.Zcamt.zstaffcore.modules.history.HistoryJoinListener;
import me.Zcamt.zstaffcore.modules.history.HistoryManager;
import me.Zcamt.zstaffcore.modules.reports.*;
import me.Zcamt.zstaffcore.modules.staffchat.StaffchatCommand;
import me.Zcamt.zstaffcore.modules.stafflist.StaffhideCommand;
import me.Zcamt.zstaffcore.modules.stafflist.StafflistCommand;
import me.Zcamt.zstaffcore.modules.stafflist.StafflistLeaveListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ZStaffCore extends Plugin {

    public static ZStaffCore instance;

    private MySql mySql;
    private String host, database, username, password, port;

    private final List<ProxiedPlayer> hiddenStaff = new ArrayList<>();
    private final ReportManager reportManager = new ReportManager();
    private final HistoryManager historyManager = new HistoryManager();
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    @Override
    public void onEnable() {
        instance = this;
        try {
            loadConfig();
        } catch (IOException e) {
            throw new RuntimeException("Unable to able to load config!", e);
        }

        host = Config.host;
        port = Config.port;
        database = Config.database;
        username = Config.username;
        password = Config.password;
        mySql = new MySql(host, port, database, username, password);

        BungeeCommandManager commandManager = new BungeeCommandManager(this);
        commandManager.registerCommand(new StaffcoreCommand(getDescription().getVersion()));

        if (Config.staffchatEnabled) {
            commandManager.registerCommand(new StaffchatCommand());
        }
        if (Config.stafflistEnabled) {
            commandManager.registerCommand(new StafflistCommand(hiddenStaff));
            commandManager.registerCommand(new StaffhideCommand(hiddenStaff));
            ProxyServer.getInstance().getPluginManager().registerListener(this, new StafflistLeaveListener(hiddenStaff));
        }
        if (Config.reportSystemEnabled) {
            commandManager.registerCommand(new ReportCommand(reportManager, historyManager));
            commandManager.registerCommand(new ActivereportsCommand(reportManager));
            commandManager.registerCommand(new InvestigateCommand(reportManager));
            ProxyServer.getInstance().getPluginManager().registerListener(this, new JoinListener(reportManager));
        }
        ProxyServer.getInstance().getPluginManager().registerListener(this, new HistoryJoinListener(historyManager));

        try {
            mySql.connect();
            getLogger().info("Connected to SQL Database!");
        } catch (SQLException e) {
            getProxy().getPluginManager().unregisterCommands(this);
            getProxy().getPluginManager().unregisterListeners(this);
            onDisable();
            throw new RuntimeException("Couldn't connect to SQL Database!" +
                    "\nPlease check config.yml and check the SQL information you've provided", e);

        }
        if (!historyManager.createHistoryDatabase()) {
            getProxy().getPluginManager().unregisterCommands(this);
            getProxy().getPluginManager().unregisterListeners(this);
            getLogger().severe("Shutting down!");
            throw new RuntimeException("Wasn't able to create database table!");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Shutting down!");
        hiddenStaff.clear();
        try {
            mySql.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException("Wasn't able to disconnect from database on shutdown!");
        }
    }

    public void loadConfig() throws IOException {
        File configFile = new File(ProxyServer.getInstance().getPluginManager().getPlugin("zStaffCore").getDataFolder(), "config.yml");
        if(!ProxyServer.getInstance().getPluginManager().getPlugin("zStaffCore").getDataFolder().exists()){
            ProxyServer.getInstance().getPluginManager().getPlugin("zStaffCore").getDataFolder().mkdir();
        }
        if(!configFile.exists()){
            configFile.createNewFile();
            try (InputStream is = getResourceAsStream("config.yml");
                 OutputStream os = new FileOutputStream(configFile)) {
                ByteStreams.copy(is, os);
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }

        Config.reload();
    }

    public MySql getMySql(){
        return mySql;
    }

    public static ZStaffCore getInstance() {
        return instance;
    }

    public static ExecutorService getThreadPool() {
        return threadPool;
    }
}
