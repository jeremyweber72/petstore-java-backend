package es.zaldo.petstore.service.validation;

/**
 * Validates a given radius.
 */
public class RadiusValidator implements Validator<Double> {

    private final double maxRadius;

    /**
     * Constructor of the class.
     *
     * @param maxRadius Maximum radius allowed
     */
    public RadiusValidator(final double maxRadius) {
        this.maxRadius = maxRadius;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(Double validable) {
        return validable > 0 && validable.intValue() <= maxRadius;
    }

    /**
     * @return the maxRadius
     */
    public double getMaxRadius() {
        return maxRadius;
    }

}
