package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Token;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;

public interface TokenRepository extends GraphRepository<Token> {
    @Query("START t=node({token}) MATCH (u:USER)-[:HAS_TOKEN]->(t) return u")
    User getOwner(@Param("token") Token token);

    Token findByData(String data);

    @Query("START t=node({token}), u=node({user}) CREATE (u)-[:HAS_TOKEN]->(t) return t")
    Token connectToUser(@Param("token")Token token, @Param("user")User user);
}
