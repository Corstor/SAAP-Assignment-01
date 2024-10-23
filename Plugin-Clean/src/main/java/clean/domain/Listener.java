package clean.domain;

public interface Listener<X extends Snapshot> {
    void eventOccured(X value);
}
