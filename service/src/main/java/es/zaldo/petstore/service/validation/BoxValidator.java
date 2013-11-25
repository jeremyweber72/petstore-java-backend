package es.zaldo.petstore.service.validation;

import es.zaldo.petstore.core.Box;

/**
 * Validates <code>Box</code> objects.
 */
public class BoxValidator implements Validator<Box> {

    private LocationValidator locationValidator;

    /**
     * Constructor of the class.
     */
    public BoxValidator(LocationValidator locationValidator) {
        this.locationValidator = locationValidator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(Box validable) {
        return locationValidator.isValid(validable.getUpperLeft()) &&
            locationValidator.isValid(validable.getLowerRight()) &&
            validable.getUpperLeft().getLatitude() > validable.getLowerRight().getLatitude() &&
                validable.getUpperLeft().getLongitude() < validable.getLowerRight().getLongitude();
    }

}
