package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

public class Specialite extends Document {
    @JsonIgnore
    public static final String collectionName = "specialites";


    private String id;
    private String name;
    private String domaine;


    public Specialite(){

    }

    public Specialite(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomaine() {
        return domaine;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }


    @Override
    public String getCollectionName() {
        return "specialites";
    }

    @Override
    public String getDocumentName() {
        return "specialite";
    }

    @Override
    public DBObject toBson() {
        BasicDBObject object = new BasicDBObject();

        if (getId() != null && !getId().isEmpty()) {
            object.append(Document.ID, new ObjectId(getId()));
        }

        object.append("name", name)
                .append("domaine", domaine)
                .append(CREATED_AT, getCreatedAt() == null ? null : getCreatedAt().toString())
                .append(UPDATED_AT, getUpdatedAt() == null ? null : getUpdatedAt().toString())
                .append(DELETED_AT, getDeletedAt() == null ? null : getDeletedAt().toString())
                .append(CREATED_BY, getCreatedBy())
                .append(UPDATED_BY, getUpdatedBy())
                .append(DELETED_BY, getDeletedBy());

        return object;

    }

    public static Specialite fromBson(DBObject bson) {
        Specialite specialite = new Specialite();

        specialite.setId(bson.get(Document.ID).toString());

        specialite.setName(bson.get("name").toString());

        Object domaine = bson.get("domaine");
        if (domaine != null) {
            specialite.setDomaine(domaine.toString());
        }


        Object createdAt = bson.get(Document.CREATED_AT);
        Object updatedAt = bson.get(Document.UPDATED_AT);
        Object deletedAt = bson.get(Document.DELETED_AT);

        specialite.setCreatedAt(createdAt == null ? null : DateTime.parse(createdAt.toString()));
        specialite.setUpdatedAt(updatedAt == null ? null : DateTime.parse(updatedAt.toString()));
        specialite.setDeletedAt(deletedAt == null ? null : DateTime.parse(deletedAt.toString()));

        Object createdBy = bson.get(Document.CREATED_BY);
        Object updatedBy = bson.get(Document.UPDATED_BY);
        Object deletedBy = bson.get(Document.DELETED_BY);

        if (createdBy != null) {
            specialite.setCreatedBy(createdBy.toString());
        }

        if (updatedBy != null) {
            specialite.setUpdatedBy(updatedBy.toString());
        }

        if (deletedBy != null) {
            specialite.setDeletedBy(deletedBy.toString());
        }

        return specialite;
    }


    public static QueryResult findAll() {
        QueryResult result = Collection.findAll("specialites", new BasicDBObject(), Specialite::fromBson);
        return result;
    }


    public static QueryResult findById(String id){

        return Collection.findById(collectionName, id, Specialite::fromBson, "Specialite not found");

    }

    public static QueryResult findByName(String name){

        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        return Collection.findAll(collectionName, query, Specialite::fromBson);
    }

    public static QueryResult remove(Specialite specialite){
        return Collection.delete(collectionName, specialite);
    }

    public static QueryResult save(Specialite specialite){
        return Collection.insert(collectionName, specialite, e-> new Error(Error.DUPLICATE_KEY, "A specialite with the same name already existe"));

    }

    public static QueryResult update(Specialite specialite){
        return Collection.update(collectionName, specialite, e-> new Error(Error.DUPLICATE_KEY, "specialite with the same name already exist"));
    }
}
