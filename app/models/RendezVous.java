package models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.bson.types.ObjectId;
import play.modules.mongodb.jackson.MongoDB;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class RendezVous extends Document {
    private String idDemande;
    private String idMedecin;
    private LocalDate jours;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String idConsultation;
    private String motifs;

    public static JacksonDBCollection<RendezVous, String> collection = MongoDB.getCollection("rendez_vous", RendezVous.class, String.class);

    public RendezVous(){
        
    }

    public RendezVous(String idDemande, String idMedecin){
        this.idDemande = idDemande;
        this.idMedecin = idMedecin;
    }

    public RendezVous(String idDemande, String idMedecin, LocalDate jours, LocalTime heureDebut){
        this.idDemande = idDemande;
        this.idMedecin = idMedecin;
        this.jours = jours;
        this.heureDebut = heureDebut;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public String getIdMedecin() {
        return idMedecin;
    }

    public void setIdMedecin(String idMedecin) {
        this.idMedecin = idMedecin;
    }

    public LocalDate getJours() {
        return jours;
    }

    public void setJours(LocalDate jours) {
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
    public DBObject toBson() {
        BasicDBObject object = new BasicDBObject();

        if (getId() != null && !getId().isEmpty()) {
            object.append(Document.ID, new ObjectId(getId()));
        }

        object.append("id_demande", idDemande)
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

    public static List<RendezVous> findAll() {
        return RendezVous.collection.find().toArray();
    }
    /*
        public static List<RendezVous> listByCentre(String centre) {
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
        /*
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

         public static List<RendezVous> findByName(String idMedecin){
            final  List<RendezVous> results = new ArrayList<>();

            BasicDBObject query = new BasicDBObject();
            query.put("idMedecin", idMedecin);
            DBCursor cursor = collection.find(query);
            while(cursor.hasNext()) {
                results.add((RendezVous) cursor.next());
            }
            return results;
        }

        */

    public static RendezVous findById(String id){
        RendezVous rendezVous = RendezVous.collection.findOneById(id);
        return rendezVous;
    }

    public static boolean remove(RendezVous rendezVous){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(rendezVous.getId()) );
        try {
            RendezVous.collection.remove(query);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static void save(RendezVous rendezVous){
        RendezVous.collection.save(rendezVous);
    }

    public static void update(RendezVous rendezVous){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(rendezVous.getId()) );
        collection.update(query, rendezVous.toBson());
    }
}
