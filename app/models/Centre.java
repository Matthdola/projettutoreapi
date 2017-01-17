package models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import play.modules.mongodb.jackson.MongoDB;

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

    public static JacksonDBCollection<Centre, String> collection = MongoDB.getCollection("centres", Centre.class, String.class);

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
        return Centre.collection.find().toArray();
    }

    public static List<Centre> listByVille(String ville) {
        ArrayList<Centre> centres = new ArrayList<>();

        BasicDBObject query = new BasicDBObject();
        query.put("ville", ville);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            Centre centre = (Centre)cursor.next();
            if(centre.ville.equals(ville)){
                centres.add(centre);
            }
        }

        return centres;
    }

    public static List<Centre> listByType(String type) {
        ArrayList<Centre> centres = new ArrayList<>();
        BasicDBObject query = new BasicDBObject();
        query.put("type", type);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            Centre centre = (Centre)cursor.next();
            if(centre.type.equals(type)){
                centres.add(centre);
            }
        }

        return centres;
    }

    public static Centre findById(String id){
        Centre centre = Centre.collection.findOneById(id);
        return centre;
    }

    public static List<Centre> findByName(String name){
        final  List<Centre> results = new ArrayList<>();

        BasicDBObject query = new BasicDBObject();
        query.put("nom", name);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            results.add((Centre) cursor.next());
        }
        return results;
    }

    public static boolean remove(Centre centre){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(centre.getId()) );
        try {
            Centre.collection.remove(query);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static void save(Centre centre){
        Centre.collection.save(centre);
    }

    public static void update(Centre centre){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(centre.getId()) );
        collection.update(query, centre.toBson());
    }
}
