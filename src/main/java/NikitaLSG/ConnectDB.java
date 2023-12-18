package NikitaLSG;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import NikitaLSG.ChangingData;
public class ConnectDB extends Interface {
    private static Connection connection;
    private String url;
    private String username;
    private String password;

    public ConnectDB(String host, String service, String user, String password) {
        super(host, service, user, password);
        this.url = "jdbc:oracle:thin:@//" + host + ":1521/" + service; //jdbc:oracle:thin:@//10.242.100.15:1521/dblab.miit.ru
        this.username = user;
        this.password = new String(password);

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Successfully connected to the database");
        } catch (SQLException e) {
            System.err.println("Failed to establish a database connection");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
