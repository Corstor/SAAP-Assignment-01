

import javax.swing.*;

import clean.application.StateImpl;
import clean.application.extension.PluginApplier;
import clean.domain.P2d;
import clean.domain.V2d;
import clean.domain.ebike.EBikeSnapshot;
import clean.domain.ebike.EBikeState;

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
                        this.centralPanel.refresh();
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

                ws.frameHandler(frame -> {
                    if (frame.isText()) {
                        String data = frame.textData();
                        JsonObject obj = new JsonObject(data);
                        String event = obj.getString("event");
                        if (event.equals("bike-update")) {
                            try {
                                String id = obj.getString("id");
                                int batteryLevel = obj.getInteger("batteryLevel");
                                double speed = obj.getDouble("speed");

                                var state = EBikeState.valueOf(obj.getString("state"));
                                var direction = new V2d(obj.getDouble("direction-x"), obj.getDouble("direction-y"));
                                var location = new P2d(obj.getDouble("location-x"), obj.getDouble("location-y"));

                                this.centralPanel.bikes = this.getBikes().stream().map(e -> {
                                    if (e.id().equals(id)) {
                                        return new EBikeSnapshot(id, state, batteryLevel, speed, direction,
                                        location);
                                    }
                                    return e;
                                }).toList();                                

                                this.centralPanel.refresh();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (event.equals("user-update")) {
                            if (this.centralPanel.id.equals(obj.getString("id"))) {
                                this.centralPanel.credit = obj.getInteger("credit");
                            }
                        }
                    }
                });
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
                        var rideId = result.getString("id");
                        new RideControl(this, rideId);
                    }
                });
    }

    public void stopRide(String rideId) {
        JsonObject request = new JsonObject();
        request.put("id", rideId);

        client.post("/api/ride/end")
                .sendBuffer(Buffer.buffer(request.encode()));
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
