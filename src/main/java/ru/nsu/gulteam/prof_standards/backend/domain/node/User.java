package ru.nsu.gulteam.prof_standards.backend.domain.node;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import ru.nsu.gulteam.prof_standards.backend.domain.type.UserRole;

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

    public User() {
    }

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

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
