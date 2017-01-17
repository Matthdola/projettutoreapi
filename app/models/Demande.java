package models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.joda.time.DateTime;
import play.modules.mongodb.jackson.MongoDB;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Demande extends Document {
    private String id;
    private LocalDateTime dateDemande;
    private String idPatient;
    private String etat;

    public static JacksonDBCollection<Demande, String> collection = MongoDB.getCollection("demandes", Demande.class, String.class);

    public Demande(){

    }

    public Demande(LocalDateTime dateDemande, String idPatient, String etat){
        this.dateDemande = dateDemande;
        this.idPatient = idPatient;
        this.etat = etat;
    }

    @Override
    public DBObject toBson() {
        return null;
    }

    public LocalDateTime getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDateTime dateDemande) {
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
