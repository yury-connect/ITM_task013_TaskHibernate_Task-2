package jm.task.core.jdbc.util;

import jm.task.core.jdbc.exception.ItmConnectionException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.logging.*;


public abstract class AbstractUtil {

    protected static final String PATH_TO_RESOURCES = "src/main/resources/";


    public static Logger getLogger(Class clazz) {
        StringBuilder sb = new StringBuilder();
        sb.append("configurations/logger_");
        sb.append(clazz.getSimpleName());
        sb.append("_config.properties");

        try (Reader reader = new InputStreamReader(new FileInputStream(PATH_TO_RESOURCES + sb), StandardCharsets.UTF_8)) {
            Properties properties = new Properties();
            properties.load(reader);

            Logger logger = Logger.getLogger(clazz.getName()); // Имя логера будет соответствовать имени переданного класса
            logger.setLevel(Level.parse(properties.getProperty("global.level").toUpperCase())); // Устанавливаем общий уровень для логгера
            logger.setUseParentHandlers(false); // Отключает использование родительских обработчиков, чтобы логгирование не дублировалось.

            if (Boolean.parseBoolean(properties.getProperty("file.is_logging_include"))) { // Создаем обработчик для записи в файл
                FileHandler fileHandler = new FileHandler( // Создаем обработчик для записи в файл
                        properties.getProperty("file.log_path") + properties.getProperty("file.log_name"),
                        Boolean.parseBoolean(properties.getProperty("file.add_true_or_overwrite_false"))); // true - для добавления в конец файла
                fileHandler.setFormatter(new CustomFormatter()); // Используем кастомный форматтер для форматирования вывода в файл
                fileHandler.setLevel(Level.parse(properties.getProperty("file.level_log").toUpperCase())); // Уровень логирования для файла
                logger.addHandler(fileHandler); // Добавляем обработчики к логгеру
            }

            if (Boolean.parseBoolean(properties.getProperty("console.is_logging_include"))) { // Создаем обработчик для вывода в консоль
                ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setFormatter(new CustomFormatter()); // Используем кастомный форматтер для консоли
                consoleHandler.setLevel(Level.parse(properties.getProperty("console.level_log").toUpperCase())); // Уровень логирования для консоли
                logger.addHandler(consoleHandler); // Добавляем обработчики к логгеру
            }

            return logger;
        } catch (IOException e) {
            String message = String.format("Ошибка при настройке логгера. //\nFailed to connect to the database. " +
                    "The logger configuration file '%s' was not found in the directory '%s'", sb, PATH_TO_RESOURCES);
            throw new ItmConnectionException(message, e);
        }
    }


    // Кастомный форматер для форматирования лога по шаблону: "%1$tF %1$tT %4$s %2$s %5$s%6$s%n"
    private final static class CustomFormatter extends Formatter {

        private static final String DELIMITER = " // "; // Собственный кастомный разделитель
        private static final String PATTERN =
                          "%1$tF" + DELIMITER
                        + "%1$tT" + DELIMITER
                        + "%4$s" + DELIMITER
                        + "%2$s" + DELIMITER
                        + "%5$s%6$s%n";

        @Override
        public String format(LogRecord record) {
            return String.format(PATTERN,
                    new java.util.Date(record.getMillis()), // Дата и время
                    record.getSourceClassName(), // Имя класса
                    record.getSourceMethodName(), // Имя метода
                    record.getLevel().getName(), // Уровень логирования
                    formatMessage(record), // Сообщение
                    record.getThrown() != null ? " " + record.getThrown() : ""); // Исключения (если есть)
        }
    }
}
