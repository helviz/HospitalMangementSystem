package org.example.bean;


import jakarta.el.MethodExpression;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.example.models.entities.Doctor;
import org.example.models.entities.Patient;
import org.example.models.entities.Staff;

import java.io.Serializable;

@Named("navigationBean")
@RequestScoped
public class NavigationBean implements Serializable {

    // Doctors
    public String navigateToDoctorForm() {
        return "/pages/doctors/form.xhtml?faces-redirect=true";
    }

    public String navigateToDoctorList() {
        return "/pages/doctors/list.xhtml?faces-redirect=true";
    }

    public String navigateToDoctorForm(Doctor doctor) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().put("doctorToEdit", doctor);
        return navigateToDoctorForm();
    }



    // Staff
    public String navigateToStaffForm() {
        return "/pages/staff/form.xhtml?faces-redirect=true";
    }

    public String navigateToStaffList() {
        return "/pages/staff/list.xhtml?faces-redirect=true";
    }

    public String navigateToStaffForm(Staff staff) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().put("staffToEdit", staff);
        return navigateToStaffForm();
    }


    // Patients
    public String navigateToPatientForm() {
        return "/pages/patients/form.xhtml?faces-redirect=true";
    }

    public String navigateToPatientList() {
        return "/pages/patients/list.xhtml?faces-redirect=true";
    }

    public String navigateToPatientForm(Patient patient) {
        FacesContext context = FacesContext.getCurrentInstance();

        context.getExternalContext().getFlash().put("patientToEdit", patient);

        return "/pages/patients/form.xhtml?faces-redirect=true";
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
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .invalidateSession();
        return "/pages/login.xhtml?faces-redirect=true";
    }


    public String navigateToUpdateAppointment() {
        return "/pages/appointments/update.xhtml?faces-redirect=true";
    }

    public String navigateToCreateAppointment() {
        return "/pages/appointments/create.xhtml?faces-redirect=true";
    }
}
