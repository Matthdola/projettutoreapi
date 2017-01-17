package mongo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Error implements QueryResult {
    @JsonIgnore
    public static final String CODE = "code";

    @JsonIgnore
    public static final String MESSAGE = "message";

    @JsonIgnore
    public static final int DUPLICATE_KEY = 82;

    @JsonIgnore
    public static final int NOT_FOUND = 64;

    @JsonIgnore
    public static final int UNEXPECTED = 128;

    @JsonIgnore
    public static final int SESSION_ACTIVE = 57;

    @JsonIgnore
    public static final int TOKEN_EXPIRED = 87;

    @JsonIgnore
    public static final int INVALID_TOKEN = 104;

    @JsonIgnore
    public static final int PERMISSION_DENIED = 54;

    @JsonIgnore
    public static final int MISSING_AUTH_TOKEN = 41;

    @JsonIgnore
    public static final int INVALID_CREDENTIALS = 42;

    @JsonIgnore
    public static final int MISSING_PARAMETER = 58;

    @JsonIgnore
    public static final int EMPTY_PARAMETER = 98;

    @JsonIgnore
    public static final int FILE_DOES_NOT_EXIST = 75;

    @JsonIgnore
    public static final int INVALID_GEOMETRY = 33;

    @JsonIgnore
    public static final int INVALID_EMAIL = 38;

    @JsonIgnore
    public static final int NO_FEATURE_SCHEMA_DEFINED = 96;

    @JsonIgnore
    public static final int WORKING_PROJECT_HEADER_MISSING = 21;

    @JsonIgnore
    public static final int FORM_PROCESSING_ERROR = 23;

    @JsonIgnore
    public static final int FEATURE_SCHEMA_PROCESSING_ERROR = 27;

    @JsonIgnore
    public static final int INVALID_FEATURE = 47;

    @JsonIgnore
    public static final int TEMPLATE_PROCESSING_ERROR = 31;

    @JsonIgnore
    public static final int INVALID_CONTENT_TYPE = 78;

    @JsonIgnore
    public static final int INVALID_USERNAME = 60;

    @JsonIgnore
    public static final int DATABASE_ACCESS_TIMEOUT = 63;

    @JsonIgnore
    public static final int MONGO_ERROR = 67;

    @JsonIgnore
    public static final int ALREADY_ACTIVATED = 45;

    @JsonIgnore
    public static final int INVALID_PERMISSION = 49;

    @JsonProperty(CODE)
    private int code;

    @JsonProperty(MESSAGE)
    private String message;

    public Error() {
    }

    public Error(int code) {
        this.code = code;
    }

    public Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean isError() {
        return true;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
