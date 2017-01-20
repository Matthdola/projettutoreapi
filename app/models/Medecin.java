package models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vz.mongodb.jackson.DBCursor;
import org.bson.types.ObjectId;
import play.modules.mongodb.jackson.MongoDB;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.Id;
import org.codehaus.jackson.annotate.JsonProperty;

public class Medecin extends Utilisateur {
    private ArrayList<String> specialites;
    private ArrayList<String> centres;
    private ArrayList<String> joursConsultation;

    public static JacksonDBCollection<Medecin, String> collection = MongoDB.getCollection("medecins", Medecin.class, String.class);
    
    public Medecin(String id) {
        super();
        this.setId(id);
        this.specialites = new ArrayList<>();
        this.centres = new ArrayList<>();
        this.joursConsultation = new ArrayList<>();
    }

    public Medecin() {
        super();
        this.specialites = new ArrayList<>();
        this.centres = new ArrayList<>();
        this.joursConsultation = new ArrayList<>();
    }

    public ArrayList<String> getSpecialites() {
        return specialites;
    }

    public String typeUtilisateur(){
        return "medecin";
    }

    public void setSpecialites(ArrayList<String> specialites) {
        this.specialites = specialites;
    }

    public void addSpecialite(String specialite){
        this.specialites.add(specialite);
    }

    public  void addCentre(String centre){
        this.centres.add(centre);
    }

    public  void addJourConsultation(String jour){
        this.joursConsultation.add(jour);
    }

    public ArrayList<String> getJoursConsultation() {
        return joursConsultation;
    }

    public void setJoursConsultation(ArrayList<String> joursConsultation) {
        this.joursConsultation = joursConsultation;
    }

    @JsonIgnore
    @Override
    public DBObject toBson() {
        BasicDBObject object = new BasicDBObject();

        if (getId() != null && !getId().isEmpty()) {
            object.append(Document.ID, new ObjectId(getId()));
        }

        object.append("nom", nom)
                .append("prenom", prenom)
                .append("telephone", telephone)
                .append("prefession", profession)
                .append("email", email)
                .append("specialites", specialites)
                .append("centres", centres)
                .append("jours_consultation", joursConsultation)
                .append(CREATED_AT, getCreatedAt() == null ? null : getCreatedAt().toString())
                .append(UPDATED_AT, getUpdatedAt() == null ? null : getUpdatedAt().toString())
                .append(DELETED_AT, getDeletedAt() == null ? null : getDeletedAt().toString())
                .append(CREATED_BY, getCreatedBy())
                .append(UPDATED_BY, getUpdatedBy())
                .append(DELETED_BY, getDeletedBy());

        return object;

    }

    public static Medecin fromBson(DBObject bson){
        Medecin medecin = new Medecin();

        return medecin;
    }


    public ArrayList<String> getCentres() {
        return centres;
    }

    public void setCentres(ArrayList<String> centres) {
        this.centres = centres;
    }

    public static List<Medecin> findAll() {
        return Medecin.collection.find().toArray();
    }

    public static List<Medecin> listByCentre(String centre) {
        ArrayList<Medecin> meds = new ArrayList<>();

        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            Medecin medecin = (Medecin)cursor.next();
            if(medecin.centres.contains(centre)){
                meds.add(medecin);
            }
        }

        return meds;
    }

    public static List<Medecin> listBySpecialite(String specialite) {
        ArrayList<Medecin> meds = new ArrayList<>();
        BasicDBObject query = new BasicDBObject();
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            Medecin medecin = (Medecin)cursor.next();
            if(medecin.specialites.contains(specialite)){
                meds.add(medecin);
            }
        }

        return meds;
    }

    public static Medecin findById(String id){
        Medecin medecin = Medecin.collection.findOneById(id);
        return medecin;
    }

    public static List<Medecin> findByName(String name){
        final  List<Medecin> results = new ArrayList<>();

        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            results.add((Medecin) cursor.next());
        }
        return results;
    }

    public static boolean remove(Medecin medecin){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(medecin.getId()) );
        try {
            Medecin.collection.remove(query);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static void save(Medecin medecin){
        Medecin.collection.save(medecin);
    }

    public static void update(Medecin medecin){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(medecin.getId()) );
        collection.update(query, medecin.toBson());
    }
}
