package me.Zcamt.zstaffcore.modules.reports;

import me.Zcamt.zstaffcore.utils.PermissionChecker;
import me.Zcamt.zstaffcore.utils.Permissions;
import me.Zcamt.zstaffcore.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

import java.util.UUID;

public class Report {
    private UUID id;
    private ProxiedPlayer reporter;
    private String targetName;
    private UUID targetUUID;
    private String reason;
    private Server server;

    protected Report(UUID id, ProxiedPlayer reporter, String targetName, UUID targetUUID, String reason, Server server) {
        this.id = id;
        this.reporter = reporter;
        this.targetName = targetName;
        this.targetUUID = targetUUID;
        this.reason = reason;
        this.server = server;
    }


    public UUID getId() {
        return id;
    }

    public ProxiedPlayer getReporter() {
        return reporter;
    }

    public String getTargetName() {
        return targetName;
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

    public String getReason() {
        return reason;
    }

    public Server getServer() {
        return server;
    }

    public void dispatchTo(ProxiedPlayer player) {
        if (!PermissionChecker.hasPermission(player, Permissions.STAFF.getPermission())) return;
        TextComponent textComponent = new TextComponent();
        textComponent.setText("Click to investigate!");
        textComponent.setColor(ChatColor.RED);
        textComponent.setBold(true);
        textComponent.setUnderlined(true);
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/investigate " + this.id));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Investigate now! " + this.id, ChatColor.RED)));
        Utils.sendCenteredMessage(player, "&7&l============ &6&lREPORT&7&l ============");
        Utils.sendMessage(player,
                "&c&lBy: &r&7" + this.reporter.getName() + " \n" +
                        "&c&lTarget: &r&7" + this.targetName + " \n" +
                        "&c&lReason: &r&7" + this.reason + " \n" +
                        "&c&lServer: &r&7" + this.server.getInfo().getName()) ;
        player.sendMessage(textComponent);
        Utils.sendCenteredMessage(player, "&7&l============ &6&lREPORT&7&l ============");
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", reporter=" + reporter +
                ", targetName='" + targetName + '\'' +
                ", targetUUID=" + targetUUID +
                ", reason='" + reason + '\'' +
                '}';
    }

}
