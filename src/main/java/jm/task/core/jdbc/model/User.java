package jm.task.core.jdbc.model;

import jakarta.persistence.*;
import lombok.*;




@Data // генерирует все полями данного класса в виде геттеров, сеттеров, конструкторов, toString() и hashCode()
@NoArgsConstructor
@AllArgsConstructor
@Builder // Чтобы генерировать красивым образом эту сущность (пример ниже по коду)
@Entity
@Table(name = "users", schema = "public") //  таблица в базе данных, к которой привязан этот класс, имеет имя "users"
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // значение первичного ключа будет генерироваться базой данных с использованием механизма автоинкремента
    @Column(name = "user_id", updatable = false, nullable = false) //  updatable = false: это поле нельзя изменять после того, как оно было установлено при первоначальном сохранении сущности в базу данных.
    private Long id;

    @Column(name = "user_name", length = 50, nullable = false)
    private String name;

    @Column(name = "user_lastName", length = 70, nullable = false)
    private String lastName;

    @Column(name = "user_age", nullable = false)
    private Byte age;


    public User(String name, String lastName, Byte age) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }
}

/*
Сгенерированный Builder позволяет создавать объект следующим образом:
User user = User.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@example.com")
                .age(30)
                .build();
 */

