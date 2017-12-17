package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Role;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Token;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;

import java.util.List;

public interface UserRepository extends GraphRepository<User> {
    User findByLoginIgnoreCase(String login);

    @Query("START t=node({token}), u=node({user}) match (u)-[r:HAS_TOKEN]->(t) return count(r) > 0")
    boolean hasToken(@Param("user") User user, @Param("token") Token token);

    @Query("START u=node({user}) MATCH (u)-[c:IS_A]->(r:ROLE) DELETE c")
    void removeRole(@Param("user")User user);

    @Query("START u=node({user}), r=node({role}) CREATE (u)-[:IS_A]->(r) return u")
    User setRole(@Param("user")User user, @Param("role")Role role);

    @Query("START r=node({role}) MATCH (u:USER)-[:IS_A]->(r) return u")
    List<User> findAllByRole(@Param("role")Role role);

    List<User> findAll();
}
