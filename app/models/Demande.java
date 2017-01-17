package models;

import com.mongodb.DBObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Demande extends Document {
    private String id;
    private LocalDateTime dateDemande;
    private String idPatient;
    private String etat;
    public static List<Demande> demandes;


    static {
        demandes = new ArrayList<Demande>();
        demandes.add(new Demande(LocalDateTime.now(), "Matthias", "Encours"));
        demandes.add(new Demande(LocalDateTime.now(), "Joseph", "Encours"));
        demandes.add(new Demande(LocalDateTime.now(), "Daniel", "traite"));
        demandes.add(new Demande(LocalDateTime.now(), "Odile", "traite"));
        demandes.add(new Demande(LocalDateTime.now(), "Natacha", "Encours"));
    }

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
        return new ArrayList<>(demandes);
    }

    public static Demande findById(String id){
        for (Demande candidate : demandes){
            if(candidate.getId().equals(id)){
                return candidate;
            }
        }
        return null;
    }

    public static Demande findByIdPatient(String idPatient){
        final  List<Demande> results = new ArrayList<>();
        for (Demande candidate : demandes){
            if(candidate.idPatient.equals(idPatient)){
                return candidate;
            }
        }
        return null;
    }

    public static List<Demande> findByIdEtat(String etat){
        final  List<Demande> results = new ArrayList<>();
        for (Demande candidate : demandes){
            if(candidate.etat.equals(etat)){
                results.add(candidate);
            }
        }
        return results;
    }

    public static boolean remove(Demande demande){
        return demandes.remove(demande);
    }

    public static void save(Demande demande){
        demandes.add(demande);
    }

    public static void update(Demande demande){
        Predicate<Demande> demandePredicate = p -> p.getId().equals(demande.getId());
        demandes.removeIf(demandePredicate);
        demandes.add(demande);
    }
}
