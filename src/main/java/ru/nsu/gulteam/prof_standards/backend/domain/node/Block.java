package ru.nsu.gulteam.prof_standards.backend.domain.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import ru.nsu.gulteam.prof_standards.backend.domain.type.AttestationForm;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@NodeEntity(label = "BLOCK")
public class Block {
    @GraphId
    @Property(name = "ID")
    private Long id;

    @Property(name = "AMOUNT")
    private int amount;

    @Property(name = "SEMESTER")
    private int semester;

    @Property(name = "ATTESTATION_FORM")
    private AttestationForm attestationForm;

    public Block(int amount, int semester, AttestationForm attestationForm) {
        this.amount = amount;
        this.semester = semester;
        this.attestationForm = attestationForm;
    }
}
