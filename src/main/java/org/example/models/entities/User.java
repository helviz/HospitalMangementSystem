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

    @Column(name  = "email")
    String email;

    @Enumerated(EnumType.STRING)
    @Column(name="role")
    Role role;

    @Column(name="password", nullable=false)
    String password;

    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private Doctor doctor;

    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private Staff staff;

    private boolean deleted = false;

    public User(String email, Role role , String password){
        this.email = email;
        this.role = role;
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

    public void setRole(Role role){
        this.role = role;
    }

    public Role getRole(){
        return this.role;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail(){
        return this.email;
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
