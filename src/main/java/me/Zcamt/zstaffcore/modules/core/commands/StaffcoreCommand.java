package me.Zcamt.zstaffcore.modules.core.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.Zcamt.zstaffcore.utils.Config;
import me.Zcamt.zstaffcore.utils.Utils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@CommandAlias("zstaffcore|staffcore")
@CommandPermission("zstaffcore.admin")
public class StaffcoreCommand extends BaseCommand {

    private String pluginVersion;

    public StaffcoreCommand(String pluginVersion) {
        this.pluginVersion = pluginVersion;
    }

    @Subcommand("help")
    @Default @CatchUnknown
    public void onDefault(ProxiedPlayer player){
        Utils.sendCenteredMessage(player, "&7&l================ &6&lStaffCore&7&l ================");
        Utils.sendCenteredMessage(player, "&6&lVersion: &c&l" + pluginVersion);
        Utils.sendMessage(player, "&6/zstaffcore reload - &cReload command");
        Utils.sendCenteredMessage(player, "&7&l================ &6&lStaffCore&7&l ================");
    }

    @Subcommand("reload")
    public void onReload(ProxiedPlayer player){
        Config.reload();
        Utils.sendMessage(player, "&6&lzStaffCore: &7&lYou've reloaded the config of zStaffCore");
    }



}
