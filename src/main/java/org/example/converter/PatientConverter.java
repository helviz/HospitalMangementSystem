package org.example.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import org.example.models.entities.Patient;
import org.example.services.PatientService;


import java.util.Optional;


@FacesConverter(value = "patientConverter", forClass = Patient.class, managed = true)
public class PatientConverter implements Converter<Patient> {


    private final PatientService patientService = getPatientService();

    private PatientService getPatientService() {
        return  CDI.current().select(PatientService.class).get();
    }

    /*Convert the specified
     string value,
    which is associated with the specified UIComponent,
    into a model data object that is appropriate for being stored during the
    Apply Request Values phase of the request processing lifecycle.*/
    @Override
    public Patient getAsObject(FacesContext context, UIComponent component, String value){
        if (value == null || value.trim().isEmpty()){
            return null;
        }
        Long id = Long.valueOf(value);
        Optional<Patient> patientOptional = patientService.getPatientById(id);

        return  patientOptional.orElse(null);
    }

    /*Convert the specified model object value,
    which is associated with the specified UIComponent,
    into a String that is suitable for being included in the response
    generated during the Render Response phase of the request
    processing lifeycle.*/

    @Override
    public String getAsString(FacesContext context, UIComponent component, Patient patient){
        if (patient == null || patient.getPatientID() == null){
            return "";
        }
        return String.valueOf(patient.getPatientID());
    }

}
