package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface SkillsRepository extends GraphRepository<Skills> {
    List<Skills> findAll();

    @Query("START l=node({laborFunction}), s=node({skills}) CREATE (l)-[:REQUIRE]->(s) return s")
    Skills connectToLaborFunction(@Param("skills")Skills skills, @Param("laborFunction")LaborFunction laborFunction);

    @Query("START c=node({course}), s=node({skills}) CREATE (c)-[:DEVELOPS]->(s) return s")
    Skills connectToCourse(@Param("skills")Skills skills, @Param("course")Course course);

    @Query("START p=node({professionalStandard}) MATCH (s:SKILLS)<-[*0..]-(p) return s")
    Set<Skills> getRequiredForStandard(@Param("professionalStandard")ProfessionalStandard professionalStandard);

    @Query("START b=node({basicEducationProgram}) MATCH (s:SKILLS)<-[*0..]-(b) return s")
    Set<Skills> getDevelopByBasicEducationProgram(@Param("basicEducationProgram") BasicEducationProgram basicEducationProgram);

    @Query("START c=node({course}) MATCH (c)-[:DEVELOPS]->(s:SKILLS) return s")
    Set<Skills> getDevelopsByCourse(@Param("course")Course course);
}
