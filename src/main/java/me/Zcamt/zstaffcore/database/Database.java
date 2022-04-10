package me.Zcamt.zstaffcore.database;

import javax.sql.rowset.CachedRowSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Database {

    void connect() throws SQLException;
    void disconnect() throws SQLException;
    boolean isConnected() throws SQLException;
    PreparedStatement preparedStatement(String query) throws SQLException;
    void execute(String query) throws SQLException;

}
