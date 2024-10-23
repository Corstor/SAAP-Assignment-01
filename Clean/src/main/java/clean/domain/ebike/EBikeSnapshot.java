package clean.domain.ebike;

import com.fasterxml.jackson.annotation.JsonProperty;

import clean.domain.P2d;
import clean.domain.Snapshot;
import clean.domain.V2d;

public record EBikeSnapshot(@JsonProperty("id") String id, @JsonProperty("state") EBikeState state,
        @JsonProperty("batteryLevel") int batteryLevel,
        @JsonProperty("speed") double speed, @JsonProperty("direction") V2d direction,
        @JsonProperty("location") P2d location) implements Snapshot {
}
