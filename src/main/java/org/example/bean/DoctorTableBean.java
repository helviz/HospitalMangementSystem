package org.example.bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.enums.Speciality;
import org.example.models.entities.Doctor;

import org.example.services.DoctorService;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named("doctorTableBean")
@ViewScoped
public class DoctorTableBean implements Serializable {

    private List<Doctor> doctors;
    private Doctor selectedDoctor;
    private Speciality selectedSpeciality;
    private String searchKeyword;
    private List<Doctor> filteredDoctors;
    private Doctor doctor;

    @Inject
    private DoctorService doctorService;


    @PostConstruct
    public void init() {
        doctors = doctorService.getAllDoctors();
        filteredDoctors = new ArrayList<>(doctors); // <-- Add this line
    }


    public void refreshDoctors() {
        doctors = doctorService.getAllDoctors();
    }

    public void filterBySpeciality() {
        if (selectedSpeciality != null) {
            doctors = doctorService.getDoctorsBySpeciality(selectedSpeciality);
        } else {
            refreshDoctors();
        }
    }

    public void deleteSelectedDoctor() {
        if (selectedDoctor != null && selectedDoctor.getDoctorID() != null) {
            doctorService.deleteDoctorById(selectedDoctor.getDoctorID());
            refreshDoctors();
            selectedDoctor = null;
        }
    }

    public void resetFilters() {
        System.out.println("Resetting filters...");
        searchKeyword = "";
        filteredDoctors = new ArrayList<>(doctors);
        System.out.println("Reset complete. Showing " + filteredDoctors.size() + " doctors");
    }

    public void searchByKeyword() {
        System.out.println("=== SEARCH DEBUG ===");
        System.out.println("Search called with keyword: '" + searchKeyword + "'");
        System.out.println("Total doctors available: " + (doctors != null ? doctors.size() : 0));

        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            System.out.println("Empty search - showing all doctors");
            filteredDoctors = new ArrayList<>(doctors);
        } else {
            String keyword = searchKeyword.toLowerCase().trim();
            System.out.println("Searching for keyword: '" + keyword + "'");

            filteredDoctors = doctors.stream()
                    .filter(p -> {
                        String firstName = p.getFirstName() != null ? p.getFirstName().toLowerCase() : "";
                        String lastName = p.getLastName() != null ? p.getLastName().toLowerCase() : "";
                        String email = p.getEmail() != null ? p.getEmail().toLowerCase() : "";

                        boolean matches = firstName.contains(keyword) ||
                                lastName.contains(keyword) ||
                                email.contains(keyword);

                        if (matches) {
                            System.out.println("Match found: " + firstName + " " + lastName + " (" + email + ")");
                        }

                        return matches;
                    })
                    .collect(Collectors.toList());
        }

        System.out.println("Filtered results: " + filteredDoctors.size() + " doctors");
        System.out.println("=== END SEARCH DEBUG ===");
    }

    public void resetForm() {

        doctor = new Doctor();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
                        "Form has been reset."));
    }



    public String cancelForm() {
        doctor = new Doctor();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
                        "Form has been cancelled."));

        return "/pages/doctors/list.xhtml?faces-redirect=true";
    }

  

    private void sortDoctorsLatestFirst() {

        doctors.sort((p1, p2) -> {
            if (p1.getDoctorID() == null || p2.getDoctorID() == null) {
                return 0; // Keep order if IDs missing
            }
            return p2.getDoctorID().compareTo(p1.getDoctorID()); // Descending order
        });


    }

    public void delete(Long doctorId) {
        try {
            doctorService.deleteDoctorById(doctorId);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Patient Deleted", "Patient deleted successfully"));
            refreshDoctors();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete doctor: " + e.getMessage()));
        }
    }


    // Getters and setters

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public Doctor getSelectedDoctor() {
        return selectedDoctor;
    }

    public void setSelectedDoctor(Doctor selectedDoctor) {
        this.selectedDoctor = selectedDoctor;
    }

    public Speciality getSelectedSpeciality() {
        return selectedSpeciality;
    }

    public void setSelectedSpeciality(Speciality selectedSpeciality) {
        this.selectedSpeciality = selectedSpeciality;
    }

    public Speciality[] getSpecialities() {
        return Speciality.values();
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public List<Doctor> getFilteredDoctors() {
        return filteredDoctors;
    }

    public void setFilteredDoctors(List<Doctor> filteredDoctors) {
        this.filteredDoctors = filteredDoctors;
    }

}
