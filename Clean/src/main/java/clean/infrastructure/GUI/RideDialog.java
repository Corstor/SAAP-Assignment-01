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

public class RideDialog extends JDialog {
    public RideDialog(RideGUI rideGUI) {
        super(rideGUI, "Start Riding on EBike", true);

        JButton startButton = new JButton("Start ride");
        JButton cancelButton = new JButton("Cancel");

        DefaultListModel<EBikeSnapshot> defaultList = new DefaultListModel<>();
        defaultList.addAll(rideGUI.getBikes());
        
        JList<EBikeSnapshot> list = new JList<>(defaultList);
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
