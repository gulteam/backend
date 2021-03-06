package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;

import java.util.List;
import java.util.Set;

public interface KnowledgeRepository extends GraphRepository<Knowledge> {
    List<Knowledge> findAll();

    @Query("START l=node({laborFunction}), k=node({knowledge}) CREATE (l)-[:REQUIRE]->(k) return k")
    Knowledge connectToLaborFunction(@Param("knowledge")Knowledge knowledge, @Param("laborFunction")LaborFunction laborFunction);

    @Query("START c=node({course}), k=node({knowledge}) CREATE (c)-[:DEVELOPS]->(k) return k")
    Knowledge connectToCourse(@Param("knowledge")Knowledge knowledge, @Param("course")Course course);

    @Query("START p=node({professionalStandard}) MATCH (k:KNOWLEDGE)<-[*0..]-(p) return k")
    Set<Knowledge> getRequiredForStandard(@Param("professionalStandard")ProfessionalStandard professionalStandard);

    @Query("START b=node({basicEducationProgram}) MATCH (k:KNOWLEDGE)<-[*0..]-(b) return k")
    Set<Knowledge> getDevelopByBasicEducationProgram(@Param("basicEducationProgram") BasicEducationProgram basicEducationProgram);

    @Query("START c=node({course}) MATCH (c)-[:DEVELOPS]->(k:KNOWLEDGE) return k")
    Set<Knowledge> getDevelopsByCourse(@Param("course")Course course);
}
