package clean.infrastructure.GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import clean.domain.ebike.EBikeSnapshot;
import clean.infrastructure.Pair;

public class RideDialog extends JDialog {
    public RideDialog(RideGUI rideGUI) {
        super(rideGUI, "Start Riding on EBike", true);

        JButton startButton = new JButton("Start ride");
        JButton cancelButton = new JButton("Cancel");

        DefaultListModel<Pair<String, EBikeSnapshot>> defaultList = new DefaultListModel<>();
        defaultList.addAll(rideGUI.getBikes().entrySet().stream().map(e -> new Pair<String, EBikeSnapshot>(e.getKey(), e.getValue())).toList());
        
        JList<Pair<String, EBikeSnapshot>> list = new JList<>(defaultList);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        inputPanel.add(list);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(cancelButton);

        startButton.addActionListener(e -> rideGUI.startNewRide(list.getSelectedValue()));

        cancelButton.addActionListener(e -> {
            rideGUI.stopRide();
            dispose();
        });

        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(rideGUI);
    }
}
