package moe.nmkmn.player_grade.listeners;

import moe.nmkmn.player_grade.PlayerGrade;
import moe.nmkmn.player_grade.database.Database;
import moe.nmkmn.player_grade.database.PlayerDB;
import moe.nmkmn.player_grade.models.PlayerModel;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.Connection;
import java.sql.SQLException;

public class BlockDestruction implements Listener {

    private final PlayerGrade playerGrade;
    private final Database database;

    public BlockDestruction(PlayerGrade playerGrade, Database database) {
        this.playerGrade = playerGrade;
        this.database = database;
    }

    public PlayerModel getPlayerFromDatabase(Connection connection, Player player) throws SQLException {
        PlayerDB playerDB = new PlayerDB();
        PlayerModel playerModel = playerDB.findByUUID(connection, player.getUniqueId().toString());

        if (playerModel == null) {
            playerModel = new PlayerModel(player.getUniqueId().toString(), 0, 0, 0.0, 0);
            playerDB.create(connection, playerModel);
        }

        return playerModel;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();

        playerGrade.getLogger().info(playerGrade.getConfig().getList("General.ignore_blocks").toString());

        if (playerGrade.getConfig().getList("General.ignore_blocks").contains(block.getType().toString())) {
            return;
        }

        try {
            PlayerDB playerDB = new PlayerDB();

            PlayerModel playerModel = getPlayerFromDatabase(database.getConnection(), player);
            playerModel.setBlockDestruction(playerModel.getBlockDestruction() + 1);
            playerDB.update(database.getConnection(), playerModel);
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }
}
