package models;

import com.mongodb.DBObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Created by matth on 28/09/2016.
 */
public class Consultation extends Document {
    private String id;
    private LocalDate dateConsultation;
    private LocalTime heureConsultation;
    private String idRendezvous;
    private ArrayList<String> acteMedicals;

    public Consultation(){

    }

    public LocalDate getDateConsultation() {
        return dateConsultation;
    }

    public void setDateConsultation(LocalDate dateConsultation) {
        this.dateConsultation = dateConsultation;
    }

    public LocalTime getHeureConsultation() {
        return heureConsultation;
    }

    public void setHeureConsultation(LocalTime heureConsultation) {
        this.heureConsultation = heureConsultation;
    }

    public String getIdRendezvous() {
        return idRendezvous;
    }

    public void setIdRendezvous(String idRendezvous) {
        this.idRendezvous = idRendezvous;
    }

    public ArrayList<String> getActeMedicals() {
        return acteMedicals;
    }

    public void setActeMedicals(ArrayList<String> acteMedicals) {
        this.acteMedicals = acteMedicals;
    }

    @Override
    public DBObject toBson() {
        return null;
    }
}
