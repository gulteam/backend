package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.node.StudyTrajectory;
import ru.nsu.gulteam.prof_standards.backend.domain.node.TrajectoryCourse;

import java.util.Set;

public interface TrajectoryCourseRepository extends GraphRepository<TrajectoryCourse> {
    @Query("START c=node({trajectory}) MATCH (c)-[:CONTAINS]->(k:TRAJECTORY_COURSE) return k")
    Set<TrajectoryCourse> getByTrajectory(@Param("trajectory")StudyTrajectory studyTrajectory);

    @Query("START l=node({trajectory}), k=node({course}) CREATE (l)-[:CONTAINS]->(k) return k")
    TrajectoryCourse connectToTrajectory(@Param("trajectory")StudyTrajectory studyTrajectory, @Param("course")TrajectoryCourse course);

    @Query("START l=node({mc}), k=node({course}) CREATE (k)-[:IS]->(l) return k")
    TrajectoryCourse connectToCourse(@Param("mc")Course mc, @Param("course")TrajectoryCourse course);
}
