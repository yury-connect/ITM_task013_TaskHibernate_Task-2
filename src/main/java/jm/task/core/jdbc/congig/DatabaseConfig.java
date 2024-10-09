package jm.task.core.jdbc.congig;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DatabaseConfig {

    private static final String PROPERTIES_FILE = "src/main/resources/db.properties";

    public static Connection getConnection() throws IOException, SQLException {
        Properties properties = new Properties();

        // Чтение файла свойств
        try (FileInputStream input = new FileInputStream(PROPERTIES_FILE)) {
            properties.load(input);
        }

        // Получение свойств из файла
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        String driver = properties.getProperty("db.driver");

        // Загрузка драйвера
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Установка соединения с БД
        return DriverManager.getConnection(url, username, password);
    }

}
