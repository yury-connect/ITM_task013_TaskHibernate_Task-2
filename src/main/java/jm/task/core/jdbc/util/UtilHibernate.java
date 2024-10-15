package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;


public final class UtilHibernate extends AbstractUtil {

    private static SessionFactory sessionFactory; // for Hibernate
    private static final Logger LOGGER = getLogger(UtilHibernate.class);


    public static SessionFactory getSessionFactory() {
        final String hibernatePropertiesFileName = "db_Hibernate_config.properties";
        if (sessionFactory == null || sessionFactory.isClosed()) {
            try (InputStream input = UtilHibernate.class.getClassLoader().getResourceAsStream(hibernatePropertiesFileName)) {
                if (input == null) {
                    throw new RuntimeException("Не удалось найти файл '" + hibernatePropertiesFileName + "'");
                }
                Properties properties = new Properties();
                properties.load(input);

                properties.forEach((key, value) -> LOGGER.fine(key + " = " + value));

                LOGGER.fine("getConnection(): Properties loaded successfully;"
                        + " driver = '" + properties.getProperty("hibernate.connection.driver_class") + "';"
                        + " url = '" + properties.getProperty("hibernate.connection.url") + "';"
                        + " username = '" + properties.getProperty("hibernate.connection.username") + "';"
                        + " password = '" + "*".repeat(properties.getProperty("hibernate.connection.password").length()) + "'");

                Configuration configuration = new Configuration();

//                configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy()); // Используем конвейер имен для Hibernate / в моем случае -я вручную именую поля таблиц в сущности

                // Подключаем 'hibernate.cfg.xml' / так давно не делают, а делают через аннотации, см. ниже
                configuration.configure();

                // Добавляем аннотированный класс до создания SessionFactory
                configuration.addAnnotatedClass(User.class); // Добавление аннотированных классов должно происходить до создания SessionFactory. // т.е. до buildSessionFactory()

                configuration.setProperties(properties);

                // Строим фабрику сессий
                sessionFactory = configuration.buildSessionFactory();

                LOGGER.fine("getConnection(): getConnection is successfully; ");
            } catch (IOException e) {
                System.err.println("\n\n\nException:   SessionFactory getSessionFactory\n\n");
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}

