package models;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.bson.types.ObjectId;
import play.modules.mongodb.jackson.MongoDB;

import java.util.ArrayList;
import java.util.List;

public class ActeMedical extends Document {
    private String nomActes;
    private double montantAssure;
    private double montantNonAssure;
    private double montantEspatrie;

    public static JacksonDBCollection<ActeMedical, String> collection = MongoDB.getCollection("actesmedicals", ActeMedical.class, String.class);


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

    public static List<ActeMedical> findAll() {
        return ActeMedical.collection.find().toArray();
    }


    public static ActeMedical findById(String id){
        ActeMedical acteMedical = ActeMedical.collection.findOneById(id);
        return acteMedical;
    }

    public static List<ActeMedical> findByName(String name){
        final  List<ActeMedical> results = new ArrayList<>();

        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            results.add((ActeMedical) cursor.next());
        }
        return results;
    }

    public static boolean remove(ActeMedical acteMedical){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(acteMedical.getId()) );
        try {
            ActeMedical.collection.remove(query);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static void save(ActeMedical acteMedical){
        ActeMedical.collection.save(acteMedical);
    }

    public static void update(ActeMedical acteMedical){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(acteMedical.getId()) );
        collection.update(query, acteMedical.toBson());
    }
}
