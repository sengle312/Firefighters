package firefighters;

import api.CityNode;
import api.Firefighter;

public class FirefighterImpl implements Firefighter {
    private CityNode location;
    private int distanceTraveled;

    public FirefighterImpl(CityNode location) {
        this.location = location;
        this.distanceTraveled = 0;
    }

    @Override
    public CityNode getLocation() {
        return location;
    }

    @Override
    public int distanceTraveled() {
        return distanceTraveled;
    }

    @Override
    public void addDistanceTraveled(int distance) {
        distanceTraveled += distance;
    }

    @Override
    public void setLocation(CityNode location) {
        this.location = location;
    }
}
