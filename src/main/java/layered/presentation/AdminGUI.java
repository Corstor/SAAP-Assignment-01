package layered.presentation;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import layered.business.EBikeCreation;
import layered.business.EBikeCreationImpl;

public class AdminGUI extends JFrame {
    private final EBikeCreation bikeCreator;
    private final JTextField bikeIdField;
    private final JTextField xCordField;
    private final JTextField yCordField;
    private final JButton createButton;
    private final JButton cancelButton;

    public AdminGUI() {
        super("Adding E-Bike");
        this.bikeCreator = new EBikeCreationImpl();
        this.bikeIdField = new JTextField();
        this.xCordField = new JTextField();
        this.yCordField = new JTextField();
        this.createButton = new JButton("Create EBike");
        this.cancelButton = new JButton("Reset fields");
    }
}
