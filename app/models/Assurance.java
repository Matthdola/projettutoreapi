package models;

import com.mongodb.DBObject;

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
    public DBObject toBson() {
        return null;
    }
}
