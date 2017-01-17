package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.joda.time.DateTime;

public class Token extends Document {
    public enum Type {
        AUTHENTICATION,
        PASSWORD_RESET,
        ACTIVATION,
        INVITATION,
        CHECKOUT
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

}
