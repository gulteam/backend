package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.*;

import java.util.List;

public interface DepartmentRepository extends GraphRepository<Department> {
    List<Department> findAll();

    @Query("START d=node({department}), f=node({faculty}) CREATE (f)-[:DIVIDES_TO]->(d) RETURN d")
    Department connectToFaculty(@Param("department") Department department, @Param("faculty")Faculty faculty);

    @Query("START f=node({faculty}) MATCH (f)-[:DIVIDES_TO]->(d:DEPARTMENT) RETURN d")
    List<Department> findAllFromFaculty(@Param("faculty")Faculty faculty);

    @Query("START u=node({user}) MATCH (u)<-[:CONTAINS]-(d:DEPARTMENT) RETURN d")
    Department getDepartment(@Param("user")User user);

    @Query("START u=node({user}) MATCH (u)<-[r:CONTAINS]-(d:DEPARTMENT) DELETE r")
    void disconnectFromDepartment(@Param("user")User user);

    @Query("START d=node({department}), u=node({user}) CREATE (d)-[:CONTAINS]->(u) RETURN d")
    Department connectToUser(@Param("department") Department department, @Param("user")User user);
}
