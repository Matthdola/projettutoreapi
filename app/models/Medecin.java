package models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vz.mongodb.jackson.DBCursor;
import org.bson.types.ObjectId;
import play.modules.mongodb.jackson.MongoDB;
import net.vz.mongodb.jackson.JacksonDBCollection;

import javax.rmi.CORBA.Util;

public class Medecin extends Utilisateur {
    private ArrayList<String> specialites;
    private String centrePrincipal;
    private ArrayList<String> centres;
    private ArrayList<String> joursConsultation;

    public static JacksonDBCollection<Utilisateur, String> collection = MongoDB.getCollection("utilisateurs", Utilisateur.class, String.class);
    
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

    public String getCentrePrincipal() {
        return centrePrincipal;
    }

    public void setCentrePrincipal(String centrePrincipal) {
        this.centrePrincipal = centrePrincipal;
    }

    public void setJoursConsultation(ArrayList<String> joursConsultation) {
        this.joursConsultation = joursConsultation;
    }

    @JsonIgnore
    @Override
    public DBObject toBson() {
        BasicDBObject object = (BasicDBObject)super.toBson();

        if (getId() != null && !getId().isEmpty()) {
            object.append(Document.ID, new ObjectId(getId()));
        }

        object.append("specialites", specialites)
                .append("centre_principal", centrePrincipal)
                .append("centres", centres)
                .append("jours_consultation", joursConsultation);

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


    public static List<Utilisateur> findAll() {
        ArrayList<Utilisateur> meds = new ArrayList<>();
        BasicDBObject query = new BasicDBObject();
        query.put("type", "MEDECIN");

        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            meds.add((Utilisateur)cursor.next());
        }

        return meds;
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

    public static Utilisateur findById(String id){
        Utilisateur medecin = Medecin.collection.findOneById(id);
        return medecin;
    }

    /*
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
    */

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
