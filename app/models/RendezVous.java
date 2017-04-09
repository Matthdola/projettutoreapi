package models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import mongo.Collection;
import mongo.Document;
import mongo.Error;
import mongo.QueryResult;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.bson.types.ObjectId;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import play.modules.mongodb.jackson.MongoDB;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RendezVous extends Document {
    public static final String collectionName = "rendez_vous";

    @JsonProperty("id_demande")
    private String idRendezVous;

    @JsonProperty("id_medecin")
    private String idMedecin;

    @JsonSerialize(using = util.DateTimeSerializer.class)
    @JsonDeserialize(using = util.DateTimeDeserializer.class)
    private DateTime jours;

    private LocalTime heureDebut;
    private LocalTime heureFin;

    @JsonProperty("id_consultation")
    private String idConsultation;

    @JsonProperty("motifs")
    private String motifs;
    
    public RendezVous(){
        this.jours = DateTime.now();
    }

    public RendezVous(String idRendezVous, String idMedecin){
        this.idRendezVous = idRendezVous;
        this.idMedecin = idMedecin;
        this.jours = DateTime.now();
    }

    public RendezVous(String idRendezVous, String idMedecin, DateTime jours, LocalTime heureDebut){
        this.idRendezVous = idRendezVous;
        this.idMedecin = idMedecin;
        this.jours = jours;
        this.heureDebut = heureDebut;
    }

    public String getIdRendezVous() {
        return idRendezVous;
    }

    public void setIdRendezVous(String idRendezVous) {
        this.idRendezVous = idRendezVous;
    }

    public String getIdMedecin() {
        return idMedecin;
    }

    public void setIdMedecin(String idMedecin) {
        this.idMedecin = idMedecin;
    }

    public DateTime getJours() {
        return jours;
    }

    public void setJours(DateTime jours) {
        this.jours = jours;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public String getIdConsultation() {
        return idConsultation;
    }

    public void setIdConsultation(String idConsultation) {
        this.idConsultation = idConsultation;
    }

    public String getMotifs() {
        return motifs;
    }

    public void setMotifs(String motifs) {
        this.motifs = motifs;
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public String getCollectionName() {
        return "rendez-vous";
    }

    @Override
    public String getDocumentName() {
        return "rendez-vous";
    }

    @Override
    public DBObject toBson() {
        BasicDBObject object = new BasicDBObject();

        if (getId() != null && !getId().isEmpty()) {
            object.append(Document.ID, new ObjectId(getId()));
        }

        object.append("id_demande", idRendezVous)
                .append("id_consultation", idConsultation)
                .append("jours", jours)
                .append("heure", heureDebut)
                .append("motifs", motifs)
                .append(CREATED_AT, getCreatedAt() == null ? null : getCreatedAt().toString())
                .append(UPDATED_AT, getUpdatedAt() == null ? null : getUpdatedAt().toString())
                .append(DELETED_AT, getDeletedAt() == null ? null : getDeletedAt().toString())
                .append(CREATED_BY, getCreatedBy())
                .append(UPDATED_BY, getUpdatedBy())
                .append(DELETED_BY, getDeletedBy());

        return object;
    }


    public static QueryResult listByEtat(String etat) {
        BasicDBObject query = new BasicDBObject();
        query.put("etat", etat);
        return Collection.findAll(collectionName, query, RendezVous::fromBson);
    }

    public static QueryResult listByIdPatient(String idPatient) {
        ArrayList<RendezVous> demandes = new ArrayList<>();
        BasicDBObject query = new BasicDBObject();
        query.put("id_patient", idPatient);
        return Collection.findAll(collectionName, query, RendezVous::fromBson);
    }

    public static QueryResult listByDate(DateTime date){
        BasicDBObject query = new BasicDBObject();
        query.put("dateRendezVous", date);
        return Collection.findAll(collectionName, query, RendezVous::fromBson);
    }

    public static RendezVous fromBson(DBObject bson){
        RendezVous rendezVous = new RendezVous();

        return rendezVous;
    }


    public static QueryResult findAll() {
        return Collection.findAll(collectionName, new BasicDBObject(), RendezVous::fromBson);
    }

    public static QueryResult findById(String id){

        return Collection.findById(collectionName, id, RendezVous::fromBson, "RendezVous not found");

    }

    public static QueryResult findByName(String name){

        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        return Collection.findAll(collectionName, query, RendezVous::fromBson);
    }

    public static QueryResult remove(RendezVous rendezVous){
        return Collection.delete(collectionName, rendezVous);
    }

    public static QueryResult save(RendezVous rendezVous){
        return Collection.insert(collectionName, rendezVous, e-> new Error(Error.DUPLICATE_KEY, "A RendezVous with the same name already existe"));

    }

    public static QueryResult update(RendezVous rendezVous){
        return Collection.update(collectionName, rendezVous, e-> new Error(Error.DUPLICATE_KEY, "RendezVous with the same name already exist"));
    }
}
