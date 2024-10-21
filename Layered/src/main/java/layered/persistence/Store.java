package layered.persistence;

import java.util.Map;
import java.util.Optional;
import java.io.IOException;


public interface Store<X> {
    Map<String, X> loadAllValues() throws IOException;

    void saveValue(String id, X value) throws IOException;
    void deleteValue(String id) throws IOException;

    X getValueFromId(String id) throws IOException;
    Optional<X> getValueFromIdOptional(String id) throws IOException;
}
