package es.zaldo.petstore.core.dao;

/**
 * Thrown when trying to retrieve a pet that do not exists in the repository.
 */
@SuppressWarnings("serial")
public class NoSuchPetException extends Exception {

    private String id;

    /**
     * Constructor of the class.
     *
     * @param id ID of the pet.
     */
    public NoSuchPetException(String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        return "No pet found with ID = " + id;
    }

}
