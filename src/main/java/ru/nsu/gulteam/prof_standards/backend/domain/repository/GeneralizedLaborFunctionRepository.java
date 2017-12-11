package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.GeneralizedLaborFunction;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;

public interface GeneralizedLaborFunctionRepository extends GraphRepository<GeneralizedLaborFunction> {
    @Query("START g=node({generalizedLaborFunction}), p=node({professionalStandard}) CREATE (p)-[:INCLUDES]->(g) return g")
    GeneralizedLaborFunction connectToProfessionalStandard(@Param("generalizedLaborFunction") GeneralizedLaborFunction generalizedLaborFunction, @Param("professionalStandard") ProfessionalStandard professionalStandard);
}
