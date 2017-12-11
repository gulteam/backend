package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;

public interface KnowledgeRepository extends GraphRepository<Knowledge> {
    @Query("START l=node({laborFunction}), k=node({knowledge}) CREATE (l)-[:REQUIRE]->(k) return k")
    Knowledge connectToLaborFunction(@Param("knowledge")Knowledge knowledge, @Param("laborFunction")LaborFunction laborFunction);

    @Query("START c=node({course}), k=node({knowledge}) CREATE (c)-[:DEVELOPS]->(k) return k")
    Knowledge connectToCourse(@Param("knowledge")Knowledge knowledge, @Param("course")Course course);
}
