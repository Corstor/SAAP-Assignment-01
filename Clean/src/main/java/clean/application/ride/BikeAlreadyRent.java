package clean.application.ride;

public class BikeAlreadyRent extends IllegalArgumentException {

    public BikeAlreadyRent(String id) {
        super("The " + id + " bike is already rent");
    }

}
