package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;

import java.util.List;

public interface FacultyRepository extends GraphRepository<Faculty> {
    List<Faculty> findAll();

    @Query("START f=node({faculty}) MATCH (f)-[r]->() DELETE r")
    void deleteConnections(@Param("faculty")Faculty faculty);

    @Query("START u=node({user}) MATCH (u)<-[:CONTAINS]-(f:FACULTY) return f")
    Faculty getFaculty(@Param("user")User user);

    @Query("START u=node({user}) MATCH (u)<-[r:CONTAINS]-(f:FACULTY) delete r")
    void disconnectFromFaculty(@Param("user")User user);

    @Query("START f=node({faculty}), u=node({user}) CREATE (f)-[:CONTAINS]->(u) RETURN f")
    Faculty connectToUser(@Param("faculty")Faculty faculty, @Param("user")User user);

    @Query("START f=node({faculty}), c=node({course}) CREATE (f)-[:RESPONSIBLE_FOR]->(c) RETURN f")
    Faculty connectToCourse(@Param("faculty")Faculty faculty, @Param("course")Course course);

    @Query("START c=node({course}) MATCH (c)<-[:RESPONSIBLE_FOR]-(f:FACULTY) return f")
    Faculty getFaculty(@Param("course")Course course);

    @Query("START c=node({course}) MATCH (c)<-[r:CONTAINS]-(f:FACULTY) delete r")
    void disconnectFromFaculty(@Param("course")Course course);
}
