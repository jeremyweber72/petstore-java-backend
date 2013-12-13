package es.zaldo.petstore.core.utils;

public class PetUtils {

    private final double maxLatitude;
    private final double minLatitude;
    private final double maxLongitude;
    private final double minLongitude;

    public PetUtils(final double maxLatitude, final double minLatitude, final double maxLongitude,
            final double minLongitude) {
        this.maxLatitude = maxLatitude;
        this.minLatitude = minLatitude;
        this.maxLongitude = maxLongitude;
        this.minLongitude = minLongitude;
    }

    public boolean areCoordsInsideBoundaries(double latitude, double longitude) {
        if (latitude <= this.maxLatitude && latitude >= this.minLatitude
                && longitude <= this.maxLongitude && longitude >= this.minLongitude)
            return true;
        return false;
    }

    /**
     * @return the maxLatitude
     */
    public double getMaxLatitude() {
        return maxLatitude;
    }

    /**
     * @return the minLatitude
     */
    public double getMinLatitude() {
        return minLatitude;
    }

    /**
     * @return the maxLongitude
     */
    public double getMaxLongitude() {
        return maxLongitude;
    }

    /**
     * @return the minLongitude
     */
    public double getMinLongitude() {
        return minLongitude;
    }
}
