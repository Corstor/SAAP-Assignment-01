package layered.persistence.user;

public class UserDoesNotExists extends IllegalArgumentException {
    public UserDoesNotExists(String id) {
        super("The " + id + " user does not exists!");
    }
}
