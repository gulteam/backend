package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Fgos;

public interface FgosRepository extends GraphRepository<Fgos> {
}
