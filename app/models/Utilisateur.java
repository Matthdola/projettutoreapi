package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
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

public class Utilisateur extends Document {
    public static final String collectionName = "utilisateurs";

    public enum Type {
        PATIENT("patient"),
        ASSUREUR("assuseur"),
        MEDECIN("medecin");

        private String value;

        Type(String value){
            this.value = value;
        }


        @JsonValue
        public String getValue() {
            return value;
        }

        public static Type fromValue(String value){
            if(value != null){
                for (Type type : values()){
                    if(type.value.equals(value)){
                        return type;
                    }
                }
            }
            return PATIENT;
        }
    }

    @JsonIgnore
    public static final String EMAIL = "email";

    @JsonIgnore
    public static final String NAME = "name";

    @JsonIgnore
    public static final String PASSWORD = "password";

    @JsonIgnore
    public static final String PASSWORD_SALT = "salt";

    @JsonIgnore
    public static final String TYPE = "type";

    @JsonIgnore
    public static final String IS_ACTIVATED = "is_activated";

    @JsonIgnore
    public static final String NOM = "nom";

    @JsonIgnore
    public static final String PRENOM = "prenom";

    @JsonIgnore
    public static final String TELEPHONE = "telephone";

    @JsonIgnore
    public static final String CELLULAIRE = "cellulaire";

    @JsonIgnore
    public static final String IMAGE_URL = "image_url";

    @JsonIgnore
    public static final String PROFESSION = "profession";

    public byte[] image;

    @JsonProperty(NOM)
    protected String nom;

    @JsonProperty(PRENOM)
    protected String prenom;

    @JsonProperty(TELEPHONE)
    protected String telephone;

    @JsonProperty(CELLULAIRE)
    protected String cellulaire;

    @JsonProperty(IMAGE_URL)
    protected String imageUrl;

    @JsonProperty(PROFESSION)
    protected String profession;

    @JsonProperty(EMAIL)
    protected String email;

    @JsonProperty(NAME)
    protected String name;

    @JsonIgnore
    protected String password;

    @JsonIgnore
    protected String passwordSalt;

    @JsonProperty(IS_ACTIVATED)
    protected boolean isActivated;

    @JsonProperty(TYPE)
    protected Type type;


    public Utilisateur() {

    }

