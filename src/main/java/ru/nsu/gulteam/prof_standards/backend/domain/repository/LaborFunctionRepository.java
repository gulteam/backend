package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.GeneralizedLaborFunction;
import ru.nsu.gulteam.prof_standards.backend.domain.node.LaborFunction;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;

public interface LaborFunctionRepository extends GraphRepository<LaborFunction> {
    @Query("START l=node({laborFunction}), g=node({generalizedLaborFunction}) CREATE (g)-[:INCLUDES]->(l) return l")
    LaborFunction connectToGeneralizedLaborFunction(@Param("laborFunction")LaborFunction laborFunction, @Param("generalizedLaborFunction") GeneralizedLaborFunction generalizedLaborFunction);
}
