// import clean.infrastructure.AdminVerticle;
import clean.infrastructure.UserGUI;
// import clean.infrastructure.UserVerticle;

public class Main {
    public static void main(String[] args) {
        // new AdminVerticle(8080).launch();
        // new UserVerticle(8081).launch();

        new UserGUI();
        new UserGUI();
    }
}
