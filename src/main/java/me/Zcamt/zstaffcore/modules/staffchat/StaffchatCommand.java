package me.Zcamt.zstaffcore.modules.staffchat;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.Zcamt.zstaffcore.utils.Config;
import me.Zcamt.zstaffcore.utils.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collection;

@CommandAlias("staffchat|sc")
@CommandPermission("zstaffcore.staffchat.use")
public class StaffchatCommand extends BaseCommand {

    @Default
    public void onCommand(ProxiedPlayer player, String[] args){

        if(args.length < 1){
            Utils.sendMessage(player, Config.staffchatPrefix + " &7Usage: /staffchat [MESSAGE]");
            return;
        }

        Collection<ProxiedPlayer> playerList = ProxyServer.getInstance().getPlayers();
        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
        }

        for(ProxiedPlayer target : playerList){
            if(target.hasPermission("zstaffcore.staffchat.use")){
                Utils.sendMessage(target, Config.staffchatPrefix +  " " +
                        "&8[&6" + player.getServer().getInfo().getName() + "&8]&r " +
                        "&b" + player.getName() +  ": " +
                        "&f" + message);
            }
        }
    }


}
