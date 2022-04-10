package me.Zcamt.zstaffcore.modules.stafflist;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.Zcamt.zstaffcore.utils.Utils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

@CommandAlias("staffhide")
@CommandPermission("zstaffcore.stafflist.hide")
public class StaffhideCommand extends BaseCommand {

    private final List<ProxiedPlayer> hiddenStaff;

    public StaffhideCommand(List<ProxiedPlayer> hiddenStaff) {
        this.hiddenStaff = hiddenStaff;
    }

    @Default@CatchUnknown
    public void onDefault(ProxiedPlayer player) {
        if(hiddenStaff.contains(player)){
            hiddenStaff.remove(player);
            Utils.sendMessage(player, "&aStaffList: &7You're now visible on the stafflist");
        } else {
            hiddenStaff.add(player);
            Utils.sendMessage(player, "&aStaffList: &7You're now invisible on the stafflist");
        }
    }

}
