package jm.task.core.jdbc.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


// реализуйте логику, которая создает соеденение с БД
public class Util {

    private static final String PROPERTIES_FILE = "src/main/resources/db_config.properties";

    private static Connection connection; // for JDBC


    public static Connection getConnection() {
        Properties properties = new Properties();


        try (Reader reader = new InputStreamReader(new FileInputStream(PROPERTIES_FILE), StandardCharsets.UTF_8)) {
            properties.load(reader); // Чтение файла свойств
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Получение свойств из файла
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        String driver = properties.getProperty("db.driver");

        System.out.printf("url = %s\nusername = %s\npassword = %s\ndriver = %s", url, username, password, driver);

        // Загрузка драйвера
        try {
            Class.forName(driver); // загрузить класс драйвера JDBC в память и зарегистрировать его в DriverManager
            connection = DriverManager.getConnection(url, username, password); // Установка соединения с БД
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
