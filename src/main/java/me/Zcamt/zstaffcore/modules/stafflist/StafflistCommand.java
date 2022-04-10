package me.Zcamt.zstaffcore.modules.stafflist;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.Zcamt.zstaffcore.utils.Config;
import me.Zcamt.zstaffcore.utils.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.config.Configuration;

import java.util.List;
import java.util.UUID;

@CommandAlias("stafflist|staff")
@CommandPermission("zstaffcore.stafflist.use")
public class StafflistCommand extends BaseCommand {
    private final List<ProxiedPlayer> hiddenStaff;

    public StafflistCommand(List<ProxiedPlayer> hiddenStaff) {
        this.hiddenStaff = hiddenStaff;
    }

    @Default @CatchUnknown
    public void onDefault(ProxiedPlayer player) {
        Configuration rankSection = Config.config.getSection("StaffList.Ranks");
        if(!Config.stafflistHeader.equalsIgnoreCase("")) {
            Utils.sendMessage(player, Config.stafflistHeader);
        }
        Utils.sendMessage(player, "&a&lOnline players: &r&7" + (ProxyServer.getInstance().getPlayers().size() - hiddenStaff.size()));
        for(String section : rankSection.getKeys()){
            Configuration rank = rankSection.getSection(section);
            String rankPrefix = rank.getString("Prefix");
            List<String> rankUsers = rank.getStringList("Staff");
            for(String userUUID : rankUsers) {
                ProxiedPlayer staff = ProxyServer.getInstance().getPlayer(UUID.fromString(userUUID));
                if(staff == null || !staff.isConnected()) continue;
                if(hiddenStaff.contains(staff)){
                    if(player.hasPermission("zstaffcore.stafflist.showall")){
                        String staffName = staff.getName();
                        Server staffServer = staff.getServer();
                        Utils.sendMessage(player, rankPrefix + " &r&7" + staffName + " - &r&a" + staffServer.getInfo().getName() + " (hidden)");
                    }
                } else {
                    String staffName = staff.getName();
                    Server staffServer = staff.getServer();
                    Utils.sendMessage(player, rankPrefix + " &r&7" + staffName + " - &r&a" + staffServer.getInfo().getName());
                }
            }
        }
        if(!Config.stafflistFooter.equalsIgnoreCase("")) {
            Utils.sendMessage(player, Config.stafflistFooter);
        }
    }


}
