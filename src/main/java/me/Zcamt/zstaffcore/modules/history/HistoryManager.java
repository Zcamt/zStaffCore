package me.Zcamt.zstaffcore.modules.history;

import me.Zcamt.zstaffcore.ZStaffCore;
import me.Zcamt.zstaffcore.utils.Config;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HistoryManager {
    
    public boolean createHistoryDatabase() {
        try {
            CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
            ZStaffCore.getThreadPool().submit(() -> {
                try {
                    ZStaffCore.getInstance().getMySql().execute("CREATE TABLE IF NOT EXISTS " + Config.database + ".zstaffcore_history " +
                            "(" +
                            "id bigint(20) AUTO_INCREMENT," +
                            "date timestamp," +
                            "name varchar(128)," +
                            "uuid varchar(36)," +
                            "ip varchar(45)," +
                            "PRIMARY KEY (id)," +
                            "INDEX (id,name,uuid,ip)" +
                            ")");
                    completableFuture.complete(true);
                } catch (SQLException e) {
                    ZStaffCore.getInstance().getLogger().severe("Wasn't able to create database table!");
                    completableFuture.complete(false);
                }
            });
            return completableFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            ZStaffCore.getInstance().getLogger().severe("Wasn't able to create database table!");
            return false;
        }
    }

    public void createHistory(ProxiedPlayer player){
        ZStaffCore.getThreadPool().submit(() -> {
            try {
                String query = "INSERT INTO " + Config.database + ".zstaffcore_history " +
                        "(id, " +
                        "date, " +
                        "name, " +
                        "uuid, " +
                        "ip) " +

                        "VALUES " +
                        "(DEFAULT, DEFAULT, ?, ?, ?)";

                PreparedStatement ps = ZStaffCore.getInstance().getMySql().preparedStatement(query);
                ps.setString(1, player.getName());
                ps.setString(2, player.getUniqueId().toString());
                ps.setString(3, player.getAddress().getHostString());

                ps.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException("zStaffCore couldn't save history to database! For uuid: " + player.getUniqueId().toString(), e);
            }
        });
    }

    public void updateHistory(ProxiedPlayer player){
        ZStaffCore.getThreadPool().submit(() -> {
            try {
                ResultSet rs = getHistoryResultSetFromUUID(player.getUniqueId());
                if (rs == null) {
                    ZStaffCore.getInstance().getLogger().severe("zStaffCore wasn't able to find " + player.getUniqueId() + " in the database and wasn't able to check for history!");
                    return;
                }

                ZStaffCore.getInstance().getMySql().preparedStatement("UPDATE " + Config.database + ".zstaffcore_history " +
                        "SET " +
                        "name = '" + player.getName() + "', " +
                        "ip = '" + player.getAddress().getHostString() + "', " +
                        "date = '" + Timestamp.from(Instant.now()) + "' " +
                        "WHERE " +
                        "uuid = '" + player.getUniqueId() + "'").executeUpdate();
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException("zStaffCore couldn't update history to database! For uuid: " + player.getUniqueId().toString(), e);
            }
        });
    }

    public String getNameFromUUID(UUID uuid) {
        try {
            ResultSet rs = getHistoryResultSetFromUUID(uuid);
            if (rs == null) {
                return null;
            }
            String name = rs.getString("name");
            rs.close();
            return name;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UUID getUUIDFromName(String name){
        try {
            ResultSet rs = getHistoryResultSetFromName(name);
            if (rs == null) {
                return null;
            }
            UUID uuid = UUID.fromString(rs.getString("uuid"));
            rs.close();
            return uuid;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getLastIPAddress(UUID uuid){
        try {
            ResultSet rs = getHistoryResultSetFromUUID(uuid);
            if (rs == null) {
                return null;
            }
            String ip = rs.getString("ip");
            rs.close();
            return ip;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getHistoryResultSetFromUUID(UUID uuid){
        try {
            CompletableFuture<ResultSet> completableResultSet = new CompletableFuture<>();
            ZStaffCore.getThreadPool().submit(() -> {
                try {
                    PreparedStatement ps = ZStaffCore.getInstance().getMySql().preparedStatement("SELECT * FROM " + Config.database + ".zstaffcore_history WHERE uuid = ?");
                    ps.setString(1, uuid.toString());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        completableResultSet.complete(rs);
                    } else {
                        completableResultSet.complete(null);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    ZStaffCore.getInstance().getLogger().severe("zStaffCore wasn't able to find " + uuid + " in the database and wasn't able to check for history!");
                    completableResultSet.complete(null);
                }
            });
            return completableResultSet.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            ZStaffCore.getInstance().getLogger().severe("zStaffCore wasn't able to find " + uuid + " in the database and wasn't able to check for history!");
            return null;
        }
    }

    public ResultSet getHistoryResultSetFromName(String name){
        try {
            CompletableFuture<ResultSet> completableResultSet = new CompletableFuture<>();
            ZStaffCore.getThreadPool().submit(() -> {
                try {
                    PreparedStatement ps = ZStaffCore.getInstance().getMySql().preparedStatement("SELECT * FROM " + Config.database + ".zstaffcore_history WHERE name = ?");
                    ps.setString(1, name);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        completableResultSet.complete(rs);
                    } else {
                        completableResultSet.complete(null);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    ZStaffCore.getInstance().getLogger().severe("zStaffCore wasn't able to find " + name + " in the database and wasn't able to check for history!");
                    completableResultSet.complete(null);
                }
            });
            return completableResultSet.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            ZStaffCore.getInstance().getLogger().severe("zStaffCore wasn't able to find " + name + " in the database and wasn't able to check for history!");
            return null;
        }
    }
    
    public List<UUID> getUUIDListFromIP(String ip) {
        CompletableFuture<List<UUID>> completableFuture = new CompletableFuture<>();
        try {
            ZStaffCore.getThreadPool().submit(() -> {
                try {
                    int uuidLimit = 10;
                    int amounts = 0;
                    List<UUID> uuids = new ArrayList<>();
                    ResultSet rs = ZStaffCore.getInstance().getMySql().preparedStatement("SELECT DISTINCT uuid, ip FROM " + Config.database + ".zstaffcore_history WHERE IP = '" + ip + "' ORDER BY ID desc").executeQuery();
                    while (rs.next()) {
                        if (amounts >= uuidLimit) break;
                        String targetUUID = rs.getString("uuid");
                        if (uuids.contains(UUID.fromString(targetUUID))) continue;
                        uuids.add(UUID.fromString(targetUUID));
                        amounts++;
                    }
                    rs.close();
                    completableFuture.complete(uuids);

                } catch (SQLException e) {
                    e.printStackTrace();
                    ZStaffCore.getInstance().getLogger().severe("zStaffCore wasn't able to find " + ip + " in the database and wasn't able to check for history!");
                    completableFuture.complete(Collections.emptyList());
                }
            });
            return completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            ZStaffCore.getInstance().getLogger().severe("zStaffCore wasn't able to find " + ip + " in the database and wasn't able to check for history!");
            return Collections.emptyList();
        }

    }

    public boolean ipExistsOnServer(String ip) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        try {
            ZStaffCore.getThreadPool().submit(() -> {
                try {
                    ResultSet rs = ZStaffCore.getInstance().getMySql().preparedStatement("SELECT * FROM " + Config.database + ".zstaffcore_history WHERE ip = '" + ip + "'").executeQuery();

                    if (!rs.next()) completableFuture.complete(false);
                    if(rs.getInt(1) == 0) completableFuture.complete(false);
                    completableFuture.complete(true);

                } catch (SQLException e) {
                    e.printStackTrace();
                    ZStaffCore.getInstance().getLogger().severe("zStaffCore wasn't able to find " + ip + " in the database and wasn't able to check for history!");
                    completableFuture.complete(false);
                }
            });
            return completableFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            ZStaffCore.getInstance().getLogger().severe("zStaffCore wasn't able to find " + ip + " in the database and wasn't able to check for history!");
            return false;
        }
    }

}
