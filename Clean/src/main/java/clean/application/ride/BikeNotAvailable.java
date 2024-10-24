package clean.application.ride;

public class BikeNotAvailable extends IllegalArgumentException {

    public BikeNotAvailable(String id) {
        super("The " + id + " bike is already rent or in maintenance");
    }

}
