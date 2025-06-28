package org.example.models.entities;
import org.example.enums.Speciality;
import org.example.models.base.SoftDeletable;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
public class Doctor extends Personel implements SoftDeletable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctorID;

    @OneToOne
    @JoinColumn(name= "user_account_id")
    private User userAccount;

    @Enumerated(EnumType.STRING)
    private Speciality speciality;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalRecord> medicalRecording = new ArrayList<>();
    private boolean deleted = false;



//    setters and getters

    public Long getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(Long doctorID) {
        this.doctorID = doctorID;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.setDoctor(this);
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
        appointment.setDoctor(null);
    }

    public void addMedicalHistory(MedicalRecord medicalRecord) {
        medicalRecording .add(medicalRecord);
        medicalRecord.setDoctor(this);
    }

    public void removeMedicalHistory(MedicalRecord medicalRecord) {
        medicalRecording .remove(medicalRecord);
        medicalRecord.setDoctor(null);
    }

    public void setUser(User userAccount){
        this.userAccount = userAccount;
    }

    public User getUser(){
        return this.userAccount;
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
        return "Doctor{" +
                "doctorID=" + doctorID +
                ", firstName='" + getFirstName() + '\'' +
                ", middleName='" + getMiddleName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", userEmail=" + (userAccount != null ? userAccount.getEmail() : "N/A") +
                ", speciality=" + speciality +
                '}';
    }




}
