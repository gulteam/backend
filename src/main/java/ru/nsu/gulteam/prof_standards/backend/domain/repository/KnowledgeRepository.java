package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Knowledge;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Skills;

public interface KnowledgeRepository extends GraphRepository<Knowledge> {
}
