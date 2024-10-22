package clean.domain.ebike;

class EBikeAlreadyCreatedException extends IllegalArgumentException {
    EBikeAlreadyCreatedException(String id) {
        super("The " + id + " EBike has already been created!");
    }
}
