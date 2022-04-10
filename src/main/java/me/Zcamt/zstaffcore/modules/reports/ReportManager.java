package me.Zcamt.zstaffcore.modules.reports;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import me.Zcamt.zstaffcore.utils.Config;
import me.Zcamt.zstaffcore.utils.PermissionChecker;
import me.Zcamt.zstaffcore.utils.Permissions;
import me.Zcamt.zstaffcore.utils.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ReportManager {

    private final HashMap<UUID, Report> activeReports = new HashMap<>();

    private final Cache<UUID, Instant> reportCooldowns =
            Caffeine.newBuilder().expireAfterWrite(Config.reportCooldown, TimeUnit.SECONDS).build();

    public void createReport(ProxiedPlayer reporter, String targetName, UUID targetUUID, String reason, Server server){
        UUID id = null;
        for(int i = 0; i<1;) {
            id = UUID.randomUUID();
            if(!activeReports.containsKey(id)) i++;
        }
        Report report = new Report(id, reporter, targetName, targetUUID, reason, server);
        activeReports.put(id, report);
        reportCooldowns.put(reporter.getUniqueId(), Instant.now());
        ProxyServer.getInstance().getPlayers().forEach(player -> {
            if (PermissionChecker.hasPermission(player, Permissions.STAFF.getPermission())) {
                report.dispatchTo(player);
            }
        });
        Utils.sendMessage(reporter, "&4&lReport:&r &7You've reported " + targetName + " for: " + reason);
    }

    public void investigateReport(ProxiedPlayer staff, Report report){

        if(ProxyServer.getInstance().getPlayer(report.getTargetUUID()) == null){
            Utils.sendMessage(staff, "&4&lReport:&r &7The reported player is offline");
            return;
        }

        Collection<ProxiedPlayer> players = ProxyServer.getInstance().getPlayers();

        for(ProxiedPlayer target : players){
            if(target.hasPermission("zstaffcore.report.staff")){
                Utils.sendMessage(target, "&4&lReport: &4&l" + staff.getName() + " &r&7is now investigating report-" + report.getId() + " \n" +
                        "On &c&l" + report.getTargetName());
            }
        }
        activeReports.remove(report.getId());
        staff.connect(report.getServer().getInfo());
    }

    public HashMap<UUID, Report> getActiveReports() {
        return activeReports;
    }

    public boolean isOnCooldown(UUID uuid){
        return reportCooldowns.asMap().containsKey(uuid);
    }

    public int cooldownLeft(UUID uuid){
        if(!reportCooldowns.asMap().containsKey(uuid)){
            return 0;
        }
        Instant cooldownStart = reportCooldowns.getIfPresent(uuid);
        Instant cooldownEnd = cooldownStart.plus(Config.reportCooldown, ChronoUnit.SECONDS);

        Instant instantNow = Instant.now();
        return (int) Duration.between(instantNow, cooldownEnd).toSeconds();
    }
}
