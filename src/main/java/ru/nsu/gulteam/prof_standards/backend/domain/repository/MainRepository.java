package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface MainRepository extends GraphRepository<Object> {
    @Query("MATCH (n) DELETE n")
    void clearAll();

    @Query("MATCH ()-[n]->() DELETE n")
    void clearAllRelations();
}
