package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;

import java.util.List;

public interface CourseRepository extends GraphRepository<Course> {
    List<Course> findAll();

    Course findById(long id);

    @Query("START p=node({program}) MATCH (p)-[:CONTAINS]->(c:COURSE) return c")
    List<Course> findAllFromProgram(@Param("program")BasicEducationProgram program);

    @Query("START p=node({program}), c=node({course}) CREATE (p)-[:CONTAINS]->(c) return c")
    Course connectToProgram(@Param("course")Course course, @Param("program")BasicEducationProgram program);

    @Query("START t=node({templateCourse}), c=node({course}) CREATE (c)-[:IMPLEMENTS]->(t) return c")
    Course connectToTemplate(@Param("course")Course course, @Param("templateCourse")TemplateCourse templateCourse);

    @Query("START p=node({program}) MATCH (p)-[:CONTAINS]->(c:COURSE) WHERE NOT (c)-[:IMPLEMENTS]->() return c")
    List<Course> findAllBaseFromProgram(@Param("program")BasicEducationProgram program);

    @Query("START t=node({templateCourse}) MATCH (c:COURSE)-[:IMPLEMENTS]->(t) return c")
    List<Course> getImplementationsOf(@Param("templateCourse")TemplateCourse templateCourse);

    @Query("START c=node({course}) MATCH (c)-[d:DEVELOPS]->() delete d")
    void deleteAllDevelopRelations(@Param("course")Course course);

    @Query("START c=node({course}) MATCH (c)-[:BASED_ON]->(b:COURSE) RETURN b")
    List<Course> getPreviousCourses(@Param("course")Course course);

    @Query("START c=node({course}) MATCH (c)<-[d:BASED_ON]-(b:COURSE) RETURN b")
    List<Course> getNextCourses(@Param("course")Course course);

    @Query("START c=node({course}), b=node({base}) CREATE (c)-[:BASED_ON]->(b)")
    void setBasedOn(@Param("course")Course course, @Param("base")Course base);

    @Query("START c=node({course}) MATCH (c)-[b:BASED_ON]->(:COURSE) delete b")
    void removeAllBased(@Param("course")Course course);

    @Query("START c=node({course}), u=node({user}) CREATE (c)-[:CREATED_BY]->(u)")
    void connectToCreator(@Param("course")Course course, @Param("user")User user);

    @Query("START c=node({course}) MATCH (c)-[:CREATED_BY]->(u) RETURN u")
    User getCreator(@Param("course")Course course);
}
