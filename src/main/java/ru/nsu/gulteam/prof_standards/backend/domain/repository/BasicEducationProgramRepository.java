package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;

import java.util.List;

public interface BasicEducationProgramRepository extends GraphRepository<BasicEducationProgram> {
    List<BasicEducationProgram> findAll();

    @Query("START p=node({program}) MATCH (p)-[c:CONTAINS]->(t) DELETE c, t")
    void deleteConnections(@Param("program")BasicEducationProgram program);

    @Query("START c=node({course}) MATCH (p)-[:CONTAINS]->(c) RETURN p")
    BasicEducationProgram getProgramOf(@Param("course")Course course);

    @Query("START p=node({program}), u=node({user}) CREATE (p)-[:CREATED_BY]->(u)")
    void connectToCreator(@Param("program")BasicEducationProgram program, @Param("user")User user);

    @Query("START p=node({program}), f=node({faculty}) CREATE (p)-[:BELONGS_TO]->(f)")
    void connectToFaculty(@Param("program")BasicEducationProgram program, @Param("faculty")Faculty faculty);

    @Query("START p=node({program}) MATCH (p)-[:CREATED_BY]->(u) RETURN u")
    User getCreator(@Param("program")BasicEducationProgram program);

    @Query("START p=node({program}) MATCH (p)-[:BELONGS_TO]->(f) RETURN f")
    Faculty getFacultyOf(@Param("program")BasicEducationProgram program);
}
