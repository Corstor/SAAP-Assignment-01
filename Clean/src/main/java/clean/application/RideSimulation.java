package clean.application;

class RideSimulation extends Thread {
    private final UserAPI user;
    private final EBikeAPIImpl bike;
    private boolean stopped;

    RideSimulation(UserAPI user, EBikeAPIImpl bike) {
        this.user = user;
        this.bike = bike;
        this.stopped = false;
    }

    @Override
    public void run() {
        this.bike.setSpeed(1);

        var lastTimeDecreasedCredit = System.currentTimeMillis();
		user.removeCredit(1);

		var lastTimeChangedDir = System.currentTimeMillis();

        while(!stopped) {
            var location = this.bike.getLocation();
            var direction = this.bike.getDirection();
            var speed = this.bike.getSpeed();

			var loc = location.sum(direction.mul(speed));
            this.bike.moveTo(loc.getX(), loc.getY());

            location = bike.getLocation();

            if (location.getX() > 200 || location.getX() < -200) {
				bike.turnBack();
				if (location.getX() > 200) {
					bike.moveTo(200, location.getY());
				} else {
					bike.moveTo(-200, location.getY());
				}
			};
			if (location.getY() > 200 || location.getY() < -200) {
				bike.turnBack();
				if (location.getY() > 200) {
					bike.moveTo(location.getX(), 200);
				} else {
					bike.moveTo(location.getX(), -200);
				}
			};

            /* change dir randomly */
			
			var elapsedTimeSinceLastChangeDir = System.currentTimeMillis() - lastTimeChangedDir;
			if (elapsedTimeSinceLastChangeDir > 500) {
				double angle = Math.random()*60 - 30;
				this.bike.turn(angle);
				elapsedTimeSinceLastChangeDir = System.currentTimeMillis();
			}

            /* update credit */
			
			var elapsedTimeSinceLastDecredit = System.currentTimeMillis() - lastTimeDecreasedCredit;
			if (elapsedTimeSinceLastDecredit > 1000) {
				this.user.removeCredit(1);
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
