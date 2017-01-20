package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.bson.types.ObjectId;
import play.data.validation.Constraints;
import play.modules.mongodb.jackson.MongoDB;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class User extends Document {
    public enum Type {
        PATIENT,
        ASSUREUR,
        MEDECIN
    }

    public byte[] image;

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

    @JsonProperty(EMAIL)
    private String email;

    @JsonProperty(NAME)
    private String name;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String passwordSalt;

    @JsonProperty(IS_ACTIVATED)
    private boolean isActivated;

    @JsonProperty(TYPE)
    private Type type;

    public static JacksonDBCollection<User, String> collection = MongoDB.getCollection("users", User.class, String.class);


    public User() {

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
                .append(PASSWORD, password)
                .append(PASSWORD_SALT, passwordSalt)
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

    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String toString(){
        return String.format("%s - %s", name, name);
    }


    public static User findByNameAndPassword(String name, String password){
        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        query.put("password", password);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            return (User) cursor.next();
        }
        return null;
    }

    public static List<User> findAll() {
        return User.collection.find().toArray();
    }

    public static User findById(String id){
        User user = User.collection.findOneById(id);
        return user;
    }

    public static User findByName(String name){
        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            return (User) cursor.next();
        }
        return null;
    }

    public static User findByEmail(String email){
        BasicDBObject query = new BasicDBObject();
        query.put("email", email);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            return (User) cursor.next();
        }
        return null;
    }

    public static boolean remove(User user){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(user.getId()) );
        try {
            User.collection.remove(query);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static void save(User user){
        User.collection.insert(user);
    }

    public static void update(User user){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(user.getId()) );
        collection.update(query, user.toBson());
    }
}
