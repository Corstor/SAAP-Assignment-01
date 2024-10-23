package clean.infrastructure.storage;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import clean.domain.ebike.EBikeSnapshot;

public class EBikeStore extends AbstractStore<EBikeSnapshot> {
    public EBikeStore() throws IOException {
        super("EBikes.json");
    }

    public List<EBikeSnapshot> loadAllValues() throws IOException {
        return objectMapper.readValue(jsonFile, new TypeReference<List<EBikeSnapshot>>() {});
    }

    @Override
    public void eventOccured(EBikeSnapshot bike) {
        try {
            this.updateValue(bike);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
