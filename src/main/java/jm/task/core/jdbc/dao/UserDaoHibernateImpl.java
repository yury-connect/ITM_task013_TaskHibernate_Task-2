package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.UtilHibernate;
import jm.task.core.jdbc.util.UtilJDBC;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {

    private static final Logger LOGGER = UtilJDBC.getLogger(UserDaoHibernateImpl.class);


    public UserDaoHibernateImpl() {
        // NOP
    }


    @Override
    public void createUsersTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                + "user_id BIGSERIAL PRIMARY KEY, "
                + "user_name VARCHAR(50) NOT NULL, "
                + "user_lastName VARCHAR(70) NOT NULL, "
                + "user_age SMALLINT NOT NULL"
                + ")";
        executeUpdate(createTableSQL, "Таблица 'users' успешно создана (если не существовала).");
    }

    @Override
    public void dropUsersTable() {
        String dropTableSQL = "DROP TABLE IF EXISTS users";
        executeUpdate(dropTableSQL, "Таблица 'users' успешно удалена (если существовала).");
    }


    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = UtilHibernate.getSessionFactory().openSession();) {
            transaction = session.beginTransaction(); // Начинаем транзакцию (у Hibernate нужно вручную открывать и закрывать транзауции)

            User userSave = User.builder()
                    .name(name)
                    .lastName(lastName)
                    .age(age)
                    .build();

            session.save(userSave); // Сохраняем пользователя
            transaction.commit(); // Подтверждаем транзакцию
            LOGGER.log(Level.FINE, "Пользователь '" + name + "' успешно сохранен.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Откатываем транзакцию в случае ошибки
            }
            LOGGER.log(Level.WARNING, "Ошибка при сохранении пользователя: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = UtilHibernate.getSessionFactory().openSession();) {
            transaction = session.beginTransaction();

            User userRemove = session.get(User.class, id);
            if (userRemove != null) {
                session.remove(userRemove);
                LOGGER.log(Level.FINE, "Пользователь с ID " + id + " успешно удален.");
                System.out.println("Пользователь с ID " + id + " успешно удален.");
            } else {
                LOGGER.log(Level.WARNING, "Пользователь с ID " + id + " не найден.");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.WARNING, "Ошибка при удалении пользователя: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = null;
        try (Session session = UtilHibernate.getSessionFactory().openSession();) {
            users = session.createQuery("FROM User", User.class).list();

            LOGGER.log(Level.FINE, "Получено " + users.size() + " пользователей.");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Ошибка при получении всех пользователей: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String cleanTableSQL = "DELETE FROM users";
        executeUpdate(cleanTableSQL, "Таблица 'users' очищена.");
    }




    // ***** services *****
    private void executeUpdate(String sql, String successMessage) {
        Transaction transaction = null;
        try (Session session = UtilHibernate.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
            LOGGER.log(Level.FINE, successMessage + " : finished");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.WARNING, "Ошибка при выполнении SQL запроса: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
