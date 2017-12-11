package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;

import java.util.List;

public interface CourseRepository extends GraphRepository<Course> {
    List<Course> findAll();

    Course findById(int id);

    @Query("START p=node({program}) MATCH (p)-[:CONTAINS]->(c:COURSE) return c")
    List<Course> findAllFromProgram(@Param("program")BasicEducationProgram program);

    @Query("START p=node({program}), c=node({course}) CREATE (p)-[:CONTAINS]->(c)")
    void connectCourseToProgram(@Param("course")Course course, @Param("program")BasicEducationProgram program);
}
