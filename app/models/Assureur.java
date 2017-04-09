package models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import mongo.Collection;
import mongo.Document;
import mongo.QueryResult;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonProperty;
import play.modules.mongodb.jackson.MongoDB;

import java.util.ArrayList;
import java.util.List;

public class Assureur extends Utilisateur {

    @JsonProperty("code_assureur")
    private String codeAsseur;

    public Assureur(){

    }

    public String typeUtilisateur(){
        return "assureur";
    }

    public String getCodeAsseur() {
        return codeAsseur;
    }

    public void setCodeAsseur(String codeAsseur) {
        this.codeAsseur = codeAsseur;
    }

    @Override
    public DBObject toBson() {
        BasicDBObject object = new BasicDBObject();

        if (getId() != null && !getId().isEmpty()) {
            object.append(Document.ID, new ObjectId(getId()));
        }

        object.append("nom", nom)
                .append("prenom", prenom)
                .append("email", email)
                .append("telephone", telephone)
                .append("profession", profession)
                .append("code_assureur", codeAsseur)
                .append(CREATED_AT, getCreatedAt() == null ? null : getCreatedAt().toString())
                .append(UPDATED_AT, getUpdatedAt() == null ? null : getUpdatedAt().toString())
                .append(DELETED_AT, getDeletedAt() == null ? null : getDeletedAt().toString())
                .append(CREATED_BY, getCreatedBy())
                .append(UPDATED_BY, getUpdatedBy())
                .append(DELETED_BY, getDeletedBy());

        return object;
    }


    public static Assureur fromBson(DBObject bson){
        Assureur assureur = (Assureur) Utilisateur.fromBson(bson);

        return assureur;
    }



    public static QueryResult findAll() {
        ArrayList<Utilisateur> assurs = new ArrayList<>();
        BasicDBObject query = new BasicDBObject();
        query.put("type", "ASSUREUR");

        return Collection.find(collectionName, query, Assureur::fromBson, "");
    }

}
