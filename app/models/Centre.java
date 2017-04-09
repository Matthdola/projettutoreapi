package models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import mongo.Collection;
import mongo.Document;
import mongo.Error;
import mongo.QueryResult;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import play.modules.mongodb.jackson.MongoDB;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Centre extends Document {
    public static final String collectionName = "centres";

    private String nom;
    private String pays;
    private String ville;
    private String type;
    private String telephone;
    private String email;
    private String cellulaire;
    private String logoUrl;
    private ArrayList<Integer> specialites;

    public Centre(){
        specialites = new ArrayList<>();
    }

    public Centre(String nom){
        this.nom = nom;
    }

    public Centre(String nom, String telephone){
        this.nom = nom;
        this.telephone = telephone;
        specialites = new ArrayList<>();
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
    public boolean isError() {
        return false;
    }

    @Override
    public String getCollectionName() {
        return "centres";
    }

    @Override
    public String getDocumentName() {
        return "centre";
    }

    @Override
    public DBObject toBson() {
        BasicDBObject object = new BasicDBObject();

        if (getId() != null && !getId().isEmpty()) {
            object.append(Document.ID, new ObjectId(getId()));
        }

        object.append("nom", nom)
                .append("ville", ville)
                .append("pays", pays)
                .append("telephone", telephone)
                .append("email", email)
                .append("cellulaire", cellulaire)
                .append("specialites", specialites)
                .append(CREATED_AT, getCreatedAt() == null ? null : getCreatedAt().toString())
                .append(UPDATED_AT, getUpdatedAt() == null ? null : getUpdatedAt().toString())
                .append(DELETED_AT, getDeletedAt() == null ? null : getDeletedAt().toString())
                .append(CREATED_BY, getCreatedBy())
                .append(UPDATED_BY, getUpdatedBy())
                .append(DELETED_BY, getDeletedBy());

        return object;

    }

    public static Centre fromBson(DBObject bson){
        Centre centre = new Centre();
        centre.setId(bson.get(Document.ID).toString());

        Object nom = bson.get("nom");
        if (nom != null) {
            centre.setNom(nom.toString());
        }

        Object ville = bson.get("ville");
        if (ville != null) {
            centre.setVille(ville.toString());
        }

        Object pays = bson.get("pays");
        if (pays != null) {
            centre.setPays(pays.toString());
        }
        Object telephone = bson.get("telephone");
        if (telephone != null) {
            centre.setTelephone(telephone.toString());
        }
        Object email = bson.get("email");
        if (email != null) {
            centre.setEmail(email.toString());
        }

        Object cellulaire = bson.get(Utilisateur.CELLULAIRE);
        if (cellulaire != null) {
            centre.setCellulaire(cellulaire.toString());
        }
        Object specialites = bson.get("specialites");
        if (specialites != null) {
            //centre.setSpecialites();
        }

        Object createdAt = bson.get(Document.CREATED_AT);
        Object updateDate = bson.get(Document.UPDATED_AT);
        Object deleteAt = bson.get(Document.DELETED_AT);

        centre.setCreatedAt(createdAt == null ? null : DateTime.parse(createdAt.toString()));
        centre.setUpdatedAt(updateDate == null ? null : DateTime.parse(updateDate.toString()));
        centre.setDeletedAt(deleteAt == null ? null : DateTime.parse(deleteAt.toString()));
        return centre;
    }


    public static QueryResult listByVille(String ville) {
        BasicDBObject query = new BasicDBObject();
        query.put("ville", ville);
        return Collection.findAll(collectionName, query, Centre::fromBson);
    }

    public static QueryResult listByType(String type) {
        BasicDBObject query = new BasicDBObject();
        query.put("type", type);
        return Collection.findAll(collectionName, query, Centre::fromBson);
    }

    public static QueryResult findAll() {
        return Collection.findAll(collectionName, new BasicDBObject(), Centre::fromBson);
    }

    public static QueryResult findById(String id){

        return Collection.findById(collectionName, id, Centre::fromBson, "centre not found");

    }

    public static QueryResult findByName(String name){

        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        return Collection.findAll(collectionName, query, Centre::fromBson);
    }

    public static QueryResult remove(Centre centre){
        return Collection.delete(collectionName, centre);
    }

    public static QueryResult save(Centre centre){
        return Collection.insert(collectionName, centre, e-> new Error(Error.DUPLICATE_KEY, "A centre with the same name already existe"));

    }

    public static QueryResult update(Centre centre){
        return Collection.update(collectionName, centre, e-> new Error(Error.DUPLICATE_KEY, "centre with the same name already exist"));
    }
}
