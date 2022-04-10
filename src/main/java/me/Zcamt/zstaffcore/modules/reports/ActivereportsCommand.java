package me.Zcamt.zstaffcore.modules.reports;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.Zcamt.zstaffcore.utils.PermissionChecker;
import me.Zcamt.zstaffcore.utils.Permissions;
import me.Zcamt.zstaffcore.utils.Utils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collection;


@CommandAlias("activereports|reports")
@CommandPermission("zstaffcore.report.staff")
public class ActivereportsCommand extends BaseCommand {

    private ReportManager reportManager;

    public ActivereportsCommand(ReportManager reportManager) {
        this.reportManager = reportManager;
    }


    @Default @CatchUnknown
    public void onDefault(ProxiedPlayer player) {
        Collection<Report> activeReports = reportManager.getActiveReports().values();

        if(activeReports.isEmpty()){
            Utils.sendMessage(player, "&4&lReport: &r&7No active reports :)");
            return;
        }

        for(Report report : activeReports){
            report.dispatchTo(player);
        }

    }

}
