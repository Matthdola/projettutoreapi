package models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import mongo.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

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
    public boolean isError() {
        return false;
    }

    @Override
    public String getCollectionName() {
        return "consultations";
    }

    @Override
    public String getDocumentName() {
        return "consultation";
    }

    @Override
    public DBObject toBson() {
        BasicDBObject object = new BasicDBObject();

        if (getId() != null && !getId().isEmpty()) {
            object.append(Document.ID, new ObjectId(getId()));
        }

        object.append("date", dateConsultation)
                .append("heure", heureConsultation)
                .append("id_rendez_vous", idRendezvous)
                .append("actes_medicales", acteMedicals)
                .append(CREATED_AT, getCreatedAt() == null ? null : getCreatedAt().toString())
                .append(UPDATED_AT, getUpdatedAt() == null ? null : getUpdatedAt().toString())
                .append(DELETED_AT, getDeletedAt() == null ? null : getDeletedAt().toString())
                .append(CREATED_BY, getCreatedBy())
                .append(UPDATED_BY, getUpdatedBy())
                .append(DELETED_BY, getDeletedBy());

        return object;
    }
}
