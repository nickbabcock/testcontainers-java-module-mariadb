package org.testcontainers.junit;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.MariaDBContainer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.rnorth.visibleassertions.VisibleAssertions.assertEquals;

public class CustomizableMariaDbTest {
    private static final String DB_NAME = "foo";
    private static final String USER = "bar";
    private static final String PWD = "baz";
    private static final String ROOT_PWD = "qux";

    // Add MYSQL_ROOT_HOST environment so that we can root login from anywhere for testing purposes
    @Rule
    public MariaDBContainer maria = (MariaDBContainer) new MariaDBContainer("mariadb:5.5")
        .withDatabaseName(DB_NAME)
        .withUsername(USER)
        .withPassword(PWD)
        .withRootPassword(ROOT_PWD)
        .withEnv("MYSQL_ROOT_HOST", "%");

    @Test
    public void testSimple() throws SQLException {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mariadb://"
            + maria.getContainerIpAddress()
            + ":" + maria.getMappedPort(MariaDBContainer.MARIADB_PORT)
            + "/" + DB_NAME);
        hikariConfig.setUsername(USER);
        hikariConfig.setPassword(PWD);

        HikariDataSource ds = new HikariDataSource(hikariConfig);
        Statement statement = ds.getConnection().createStatement();
        statement.execute("SELECT 1");
        ResultSet resultSet = statement.getResultSet();

        resultSet.next();
        int resultSetInt = resultSet.getInt(1);
        assertEquals("A basic SELECT query succeeds", 1, resultSetInt);
    }

    @Test
    public void testRootSimple() throws SQLException {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mariadb://"
                + maria.getContainerIpAddress()
                + ":" + maria.getMappedPort(MariaDBContainer.MARIADB_PORT)
                + "/" + DB_NAME);
        hikariConfig.setUsername("root");
        hikariConfig.setPassword(ROOT_PWD);

        HikariDataSource ds = new HikariDataSource(hikariConfig);
        Statement statement = ds.getConnection().createStatement();
        statement.execute("SELECT 1");
        ResultSet resultSet = statement.getResultSet();

        resultSet.next();
        int resultSetInt = resultSet.getInt(1);
        assertEquals("A basic SELECT query succeeds", 1, resultSetInt);
    }
}
