package clean.domain.user;

class UserDoesNotExists extends IllegalArgumentException {
    UserDoesNotExists(String id) {
        super("The " + id + " user does not exists!");
    }
}
