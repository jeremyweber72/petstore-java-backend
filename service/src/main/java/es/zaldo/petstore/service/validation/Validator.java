package es.zaldo.petstore.service.validation;

/**
 * Defines the validation interface.
 *
 * @param T Object to be validated
 */
public interface Validator <T> {

    /**
     * Validate an object.
     *
     * @param validable Object to be validated
     *
     * @return <code>True</code> if the object is valid or <code>false</code>
     * otherwise.
     */
    boolean isValid(T validable);

}
