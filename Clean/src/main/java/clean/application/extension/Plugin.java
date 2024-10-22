package clean.application.extension;

import java.io.IOException;

public interface Plugin<X> {
    void applyPlugin(X value) throws IOException;
}
