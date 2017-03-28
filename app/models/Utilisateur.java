package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.bson.types.ObjectId;
import play.modules.mongodb.jackson.MongoDB;

import java.util.ArrayList;
import java.util.List;

public class Utilisateur extends Document {
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

    public static JacksonDBCollection<Utilisateur, String> collection = MongoDB.getCollection("utilisateurs", Utilisateur.class, String.class);


    public Utilisateur() {

    }

    public Utilisateur(String username, String email, String password, String passwordSalt) {
        this.name = username;
        this.email = email;
        this.password = password;
        this.passwordSalt = passwordSalt;
    }

    @Override
    public DBObject toBson() {
        BasicDBObject object = new BasicDBObject();

        if (getId() != null && !getId().isEmpty()) {
            object.append(Document.ID, new ObjectId(getId()));
        }

        object.append(EMAIL, email)
                .append(NAME, name)
                .append(TYPE, type.toString())
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

    public static Utilisateur findByNameAndPassword(String name, String password){
        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        query.put("password", password);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            return (Utilisateur) cursor.next();
        }
        return null;
    }

    public static List<Utilisateur> findAll() {
        return Utilisateur.collection.find().toArray();
    }

    public static List<Utilisateur> findAllPatient() {
        ArrayList<Utilisateur> patients = new ArrayList<>();
        BasicDBObject query = new BasicDBObject();
        query.put("type", "PATIENT" );
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            Utilisateur patient = (Utilisateur) cursor.next();
            patients.add(patient);

        }

        return patients;
    }

    public static List<Utilisateur> findAllMedecin() {
        ArrayList<Utilisateur> medecins = new ArrayList<>();
        BasicDBObject query = new BasicDBObject();
        query.put("type", "medecin" );
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            Utilisateur medecin = (Utilisateur) cursor.next();
            medecins.add(medecin);

        }

        return medecins;
    }

    public static List<Assureur> findAllAssureur() {
        ArrayList<Assureur> assureurs = new ArrayList<>();
        BasicDBObject query = new BasicDBObject();
        query.put("type", "assureur" );
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            Assureur assureur = (Assureur) cursor.next();
            assureurs.add(assureur);

        }

        return assureurs;
    }

    public static Utilisateur findById(String id){
        Utilisateur user = Utilisateur.collection.findOneById(id);
        return user;
    }

    public static Utilisateur findByName(String name){
        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            return (Utilisateur) cursor.next();
        }
        return null;
    }

    public static Utilisateur findByEmail(String email){
        BasicDBObject query = new BasicDBObject();
        query.put("email", email);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            return (Utilisateur) cursor.next();
        }
        return null;
    }

    public static boolean remove(Utilisateur user){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(user.getId()) );
        try {
            Utilisateur.collection.remove(query);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static void save(Utilisateur user){
        Utilisateur.collection.insert(user);
    }

    public static void update(Utilisateur user){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(user.getId()) );
        collection.update(query, user.toBson());
    }
}
