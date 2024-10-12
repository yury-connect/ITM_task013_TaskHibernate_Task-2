\c ITM_task013_TaskHibernate_Task_2_db;                         -- Переключает активную базу данных на ITM_task013_TaskHibernate_Task_2_db;

DROP TABLE IF EXISTS users;
DROP DATABASE IF EXISTS ITM_task013_TaskHibernate_Task_2_db;    -- Удаляю базу если она существует;

CREATE DATABASE ITM_task013_TaskHibernate_Task_2_db;            -- Создаю базу если она еще не существует;

CREATE USER user1 WITH PASSWORD '1234';                                         -- Создаем пользователя с паролем
GRANT ALL PRIVILEGES ON DATABASE ITM_task013_TaskHibernate_Task_2_db TO user1;  -- Даём пользователю права на базу данных

CREATE TABLE users (
                       user_id BIGSERIAL PRIMARY KEY,           -- Поле id с автоинкрементом (эквивалент LONG в Java);
                       user_name VARCHAR(50) NOT NULL,          -- Поле name с максимальной длиной 50 символов;
                       user_lastName VARCHAR(70) NOT NULL,      -- Поле lastName с максимальной длиной 70 символов;
                       user_age SMALLINT NOT NULL               -- Поле age, хранящее возраст как целое число;
);
SELECT * FROM users;

DROP TABLE IF EXISTS users;
DROP DATABASE IF EXISTS ITM_task013_TaskHibernate_Task_2_db;        -- Удаляю базу если она существует;
