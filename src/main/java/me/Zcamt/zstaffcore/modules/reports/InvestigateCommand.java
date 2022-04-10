package me.Zcamt.zstaffcore.modules.reports;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.Zcamt.zstaffcore.utils.Utils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

@CommandAlias("investigate")
@CommandPermission("zstaffcore.report.staff")
public class InvestigateCommand extends BaseCommand {

    private ReportManager reportManager;

    public InvestigateCommand(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    @Default
    public void onDefault(ProxiedPlayer player, String[] args) {
        if(args.length != 1){
            Utils.sendMessage(player, "Usage: /investigate [REPORT ID]");
            return;
        }
        String reportIDArg = args[0];
        UUID reportID = UUID.fromString(reportIDArg);
        if(!reportManager.getActiveReports().containsKey(reportID)){
            Utils.sendMessage(player, "&4&lReport: &r&7No active report found with that ID");
            return;}

        Report report = reportManager.getActiveReports().get(reportID);
        reportManager.investigateReport(player, report);
    }

}
