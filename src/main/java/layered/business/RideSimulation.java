package layered.business;

import layered.persistence.ebike.EBike;
import layered.persistence.user.User;

class RideSimulation extends Thread {
    private final User user;
    private final EBike bike;
    private boolean stopped;

    public RideSimulation(User user, EBike bike) {
        this.user = user;
        this.bike = bike;
        this.stopped = false;
    }

    @Override
    public void run() {
        this.bike.updateSpeed(1);

        var lastTimeDecreasedCredit = System.currentTimeMillis();
		user.decreaseCredit(1);

		var lastTimeChangedDir = System.currentTimeMillis();

        while(!stopped) {
            
        }
    }
}
