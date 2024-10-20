package layered.business.ebike;

class EBikeDoesNotExists extends IllegalArgumentException {
    public EBikeDoesNotExists(String id) {
        super("The " + id + " eBike does not exists!");
    }
}
