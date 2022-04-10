package me.Zcamt.zstaffcore.utils;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PermissionChecker {

    public static boolean hasPermissionWithMessage(@NotNull ProxiedPlayer player, @NotNull String permission, @Nullable String denyMessage) {
        if (player.hasPermission("*")) return true;
        if (player.hasPermission(Permissions.OVERRIDE.getPermission())) return true;
        boolean hasPerm = player.hasPermission(permission);
        if (!hasPerm) {
            Utils.sendMessage(player,
                    denyMessage == null ? "&4Error: &cYou're not allowed to do that!" : denyMessage);
        }
        return hasPerm;
    }

    public static boolean hasPermissionWithMessage(@NotNull ProxiedPlayer player, @NotNull List<String> permissions, @Nullable String denyMessage) {
        for (String permission : permissions) {
            if (hasPermissionWithMessage(player, permission, denyMessage)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPermission(@NotNull ProxiedPlayer player, @NotNull String permission) {
        if (player.hasPermission("*")) return true;
        if (player.hasPermission(Permissions.OVERRIDE.getPermission())) return true;
        return player.hasPermission(permission);
    }

    public static boolean hasPermission(@NotNull ProxiedPlayer player, @NotNull List<String> permissions) {
        for (String permission : permissions) {
            if (hasPermission(player, permission)) {
                return true;
            }
        }
        return false;
    }

}
