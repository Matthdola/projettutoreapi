package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import mongo.Collection;
import mongo.Document;
import mongo.Error;
import mongo.QueryResult;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import play.modules.mongodb.jackson.MongoDB;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Demande extends Document {
    public static final String collectionName = "demandes";
    
    @JsonProperty("date_demande")
    @JsonSerialize(using = util.DateTimeSerializer.class)
    @JsonDeserialize(using = util.DateTimeDeserializer.class)
    private DateTime dateDemande;

    @JsonProperty("id_patient")
    private String idPatient;

    private String etat;
    private String motifs;
    
    public Demande(){

    }

    public Demande(String idPatient, String etat){
        this.dateDemande = DateTime.now();
        this.idPatient = idPatient;
        this.etat = etat;
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public String getCollectionName() {
        return "demandes";
    }

    @Override
    public String getDocumentName() {
        return "demande";
    }

    @Override
    public DBObject toBson() {
        BasicDBObject object = new BasicDBObject();

        if (getId() != null && !getId().isEmpty()) {
            object.append(Document.ID, new ObjectId(getId()));
        }

        object.append("date", dateDemande)
                .append("id_patient", idPatient)
                .append("etat", etat)
                .append("motifs", motifs)
                .append(CREATED_AT, getCreatedAt() == null ? null : getCreatedAt().toString())
                .append(UPDATED_AT, getUpdatedAt() == null ? null : getUpdatedAt().toString())
                .append(DELETED_AT, getDeletedAt() == null ? null : getDeletedAt().toString())
                .append(CREATED_BY, getCreatedBy())
                .append(UPDATED_BY, getUpdatedBy())
                .append(DELETED_BY, getDeletedBy());
        return object;
    }

    public DateTime getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(DateTime dateDemande) {
        this.dateDemande = dateDemande;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getMotifs() {
        return motifs;
    }

    public void setMotifs(String motifs) {
        this.motifs = motifs;
    }

    public static QueryResult listByEtat(String etat) {
        BasicDBObject query = new BasicDBObject();
        query.put("etat", etat);
        return Collection.findAll(collectionName, query, Demande::fromBson);
    }

    public static QueryResult listByIdPatient(String idPatient) {
        ArrayList<Demande> demandes = new ArrayList<>();
        BasicDBObject query = new BasicDBObject();
        query.put("id_patient", idPatient);
        return Collection.findAll(collectionName, query, Demande::fromBson);
    }

    public static QueryResult listByDate(DateTime date){
        BasicDBObject query = new BasicDBObject();
        query.put("dateDemande", date);
        return Collection.findAll(collectionName, query, Demande::fromBson);
    }

    public static ActeMedical fromBson(DBObject bson){
        ActeMedical acteMedical = new ActeMedical();

        return acteMedical;
    }


    public static QueryResult findAll() {
        return Collection.findAll(collectionName, new BasicDBObject(), Demande::fromBson);
    }

    public static QueryResult findById(String id){

        return Collection.findById(collectionName, id, Demande::fromBson, "Demande not found");

    }

    public static QueryResult findByName(String name){

        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        return Collection.findAll(collectionName, query, Demande::fromBson);
    }

    public static QueryResult remove(Demande demande){
        return Collection.delete(collectionName, demande);
    }

    public static QueryResult save(Demande demande){
        return Collection.insert(collectionName, demande, e-> new Error(Error.DUPLICATE_KEY, "A Demande with the same name already existe"));

    }

    public static QueryResult update(Demande demande){
        return Collection.update(collectionName, demande, e-> new Error(Error.DUPLICATE_KEY, "Demande with the same name already exist"));
    }
}
