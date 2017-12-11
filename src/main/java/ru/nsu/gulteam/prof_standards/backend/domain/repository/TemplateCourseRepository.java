package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.node.TemplateCourse;

public interface TemplateCourseRepository extends GraphRepository<TemplateCourse> {
    @Query("START p=node({program}), t=node({templateCourse}) CREATE (p)-[:CONTAINS]->(t) return t")
    TemplateCourse connectToProgram(@Param("templateCourse")TemplateCourse templateCourse, @Param("program")BasicEducationProgram program);
}
