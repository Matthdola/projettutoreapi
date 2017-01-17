package models;

import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Specialite extends Document {
    private String id;
    private String nom;
    private String domaine;

    public static List<Specialite> specialites;

    static {
        specialites = new ArrayList<Specialite>();
        specialites.add(new Specialite());
        specialites.add(new Specialite());
        specialites.add(new Specialite());
        specialites.add(new Specialite());
        specialites.add(new Specialite());
    }
    
    public Specialite(){

    }

    public Specialite(String nom){
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDomaine() {
        return domaine;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }


    @Override
    public DBObject toBson() {
        return null;
    }

    public static List<Specialite> findAll() {
        return new ArrayList<>(specialites);
    }

    public static Specialite findById(String id){
        for (Specialite candidate : specialites){
            if(candidate.getId().equals(id)){
                return candidate;
            }
        }
        return null;
    }

    public static List<Specialite> findByName(String term){
        final  List<Specialite> results = new ArrayList<>();
        for (Specialite candidate : specialites){
            if(candidate.nom.equals(term)){
                results.add(candidate);
            }
        }
        return results;
    }

    public static boolean remove(Specialite specialite){
        return specialites.remove(specialite);
    }

    public static void save(Specialite specialite){
        specialites.add(specialite);
    }

    public static void update(Specialite specialite){
        Predicate<Specialite> specialitePredicate = p -> p.getId().equals(specialite.getId());
        specialites.removeIf(specialitePredicate);
        specialites.add(specialite);
    }
}
