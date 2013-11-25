package es.zaldo.petstore.service;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Represents a generic error that travels up to Jersey resource.
 */
@SuppressWarnings("serial")
public class GenericErrorException extends WebApplicationException {

    /**
     * Constructor of the class.
     *
     * @param e Root exception
     */
    public GenericErrorException(Exception e) {
        super(Response.status(Status.INTERNAL_SERVER_ERROR).entity(
                new Error(Status.INTERNAL_SERVER_ERROR.getStatusCode(), e.getMessage())).build());
    }

}
