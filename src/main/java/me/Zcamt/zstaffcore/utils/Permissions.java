package me.Zcamt.zstaffcore.utils;

public enum Permissions {
    STAFF("zStaffCore.staff"),
    OVERRIDE("zStaffCore.*");

    private String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
