package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Knowledge;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.domain.node.StudyTrajectory;
import ru.nsu.gulteam.prof_standards.backend.domain.node.TrajectoryCourse;

import java.util.List;
import java.util.Set;

public interface ProfessionalStandardRepository extends GraphRepository<ProfessionalStandard> {
    List<ProfessionalStandard> findAll();

    @Query("MATCH (n:`PROFESSIONAL_STANDARD`) WHERE ID(n) = {id} RETURN n")
    ProfessionalStandard findById(@Param("id") Long professionalStandardId);

    @Query("START s=node({skills}) MATCH (s)<-[*0..]-(p:`PROFESSIONAL_STANDARD`) RETURN p")
    List<ProfessionalStandard> findBySkills(@Param("skills") Skills skills);

    @Query("START k=node({knowledge}) MATCH (k)<-[*0..]-(p:`PROFESSIONAL_STANDARD`) RETURN p")
    List<ProfessionalStandard> findByKnowledge(@Param("knowledge") Knowledge knowledge);
  
    @Query("START c=node({trajectory}) MATCH (c)-[:PREPARE_TO]->(k:PROFESSIONAL_STANDARD) return k")
    Set<ProfessionalStandard> getByTrajectory(@Param("trajectory")StudyTrajectory studyTrajectory);

    @Query("START p=node({program}) MATCH (p)-[:BASED_ON]->(:FGOS)-[:CAN_DEVELOP]->(r:PROFESSIONAL_STANDARD) return r")
    Set<ProfessionalStandard> findAllFromProgram(@Param("program")BasicEducationProgram program);
}
