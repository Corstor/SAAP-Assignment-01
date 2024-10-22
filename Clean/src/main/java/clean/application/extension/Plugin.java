package clean.application.extension;

import java.io.IOException;

import clean.application.State;

public interface Plugin {
    void applyPlugin(State state) throws IOException;
}
