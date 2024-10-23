package clean.domain.user;

import clean.domain.Snapshot;

public record UserSnapshot(String id, int credit) implements Snapshot {

}
