package org.example.models.entities;
import jakarta.persistence.*;
import org.example.enums.Role;
import org.example.models.base.SoftDeletable;

@Entity
@Table(name = "users")
public class User implements SoftDeletable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    Long userId;

    @Column(name="password", nullable=false)
    String password;

    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private Doctor doctor;

    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private Staff staff;

    private boolean deleted = false;

    public User(String password){
        this.password = password;
    }

    public User() {

    }


    //    setters and getters
    public void setUserId(Long userId){
        this.userId = userId;
    }

    public Long getUserId(){
        return this.userId;
    }



    public Role getUserRole() {
        if (doctor != null) return Role.DOCTOR;
        if (staff != null) return staff.getRole();
        return null;
    }

    public String getEmail(){
        if (doctor != null) return doctor.getEmail();
        if (staff != null) return staff.getEmail();
        return null;
    }

    public void setDoctor(Doctor doctor){
        this.doctor = doctor;
    }

    public Doctor getDoctor(){
        return doctor;
    }

    public void setStaff(Staff staff){
        this.staff = staff;
    }

    public Staff getStaff(){
        return this.staff;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

    @Override
    public void setDelete(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean isDelete() {
        return this.deleted;
    }

}
