package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;

import java.util.List;

public interface CourseRepository extends GraphRepository<Course> {
    List<Course> findAll();

    @Query("START p=node({program}) MATCH (p)-[:CONTAINS]->(c:COURSE) return c")
    List<Course> findAllFromProgram(@Param("program")BasicEducationProgram program);

    @Query("START p=node({program}), c=node({course}) CREATE (p)-[:CONTAINS]->(c) return c")
    Course connectToProgram(@Param("course")Course course, @Param("program")BasicEducationProgram program);

    @Query("START b=node({block}), c=node({course}) CREATE (c)<-[:CONTAINS]-(b) return c")
    Course connectToBlock(@Param("course")Course course, @Param("block")Block block);

    @Query("START p=node({program}) MATCH (p)-[:CONTAINS]->(c:COURSE) WHERE NOT (c)-[:IMPLEMENTS]->() return c")
    List<Course> findAllBaseFromProgram(@Param("program")BasicEducationProgram program);

    @Query("START b=node({block}) MATCH (c:COURSE)<-[:CONTAINS]-(b) return c")
    List<Course> getAllCoursesFromBlock(@Param("block")Block block);

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

    @Query("START c=node({course}), u=node({user}) CREATE (c)-[:DEVELOPS_BY]->(u)")
    void connectToDeveloper(@Param("course")Course course, @Param("user")User user);

    @Query("START c=node({course}) MATCH (c)-[d:DEVELOPS_BY]->(u:USER) delete d")
    void deleteAllDevelopsBy(@Param("course")Course course);
}
