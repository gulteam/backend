package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Fgos;
import ru.nsu.gulteam.prof_standards.backend.domain.node.FgosCourseRequirement;

public interface FgosCourseRequirementRepository extends GraphRepository<FgosCourseRequirement> {
    @Query("START c=node({course}) MATCH (c)<-[r:REQUIRE]-() delete r")
    void deleteReferences(@Param("course") FgosCourseRequirement fgosCourseRequirement);
}
