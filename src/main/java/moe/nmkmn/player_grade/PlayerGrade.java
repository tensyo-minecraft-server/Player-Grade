package moe.nmkmn.player_grade;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import moe.nmkmn.player_grade.database.Database;
import moe.nmkmn.player_grade.listeners.BlockDestruction;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public final class PlayerGrade extends JavaPlugin {
    private DataSource dataSource;
    private static int increment = 1;
    private static boolean useMariaDbDriver = false;

    private static synchronized void increment() {
        increment++;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        try {
            loadDataSource();
        } catch (RuntimeException e) {
            dataSource = null;
            useMariaDbDriver = true;
            loadDataSource();
        }

        try {
            Database database = new Database(dataSource.getConnection());

            database.initialize();

            getServer().getPluginManager().registerEvents(new BlockDestruction(this, database), this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void loadDataSource() {
        try {

            HikariConfig hikariConfig = new HikariConfig();

            String host = getConfig().getString("Database.mysql.host");
            String port = getConfig().getString("Database.mysql.port");
            String database = getConfig().getString("Database.mysql.database");
            String launchOptions = getConfig().getString("Database.mysql.launch_options");

            if (launchOptions.isEmpty() || !launchOptions.matches("\\?((([\\w-])+=.+)&)*(([\\w-])+=.+)")) {
                launchOptions = "?rewriteBatchedStatements=true&useSSL=false";
            }

            hikariConfig.setDriverClassName(useMariaDbDriver ? "org.mariadb.jdbc.Driver" : "com.mysql.cj.jdbc.Driver");
            String protocol = useMariaDbDriver ? "jdbc:mariadb" : "jdbc:mysql";
            hikariConfig.setJdbcUrl(protocol + "://" + host + ":" + port + "/" + database + launchOptions);

            String username = getConfig().getString("Database.mysql.user");
            String password = getConfig().getString("Database.mysql.password");

            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            hikariConfig.addDataSourceProperty("connectionInitSql", "set time_zone = '+00:00'");

            hikariConfig.setPoolName("Plan Connection Pool-" + increment);
            increment();

            hikariConfig.setAutoCommit(false);
            hikariConfig.setMaximumPoolSize(getConfig().getInt("Database.mysql.max_connections"));
            hikariConfig.setMaxLifetime(getConfig().getInt("Database.mysql.max_lifetime") * 60000L);
            hikariConfig.setLeakDetectionThreshold((getConfig().getInt("Database.mysql.max_lifetime") * 60000L) + TimeUnit.SECONDS.toMillis(4L));

            this.dataSource = new HikariDataSource(hikariConfig);
        } catch (HikariPool.PoolInitializationException e) {
            if (e.getMessage().contains("Unknown system variable 'transaction_isolation'")) {
                throw new RuntimeException("MySQL driver is incompatible with database that is being used.", e);
            }
            throw new RuntimeException("Failed to set-up HikariCP Datasource: " + e.getMessage(), e);
        }
    }
}
