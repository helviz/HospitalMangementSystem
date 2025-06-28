package org.example.models.entities;
import javax.persistence.*;
import org.example.enums.Role;

@Entity
@Table(name = "users")
public class User {
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

    public User(String email, Role role , String password){
        this.email = email;
        this.role = role;
        this.password = password;
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

}
