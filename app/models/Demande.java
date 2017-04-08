package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
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
    private String id;

    @JsonProperty("date_demande")
    @JsonSerialize(using = util.DateTimeSerializer.class)
    @JsonDeserialize(using = util.DateTimeDeserializer.class)
    private DateTime dateDemande;

    @JsonProperty("id_patient")
    private String idPatient;

    private String etat;
    private String motifs;

    public static JacksonDBCollection<Demande, String> collection = MongoDB.getCollection("demandes", Demande.class, String.class);

    public Demande(){

    }

    public Demande(String idPatient, String etat){
        this.dateDemande = DateTime.now();
        this.idPatient = idPatient;
        this.etat = etat;
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

    public static List<Demande> findAll() {
        return Demande.collection.find().toArray();
    }

    public static List<Demande> listByEtat(String etat) {
        ArrayList<Demande> demandes = new ArrayList<>();

        BasicDBObject query = new BasicDBObject();
        query.put("etat", etat);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            Demande demande = (Demande)cursor.next();
            if(demande.etat.contains(etat)){
                demandes.add(demande);
            }
        }

        return demandes;
    }

    public static List<Demande> listByIdPatient(String idPatient) {
        ArrayList<Demande> demandes = new ArrayList<>();
        BasicDBObject query = new BasicDBObject();
        query.put("id_patient", idPatient);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            Demande demande = (Demande)cursor.next();
            if(demande.getIdPatient().equals(idPatient)){
                demandes.add(demande);
            }
        }

        return demandes;
    }

    public static Demande findById(String id){
        Demande demande = Demande.collection.findOneById(id);
        return demande;
    }

    public static List<Demande> listByDate(DateTime date){
        final  List<Demande> demandes = new ArrayList<>();

        BasicDBObject query = new BasicDBObject();
        query.put("dateDemande", date);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            demandes.add((Demande) cursor.next());
        }
        return demandes;
    }

    public static boolean remove(Demande demande){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(demande.getId()) );
        try {
            Demande.collection.remove(query);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static void save(Demande demande){
        Demande.collection.save(demande);
    }

    public static void update(Demande demande){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(demande.getId()) );
        collection.update(query, demande.toBson());
    }
}
