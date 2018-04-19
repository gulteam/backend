package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.domain.node.StudyTrajectory;
import ru.nsu.gulteam.prof_standards.backend.domain.node.TrajectoryCourse;

import java.util.List;
import java.util.Set;

public interface ProfessionalStandardRepository extends GraphRepository<ProfessionalStandard> {
    List<ProfessionalStandard> findAll();

    @Query("START c=node({trajectory}) MATCH (c)-[:PREPARE_TO]->(k:PROFESSIONAL_STANDARD) return k")
    Set<ProfessionalStandard> getByTrajectory(@Param("trajectory")StudyTrajectory studyTrajectory);
}
