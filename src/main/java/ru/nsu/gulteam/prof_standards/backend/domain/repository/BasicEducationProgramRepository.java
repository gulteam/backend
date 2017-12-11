package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;

import java.util.List;

public interface BasicEducationProgramRepository extends GraphRepository<BasicEducationProgram> {
    List<BasicEducationProgram> findAll();

    @Query("START p=node({program}) MATCH (p)-[c:CONTAINS]->(t) DELETE c, t")
    void deleteConnections(@Param("program")BasicEducationProgram program);
}
