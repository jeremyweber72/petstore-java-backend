package es.zaldo.petstore.service;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import es.zaldo.petstore.core.exceptions.PetsValidationException;

/**
 * Thrown when data entered by users is not valid.
 */
@SuppressWarnings("serial")
public class DataValidationException extends WebApplicationException {

    /**
     * Constructor of the class.
     *
     * @param e Root exception
     */
    public DataValidationException(PetsValidationException e) {
        super(Response.status(Status.BAD_REQUEST).entity(
                new Error(Status.BAD_REQUEST.getStatusCode(), e.getMessage())).build());
    }

    /**
     * Constructor of the class.
     *
     * @param message Description of the error
     */
    public DataValidationException(String message) {
        super(Response.status(Status.BAD_REQUEST).entity(
                new Error(Status.BAD_REQUEST.getStatusCode(), message)).build());
    }

}
