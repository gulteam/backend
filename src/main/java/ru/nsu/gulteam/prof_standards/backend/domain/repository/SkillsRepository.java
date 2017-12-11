package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;

public interface SkillsRepository extends GraphRepository<Skills> {
    @Query("START l=node({laborFunction}), s=node({skills}) CREATE (l)-[:REQUIRE]->(s) return s")
    Skills connectToLaborFunction(@Param("skills")Skills skills, @Param("laborFunction")LaborFunction laborFunction);

    @Query("START c=node({course}), s=node({skills}) CREATE (c)-[:DEVELOPS]->(s) return s")
    Skills connectToCourse(@Param("skills")Skills skills, @Param("course")Course course);
}
