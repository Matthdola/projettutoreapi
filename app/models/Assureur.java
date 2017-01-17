package models;


import com.mongodb.DBObject;

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
        return null;
    }
}
