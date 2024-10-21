package layered.business.ebike;

class EBikeAlreadyCreatedException extends IllegalArgumentException {
    public EBikeAlreadyCreatedException(String id) {
        super("The " + id + " EBike has already been created!");
    }
}
