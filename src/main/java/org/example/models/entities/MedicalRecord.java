package org.example.models.entities;
import javax.persistence.*;
import org.example.models.base.SoftDeletable;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "medicalrecords")
public class MedicalRecord implements SoftDeletable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long  recordID;
    private String diagnosis;

    @Column(name="record_date")
    private LocalDate recordDate;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private boolean deleted = false;



    public void setRecordID(Long recordID) {
        this.recordID = recordID;
    }

    public Long getRecordID() {
        return recordID;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    @Override
    public void setDelete(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean isDelete() {
        return this.deleted;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "recordID=" + recordID +
                ", diagnosis='" + diagnosis + '\'' +
                ", recordDate=" + recordDate +
                ", patient=" + (patient != null ? patient.getFirstName() + " " + patient.getLastName() : "N/A") +
                ", doctor=" + (doctor != null ? doctor.getFirstName() + " " + doctor.getLastName() : "N/A") +
                '}';


    }

}
