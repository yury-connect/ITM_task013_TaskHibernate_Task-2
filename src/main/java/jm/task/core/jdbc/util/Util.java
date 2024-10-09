package jm.task.core.jdbc.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


// реализуйте логику, которая создает соеденение с БД
public class Util {

    private static final String PROPERTIES_FILE = "db.properties";

    public static Connection getConnection() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE)) {
            props.load(fis);
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            return DriverManager.getConnection(url, user, password);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
