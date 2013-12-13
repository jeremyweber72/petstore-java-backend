package es.zaldo.petstore.core.utils;

import java.util.UUID;

public class PetUtils {

    protected String urlBase;
    protected double maxLatitude;
    protected double minLatitude;
    protected double maxLongitude;
    protected double minLongitude;

    public PetUtils(double maxLatitude, double minLatitude, double maxLongitude, double minLongitude) {
        this("localhost:8080", maxLatitude, minLatitude, maxLongitude, minLongitude);
    }

    public PetUtils(String urlBase, double maxLatitude, double minLatitude, double maxLongitude,
            double minLongitude) {
        this.urlBase = urlBase;
        this.maxLatitude = maxLatitude;
        this.minLatitude = minLatitude;
        this.maxLongitude = maxLongitude;
        this.minLongitude = minLongitude;
    }

    public static final UUID generateUUID() {
        // generate random UUIDs
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    public boolean areCoordsInsideBoundaries(double latitude, double longitude) {
        if (latitude <= this.maxLatitude && latitude >= this.minLatitude
                && longitude <= this.maxLongitude && longitude >= this.minLongitude)
            return true;
        return false;
    }

    public String getUrlBase() {
        return this.urlBase;
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
