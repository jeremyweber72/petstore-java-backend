package es.zaldo.petstore.core.utils;



/**
 * Defines basic stats data.
 */
public class MonitorShortData {

    private Double hits;
    private Double average;
    private Double last;

    /**
     * Gets the value of the hits property.
     *
     * @return
     *     possible object is
     *     {@link Double }
     *
     */
    public Double getHits() {
        return hits;
    }

    /**
     * Sets the value of the hits property.
     *
     * @param value
     *     allowed object is
     *     {@link Double }
     *
     */
    public void setHits(Double value) {
        this.hits = value;
    }

    /**
     * Gets the value of the average property.
     *
     * @return
     *     possible object is
     *     {@link Double }
     *
     */
    public Double getAverage() {
        return average;
    }

    /**
     * Sets the value of the average property.
     *
     * @param value
     *     allowed object is
     *     {@link Double }
     *
     */
    public void setAverage(Double value) {
        this.average = value;
    }

    /**
     * Gets the value of the last property.
     *
     * @return
     *     possible object is
     *     {@link Double }
     *
     */
    public Double getLast() {
        return last;
    }

    /**
     * Sets the value of the last property.
     *
     * @param value
     *     allowed object is
     *     {@link Double }
     *
     */
    public void setLast(Double value) {
        this.last = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "hits=" + hits + ";avg=" + average + ";last=" + last;
    }

}
