package org.example.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.enums.AppointmentStatus;
import org.example.models.entities.Appointment;
import org.example.services.AppointmentService;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Named("appointmentScheduleView")
@ViewScoped
public class AppointmentScheduleView implements Serializable {

    private ScheduleModel lazyEventModel;

    private Long selectedAppointmentId;
    private AppointmentStatus selectedStatus = null;
    private AppointmentStatus[] allStatuses;

    @Inject
    private AppointmentService appointmentService;

    @PostConstruct
    public void init(){
        lazyEventModel = new LazyScheduleModel() {
            @Override
            public void loadEvents(LocalDateTime startDateTime, LocalDateTime endDateTime) {
                clear();

                List<Appointment> appointments = appointmentService.findAppointmentsBetween(startDateTime, endDateTime);

                for(Appointment appointment: appointments){
                    String title = appointment.getPatient().getFullName() + " with Dr." +
                            appointment.getDoctor().getFullName();

                    addEvent(DefaultScheduleEvent.builder()
                            .title(title)
                            .startDate(appointment.getAppointmentDateTime())
                            .endDate(appointment.getAppointmentDateTime().plusMinutes(30))
                            .description("Status: " + appointment.getStatus())
                            .data(appointment.getAppointmentID())
                            .build()
                    );
                }
            }
        };
    }

    public void changeStatus() {

        System.out.println("Changing the status");

        if (selectedAppointmentId != null && selectedStatus != null) {
            boolean success = appointmentService.updateAppointmentStatus(selectedAppointmentId, selectedStatus);
            if (success) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("Appointment updated to " + selectedStatus));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to update appointment", null));
            }

            // Optional: refresh schedule component
            PrimeFaces.current().ajax().update("appointmentSchedule", "statusDialog");
        }
    }

    public void onEventSelect(SelectEvent<ScheduleEvent<?>> event){
        ScheduleEvent<?> selectedEvent = event.getObject();
        this.selectedAppointmentId = (long) selectedEvent.getData();

    }

    // Getters and setters
    public ScheduleModel getLazyEventModel(){
        return lazyEventModel;
    }

    public AppointmentStatus[] getAllStatuses() {
        return AppointmentStatus.values();
    }

    public void setAllStatuses(AppointmentStatus[] allStatuses){
        this.allStatuses =allStatuses;
    }

    public AppointmentStatus getSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(AppointmentStatus selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

    public Long getSelectedAppointmentId() {
        return selectedAppointmentId;
    }

    public void setSelectedAppointmentId(Long selectedAppointmentId) {
        this.selectedAppointmentId = selectedAppointmentId;
    }



}