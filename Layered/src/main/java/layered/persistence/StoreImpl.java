package layered.persistence;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

import java.io.File;
import java.io.IOException;

public class StoreImpl<X> implements Store<X> {
    private final File jsonFile;
    private Map<String, X> values;
    private final ObjectMapper objectMapper;
    private final MapType type;

    public StoreImpl(String fileName, Class<X> classRef) throws IOException {
        this.jsonFile = new File(fileName + ".json");
        this.objectMapper = new ObjectMapper();
        this.type = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, classRef);
        this.values = new HashMap<>();

        //Create a new file if it does not exists
        if (jsonFile.createNewFile()) {
            this.saveAllValues(values);
        } 
    }

    @Override
    public Map<String, X> loadAllValues() throws IOException {
        return objectMapper.readValue(jsonFile, type);
    }

    private void saveAllValues(Map<String, X> values) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, values);
    }

    @Override
    public void saveValue(String id, X value) throws IOException {
        this.values = loadAllValues();
        this.values.put(id, value);
        this.saveAllValues(this.values);
    }

    @Override
    public void deleteValue(String id) throws IOException {
        this.values = loadAllValues().entrySet().stream()
            .filter(
                value -> !value.getKey().equals(id)
            )
            .collect(
                Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue())
            );
        this.saveAllValues(this.values);
    }

    @Override
    public X getValueFromId(String id) throws IOException {
        return getValueFromIdOptional(id).get();
    }

    @Override
    public Optional<X> getValueFromIdOptional(String id) throws IOException {
        this.values = loadAllValues();
        return values.entrySet().stream()
            .filter(value -> value.getKey().equals(id))
            .findFirst().map(option -> option.getValue());
    }
}
