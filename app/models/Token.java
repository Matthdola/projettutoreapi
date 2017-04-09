package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mongodb.*;
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

@JsonIgnoreProperties({"type", "created_at", "updated_at", "deleted_at", "created_by", "updated_by", "deleted_by"})
public class Token extends Document {
    public static final String collectionName = "tokens";

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
    public boolean isError() {
        return false;
    }

    @Override
    public String getCollectionName() {
        return "tokens";
    }

    @Override
    public String getDocumentName() {
        return "token";
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


    public static QueryResult listByUserId(String uId) {
        ArrayList<Token> tokens = new ArrayList<>();

        BasicDBObject query = new BasicDBObject();
        query.put("user_id", uId);
        return Collection.findAll(collectionName, query, Token::fromBson);
    }

    public static QueryResult findByKey(String key){
        final  List<Token> results = new ArrayList<>();

        BasicDBObject query = new BasicDBObject();
        query.put("key", key);
        return Collection.findAll(collectionName, query, Token::fromBson);
    }


    public static Token findByOrigin(String origin) {

        BasicDBObject query = new BasicDBObject();
        query.put("origin", origin);
        QueryResult result = Collection.findAll(collectionName, query, Token::fromBson);
        if(!result.isError()){
            return (Token)result;
        }
        return null;
    }

    public static Token findByOriginAndUserId(String origin, String userId) {
        BasicDBObject query = new BasicDBObject();
        query.put("origin", origin);
        query.put("user_id", userId);
        QueryResult result = Collection.findAll(collectionName, query, Token::fromBson);
        if(!result.isError()){
            return (Token)result;
        }
        return null;
    }

    public static Token findByUserAgentAndUserId(String userAgent, String userId) {
        BasicDBObject query = new BasicDBObject();
        query.put("user_agent", userAgent);
        query.put("user_id", userId);
        QueryResult result = Collection.findAll(collectionName, query, Token::fromBson);
        if(!result.isError()){
            return (Token)result;
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
            QueryResult result = Collection.find(collectionName, query, Token::fromBson, "not found");
            if(!result.isError()){

                result = Collection.delete(collectionName, (Token) result);
                return true;

            }
            return false;
        } catch (MongoTimeoutException e) {
            return false;

        }
    }

    public static QueryResult findAll() {
        return Collection.findAll(collectionName, new BasicDBObject(), Token::fromBson);
    }

    public static QueryResult findById(String id){

        return Collection.findById(collectionName, id, Token::fromBson, "Token not found");

    }

    public static QueryResult findByName(String name){

        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        return Collection.findAll(collectionName, query, Token::fromBson);
    }

    public static QueryResult remove(Token token){
        return Collection.delete(collectionName, token);
    }

    public static QueryResult save(Token token){
        return Collection.insert(collectionName, token, e-> new Error(Error.DUPLICATE_KEY, "A Token with the same name already existe"));

    }

    public static QueryResult update(Token token){
        return Collection.update(collectionName, token, e-> new Error(Error.DUPLICATE_KEY, "Token with the same name already exist"));
    }
}
