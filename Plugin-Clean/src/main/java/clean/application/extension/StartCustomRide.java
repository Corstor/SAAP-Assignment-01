package clean.application.extension;

import java.io.IOException;

import clean.application.State;

public class StartCustomRide implements Plugin {

    @Override
    public void applyPlugin(State state) throws IOException {
        new CustomRideGUIDialog(state);
    }
    
}
