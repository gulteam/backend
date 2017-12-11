package ru.nsu.gulteam.prof_standards.backend.domain.node;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import ru.nsu.gulteam.prof_standards.backend.domain.type.AttestationForm;

@NodeEntity(label = "COURSE")
public class Course {
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

    public Course() {
    }

    public Course(int amount, int semester, AttestationForm attestationForm, String name) {
        this.amount = amount;
        this.semester = semester;
        this.attestationForm = attestationForm;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isApproved() {
        return approved;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                '}';
    }
}
