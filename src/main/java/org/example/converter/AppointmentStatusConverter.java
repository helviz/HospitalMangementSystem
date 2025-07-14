package org.example.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.example.enums.AppointmentStatus;

@FacesConverter(value = "appointmentStatusConverter", managed = true)
public class AppointmentStatusConverter implements Converter<AppointmentStatus> {

    @Override
    public AppointmentStatus getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return AppointmentStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, AppointmentStatus status) {
        if (status == null) {
            return "";
        }
        return status.name();
    }
}