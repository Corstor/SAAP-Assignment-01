import layered.presentation.AdminGUI;
import layered.presentation.UserGUI;

public class Main {
    public static void main(String[] args) {
        new AdminGUI(8080);
        new UserGUI();
    }
}
