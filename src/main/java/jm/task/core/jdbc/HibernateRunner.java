package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateRunner {

    public static void main(String[] args) {
//        UtilHibernate.getConnection();

        Configuration configuration = new Configuration();
        configuration.configure(); // в нашем случае можем опустить путь "hibernate.cfg.xml"
        configuration.addAnnotatedClass(User.class); // Добавление аннотированных классов должно происходить до создания SessionFactory. // т.е. до buildSessionFactory()

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            User user = User.builder()
                    .name("John Doe")
                    .lastName("Smith")
                    .age((byte) 30)
                    .build();

            session.save(user);
            System.out.println("ок");
        }
    }
}
