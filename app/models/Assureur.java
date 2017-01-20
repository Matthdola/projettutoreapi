package models;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

public class Assureur extends Utilisateur {
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
}
