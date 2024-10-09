package layered.presentation;

import java.util.logging.Logger;

import io.vertx.core.json.JsonObject;
import layered.business.UserCreation;
import layered.business.UserCreationImpl;

public class UserVerticle extends MyVerticle {
    private final UserCreation userCreator;

    public UserVerticle(final int port) {
        super(port, "User", "user");
        logger = Logger.getLogger("User Verticle");
        this.userCreator = new UserCreationImpl();
    }

    @Override
    protected void additionalSetups() {
        //ADD post to do ride
    }

    /**
     * Create a user with an id.
     * 
     * @param request json request with the id of the user to create.
     */
    @Override
    protected void create(JsonObject request) {
        this.userCreator.createUser(request.getString("id"));
    }
}
