package clean.domain;

import java.io.IOException;
import java.util.List;

public interface Repository<X extends Snapshot> extends Listener<X> {
    List<X> loadAllValues() throws IOException;

    void saveValue(X value) throws IOException;
    void deleteValue(X value) throws IOException;
}

