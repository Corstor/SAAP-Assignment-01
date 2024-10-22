package clean.domain.ebike;

class EBikeDoesNotExists extends IllegalArgumentException {
    EBikeDoesNotExists(String id) {
        super("The " + id + " eBike does not exists!");
    }
}
