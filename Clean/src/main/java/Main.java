import java.io.IOException;

import clean.domain.ebike.EBikeFactory;
import clean.domain.user.UserFactory;
import clean.infrastructure.AdminVerticle;
import clean.infrastructure.UserVerticle;
import clean.infrastructure.storage.EBikeStore;
import clean.infrastructure.storage.UserStore;

public class Main {
    private static final int ADMIN_PORT = 8080;
    private static final int USER_PORT = 8081;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        try {
            EBikeFactory.getIstance().setRepository(new EBikeStore());
            UserFactory.getIstance().setRepository(new UserStore());
        } catch (IOException e) {
            e.printStackTrace();
        }

        new AdminVerticle(ADMIN_PORT).launch();
        new UserVerticle(USER_PORT).launch();

        new UserGUI(HOST, USER_PORT);
        new UserGUI(HOST, USER_PORT);
    }
}
