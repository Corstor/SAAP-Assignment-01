package clean.application.extension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import clean.application.ride.Ride;

public class CustomRideControl extends JFrame {
    public CustomRideControl(Ride ride) {
        super("Ongoing ride " + ride.getId());

        JButton stopButton = new JButton("Stop riding");

        stopButton.addActionListener(e -> {
            ride.end();
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(stopButton);

        setLayout(new BorderLayout(10, 10));
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }
}
