package clean.infrastructure;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fasterxml.jackson.databind.ObjectMapper;

import clean.domain.ebike.EBikeSnapshot;

import java.awt.*;

import java.util.Map;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class UserGUI extends JFrame {
    private final WebClient client;
    private final JTextField idField;
    private final JLabel messageField;
    private final JButton createButton;
    private final JButton loginButton;

    public UserGUI() {
        Vertx vertx = Vertx.vertx();
        this.client = WebClient.create(
                vertx,
                new WebClientOptions().setDefaultPort(8081).setDefaultHost("localhost"));

        this.idField = new JTextField(30);
        this.messageField = new JLabel();
        this.createButton = new JButton("Create");
        this.loginButton = new JButton("Login");

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.add(new JLabel("User ID:"));
        inputPanel.add(idField);
        inputPanel.add(messageField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createButton);
        buttonPanel.add(loginButton);

        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        this.createButton.addActionListener(e -> {
            String id = idField.getText();
            JsonObject request = new JsonObject();
            request.put("id", id);

            client.post("/api/register")
                    .sendBuffer(Buffer.buffer(request.encode()), res -> {
                        var result = res.result().bodyAsJsonObject();
                        if (result.getString("result").equals("Ok")) {
                            messageField.setText("User created!");
                        } else {
                            messageField.setText(result.getString("result"));
                        }
                    });
        });

        this.loginButton.addActionListener(e -> {
            String id = idField.getText();
            client.get("/api")
                    .addQueryParam("id", id)
                    .send(res -> {
                        var result = res.result().bodyAsJsonObject();
                        if (result.getString("result").equals("Ok")) {
                            createRideGUI(id, result);
                        } else {
                            messageField.setText(result.getString("result"));
                        }
                    });
        });

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void createRideGUI(String id, JsonObject result) {
        client.get("/api/bikes").send(res2 -> {
            var result2 = res2.result().bodyAsJsonObject();
            if (result2.getString("result").equals("Ok")) {
                var objectMapper = new ObjectMapper();
                var type = objectMapper.getTypeFactory().constructMapType(Map.class,
                        String.class, EBikeSnapshot.class);
                try {
                    Map<String, EBikeSnapshot> bikes = objectMapper.readValue(result2.getJsonArray("bikes").toString(), type);

                    new RideGUI(id, result.getInteger("credit"), client, bikes);
                    dispose();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
    }
}
