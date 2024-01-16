package moe.nmkmn.player_grade.database;

import moe.nmkmn.player_grade.models.PlayerModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDB {
    public void create(Connection connection, PlayerModel playerModel) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO Player(uuid, votesCast, playTime, balance, blockDestruction) VALUES (?, ?, ?, ?, ?)"
        );

        statement.setString(1, playerModel.getUUID());
        statement.setInt(2, playerModel.getVotesCast());
        statement.setLong(3, playerModel.getPlayTime());
        statement.setDouble(4, playerModel.getBalance());
        statement.setLong(5, playerModel.getBlockDestruction());

        statement.executeUpdate();
        connection.commit();
        statement.close();
    }

    public void update(Connection connection, PlayerModel playerModel) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE Player SET votesCast = ?, playTime = ?, balance = ?, blockDestruction = ? WHERE uuid = ?"
        );

        statement.setInt(1, playerModel.getVotesCast());
        statement.setLong(2, playerModel.getPlayTime());
        statement.setDouble(3, playerModel.getBalance());
        statement.setLong(4, playerModel.getBlockDestruction());
        statement.setString(5, playerModel.getUUID());

        statement.executeUpdate();
        connection.commit();
        statement.close();
    }

    public PlayerModel findByUUID(Connection connection, String uuid) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Player WHERE uuid = ?");
        statement.setString(1, uuid);

        ResultSet resultSet = statement.executeQuery();

        PlayerModel playerModel;

        if(resultSet.next()){
            playerModel = new PlayerModel(resultSet.getString("uuid"), resultSet.getInt("votesCast"), resultSet.getLong("playTime"), resultSet.getLong("balance"), resultSet.getLong("blockDestruction"));

            statement.close();

            return playerModel;
        }

        statement.close();

        return null;
    }
}
