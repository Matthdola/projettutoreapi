package mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface QueryResult {

    @JsonIgnore
    public boolean isError();
}
