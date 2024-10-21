package clean.application;

import clean.domain.P2d;
import clean.domain.V2d;
import clean.domain.ebike.EBike;
import clean.domain.user.User;

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
            var location = this.bike.getEBikeSnapshot().location();
            var direction = this.bike.getEBikeSnapshot().direction();
            var speed = this.bike.getEBikeSnapshot().speed();

            this.bike.updateLocation(location.sum(direction.mul(speed)));
            location = bike.getEBikeSnapshot().location();

            if (location.getX() > 200 || location.getX() < -200) {
				bike.updateDirection(new V2d(-direction.getX(), direction.getY()));
				if (location.getX() > 200) {
					bike.updateLocation(new P2d(200, location.getY()));
				} else {
					bike.updateLocation(new P2d(-200, location.getY()));
				}
			};
			if (location.getY() > 200 || location.getY() < -200) {
				bike.updateDirection(new V2d(direction.getX(), -direction.getY()));
				if (location.getY() > 200) {
					bike.updateLocation(new P2d(location.getX(), 200));
				} else {
					bike.updateLocation(new P2d(location.getX(), -200));
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
