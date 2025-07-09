package org.example.managedbeans;


import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("navigationBean")
@SessionScoped
public class NavigationBean implements Serializable {

    // Doctors
    public String navigateToDoctorForm() {
        return "/pages/doctors/form.xhtml?faces-redirect=true";
    }

    public String navigateToDoctorList() {
        return "/pages/doctors/list.xhtml?faces-redirect=true";
    }

    // Staff
    public String navigateToStaffForm() {
        return "/pages/staff/form.xhtml?faces-redirect=true";
    }

    public String navigateToStaffList() {
        return "/pages/staff/list.xhtml?faces-redirect=true";
    }

    // Patients
    public String navigateToPatientForm() {
        return "/pages/patients/form.xhtml?faces-redirect=true";
    }

    public String navigateToPatientList() {
        return "/pages/patients/list.xhtml?faces-redirect=true";
    }

    // Appointments
    public String navigateToAppointmentForm() {
        return "/pages/appointments/form.xhtml?faces-redirect=true";
    }

    public String navigateToAppointmentList() {
        return "/pages/appointments/list.xhtml?faces-redirect=true";
    }

    // Medical Records
    public String navigateToMedicalRecordForm() {
        return "/pages/medicalRecords/form.xhtml?faces-redirect=true";
    }

    public String navigateToMedicalRecordList() {
        return "/pages/medicalRecords/list.xhtml?faces-redirect=true";
    }

    // Users
    public String navigateToUserCredentialsForm() {
        return "/pages/users/login.xhtml?faces-redirect=true";
    }

    // Dashboard
    public String navigateToDashboard() {
        return "/pages/dashboard/dashboard.xhtml?faces-redirect=true";
    }

    public String logout() {
        return "/pages/login.xhtml?faces-redirect=true";
    }


}
