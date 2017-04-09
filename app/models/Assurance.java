package models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import mongo.Document;
import org.bson.types.ObjectId;

public class Assurance extends Document {
    private String nomAssurance;
    private String addresse;
    private String telephone;

    public Assurance(){

    }

    public Assurance(String nomAss, String address, String telephone){
        this.nomAssurance = nomAss;
        this.addresse = address;
        this.telephone = telephone;
    }

    public String getNomAssurance() {
        return nomAssurance;
    }

    public void setNomAssurance(String nomAssurance) {
        this.nomAssurance = nomAssurance;
    }

    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public String getCollectionName() {
        return "assurances";
    }

    @Override
    public String getDocumentName() {
        return "assurance";
    }

    @Override
    public DBObject toBson() {
        BasicDBObject object = new BasicDBObject();

        if (getId() != null && !getId().isEmpty()) {
            object.append(Document.ID, new ObjectId(getId()));
        }

        object.append("nom", nomAssurance)
                .append("address", addresse)
                .append("telephone", telephone)
                .append(CREATED_AT, getCreatedAt() == null ? null : getCreatedAt().toString())
                .append(UPDATED_AT, getUpdatedAt() == null ? null : getUpdatedAt().toString())
                .append(DELETED_AT, getDeletedAt() == null ? null : getDeletedAt().toString())
                .append(CREATED_BY, getCreatedBy())
                .append(UPDATED_BY, getUpdatedBy())
                .append(DELETED_BY, getDeletedBy());

        return object;

    }
}
