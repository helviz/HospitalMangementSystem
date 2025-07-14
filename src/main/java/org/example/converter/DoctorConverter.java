package org.example.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.models.entities.Doctor;
import org.example.services.DoctorService;

import java.util.Optional;

@FacesConverter(value = "doctorConverter", managed = true)
@ApplicationScoped
public class DoctorConverter implements Converter<Doctor> {

    @Inject
    private DoctorService doctorService;

    @Override
    public Doctor getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        Long id = Long.valueOf(value);
        Optional<Doctor> doctorOptional = doctorService.getDoctorById(id);

        return doctorOptional.orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Doctor doctor) {
        if (doctor == null || doctor.getDoctorID() == null) return "";
        return doctor.getDoctorID().toString();
    }
}
