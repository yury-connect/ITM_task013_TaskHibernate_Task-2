package jm.task.core.jdbc.util;

import jm.task.core.jdbc.exception.ItmConnectionException;
import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;


public class UtilHibernate {

    private static SessionFactory sessionFactory; // for Hibernate


    public static SessionFactory getSessionFactory() {
        final String hibernatePropertiesFileName = "hibernateConfig.properties";
        if (sessionFactory == null) {
            try (InputStream input = UtilHibernate.class.getClassLoader().getResourceAsStream(hibernatePropertiesFileName)) {
                if (input == null) {
                    throw new RuntimeException("Не удалось найти файл '" + hibernatePropertiesFileName + "'");
                }
                Properties properties = new Properties();
                properties.load(input);

                Configuration configuration = new Configuration();

                // Подключаем 'hibernate.cfg.xml'
                configuration.configure();

                // Добавляем аннотированный класс до создания SessionFactory
                configuration.addAnnotatedClass(User.class); // Добавление аннотированных классов должно происходить до создания SessionFactory. // т.е. до buildSessionFactory()

//                configuration.setProperties(properties);

//                sessionFactory = configuration.configure().buildSessionFactory();

                // Строим фабрику сессий
                sessionFactory = configuration.buildSessionFactory();
            } catch (IOException e) {
                System.err.println("\n\n\nException:   SessionFactory getSessionFactory\n\n");
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}

