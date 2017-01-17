package models;


import com.mongodb.DBObject;

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
        return null;
    }
}
