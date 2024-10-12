package jm.task.core.jdbc.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;


public class UtilHibernate {


    public static Connection getConnection() {
        Configuration configuration = new Configuration();
//        configuration.configure("hibernate.cfg.xml"); // path to configuration file
        configuration.configure(); // в нашем случае можем опустить путь

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            System.out.println("ок");
        }
        return null; // Placeholder for actual connection
    }



}
