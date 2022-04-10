package me.Zcamt.zstaffcore.modules.reports;

import me.Zcamt.zstaffcore.utils.PermissionChecker;
import me.Zcamt.zstaffcore.utils.Permissions;
import me.Zcamt.zstaffcore.utils.Utils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.UUID;

public class JoinListener implements Listener {

    private final ReportManager reportManager;

    public JoinListener(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    @EventHandler
    public void onStaffJoin(PostLoginEvent event){
        ProxiedPlayer player = event.getPlayer();
        if (!PermissionChecker.hasPermission(player, Permissions.STAFF.getPermission())) return;

        HashMap<UUID, Report> activeReports = reportManager.getActiveReports();

        if(!activeReports.isEmpty()){
            Utils.sendMessage(player, "&4&lReport: &r&7There are " + activeReports.size() + " active reports");
        }
    }
}
