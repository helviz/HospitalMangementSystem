package org.example.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.example.enums.Speciality;

@FacesConverter(value = "specialityConverter", managed = true)
public class SpecialityConverter implements Converter<Speciality> {

    @Override
    public Speciality getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Speciality.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Speciality speciality) {
        if (speciality == null) {
            return "";
        }
        return speciality.name();
    }
}