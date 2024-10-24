package clean.application.extension;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;

import clean.application.State;
import clean.application.ride.Ride;
import clean.domain.ebike.EBike;

public class CustomRideGUIDialog extends JFrame {
    private static int id;

    public CustomRideGUIDialog(State state) {
        super("Choose which bike to use on your custom ride");

        JButton startButton = new JButton("Start ride");

        DefaultListModel<EBike> defaultList = new DefaultListModel<>();
        defaultList.addAll(state.getBikes());
        
        JList<EBike> list = new JList<>(defaultList);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        inputPanel.add(list);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);

        startButton.addActionListener(e -> {
            try {
                Ride ride = new CustomRide("custom-" + id++, state.getUser(), list.getSelectedValue());
                new CustomRideControl(ride);
                ride.start();
                dispose();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }
}