package org.mmo.common.config.server;

/**
 * mongodb 配置
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
public class MongoConfig {

    private String host;
    private int port;
    private String authenticationDatabase;
    private String database;
    private String password;
    private String username;
    /**
     * mongodb连接地址
     */
    private String url;


    public MongoConfig() {
    }

    public MongoConfig(String host, int port, String authenticationDatabase, String database, String password, String username) {
        this.host = host;
        this.port = port;
        this.authenticationDatabase = authenticationDatabase;
        this.database = database;
        this.password = password;
        this.username = username;
    }

    public MongoConfig(String database, String url) {
        this.database = database;
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAuthenticationDatabase() {
        return authenticationDatabase;
    }

    public void setAuthenticationDatabase(String authenticationDatabase) {
        this.authenticationDatabase = authenticationDatabase;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 通过ip地址，端口构建URL
     *
     * @return
     */
    public String buildUrl() {
        return String.format("mongodb://%s:%s@%s:%s", username, password, host, port);
    }
}
