package models;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

public class ActeMedical extends Document {
    private String nomActes;
    private double montantAssure;
    private double montantNonAssure;
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
}
