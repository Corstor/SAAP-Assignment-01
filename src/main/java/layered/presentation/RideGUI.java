package layered.presentation;

import javax.swing.JFrame;

import io.vertx.ext.web.client.WebClient;

public class RideGUI extends JFrame {
    private final String id;
    private int credit;

    public RideGUI(String id, int credit, WebClient client) {
        this.id = id;
        this.credit = credit;

        this.setVisible(true);
    }
}
