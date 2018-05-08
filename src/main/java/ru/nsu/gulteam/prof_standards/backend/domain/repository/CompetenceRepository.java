package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Competence;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.node.FgosCourseRequirement;

public interface CompetenceRepository extends GraphRepository<Competence> {
    @Query("START c=node({competence}) MATCH (c)<-[r:REQUIRE]-() delete r")
    void deleteReferences(@Param("competence") Competence competence);


    @Query("START c=node({course}), k=node({competence}) CREATE (c)-[:DEVELOPS]->(k) return k")
    void connectToCourse(@Param("competence")Competence competence, @Param("course")Course course);
}
