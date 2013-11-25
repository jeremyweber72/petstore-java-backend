package es.zaldo.petstore.core;

/**
 * Holds boundary information.
 */
public class Box {

    private final Location lowerRight;
    private final Location upperLeft;

    /**
     * Constructor of the class.
     *
     * @param lowerRight Lower right bound
     * @param upperLeft Upper left bound
     */
    public Box(Location upperLeft, Location lowerRight) {
        super();
        this.lowerRight = lowerRight;
        this.upperLeft = upperLeft;
    }

    /**
     * @return the lower right coordinates
     */
    public Location getLowerRight() {
        return lowerRight;
    }

    /**
     * @return the upper left coordinates
     */
    public Location getUpperLeft() {
        return upperLeft;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((lowerRight == null) ? 0 : lowerRight.hashCode());
        result = prime * result
                + ((upperLeft == null) ? 0 : upperLeft.hashCode());
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
        Box other = (Box) obj;
        if (lowerRight == null) {
            if (other.lowerRight != null)
                return false;
        } else if (!lowerRight.equals(other.lowerRight))
            return false;
        if (upperLeft == null) {
            if (other.upperLeft != null)
                return false;
        } else if (!upperLeft.equals(other.upperLeft))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Bounds [upperLeft=" + upperLeft + ", lowerRight=" + lowerRight
                + "]";
    }

}
