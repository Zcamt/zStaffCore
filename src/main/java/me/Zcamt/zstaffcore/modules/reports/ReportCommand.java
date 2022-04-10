package me.Zcamt.zstaffcore.modules.reports;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.Zcamt.zstaffcore.utils.Utils;
import me.Zcamt.zstaffcore.modules.history.HistoryManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

import java.util.UUID;


@CommandPermission("zstaffcore.report.use")
@CommandAlias("Report")
public class ReportCommand extends BaseCommand {

    private final ReportManager reportManager;
    private final HistoryManager historyManager;

    public ReportCommand(ReportManager reportManager, HistoryManager historyManager) {
        this.reportManager = reportManager;
        this.historyManager = historyManager;
    }

    @Default
    @CommandCompletion("@players")
    public void onDefault(ProxiedPlayer player, String[] args){
        if(args.length < 2){
            Utils.sendMessage(player, "&4&lReport: &r&7Usage: /Report [PLAYER] [REASON]");
            return;
        }

        int timeLeft = reportManager.cooldownLeft(player.getUniqueId());
        boolean isOnCooldown = reportManager.isOnCooldown(player.getUniqueId());

        if(isOnCooldown){
            Utils.sendMessage(player, "&4&lReport: &r&7Sorry! You have to wait &c" + timeLeft + " &7seconds until you can submit another report!");
            return;
        }

        String playerName = args[0];
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(playerName);

        if(target == player) {
            Utils.sendMessage(player, "&a&lReport: &7You can't report yourself");
            return;
        }

        UUID targetUUID;
        Server targetServer;
        if (target == null) {
            targetUUID = historyManager.getUUIDFromName(playerName);
            targetServer = player.getServer();
        } else {
            targetUUID = target.getUniqueId();
            targetServer = target.getServer();
        }
        if(targetUUID == null) {
            Utils.sendMessage(player, "&a&lReport: &7That player doesn't exist");
            return;
        }


        StringBuilder reportReason = new StringBuilder();
        for(int i = 1; i < args.length; i++){
            reportReason.append(args[i]).append(" ");
        }

        reportManager.createReport(player, playerName, targetUUID, reportReason.toString(), targetServer);
    }



}
