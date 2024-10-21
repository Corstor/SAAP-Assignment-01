package clean.domain;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public interface Repository<X> {
    Map<String, X> loadAllValues() throws IOException;

    void saveValue(String id, X value) throws IOException;
    void deleteValue(String id) throws IOException;

    X getValueFromId(String id) throws IOException;
    Optional<X> getValueFromIdOptional(String id) throws IOException;
}

