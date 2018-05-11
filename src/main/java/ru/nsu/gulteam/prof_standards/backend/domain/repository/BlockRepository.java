package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.BasicEducationProgram;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Block;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Course;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;

import java.util.List;

public interface BlockRepository extends GraphRepository<Block> {
    @Query("START p=node({program}), t=node({block}) CREATE (p)-[:CONTAINS]->(t) return t")
    Block connectToProgram(@Param("block")Block block, @Param("program")BasicEducationProgram program);

    @Query("START p=node({program}) MATCH (p)-[:CONTAINS]->(t:BLOCK) return t")
    List<Block> findAllFromProgram(@Param("program")BasicEducationProgram program);

    @Query("START c=node({course}) MATCH (c)<-[:CONTAINS]-(b:BLOCK) return b")
    Block findBlockOf(@Param("course")Course course);

    @Query("START b=node({block}), u=node({user}) CREATE (b)-[:CREATED_BY]->(u)")
    void connectToCreator(@Param("block")Block course, @Param("user")User user);
}
