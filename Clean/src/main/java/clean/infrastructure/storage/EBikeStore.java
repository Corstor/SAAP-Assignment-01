package clean.infrastructure.storage;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import clean.domain.ebike.EBikeListener;
import clean.domain.ebike.EBikeSnapshot;

public class EBikeStore extends AbstractStore<EBikeSnapshot> implements EBikeListener {
    public EBikeStore() throws IOException {
        super("EBikes.json");
    }

    public Map<String, EBikeSnapshot> loadAllValues() throws IOException {
        return objectMapper.readValue(jsonFile, new TypeReference<Map<String, EBikeSnapshot>>() {});
    }

    @Override
    public void bikeChanged(String bikeId, EBikeSnapshot bike) {
        try {
            this.saveValue(bikeId, bike);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
