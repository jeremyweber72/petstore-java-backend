package es.zaldo.petstore.core;

import java.util.StringTokenizer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Represents a location in a map.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Location {

    /**
     * Longitude of pet on map.
     */
    private double longitude;

    /**
     * Latitude of pet on map.
     */
    private double latitude;

    /**
     * Needed by JAXB marshaller.
     */
    protected Location() {}

    /**
     * Constructor of the class.
     *
     * @param latitude Latitude
     * @param longitude Longitude
     */
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    /**
     * @return The location as an array of doubles.
     */
    public double[] toArray() {
        return new double[] { longitude, latitude };
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Location other = (Location) obj;
        if (Double.doubleToLongBits(latitude) != Double
                .doubleToLongBits(other.latitude))
            return false;
        if (Double.doubleToLongBits(longitude) != Double
                .doubleToLongBits(other.longitude))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Location [longitude=" + longitude + ", latitude=" + latitude
                + "]";
    }

    /**
     * Parses a String into a Location object.
     *
     * <p>Valid format is "latitude, longitude".</p>
     *
     * @param locationAsString Location as String
     *
     * @return A Location instace.
     */
    public static Location parseLocation(String locationAsString) {
        StringTokenizer tokenizer = new StringTokenizer(locationAsString, ",");
        double lat = Double.parseDouble(tokenizer.nextToken().trim());
        double lon = Double.parseDouble(tokenizer.nextToken().trim());
        return new Location(lat, lon);
    }

}
