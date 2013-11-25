package es.zaldo.petstore.core.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Defines extended stats data.
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MonitorData extends MonitorShortData {

    private String name;
    private Double max;
    private Double min;

    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the max property.
     *
     * @return
     *     possible object is
     *     {@link Double }
     *
     */
    public Double getMax() {
        return max;
    }

    /**
     * Sets the value of the max property.
     *
     * @param value
     *     allowed object is
     *     {@link Double }
     *
     */
    public void setMax(Double value) {
        this.max = value;
    }

    /**
     * Gets the value of the min property.
     *
     * @return
     *     possible object is
     *     {@link Double }
     *
     */
    public Double getMin() {
        return min;
    }

    /**
     * Sets the value of the min property.
     *
     * @param value
     *     allowed object is
     *     {@link Double }
     *
     */
    public void setMin(Double value) {
        this.min = value;
    }

}
