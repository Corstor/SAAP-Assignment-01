package clean.infrastructure.storage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import clean.domain.Repository;

public abstract class AbstractStore<X> implements Repository<X> {
    protected final File jsonFile;
    protected Map<String, X> values;
    protected final ObjectMapper objectMapper;

    public AbstractStore(String name) throws IOException {
        this.jsonFile = new File(name);
        this.objectMapper = new ObjectMapper();
        this.values = new HashMap<>();

        //Create a new file if it does not exists
        if (jsonFile.createNewFile()) {
            this.saveAllValues();
        } 
    }

    public abstract Map<String, X> loadAllValues() throws IOException;

    protected void saveAllValues() throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, values);
    }

    public void saveValue(String id, X value) throws IOException {
        this.values = loadAllValues();
        this.values.put(id, value);
        this.saveAllValues();
    }

    public void deleteValue(String id) throws IOException {
        this.values = loadAllValues().entrySet().stream()
            .filter(
                value -> !value.getKey().equals(id)
            ).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        this.saveAllValues();
    }
}
