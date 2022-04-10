package me.Zcamt.zstaffcore.modules.history;

import me.Zcamt.zstaffcore.ZStaffCore;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoryJoinListener implements Listener {

    private final HistoryManager historyManager;

    public HistoryJoinListener(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @EventHandler
    public void onJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ResultSet rs = historyManager.getHistoryResultSetFromUUID(player.getUniqueId());
        if (rs != null) {
            historyManager.updateHistory(player);
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                ZStaffCore.getInstance().getLogger().severe("Wasn't able to close resultset onJoin (" + player.getName() + ")");
            }
        } else {
            historyManager.createHistory(player);
        }
    }

}
