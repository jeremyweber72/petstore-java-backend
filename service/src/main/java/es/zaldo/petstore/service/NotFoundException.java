package es.zaldo.petstore.service;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import es.zaldo.petstore.core.dao.NoSuchPetException;

/**
 * Exception thrown if a resource is not found.
 */
@SuppressWarnings("serial")
public class NotFoundException extends WebApplicationException {

    /**
     * Constructor of the class.
     *
     * @param e Root exception
     */
    public NotFoundException(NoSuchPetException e) {
        super(Response.status(Status.NOT_FOUND).entity(
                new Error(Status.NOT_FOUND.getStatusCode(), e.getMessage())).build());
    }

}
