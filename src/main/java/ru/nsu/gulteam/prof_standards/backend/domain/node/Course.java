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
@NodeEntity(label = "COURSE")
public class Course implements Comparable<Course> {
    @GraphId
    @Property(name = "ID")
    private Long id;

    @Property(name = "AMOUNT")
    private int amount = 0;

    @Property(name = "SEMESTER")
    private int semester = 1;

    @Property(name = "ATTESTATION_FORM")
    private AttestationForm attestationForm = AttestationForm.CREDIT;

    @Property(name = "NAME")
    private String name = "";

    @Property(name = "APPROVED")
    private boolean approved = false;

    public Course(int amount, int semester, AttestationForm attestationForm, String name) {
        this.amount = amount;
        this.semester = semester;
        this.attestationForm = attestationForm;
        this.name = name;
    }

    @Override
    public int compareTo(Course o) {
        return (int)(id - o.id);
    }
}
