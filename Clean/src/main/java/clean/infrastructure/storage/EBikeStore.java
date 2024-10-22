package clean.infrastructure.storage;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import clean.domain.ebike.EBikeListener;
import clean.domain.ebike.EBikeSnapshot;

public class EBikeStore extends AbstractStore<EBikeSnapshot> implements EBikeListener {
    public EBikeStore() throws IOException {
        super("EBikes.json");
    }

    public List<EBikeSnapshot> loadAllValues() throws IOException {
        return objectMapper.readValue(jsonFile, new TypeReference<List<EBikeSnapshot>>() {});
    }

    @Override
    public void bikeChanged(EBikeSnapshot bike) {
        try {
            this.saveValue(bike);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
