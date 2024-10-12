package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.UtilHibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Test01 {

    public static void main(String[] args) {
        Transaction transaction = null; // Переносим Transaction вне try

        try (Session session = UtilHibernate.getSessionFactory().openSession()) {
            transaction = session.beginTransaction(); // Начинаем транзакцию

            User user = User.builder()
                    .id(2L)
                    .name("John Doe")
                    .lastName("Smith")
                    .age((byte) 30)
                    .build();

            session.save(user); // Сохраняем пользователя

            transaction.commit(); // Подтверждаем транзакцию
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Откатываем транзакцию в случае ошибки
            }
            e.printStackTrace(); // Логируем ошибку
        }
    }
}
