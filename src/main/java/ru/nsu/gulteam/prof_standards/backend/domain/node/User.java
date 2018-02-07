package ru.nsu.gulteam.prof_standards.backend.domain.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@NodeEntity(label = "USER")
public class User {
    @GraphId
    @Property(name = "ID")
    private Long id;

    /**
     * Имя
     */
    @Property(name = "FIRST_NAME")
    private String firstName;

    /**
     * Фамилия
     */
    @Property(name = "SECOND_NAME")
    private String secondName;

    /**
     * Логин
     */
    @Property(name = "LOGIN")
    private String login;

    /**
     * Хэш пароля
     */
    @Property(name = "PASSWORD")
    private String passwordHash;

    public User(Long id, String firstName, String secondName, String login) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.login = login;
    }

    public User(String firstName, String secondName, String login, String passwordHash) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.login = login;
        this.passwordHash = passwordHash;
    }
}
