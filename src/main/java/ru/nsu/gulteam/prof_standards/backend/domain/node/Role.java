package ru.nsu.gulteam.prof_standards.backend.domain.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import ru.nsu.gulteam.prof_standards.backend.domain.type.UserRole;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@NodeEntity(label = "ROLE")
public class Role {
    @GraphId
    @Property(name = "ID")
    private Long id;

    @Property(name = "NAME")
    private UserRole name;

    public Role(UserRole name) {
        this.name = name;
    }
}
