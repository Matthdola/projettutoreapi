package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import mongo.Document;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonProperty;
import play.modules.mongodb.jackson.MongoDB;

import java.util.ArrayList;
import java.util.List;

public class Patient extends Utilisateur {

    @JsonIgnore
    public static final String CODE_ASSURANCE = "code_assurance";

    @JsonIgnore
    public static final String ID_ASSUREUR = "id_assureur";

    @JsonIgnore
    public static final String MALADIE_CHRONIQUES = "maladies_chroniques";

    @JsonIgnore
    public static final String ANTECEDENTS = "antecedents";

    @JsonIgnore
    public static final String ALLERGIES = "allergies";

    @JsonProperty(CODE_ASSURANCE)
    private String codeAssurance;

    @JsonProperty(ID_ASSUREUR)
    private String idAssureur;

    @JsonProperty(MALADIE_CHRONIQUES)
    private List<String> maladieChroniques;

    @JsonProperty(ANTECEDENTS)
    private List<String> antecedents;

    @JsonProperty(ALLERGIES)
    private List<String> allergies;

    public Patient(){
        maladieChroniques = new ArrayList<>();
        antecedents = new ArrayList<>();
        allergies = new ArrayList<>();
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
        BasicDBObject object = (BasicDBObject)super.toBson();

        if (getId() != null && !getId().isEmpty()) {
            object.append(Document.ID, new ObjectId(getId()));
        }

        object.append("code_assurance", codeAssurance)
                .append("id_assureur", idAssureur)
                .append("maladie_chroniques", maladieChroniques)
                .append("antecedents", antecedents)
                .append("allergies", allergies);

        return object;
    }

    public static Patient fromBson(DBObject bson){
        Patient patient = (Patient) Utilisateur.fromBson(bson);

        Object codeAssurance = bson.get(Patient.CODE_ASSURANCE);
        if (codeAssurance != null) {
            //patient.setSpecialites(specialite);
        }
        Object idAssureur = bson.get(Patient.ID_ASSUREUR);
        if (idAssureur != null) {
            //patient.setCentrePrincipal(centrePrin.toString());
        }
        Object maladisChroniques = bson.get(Patient.MALADIE_CHRONIQUES);
        if (maladisChroniques != null) {
            //patient.setCentres(profession.toString());
        }
        Object antecedents = bson.get(Patient.ANTECEDENTS);
        if (antecedents != null) {
            //patient.setJoursConsultation(telephone.toString());
        }
        Object allergies = bson.get(Patient.ALLERGIES);
        if (allergies != null) {
            //patient.setJoursConsultation(telephone.toString());
        }
        return patient;
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

}
