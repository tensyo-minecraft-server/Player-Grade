package moe.nmkmn.player_grade.database;

import moe.nmkmn.player_grade.PlayerGrade;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private final Connection connection;

    public Database(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void initialize() throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS Player (uuid varchar(36) primary key, votesCast int, playTime long, balance double, blockDestruction long)");
        statement.close();
    }
}
