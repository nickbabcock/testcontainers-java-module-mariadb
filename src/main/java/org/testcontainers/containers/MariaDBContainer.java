package org.testcontainers.containers;

/**
 * Container implementation for the MariaDB project.
 * 
 * @author Miguel Gonzalez Sanchez
 */
public class MariaDBContainer<SELF extends MariaDBContainer<SELF>> extends JdbcDatabaseContainer<SELF> {

    public static final String NAME = "mariadb";
    public static final String IMAGE = "mariadb";
    public static final Integer MARIADB_PORT = 3306;
    private static final String MY_CNF_CONFIG_OVERRIDE_PARAM_NAME = "TC_MY_CNF";
    private String databaseName = "test";
    private String username = "test";
    private String password = "test";
    private String rootPassword = "test";

    public MariaDBContainer() {
        super(IMAGE + ":latest");
    }

    public MariaDBContainer(String dockerImageName) {
        super(dockerImageName);
    }

    @Override
    protected Integer getLivenessCheckPort() {
        return getMappedPort(MARIADB_PORT);
    }

    @Override
    protected void configure() {
        optionallyMapResourceParameterAsVolume(MY_CNF_CONFIG_OVERRIDE_PARAM_NAME, "/etc/mysql/conf.d", "mariadb-default-conf");

        addExposedPort(MARIADB_PORT);
        addEnv("MYSQL_DATABASE", databaseName);
        addEnv("MYSQL_USER", username);
        addEnv("MYSQL_PASSWORD", password);
        addEnv("MYSQL_ROOT_PASSWORD", rootPassword);
        setCommand("mysqld");
        setStartupAttempts(3);
    }

    @Override
    public String getDriverClassName() {
        return "org.mariadb.jdbc.Driver";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:mariadb://" + getContainerIpAddress() + ":" + getMappedPort(MARIADB_PORT) + "/" + databaseName;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getTestQueryString() {
        return "SELECT 1";
    }

    public SELF withConfigurationOverride(String s) {
        parameters.put(MY_CNF_CONFIG_OVERRIDE_PARAM_NAME, s);
        return self();
    }

    public SELF withDatabaseName(final String databaseName) {
        this.databaseName = databaseName;
        return self();
    }

    public SELF withUsername(final String username) {
        this.username = username;
        return self();
    }

    public SELF withPassword(final String password) {
        this.password = password;
        return self();
    }

    public SELF withRootPassword(final String password) {
        this.rootPassword = password;
        return self();
    }
}
