package layered.business;

public class UserAlreadyCreatedException extends IllegalArgumentException {
    public UserAlreadyCreatedException() {
        super("The user has already been created!");
    }
}