    public Utilisateur(String username, String email, String password, String passwordSalt) {
        this.name = username;
        this.email = email;
        this.password = password;
        this.passwordSalt = passwordSalt;
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public String getCollectionName() {
        return "utilisateurs";
    }

    @Override
    public String getDocumentName() {
        return "utilisateur";
    }

    @Override
    public DBObject toBson() {
        BasicDBObject object = new BasicDBObject();

        if (getId() != null && !getId().isEmpty()) {
            object.append(Document.ID, new ObjectId(getId()));
        }

        object.append(EMAIL, email)
                .append(NAME, name)
                .append(NOM, nom)
                .append(PRENOM, prenom)
                .append(PROFESSION, profession)
                .append(TELEPHONE, telephone)
                .append(CELLULAIRE, cellulaire)
                .append(TYPE, type.getValue())
                .append(IS_ACTIVATED, isActivated)
                .append(CREATED_AT, getCreatedAt() == null ? null : getCreatedAt().toString())
                .append(UPDATED_AT, getUpdatedAt() == null ? null : getUpdatedAt().toString())
                .append(DELETED_AT, getDeletedAt() == null ? null : getDeletedAt().toString())
                .append(CREATED_BY, getCreatedBy())
                .append(UPDATED_BY, getUpdatedBy())
                .append(DELETED_BY, getDeletedBy());

        return object;
    }

    public static Utilisateur fromBson(DBObject bson){
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(bson.get(Document.ID).toString());

        Object email = bson.get(Utilisateur.EMAIL);
        if (email != null) {
            utilisateur.setEmail(email.toString());
        }

        Object username = bson.get(Utilisateur.NAME);
        if (username != null) {
            utilisateur.setName(username.toString());
        }

        Object nom = bson.get(Utilisateur.NOM);
        if (nom != null) {
            utilisateur.setNom(nom.toString());
        }
        Object prenom = bson.get(Utilisateur.PRENOM);
        if (prenom != null) {
            utilisateur.setPrenom(prenom.toString());
        }
        Object profession = bson.get(Utilisateur.PROFESSION);
        if (profession != null) {
            utilisateur.setProfession(profession.toString());
        }
        Object telephone = bson.get(Utilisateur.TELEPHONE);
        if (telephone != null) {
            utilisateur.setTelephone(telephone.toString());
        }
        Object cellulaire = bson.get(Utilisateur.CELLULAIRE);
        if (cellulaire != null) {
            utilisateur.setCellulaire(cellulaire.toString());
        }
        Object type = bson.get(Utilisateur.TYPE);
        if (type != null) {
            utilisateur.setType(Type.fromValue(type.toString()));
        }

        Object createdAt = bson.get(Utilisateur.CREATED_AT);
        Object updateDate = bson.get(Utilisateur.UPDATED_AT);
        Object deleteAt = bson.get(Utilisateur.DELETED_AT);

        utilisateur.setCreatedAt(createdAt == null ? null : DateTime.parse(createdAt.toString()));
        utilisateur.setUpdatedAt(updateDate == null ? null : DateTime.parse(updateDate.toString()));
        utilisateur.setDeletedAt(deleteAt == null ? null : DateTime.parse(deleteAt.toString()));
        return utilisateur;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public Utilisateur(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String toString(){
        return String.format("%s - %s", name, name);
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCellulaire() {
        return cellulaire;
    }

    public void setCellulaire(String cellulaire) {
        this.cellulaire = cellulaire;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static QueryResult findByNameAndPassword(String name, String password){
        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        query.put("password", password);

        return Collection.find(collectionName, query, Utilisateur::fromBson, "User with the name " + name + " and " + password +" not found");
    }


    public static QueryResult findAllPatient() {
        BasicDBObject query = new BasicDBObject();
        query.put("type", "PATIENT" );
        return Collection.findAll(collectionName, query, Patient::fromBson);

    }

    public static QueryResult findAllMedecin() {
        BasicDBObject query = new BasicDBObject();
        query.put("type", "MEDECIN" );

        QueryResult result = Collection.findAll(collectionName, query, Medecin::fromBson);
        return result;
    }

    public static QueryResult findAllAssureur() {
        BasicDBObject query = new BasicDBObject();
        query.put("type", "ASSUREUR" );

        QueryResult result = Collection.findAll(collectionName, query, Medecin::fromBson);
        return result;
    }

    public static QueryResult findById(String id){

        return Collection.findById(collectionName, id, Utilisateur::fromBson, "User with the id " + id + "not found");
    }

    public static QueryResult findByName(String name){
        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        return Collection.find(collectionName, query, Utilisateur::fromBson, "User with the name " + name + "not found");
    }

    public static QueryResult findByEmail(String email){
        BasicDBObject query = new BasicDBObject();
        query.put("email", email );
        return Collection.find(collectionName, query, Utilisateur::fromBson, "User with the email " + email + "not found");
    }

    public static QueryResult findAll() {
        return Collection.findAll(collectionName, new BasicDBObject(), Utilisateur::fromBson);
    }


    public static QueryResult remove(Utilisateur utilisateur){
        return Collection.delete(collectionName, utilisateur);
    }

    public static QueryResult save(Utilisateur utilisateur){
        return Collection.insert(collectionName, utilisateur, e-> new Error(Error.DUPLICATE_KEY, "An user with the same name already existe"));

    }

    public static QueryResult update(Utilisateur utilisateur){
        return Collection.update(collectionName, utilisateur, e-> new Error(Error.DUPLICATE_KEY, "user with the same name already exist"));
    }
}
