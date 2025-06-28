package org.example.models.entities;
import javax.persistence.*;
import java.time.LocalDate;

@MappedSuperclass
public class Personel {

    @Column(name="first_name")
    private String firstName;

    @Column(nullable = true, name="middle_name")
    private String middleName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name="contact_number")
    private String contactNumber;

    @Column(name="email ")
    private String email;

//    setters and getters

    public void setFirstName(String firstName) {
    this.firstName = firstName;
}

    public String getFirstName() {
        return firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }



}
