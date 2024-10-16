package jm.task.core.jdbc.util;

import jm.task.core.jdbc.exception.ItmConnectionException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.*;


public final class UtilJDBC extends AbstractUtil {

    private static final String DB_PROPERTIES_FILE_NAME = "configurations/db_JDBC_config.properties";
    private static Connection connection; // for JDBC
    private static final Logger LOGGER = getLogger(UtilJDBC.class);


    public static Connection getConnection() {

        String url = null;
        String username = null;
        String password = null;
        String driver = null;

        try (Reader reader = new InputStreamReader(
                new FileInputStream(PATH_TO_RESOURCES + DB_PROPERTIES_FILE_NAME) , StandardCharsets.UTF_8)) {
            if (connection == null || connection.isClosed()) {
                Properties properties = new Properties();
                properties.load(reader);

                url = properties.getProperty("db.url");
                username = properties.getProperty("db.username");
                password = properties.getProperty("db.password");
                driver = properties.getProperty("db.driver");

                LOGGER.fine("getConnection(): Properties loaded successfully; " +
                          "driver = '" + driver
                        + "'; url = '" + url
                        + "'; username = '" + username + "'"
                        + "'; password = '" + "*".repeat(password.length()) + "'");

            /*
             // Данный кусок кода почему-то не работает корректно, передается ЛИБО имя метода ЛИБО имя класса, вместе никак!
            LOGGER.logp(
                    Level.FINE,
                    Thread.currentThread().getStackTrace()[1].getMethodName(), // Имя текущего метода получим динамически
                    Thread.currentThread().getStackTrace()[0].getClassName(), // Имя текущего класса получим динамически
                    "Тестовое сообщение"); // удалить
            */

                Class.forName(driver); // загрузить класс драйвера JDBC в память и зарегистрировать его в DriverManager
                connection = DriverManager.getConnection(url, username, password); // Установка соединения с БД

                LOGGER.fine("getConnection(): getConnection is successfully; ");
            }

        } catch (FileNotFoundException e) { // файл 'db_config.properties' не найден по указанному пути
            String message = String.format("The file named '%s' was not found", DB_PROPERTIES_FILE_NAME);
            LOGGER.log(Level.SEVERE, message, e);
            throw new ItmConnectionException(message, e);

        } catch (IOException e) { // Чтение файла 'db_config.properties' не удалось
            String message = String.format("Failed to read the file named '%s'", DB_PROPERTIES_FILE_NAME);
            LOGGER.log(Level.SEVERE, message, e);
            throw new ItmConnectionException(message, e);

        } catch (ClassNotFoundException e) { // Class.forName(driver) - загрузка драйвера JDBC не удалась
            String message = String.format("Failed to load the driver named '%s'", driver);
            LOGGER.log(Level.SEVERE, message, e);
            throw new ItmConnectionException(message, e);

        } catch (SQLException e) { // DriverManager.getConnection(url, username, password) - соединение с БД не удалось
            String message = String.format("Failed to connect to the database. " +
                    "Parameters: driver: '%s' url: '%s'; username: '%s'", driver, url, username);
            LOGGER.log(Level.SEVERE, message, e);
            throw new ItmConnectionException(message, e);

        }
        LOGGER.log(Level.FINER, "getConnection(): Connection established successfully");
        return connection;
    }


    public static void closeConnection() {
        try {
            if (connection!= null && !connection.isClosed()) {
                connection.close();
                LOGGER.log(Level.FINE, "closeConnection(): Connection closed successfully");
            }
        } catch (SQLException e) {
            String message = "Failed to close the connection";
            LOGGER.log(Level.SEVERE, message, e);
            throw new ItmConnectionException(message, e);
        }
    }




    /*
    Для PostgreSQL, чтобы убедиться, что таблица существует в конкретной схеме (например, в схеме 'public',
    которая является схемой по умолчанию в PostgreSQL), база данных при подключении уже используется автоматически,
    так что на этом этапе достаточно проверить наличие таблицы в схеме.
    - pg_tables: Используется для получения списка таблиц в текущей подключенной базе данных.
    - Схема: В запросе проверяется, существует ли таблица 'users' в схеме 'public' (по умолчанию для PostgreSQL).
    - База данных: При подключении через JDBC подключение автоматически работает с базой данных
    'ITM_task012_TaskJDBC_Task_1_db', так что дополнительной проверки для имени базы данных в запросе не требуется.
     */
    public static boolean isExistsTable() {
        boolean result = false; // ITM_task012_TaskJDBC_Task_1_db
        String query = "SELECT EXISTS (SELECT 1 FROM pg_tables WHERE schemaname = 'public' AND tablename = 'users')";

        try (Statement statement = connection.createStatement()) {

            /*
        - Запрос проверяет наличие таблицы в схеме 'public' через системное представление 'information_schema.tables';
        - Если таблица существует, будет возвращено TRUE. Если не существует — FALSE.
        */

            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                result = resultSet.getBoolean(1); // Получаем результат (true или false)
            }
        } catch (SQLException e) {
            LOGGER.warning("SQLException " + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return result;
    }
}
