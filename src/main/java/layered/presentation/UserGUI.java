package layered.presentation;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            new WebClientOptions().setDefaultPort(8081).setDefaultHost("localhost")
        );

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

        this.createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        this.loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                client.get("/api")
                    .addQueryParam("id", id)
                    .send(res -> {
                        var result = res.result().bodyAsJsonObject();
                        if (result.getString("result").equals("Ok")) {
                            new RideGUI(id, result.getInteger("credit"), client);
                            dispose();
                        } else {
                            messageField.setText(result.getString("result"));
                        }
                    });
            }
        });

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
