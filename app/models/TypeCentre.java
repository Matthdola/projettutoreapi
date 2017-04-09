package models;

import com.mongodb.DBObject;
import mongo.Document;


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
    public boolean isError() {
        return false;
    }

    @Override
    public String getCollectionName() {
        return "type_centres";
    }

    @Override
    public String getDocumentName() {
        return "type_centre";
    }

    @Override
    public DBObject toBson() {
        return null;
    }
}
