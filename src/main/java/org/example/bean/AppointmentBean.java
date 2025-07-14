package org.example.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.model.SelectItemGroup;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.enums.Speciality;
import org.example.models.entities.Doctor;
import org.example.models.entities.MedicalRecord;
import org.example.models.entities.Patient;
import org.example.services.AppointmentService;
import org.example.services.DoctorService;
import org.example.services.PatientService;
import org.primefaces.event.SelectEvent;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named("appointmentBean")
@ViewScoped
public class AppointmentBean implements Serializable {

    private LocalDateTime appointmentDateTime;

    private Speciality selectedDoctorSpeciality;

    private List<Speciality> specialities;

    private List<Doctor> doctors;

    private Doctor selectedDoctor;

    private Patient selectedPatient;

    @Inject
    private DoctorService doctorService;

    @Inject
    private PatientService patientService;

    @Inject
    private AppointmentService appointmentService;
    @Named("navigationBean")
    @Inject
    private NavigationBean navigationBean;

    @PostConstruct
    public void init(){
        System.out.println("AppointmentBean initialized");
        setSpecialities();
        doctors = new ArrayList<>();
        System.out.println("Initial doctors list size: " + doctors.size());
    }

    public void onSelectSpeciality() {
        System.out.println("=== onSelectSpeciality called ===");
        System.out.println("Selected speciality: " + selectedDoctorSpeciality);
        System.out.println("Previous selectedDoctor: " + selectedDoctor);

        // Reset selected doctor when speciality changes
        this.selectedDoctor = null;
        System.out.println("Reset selectedDoctor to null");

        // Get doctors for the selected speciality
        if (selectedDoctorSpeciality != null) {
            try {
                this.doctors = doctorService.getDoctorsBySpeciality(selectedDoctorSpeciality);
                System.out.println("Doctors found: " + doctors.size());
                if (doctors != null && !doctors.isEmpty()) {
                    for (Doctor doctor : doctors) {
                        System.out.println("Doctor: " + doctor.getFullName());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error getting doctors: " + e.getMessage());
                e.printStackTrace();
                this.doctors = new ArrayList<>();
            }
        } else {
            System.out.println("No speciality selected, clearing doctors list");
            this.doctors = new ArrayList<>();
        }

        System.out.println("Final doctors list size: " + doctors.size());
        System.out.println("=== onSelectSpeciality completed ===");
    }

    // Alternative method for valueChangeListener
    public void onSpecialityChange(ValueChangeEvent event) {
        System.out.println("=== onSpecialityChange called ===");
        System.out.println("Old value: " + event.getOldValue());
        System.out.println("New value: " + event.getNewValue());

        if (event.getNewValue() != null) {
            selectedDoctorSpeciality = (Speciality) event.getNewValue();
            onSelectSpeciality(); // Reuse the logic
        }
    }


    //    Autocomplete method gets called on every keystroke
    public List<Patient> completePatients(String query){
        String keyword = query.toLowerCase();
        return  patientService.searchPatients(keyword);
    }

    // jsf triggers this method uphold selection
    public void onPatientSelect(SelectEvent<Patient> event){
        Patient patient = event.getObject();
        setSelectedPatient(patient);
    }

    //    save appointment
    public String saveAppointment() {
        if (selectedPatient != null && selectedDoctor != null && appointmentDateTime != null) {
            Long patientId = selectedPatient.getPatientID();
            Long doctorId = selectedDoctor.getDoctorID();
            LocalDateTime dateTime = appointmentDateTime;

            // ✅ Working hours validation: 8:00 AM to 5:00 PM
            LocalTime startTime = LocalTime.of(8, 0);
            LocalTime endTime = LocalTime.of(17, 0); // 5:00 PM

            LocalTime selectedTime = dateTime.toLocalTime();

            if (selectedTime.isBefore(startTime) || selectedTime.isAfter(endTime)) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Invalid Time",
                                "Appointment time must be between 8:00 AM and 5:00 PM"));
                return null; // 👈 Stay on the same page
            }

            boolean success = appointmentService.createAppointment(patientId, doctorId, dateTime);
            if (success) {
                FacesContext.getCurrentInstance()
                        .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Appointment created"));
                return navigationBean.navigateToAppointmentForm();
            } else {
                FacesContext.getCurrentInstance()
                        .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Failure", "Appointment time is taken"));
                return null; // 👈 Stay on the same page
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Validation Error",
                            "Please fill all required fields"));
            return null;
        }
    }



// grouping doctors by speciality

    public List<SelectItem> getGroupedDoctorItems() {
        Map<Speciality, List<Doctor>> groupedDoctors = doctorService.getDoctorsGroupedBySpeciality();
        List<SelectItem> items = new ArrayList<>();

        for (Map.Entry<Speciality, List<Doctor>> entry : groupedDoctors.entrySet()) {
            Speciality speciality = entry.getKey();
            List<Doctor> doctors = entry.getValue();

            SelectItemGroup group = new SelectItemGroup(speciality.name());
            SelectItem[] doctorItems = doctors.stream()
                    .map(doc -> new SelectItem(doc, doc.getFullName()))
                    .toArray(SelectItem[]::new);

            group.setSelectItems(doctorItems);
            items.add(group);
        }

        return items;
    }


    //    getters and setters
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime){
        this.appointmentDateTime = appointmentDateTime;
    }

    public LocalDateTime getAppointmentDateTime(){
        return  this.appointmentDateTime;
    }

    public Speciality getSelectedDoctorSpeciality(){
        return this.selectedDoctorSpeciality;
    }

    public void setSelectedDoctorSpeciality(Speciality selectedDoctorSpeciality){
        System.out.println("Setting selectedDoctorSpeciality to: " + selectedDoctorSpeciality);
        this.selectedDoctorSpeciality = selectedDoctorSpeciality;
    }

    public void setSpecialities(){
        this.specialities = List.of(Speciality.values());
    }

    public List<Speciality> getSpecialities(){
        return  this.specialities;
    }

    public void setDoctors(List<Doctor> doctors){
        this.doctors = doctors;
    }

    public List<Doctor> getDoctors(){
        System.out.println("getDoctors() called, returning " + (doctors != null ? doctors.size() : "null") + " doctors");
        return  this.doctors;
    }

    public void setSelectedDoctor(Doctor selectedDoctor){
        System.out.println("Setting selectedDoctor to: " + selectedDoctor);
        this.selectedDoctor = selectedDoctor;
    }

    public Doctor getSelectedDoctor(){
        System.out.println("getSelectedDoctor() called, returning: " + selectedDoctor);
        return selectedDoctor;
    }

    public Patient getSelectedPatient(){
        return this.selectedPatient;
    }

    public void setSelectedPatient(Patient selectedPatient){
        this.selectedPatient = selectedPatient;
    }
}