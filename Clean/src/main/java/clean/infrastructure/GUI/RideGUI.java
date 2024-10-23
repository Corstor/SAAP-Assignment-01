package clean.infrastructure.GUI;

import javax.swing.*;

import clean.application.StateImpl;
import clean.application.extension.PluginApplier;
import clean.domain.ebike.EBikeSnapshot;

import java.awt.*;
import java.util.List;
import java.io.*;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketConnectOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public class RideGUI extends JFrame {
    private final VisualiserPanel centralPanel;
    private final String userId;
    private final WebClient client;
    private String rideId = null;
    private final PluginApplier pluginApplier;

    public RideGUI(String userId, int credit, WebClient client, List<EBikeSnapshot> bikes, String host, int port) {
        this.client = client;
        this.userId = userId;
        this.pluginApplier = new PluginApplier();

        final JButton startRideButton = new JButton("Start a ride");
        final JButton addPlugin = new JButton("Add plugin");

        JPanel topPanel = new JPanel();
        topPanel.add(startRideButton);
        topPanel.add(addPlugin);
        add(topPanel, BorderLayout.NORTH);

        startListeningToWebSocket(host, port);

        centralPanel = new VisualiserPanel(800, 500, bikes, userId, credit);
        add(centralPanel, BorderLayout.CENTER);

        startRideButton.addActionListener(e -> {
            JDialog d = new RideDialog(this);
            d.setVisible(true);
        });

        addPlugin.addActionListener(e -> {
            var fileDialog = new JFileChooser(new File("plugins"));
            int returnValue = fileDialog.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileDialog.getSelectedFile();
                var name = selectedFile.getName().replaceFirst(".jar", "");
                pluginApplier.loadNewEffect(selectedFile, name);
                var newPluginButton = new JButton(name);
                newPluginButton.addActionListener(e2 -> {
                    try {
                        pluginApplier.applyEffect(name, new StateImpl(userId));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                });
                topPanel.add(newPluginButton);
                pack();
                setSize(720, 640);
            }
        });

        setSize(720, 640);
        this.setVisible(true);
    }

    private void startListeningToWebSocket(String host, int port) {
        WebSocketConnectOptions wsOptions = new WebSocketConnectOptions()
                .setHost(host)
                .setPort(port)
                .setURI("/api/events")
                .setAllowOriginHeader(false);
        
        Vertx vertx = Vertx.vertx();

        vertx.createHttpClient().webSocket(wsOptions, result -> {
            if (result.succeeded()) {
                WebSocket ws = result.result();

                ws.frameHandler(null); //TODO
            }
        });
    }

    public List<EBikeSnapshot> getBikes() {
        return this.centralPanel.bikes;
    }

    public void startNewRide(EBikeSnapshot bike) {
        JsonObject request = new JsonObject();
        request.put("userId", userId);
        request.put("bikeId", bike.id());

        client.post("/api/ride/start")
                .sendBuffer(Buffer.buffer(request.encode()), res -> {
                    var result = res.result().bodyAsJsonObject();
                    if (result.getString("result").equals("Ok")) {
                        this.rideId = result.getString("id");
                    }
                });
    }

    public void stopRide() {
        if (rideId != null) {
            JsonObject request = new JsonObject();
            request.put("id", rideId);

            client.post("/api/ride/end")
                    .sendBuffer(Buffer.buffer(request.encode()), res -> {
                        var result = res.result().bodyAsJsonObject();
                        if (result.getString("result").equals("Ok")) {
                            rideId = null;
                        }
                    });
        }
    }

    static class VisualiserPanel extends JPanel {
        private long dx;
        private long dy;
        private List<EBikeSnapshot> bikes;
        private final String id;
        private int credit;

        VisualiserPanel(int w, int h, List<EBikeSnapshot> bikes, String id, int credit) {
            setSize(w, h);
            dx = w / 2 - 20;
            dy = h / 2 - 20;
            this.bikes = bikes;
            this.id = id;
            this.credit = credit;
        }

        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g2.clearRect(0, 0, this.getWidth(), this.getHeight());

            var it = bikes.iterator();
            while (it.hasNext()) {
                var b = it.next();
                var p = b.location();
                int x0 = (int) (dx + p.getX());
                int y0 = (int) (dy - p.getY());
                g2.drawOval(x0, y0, 20, 20);
                g2.drawString(b.id(), x0, y0 + 35);
                g2.drawString("(" + (int) p.getX() + "," + (int) p.getY() + ")", x0, y0 + 50);
            }

            g2.drawRect(10, 20, 20, 20);
            g2.drawString(id + " - credit: " + credit, 35, 35);
        }

        public void refresh() {
            repaint();
        }
    }
}
