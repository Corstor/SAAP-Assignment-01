package clean.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import clean.domain.Snapshot;

public record UserSnapshot(@JsonProperty("id") String id, @JsonProperty("credit") int credit) implements Snapshot {

}
