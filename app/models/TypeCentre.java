package models;

import com.mongodb.DBObject;

/**
 * Created by matth on 28/09/2016.
 */
public class TypeCentre extends Document {
    private String nomTypeCentre;

    public TypeCentre(){

    }

    public TypeCentre(String nomTypeCentre){
        this.nomTypeCentre = nomTypeCentre;
    }

    public String getNomTypeCentre() {
        return nomTypeCentre;
    }

    public void setNomTypeCentre(String nomTypeCentre) {
        this.nomTypeCentre = nomTypeCentre;
    }

    @Override
    public DBObject toBson() {
        return null;
    }
}
