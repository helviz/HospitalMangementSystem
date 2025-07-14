package org.example.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.enums.Role;
import org.example.models.entities.Appointment;
import org.example.models.entities.Doctor;
import org.example.models.entities.Staff;
import org.example.services.AppointmentService;
import org.example.services.DoctorService;
import org.example.services.StaffService;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Named("dashboardBean")
@RequestScoped
public class DashboardBean {
    private int nursesNum;
    private int doctorsNum;
    private int CurrentAppointsNum;
    
    @Inject
    private StaffService staffService;

    @Inject
    private DoctorService doctorService;

    @Inject
    private AppointmentService appointmentService;


// setting the variables
    @PostConstruct
    public void init(){
        List<Staff> staffList =  staffService.getAllStaff();
        nursesNum = (int) staffList.stream()
                .filter(p->p.getRole() == Role.NURSE)
                .count();

        List<Doctor> doctorList = doctorService.getAllDoctors();
        doctorsNum = doctorList.size();

        List<Appointment> appointmentList =  appointmentService.findAppointmentsForToday();
        CurrentAppointsNum = appointmentList.size();

    }


        


//    getters and setters
    public void setNursesNum(int num){
        this.nursesNum = num;
    }

    public int getNursesNum(){
        return  this.nursesNum;
    }

    public void setDoctorsNum(int num){
        this.doctorsNum = num;
    }

    public int getDoctorsNum(){
        return this.doctorsNum;
    }

    public void setCurrentAppointsNum(int num){
        this.CurrentAppointsNum= num;
    }

    public int getCurrentAppointsNum(){
        return  this.CurrentAppointsNum;
    }

    public String getCurrentTime() {
        return java.time.LocalTime.now().withNano(0).toString();
    }
    public String getCurrentDateDetails() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy");
        return now.format(formatter);
    }






}
