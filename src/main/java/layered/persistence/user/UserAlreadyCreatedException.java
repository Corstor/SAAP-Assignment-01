package layered.persistence.user;

class UserAlreadyCreatedException extends IllegalArgumentException {
    public UserAlreadyCreatedException(String id) {
        super("The " + id + " user has already been created!");
    }
}
