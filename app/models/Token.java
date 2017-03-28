package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mongodb.*;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import play.modules.mongodb.jackson.MongoDB;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"type", "created_at", "updated_at", "deleted_at", "created_by", "updated_by", "deleted_by"})
public class Token extends Document {
    public enum Type {
        AUTHENTICATION,
        PASSWORD_RESET,
        ACTIVATION,
        INVITATION
    }

    @JsonIgnore
    public static final int DEFAULT_KEY_BYTES_LENGTH = 128;

    @JsonIgnore
    public static final int DEFAULT_DURATION_HOURS = 24;

    @JsonIgnore
    public static final String USER_ID = "user_id";

    @JsonIgnore
    public static final String KEY = "key";

    @JsonIgnore
    public static final String REFERENCE_COUNT = "reference_count";

    @JsonIgnore
    public static final String TYPE = "type";

    @JsonIgnore
    public static final String USER_AGENT = "user_agent";

    @JsonIgnore
    public static final String ORIGIN = "origin";

    @JsonIgnore
    public static final String CREATED_AT = "created_at";

    @JsonIgnore
    public static final String EXPIRES_AT = "expires_at";

    @JsonIgnore
    public static final String LAST_USED_AT = "last_used_at";

    @JsonIgnore
    private String id;

    @JsonProperty(USER_ID)
    private String userId;

    @JsonProperty(KEY)
    private String key;

    @JsonProperty(TYPE)
    private Type type;

    @JsonIgnore
    private String userAgent;

    @JsonIgnore
    private String origin;

    @JsonIgnore
    private DateTime createdAt;

    @JsonProperty(EXPIRES_AT)
    @JsonSerialize(using = util.DateTimeSerializer.class)
    @JsonDeserialize(using = util.DateTimeDeserializer.class)
    private DateTime expiresAt;

    @JsonIgnore
    private DateTime lastUsedAt;

    public static JacksonDBCollection<Token, String> collection = MongoDB.getCollection("tokens", Token.class, String.class);

    public Token() {
    }

    public Token(String userId, String key, DateTime createdAt, DateTime expiresAt) {
        this.userId = userId;
        this.key = key;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    @Override
    public DBObject toBson() {
        return new BasicDBObject(USER_ID, userId)
                .append(KEY, key)
                .append(TYPE, type.toString())
                .append(USER_AGENT, userAgent)
                .append(ORIGIN, origin)
                .append(CREATED_AT, createdAt == null ? null : createdAt.toString())
                .append(EXPIRES_AT, expiresAt == null ? null : expiresAt.toString())
                .append(LAST_USED_AT, lastUsedAt == null ? null : lastUsedAt.toString());
    }

    public static Token fromBson(DBObject bson) {
        Token token = new Token();

        token.setId(bson.get(Document.ID).toString());
        token.setUserId(bson.get(Token.USER_ID).toString());
        token.setKey(bson.get(Token.KEY).toString());
        token.setType(Token.Type.valueOf(bson.get(Token.TYPE).toString()));

        Object userAgent = bson.get(Token.USER_AGENT);
        Object origin = bson.get(Token.ORIGIN);
//        Object referenceCount = bson.get(Token.REFERENCE_COUNT);

        if (userAgent != null) {
            token.setUserAgent(userAgent.toString());
        }

        if (origin != null) {
            token.setOrigin(origin.toString());
        }

//        if(referenceCount != null){
//            token.setReferenceCount(Long.parseLong(referenceCount.toString()));
//        } else {
//            token.setReferenceCount(0);
//        }

        Object createdAt = bson.get(Token.CREATED_AT);
        Object expiresAt = bson.get(Token.EXPIRES_AT);
        Object lastUsedAt = bson.get(Token.LAST_USED_AT);

        token.setCreatedAt(createdAt == null ? null : DateTime.parse(createdAt.toString()));
        token.setExpiresAt(expiresAt == null ? null : DateTime.parse(expiresAt.toString()));
        token.setLastUsedAt(lastUsedAt == null ? null : DateTime.parse(lastUsedAt.toString()));

        return token;
    }

    @JsonIgnore
    public Boolean isExpired() {
        return expiresAt.isBeforeNow();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(DateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public DateTime getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(DateTime lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }


    public static List<Token> findAll() {
        return Token.collection.find().toArray();
    }

    public static List<Token> listByUserId(String uId) {
        ArrayList<Token> tokens = new ArrayList<>();

        BasicDBObject query = new BasicDBObject();
        query.put("user_id", uId);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            Token token = (Token)cursor.next();
            if(token.userId.equals(uId)){
                tokens.add(token);
            }
        }

        return tokens;
    }


    public static Token findById(String id){
        Token token = Token.collection.findOneById(id);
        return token;
    }

    public static List<Token> findByKey(String key){
        final  List<Token> results = new ArrayList<>();

        BasicDBObject query = new BasicDBObject();
        query.put("key", key);
        DBCursor cursor = collection.find(query);
        while(cursor.hasNext()) {
            results.add((Token) cursor.next());
        }
        return results;
    }

    public static boolean remove(Token token){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(token.getId()) );
        try {
            Token.collection.remove(query);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static void save(Token token){
        Token.collection.save(token);
    }

    public static void update(Token token){
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new org.bson.types.ObjectId(token.getId()) );
        collection.update(query, token.toBson());
    }

    public static Token findByOrigin(String origin) {

        BasicDBObject query = new BasicDBObject();
        query.put("origin", origin);
        DBCursor cursor = collection.find(query);
        try {
            while(cursor.hasNext()) {
                Token token = (Token)cursor.next();
                return token;
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }

    public static Token findByOriginAndUserId(String origin, String userId) {
        BasicDBObject query = new BasicDBObject();
        query.put("origin", origin);
        query.put("user_id", userId);
        DBCursor cursor = collection.find(query);
        try {
            while(cursor.hasNext()) {
                Token token = (Token)cursor.next();
                return token;
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }

    public static Token findByUserAgentAndUserId(String userAgent, String userId) {
        BasicDBObject query = new BasicDBObject();
        query.put("user_agent", userAgent);
        query.put("user_id", userId);
        DBCursor cursor = collection.find(query);
        try {
            while(cursor.hasNext()) {
                Token token = (Token)cursor.next();
                return token;
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }


    public static boolean removeMatchingKey(String key) {
        BasicDBObject query = new BasicDBObject(Token.KEY, key);

        return remove(query);
    }

    public static boolean removeMatchingUserId(String userId) {
        if (!ObjectId.isValid(userId)) {
            return false;
        }

        BasicDBObject query = new BasicDBObject(Token.USER_ID, userId);

        return remove(query);
    }

    private static boolean remove(DBObject query) {

        try  {
            DBCursor cursor = collection.find(query);
            if (!cursor.hasNext()) {
                return true;
            }

            Token document = (Token) cursor.next();

            try {
                collection.remove(document, WriteConcern.ACKNOWLEDGED);
                return true;

            } catch (MongoException e) {
                return false;
            }

        } catch (MongoTimeoutException e) {
            return false;

        }
    }
}
