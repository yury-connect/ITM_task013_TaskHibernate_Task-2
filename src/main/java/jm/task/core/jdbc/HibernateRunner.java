package jm.task.core.jdbc;


import jm.task.core.jdbc.util.UtilHibernate;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateRunner {

    public static void main(String[] args) {
        UtilHibernate.getConnection();
    }
}
