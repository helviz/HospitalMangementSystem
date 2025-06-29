package org.example.models.entities;
import jakarta.persistence.*;
import org.example.enums.Role;
import org.example.models.base.SoftDeletable;

@Entity
@Table(name = "staff")
public class Staff extends Personel implements SoftDeletable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Long staffID;

    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean deleted = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "user_id")
    private User userAccount;

//    gettters and setters


    public void setStaffID(Long staffID) {
        this.staffID = staffID;
    }

    public Long getStaffID() {
        return staffID;
    }

    public void setRole(Role role){
        this.role = role;
    }

    public Role  getRole(){
        return this.role;
    }

    @Override
    public void setDelete(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean isDelete() {
        return this.deleted;
    }

    public void setUser(User user){
        this.userAccount = user;
    }

    public User getUser(){
        return this.userAccount;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "staffID=" + staffID +
                ", firstName='" + getFirstName() + '\'' +
                ", middleName='" + getMiddleName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", userEmail=" + (userAccount != null ? userAccount.getEmail() : "N/A") +
                '}';
    }



}
