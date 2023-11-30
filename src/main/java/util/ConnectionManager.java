package util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.SQLException;

import static util.properties.Properties.DATABASE_PROPERTIES;

@UtilityClass
public class ConnectionManager {
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        config.setDriverClassName(DATABASE_PROPERTIES.dbDriver());
        config.setJdbcUrl(DATABASE_PROPERTIES.dbUrl());
        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
