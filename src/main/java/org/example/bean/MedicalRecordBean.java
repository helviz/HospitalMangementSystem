package org.example.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.models.entities.MedicalRecord;
import org.example.models.entities.Patient;
import org.example.services.Impl.MedicalRecordServiceImpl;
import org.example.services.MedicalRecordService;
import org.example.services.PatientService;
import org.primefaces.event.SelectEvent;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Named("medicalRecordBean")
@SessionScoped
public class MedicalRecordBean implements Serializable {

    private Patient selectedPatient;
    private List<MedicalRecord> patientMedicalrecords;

    @Inject
    private PatientService patientService;

    @Inject
    private MedicalRecordService medicalRecordService;

    @Inject
    private UserBean userBean;

    @Inject
    private NavigationBean navigationBean;

    String diagnosis;





    @PostConstruct
    public void init(){
        patientMedicalrecords = new ArrayList<>();
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
        getSelectedPatientRecords(patient);

        FacesMessage msg = new FacesMessage("Selected", selectedPatient.getFullName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

//    getting the medicalredords for a paticular patient
    public void getSelectedPatientRecords(Patient patient){
        List<MedicalRecord> records = medicalRecordService.getMedicalRecordsByPatient(patient.getPatientID());
        setMedicalRecords(records);
        refreshRecords();
    }

//    Saving a new diagnosis for the selected patient
    public String DiagnosePatient(){
        System.out.println("$$$$ saving diagnosis $$$$");
        Long patientId = selectedPatient.getPatientID();
        Long doctorId = userBean.getCurrentUser().getDoctor().getDoctorID();

        LocalDate date = LocalDate.now();
        getSelectedPatientRecords(selectedPatient);



        medicalRecordService.createMedicalRecord( patientId, doctorId, diagnosis, date);

        return navigationBean.navigateToMedicalRecordList();
    }

//    refresh medicalRecords
    public void refreshRecords(){
        patientMedicalrecords = patientMedicalrecords.stream()
                .sorted(
                        Comparator.comparing(MedicalRecord::getRecordDate)
                                .reversed()
                                .thenComparing(MedicalRecord::getRecordID)
                )
                .collect(Collectors.toList());
    }


    //    getters and setters
    public Patient getSelectedPatient(){
        return this.selectedPatient;
    }

    public void setSelectedPatient(Patient selectedPatient){
        this.selectedPatient = selectedPatient;
    }

    public void setMedicalRecords(List<MedicalRecord> records){
        this.patientMedicalrecords = records;
    }

    public List<MedicalRecord> getPatientMedicalrecords(){
        return  this.patientMedicalrecords;
    }

    public void setDiagnosis(String diagnosis){
        this.diagnosis = diagnosis;
    }

    public String getDiagnosis(){
        return this.diagnosis;
    }



}