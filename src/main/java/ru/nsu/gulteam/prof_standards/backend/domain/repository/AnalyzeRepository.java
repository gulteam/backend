package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;

import java.util.List;

public interface AnalyzeRepository extends GraphRepository<BasicEducationProgram> {

    @Query("START p=node({program}) MATCH (c:COURSE)<-[:CONTAINS]-(p) WHERE NOT (c)--(:BLOCK) return sum(c.AMOUNT)")
    double getVolumeOfBasicPart(@Param("program")BasicEducationProgram program);

    @Query("START p=node({program}) MATCH (b:BLOCK)<-[:CONTAINS]-(p) return sum(b.AMOUNT)")
    double getVolumeOfVariablePart(@Param("program")BasicEducationProgram program);
}
