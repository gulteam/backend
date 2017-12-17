package ru.nsu.gulteam.prof_standards.backend.domain.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import ru.nsu.gulteam.prof_standards.backend.domain.node.ProfessionalStandard;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Role;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.domain.type.UserRole;

import java.util.List;

public interface RoleRepository extends GraphRepository<Role> {
    List<Role> findAll();

    Role findRoleByName(UserRole name);

    @Query("START u=node({user}) MATCH (u)-[:IS_A]->(r:ROLE) RETURN r")
    Role getRole(@Param("user")User user);
}
