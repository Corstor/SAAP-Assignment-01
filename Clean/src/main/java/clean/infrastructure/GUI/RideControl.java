package clean.infrastructure.GUI;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RideControl extends JFrame {
    public RideControl(RideGUI ride, String rideId) {
        super("Ongoing ride " + rideId);

        setSize(400, 200);

        JButton stopButton = new JButton("Stop riding");

        stopButton.addActionListener(e -> {
            ride.stopRide(rideId);
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(stopButton);

        setLayout(new BorderLayout(10, 10));
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
