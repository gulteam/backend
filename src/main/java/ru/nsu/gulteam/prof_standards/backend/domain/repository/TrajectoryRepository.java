package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.domain.node.StudyTrajectory;

import java.util.Set;

public interface TrajectoryRepository extends GraphRepository<StudyTrajectory> {
    @Query("START f=node({program}) MATCH (f)-[:STUDY_TRAJECTORY]->(d:STUDY_TRAJECTORY) RETURN d")
    Set<StudyTrajectory> findAllByProgram(@Param("program")BasicEducationProgram program);

    @Query("START l=node({trajectory}), k=node({program}) CREATE (k)-[:STUDY_TRAJECTORY]->(l) return l")
    StudyTrajectory connectToProgram(@Param("trajectory")StudyTrajectory studyTrajectory, @Param("program")BasicEducationProgram program);

    @Query("START l=node({trajectory}), k=node({standard}) CREATE (l)-[:PREPARE_TO]->(k) return l")
    StudyTrajectory connectToStandard(@Param("trajectory")StudyTrajectory studyTrajectory, @Param("standard")ProfessionalStandard standard);

    @Query("START p=node({program}) MATCH (p)-[:STUDY_TRAJECTORY]->(b:STUDY_TRAJECTORY)-[:CONTAINS]->(d:TRAJECTORY_COURSE)-[e]-() DETACH DELETE b, d")
    void deleteAllFromProgram(@Param("program")BasicEducationProgram program);
}
