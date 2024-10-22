package clean.domain.user;

class UserAlreadyCreatedException extends IllegalArgumentException {
    UserAlreadyCreatedException(String id) {
        super("The " + id + " user has already been created!");
    }
}
