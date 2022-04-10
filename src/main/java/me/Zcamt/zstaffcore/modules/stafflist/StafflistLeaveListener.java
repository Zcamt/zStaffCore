package me.Zcamt.zstaffcore.modules.stafflist;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class StafflistLeaveListener implements Listener {

    private final List<ProxiedPlayer> hiddenStaff;

    public StafflistLeaveListener(List<ProxiedPlayer> hiddenStaff) {
        this.hiddenStaff = hiddenStaff;
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if(hiddenStaff.contains(player)){
            hiddenStaff.remove(player);
        }

    }

}
