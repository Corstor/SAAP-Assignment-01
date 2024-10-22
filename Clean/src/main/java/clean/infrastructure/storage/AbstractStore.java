package clean.infrastructure.storage;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import clean.domain.Repository;

public abstract class AbstractStore<X> implements Repository<X> {
    protected final File jsonFile;
    protected List<X> values;
    protected final ObjectMapper objectMapper;

    public AbstractStore(String name) throws IOException {
        this.jsonFile = new File(name);
        this.objectMapper = new ObjectMapper();
        this.values = new LinkedList<>();

        //Create a new file if it does not exists
        if (jsonFile.createNewFile()) {
            this.saveAllValues();
        } 
    }

    public abstract List<X> loadAllValues() throws IOException;

    protected void saveAllValues() throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, values);
    }

    public void saveValue(X value) throws IOException {
        this.values = loadAllValues();
        this.values.add(value);
        this.saveAllValues();
    }

    public void deleteValue(X value) throws IOException {
        this.values.remove(value);
        this.saveAllValues();
    }
}
