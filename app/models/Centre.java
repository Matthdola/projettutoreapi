package models;

import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Centre extends Document {
    private String nom;
    private String pays;
    private String ville;
    private String type;
    private String telephone;
    private String email;
    private String cellulaire;
    private String logoUrl;
    private ArrayList<Integer> specialites;


    public static List<Centre> centres;

    static {
        centres = new ArrayList<Centre>();
        centres.add(new Centre());
        centres.add(new Centre());
        centres.add(new Centre());
        centres.add(new Centre());
        centres.add(new Centre());
    }

    public Centre(){

    }

    public Centre(String nom){
        this.nom = nom;
    }

    public Centre(String nom, String telephone){
        this.nom = nom;
        this.telephone = telephone;
    }

    public Centre fromBson(Object bson){
        return null;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellulaire() {
        return cellulaire;
    }

    public void setCellulaire(String cellulaire) {
        this.cellulaire = cellulaire;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public ArrayList<Integer> getSpecialites() {
        return specialites;
    }

    public void setSpecialites(ArrayList<Integer> specialites) {
        this.specialites = specialites;
    }

    public void addSpecialite(int numeroSpecialite){
        this.specialites.add(numeroSpecialite);
    }

    @Override
    public DBObject toBson() {
        return null;
    }

    public static List<Centre> findAll() {
        return new ArrayList<>(centres);
    }

    public static Centre findById(String id){
        for (Centre candidate : centres){
            if(candidate.getId().equals(id)){
                return candidate;
            }
        }
        return null;
    }

    public static List<Centre> findByName(String term){
        final  List<Centre> results = new ArrayList<>();
        for (Centre candidate : centres){
            if(candidate.getNom().equals(term)){
                results.add(candidate);
            }
        }
        return results;
    }

    public static boolean remove(Centre centre){
        return centres.remove(centre);
    }

    public static void save(Centre centre){
        centres.add(centre);
    }

    public static void update(Centre centre){
        Predicate<Centre> centrePredicate = p -> p.getId().equals(centre.getId());
        centres.removeIf(centrePredicate);
        centres.add(centre);
    }
}
