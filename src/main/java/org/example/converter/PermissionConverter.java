package org.example.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.example.enums.Permissions;

@FacesConverter(value = "permissionConverter", forClass = Permissions.class)
public class PermissionConverter implements Converter<Permissions> {

    @Override
    public Permissions getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Permissions.valueOf(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Permissions value) {
        if (value == null) {
            return "";
        }
        return value.name();
    }
}
