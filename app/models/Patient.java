package models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import play.modules.mongodb.jackson.MongoDB;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Patient extends Utilisateur {
    private String codeAssurance;
    private String idAssureur;
    private List<String> maladieChroniques;
    private List<String> antecedents;
    private List<String> allergies;

    public static JacksonDBCollection<Patient, String> collection = MongoDB.getCollection("patients", Patient.class, String.class);

    public Patient(){

    }

    public String typeUtilisateur(){
        return "patient";
    }

    public String getCodeAssurance() {
        return codeAssurance;
    }

    public void setCodeAssurance(String codeAssurance) {
        this.codeAssurance = codeAssurance;
    }

    public String getIdAssureur() {
        return idAssureur;
    }

    public void setIdAssureur(String idAssureur) {
        this.idAssureur = idAssureur;
    }

    public Patient(String ean, String name, String description){

    }

    @Override
    public DBObject toBson() {
        return null;
    }

    public String toString(){
        return String.format("%s - %s", codeAssurance, idAssureur);
    }


    public List<String> getMaladieChroniques() {
        return maladieChroniques;
    }

    public void setMaladieChroniques(List<String> maladieChroniques) {
        this.maladieChroniques = maladieChroniques;
    }

    public List<String> getAntecedents() {
        return antecedents;
    }

    public void setAntecedents(List<String> antecedents) {
        this.antecedents = antecedents;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }


    public static List<Patient> findAll() {
        return Patient.collection.find().toArray();
    }


    public static Patient findById(String id){
        Patient patient = Patient.collection.findOneById(id);
        return patient;
    }

    public static List<Patient> findByName(String name){
        final  List<Patient> results = new ArrayList<>();

        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            results.add((Patient) cursor.next());
        }
        return results;
    }

    public static boolean remove(Patient patient){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(patient.getId()) );
        try {
            Patient.collection.remove(query);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static void save(Patient patient){
        Patient.collection.save(patient);
    }

    public static void update(Patient patient){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(patient.getId()) );
        collection.update(query, patient.toBson());
    }
}
