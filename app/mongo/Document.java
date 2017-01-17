package mongo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mongodb.DBObject;
import org.joda.time.DateTime;

public abstract class Document implements QueryResult {
    @JsonIgnore
    public static final String ID = "_id";

    @JsonIgnore
    public static final String CREATED_AT = "created_at";

    @JsonIgnore
    public static final String UPDATED_AT = "updated_at";

    @JsonIgnore
    public static final String DELETED_AT = "deleted_at";

    @JsonIgnore
    public static final String CREATED_BY = "created_by";

    @JsonIgnore
    public static final String UPDATED_BY = "updated_by";

    @JsonIgnore
    public static final String DELETED_BY = "deleted_by";

    @JsonProperty("id")
    private String id;

    @JsonProperty(CREATED_AT)
    @JsonSerialize(using = util.DateTimeSerializer.class)
    @JsonDeserialize(using = util.DateTimeDeserializer.class)
    private DateTime createdAt;

    @JsonProperty(UPDATED_AT)
    @JsonSerialize(using = util.DateTimeSerializer.class)
    @JsonDeserialize(using = util.DateTimeDeserializer.class)
    private DateTime updatedAt;

    @JsonProperty(DELETED_AT)
    @JsonSerialize(using = util.DateTimeSerializer.class)
    @JsonDeserialize(using = util.DateTimeDeserializer.class)
    private DateTime deletedAt;

    @JsonProperty(CREATED_BY)
    private String createdBy;

    @JsonProperty(UPDATED_BY)
    private String updatedBy;

    @JsonProperty(DELETED_BY)
    private String deletedBy;

    @JsonIgnore
    @JsonProperty
    public boolean isError() {
        return false;
    }

    @JsonIgnore
    public abstract String getCollectionName();

    @JsonIgnore
    public abstract String getDocumentName();

    public abstract DBObject toBson();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(DateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public DateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(DateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }
}
