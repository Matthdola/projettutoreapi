package models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import mongo.Collection;
import mongo.Document;
import mongo.QueryResult;
import net.vz.mongodb.jackson.DBCursor;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonProperty;
import play.modules.mongodb.jackson.MongoDB;
import net.vz.mongodb.jackson.JacksonDBCollection;

import javax.rmi.CORBA.Util;

public class Medecin extends Utilisateur {
    public static final String collectionName = "medecins";
    @JsonIgnore
    public static final String SPECIALITES = "specialites";

    @JsonIgnore
    public static final String CENTRE_PRINCIPAL = "centre_principal";

    @JsonIgnore
    public static final String CENTRES = "centres";

    @JsonIgnore
    public static final String JOURS_CONSULTATION = "jours_consultations";

    @JsonProperty(SPECIALITES)
    private ArrayList<String> specialites;

    @JsonProperty(CENTRE_PRINCIPAL)
    private String centrePrincipal;

    @JsonProperty(CENTRES)
    private ArrayList<String> centres;

    @JsonProperty(JOURS_CONSULTATION)
    private ArrayList<String> joursConsultation;

    
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
        Medecin medecin = (Medecin) Utilisateur.fromBson(bson);

        Object specialite = bson.get(Medecin.SPECIALITES);
        if (specialite != null) {
            //medecin.setSpecialites(specialite);
        }
        Object centrePrin = bson.get(Medecin.CENTRE_PRINCIPAL);
        if (centrePrin != null) {
            //medecin.setCentrePrincipal(centrePrin.toString());
        }
        Object centres = bson.get(Medecin.CENTRES);
        if (centres != null) {
            //medecin.setCentres(profession.toString());
        }
        Object jours = bson.get(Medecin.JOURS_CONSULTATION);
        if (jours != null) {
            //medecin.setJoursConsultation(telephone.toString());
        }
        return medecin;
    }


    public ArrayList<String> getCentres() {
        return centres;
    }

    public void setCentres(ArrayList<String> centres) {
        this.centres = centres;
    }



    public static QueryResult listByCentre(String centre) {
        BasicDBObject query = new BasicDBObject();
        query.put("centre", centre );
        return Collection.find(collectionName, query, Utilisateur::fromBson, "User with the centre " + centre + "not found");


    }

    public static QueryResult listBySpecialite(String specialite) {
        BasicDBObject query = new BasicDBObject();
        query.put("specialite", specialite );
        return Collection.find(collectionName, query, Utilisateur::fromBson, "User with the specialite " + specialite + "not found");
    }

}
