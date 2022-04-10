package me.Zcamt.zstaffcore.database;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySql implements Database {
    private Connection connection;
    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;

    public MySql(String host, String port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    @Override
    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
    }

    @Override
    public void disconnect() throws SQLException {
        if(isConnected()){
            connection.close();
        }
    }

    @Override
    public boolean isConnected() throws SQLException {
        return !connection.isClosed();
    }

    @Override
    public PreparedStatement preparedStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    @Override
    public void execute(String query) throws SQLException {
        preparedStatement(query).execute();
    }
}
