package org.example.models.entities;
import javax.persistence.*;
import org.example.models.base.SoftDeletable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="patients")
@Inheritance(strategy = InheritanceType.JOINED)
public class Patient extends Personel implements SoftDeletable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="patient_id")
    private Long patientID;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalRecord> medicalHistory = new ArrayList<>();

    private boolean deleted = false;


    //    setters and getters
    public void setPatientID(Long patientID) {
        this.patientID = patientID;
    }

    public Long getPatientID() {
        return patientID;
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.setPatient(this);
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
        appointment.setPatient(null);
    }

    public void addMedicalHistory(MedicalRecord medicalRecord) {
        medicalHistory.add(medicalRecord);
        medicalRecord.setPatient(this);
    }

    public void removeMedicalHistory(MedicalRecord medicalRecord) {
        medicalHistory.remove(medicalRecord);
        medicalRecord.setPatient(null);
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
        return "Patient{" +
                "patientID=" + patientID +
                ", firstName='" + getFirstName() + '\'' +
                ", middleName='" + getMiddleName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                '}';


    }



}