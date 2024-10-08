package layered.business;

import layered.persistence.P2d;
import layered.persistence.V2d;
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
            var location = this.bike.getLocation();
            var direction = this.bike.getDirection();
            var speed = this.bike.getSpeed();

            this.bike.updateLocation(location.sum(direction.mul(speed)));
            location = bike.getLocation();

            if (location.x() > 200 || location.x() < -200) {
				bike.updateDirection(new V2d(-direction.x(), direction.y()));
				if (location.x() > 200) {
					bike.updateLocation(new P2d(200, location.y()));
				} else {
					bike.updateLocation(new P2d(-200, location.y()));
				}
			};
			if (location.y() > 200 || location.y() < -200) {
				bike.updateDirection(new V2d(direction.x(), -direction.y()));
				if (location.y() > 200) {
					bike.updateLocation(new P2d(location.x(), 200));
				} else {
					bike.updateLocation(new P2d(location.x(), -200));
				}
			};

            /* change dir randomly */
			
			var elapsedTimeSinceLastChangeDir = System.currentTimeMillis() - lastTimeChangedDir;
			if (elapsedTimeSinceLastChangeDir > 500) {
				double angle = Math.random()*60 - 30;
				this.bike.updateDirection(direction.rotate(angle));
				elapsedTimeSinceLastChangeDir = System.currentTimeMillis();
			}

            /* update credit */
			
			var elapsedTimeSinceLastDecredit = System.currentTimeMillis() - lastTimeDecreasedCredit;
			if (elapsedTimeSinceLastDecredit > 1000) {
				this.user.decreaseCredit(1);
				lastTimeDecreasedCredit = System.currentTimeMillis();
			}

            try {
				Thread.sleep(20);
			} catch (Exception ex) {}
        }
    }

    public void stopSimulation() {
        stopped = true;
        interrupt();
    }
}
