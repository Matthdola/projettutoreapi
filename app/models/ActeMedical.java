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
import org.codehaus.jackson.annotate.JsonProperty;
import play.modules.mongodb.jackson.MongoDB;

import java.util.ArrayList;
import java.util.List;

public class ActeMedical extends Document {

    public static final String collectionName = "actes_medicales";
    @JsonProperty("nom_actes")
    private String nomActes;

    @JsonProperty("montant_assure")
    private double montantAssure;

    @JsonProperty("montant_non_assure")
    private double montantNonAssure;

    @JsonProperty("montant_expatrie")
    private double montantEspatrie;


    public ActeMedical(){

    }

    public String getNomActes() {
        return nomActes;
    }

    public void setNomActes(String nomActes) {
        this.nomActes = nomActes;
    }

    public double getMontantAssure() {
        return montantAssure;
    }

    public void setMontantAssure(double montantAssure) {
        this.montantAssure = montantAssure;
    }

    public double getMontantNonAssure() {
        return montantNonAssure;
    }

    public void setMontantNonAssure(double montantNonAssure) {
        this.montantNonAssure = montantNonAssure;
    }

    public double getMontantEspatrie() {
        return montantEspatrie;
    }

    public void setMontantEspatrie(double montantEspatrie) {
        this.montantEspatrie = montantEspatrie;
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public String getCollectionName() {
        return "actesmedicals";
    }

    @Override
    public String getDocumentName() {
        return "actemedical";
    }

    @Override
    public DBObject toBson() {
        BasicDBObject object = new BasicDBObject();

        if (getId() != null && !getId().isEmpty()) {
            object.append(Document.ID, new ObjectId(getId()));
        }

        object.append("nom", nomActes)
                .append("montant_assure", montantAssure)
                .append("montant_non_assure", montantNonAssure)
                .append("montant_espatrie", montantEspatrie)
                .append(CREATED_AT, getCreatedAt() == null ? null : getCreatedAt().toString())
                .append(UPDATED_AT, getUpdatedAt() == null ? null : getUpdatedAt().toString())
                .append(DELETED_AT, getDeletedAt() == null ? null : getDeletedAt().toString())
                .append(CREATED_BY, getCreatedBy())
                .append(UPDATED_BY, getUpdatedBy())
                .append(DELETED_BY, getDeletedBy());

        return object;

    }

    public static ActeMedical fromBson(DBObject bson){
        ActeMedical acteMedical = new ActeMedical();

        return acteMedical;
    }

    public static QueryResult findAll() {
        return Collection.findAll(collectionName, new BasicDBObject(), ActeMedical::fromBson);
    }


    public static QueryResult findById(String id){

        return Collection.findById(collectionName, id, ActeMedical::fromBson, "Acte not found");

    }

    public static QueryResult findByName(String name){
        final  List<ActeMedical> results = new ArrayList<>();

        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        return Collection.findAll(collectionName, query, ActeMedical::fromBson);
    }

    public static QueryResult remove(ActeMedical acteMedical){
        return Collection.delete(collectionName, acteMedical);
    }

    public static QueryResult save(ActeMedical acteMedical){
        return Collection.insert(collectionName, acteMedical, e-> new Error(Error.DUPLICATE_KEY, "An actes with the same name already existe"));

    }

    public static QueryResult update(ActeMedical acteMedical){
        return Collection.update(collectionName, acteMedical, e-> new Error(Error.DUPLICATE_KEY, "acte with the same name already exist"));
    }
}
