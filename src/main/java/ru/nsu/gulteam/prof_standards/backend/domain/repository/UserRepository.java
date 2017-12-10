package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Token;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;

public interface UserRepository extends GraphRepository<User> {
    User findByLoginIgnoreCase(String login);

    @Query("START t=node({token}), u=node({user}) match (u)-[r:HAS_TOKEN]->(t) return count(r) > 0")
    boolean hasToken(@Param("user") User user, @Param("token") Token token);
}
