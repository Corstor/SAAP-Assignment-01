package layered.presentation;

import javax.swing.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.util.List;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import layered.business.ebike.EBike;
import layered.business.ebike.EBikeImpl;

public class RideGUI extends JFrame {
    private final VisualiserPanel centralPanel;
    private final String userId;
    private final WebClient client;
    private String rideId = null;

    public RideGUI(String userId, int credit, WebClient client, List<EBike> bikes) {
        this.client = client;
        this.userId = userId;

        final JButton startRideButton = new JButton("Start a ride");

        JPanel topPanel = new JPanel();
        topPanel.add(startRideButton);
        add(topPanel, BorderLayout.NORTH);

        centralPanel = new VisualiserPanel(800, 500, bikes, userId, credit);
        add(centralPanel, BorderLayout.CENTER);

        startRideButton.addActionListener(e -> {
            JDialog d = new RideDialog(this);
            d.setVisible(true);
        });

        setSize(720, 640);
        this.setVisible(true);
    }

    public List<EBike> getBikes() {
        return this.centralPanel.bikes;
    }

    public void startNewRide(EBike bike) {
        JsonObject request = new JsonObject();
        request.put("userId", userId);
        request.put("bikeId", bike.getId());

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
                            updateBikes();
                            updateUserCredit();
                            rideId = null;
                        }
                    });
        }
    }

    private void updateBikes() {
        this.client.get("/api/bikes").send(res2 -> {
            var result2 = res2.result().bodyAsJsonObject();
            if (result2.getString("result").equals("Ok")) {
                var objectMapper = new ObjectMapper();
                var type = objectMapper.getTypeFactory().constructCollectionType(List.class,
                        EBikeImpl.class);
                try {
                    List<EBike> bikes = objectMapper
                            .readValue(result2.getJsonArray("bikes").toString(), type);
                    this.centralPanel.bikes = bikes;
                    this.centralPanel.refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateUserCredit() {
        this.client.get("/api")
                .addQueryParam("id", userId)
                .send(res -> {
                    var result = res.result().bodyAsJsonObject();
                    if (result.getString("result").equals("Ok")) {
                        this.centralPanel.credit = result.getInteger("credit");
                        this.centralPanel.refresh();
                    }
                });
    }

    static class VisualiserPanel extends JPanel {
        private long dx;
        private long dy;
        private List<EBike> bikes;
        private final String id;
        private int credit;

        VisualiserPanel(int w, int h, List<EBike> bikes, String id, int credit) {
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
                var p = b.getLocation();
                int x0 = (int) (dx + p.getX());
                int y0 = (int) (dy - p.getY());
                g2.drawOval(x0, y0, 20, 20);
                g2.drawString(b.getId(), x0, y0 + 35);
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
