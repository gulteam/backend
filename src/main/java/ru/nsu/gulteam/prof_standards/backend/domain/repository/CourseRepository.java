package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;
import ru.nsu.gulteam.prof_standards.backend.domain.node.TemplateCourse;

import java.util.List;

public interface CourseRepository extends GraphRepository<Course> {
    List<Course> findAll();

    Course findById(int id);

    @Query("START p=node({program}) MATCH (p)-[:CONTAINS]->(c:COURSE) return c")
    List<Course> findAllFromProgram(@Param("program")BasicEducationProgram program);

    @Query("START p=node({program}), c=node({course}) CREATE (p)-[:CONTAINS]->(c) return c")
    Course connectToProgram(@Param("course")Course course, @Param("program")BasicEducationProgram program);

    @Query("START t=node({templateCourse}), c=node({course}) CREATE (c)-[:IMPLEMENTS]->(t) return c")
    Course connectToTemplate(@Param("course")Course course, @Param("templateCourse")TemplateCourse templateCourse);

    @Query("START p=node({program}) MATCH (p)-[:CONTAINS]->(c:COURSE) WHERE NOT (p)-[:IMPLEMENTS]->() return c")
    List<Course> findAllBaseFromProgram(@Param("program")BasicEducationProgram program);

    @Query("START t=node({templateCourse}) MATCH (c:COURSE)-[:IMPLEMENTS]->(t) return c")
    List<Course> getImplementationsOf(@Param("templateCourse")TemplateCourse templateCourse);
}
