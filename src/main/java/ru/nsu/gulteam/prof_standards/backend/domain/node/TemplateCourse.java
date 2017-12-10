package ru.nsu.gulteam.prof_standards.backend.domain.node;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import ru.nsu.gulteam.prof_standards.backend.domain.type.AttestationForm;

@NodeEntity(label = "TEMPLATE_COURSE")
public class TemplateCourse {
    @GraphId
    @Property(name = "ID")
    private Long id;

    @Property(name = "AMOUNT")
    private int amount;

    @Property(name = "SEMESTER")
    private int semester;

    @Property(name = "ATTESTATION_FORM")
    private AttestationForm attestationForm;

    public TemplateCourse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public AttestationForm getAttestationForm() {
        return attestationForm;
    }

    public void setAttestationForm(AttestationForm attestationForm) {
        this.attestationForm = attestationForm;
    }
}
