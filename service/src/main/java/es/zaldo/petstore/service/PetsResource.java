package es.zaldo.petstore.service;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import es.zaldo.petstore.core.Box;
import es.zaldo.petstore.core.Location;
import es.zaldo.petstore.core.PageRequest;
import es.zaldo.petstore.core.Pet;
import es.zaldo.petstore.core.PetManager;
import es.zaldo.petstore.core.Pets;
import es.zaldo.petstore.core.dao.NoSuchPetException;
import es.zaldo.petstore.core.exceptions.PetsValidationException;
import es.zaldo.petstore.core.utils.PerformanceMonitor;
import es.zaldo.petstore.core.utils.PerformanceMonitor.AvailableMonitors;
import es.zaldo.petstore.service.marshalling.PetMarshaller;
import es.zaldo.petstore.service.marshalling.PetsMarshaller;
import es.zaldo.petstore.service.validation.BoxValidator;
import es.zaldo.petstore.service.validation.PageRequestValidator;

/**
 * Resource used to add and search pets.
 * 
 * <p>
 * This resource will be used to adds and return pets given different
 * parameters.
 * </p>
 */
@Path("/api/v1/pets/")
@org.codehaus.enunciate.modules.jersey.ExternallyManagedLifecycle
public class PetsResource {

    private static final String DEBUG_PREFIX = "Debug-";

    private static final String MAX_PAGE_SIZE = "Maximum number of results per page is ";
    private static final String MSG_MAX_PAGE = "Maximum page allowed is ";
    private static final String MSG_INVALID_BOX = "Invalid box: latitude params must be within [ -90 , 90], longitude params must be within [ -180, 180], nwlat > selat and nwlon < selon";

    private static final Logger LOGGER = Logger.getLogger(PetsResource.class.getName());

    private final PetManager manager;
    private final MarshalHandler marshallHandler;
    private final PetMarshaller petMarshaller;
    private final PetsMarshaller petsMarshaller;
    private final BoxValidator boxValidator;
    private final PageRequestValidator pageRequestValidator;
    private final PerformanceMonitor monitor;

    /**
     * Constructor of the class.
     * 
     * @param manager
     *            Manager to use to access the model
     */
    public PetsResource(PetManager manager, MarshalHandler marshalHandler,
            PetMarshaller petMarshaller, PetsMarshaller petsMarshaller, BoxValidator boxValidator,
            PageRequestValidator pageRequestValidator, PerformanceMonitor monitor) {
        this.manager = manager;
        this.marshallHandler = marshalHandler;
        this.petMarshaller = petMarshaller;
        this.petsMarshaller = petsMarshaller;
        this.boxValidator = boxValidator;
        this.pageRequestValidator = pageRequestValidator;
        this.monitor = monitor;
    }

    /**
     * Returns a pet object.
     * 
     * @return Response containing a pet object.
     */
    @GET
    @Path("/pet/{id}")
    @Produces({ MediaType.APPLICATION_JSON + Constants.CHARSET_UTF8,
            MediaType.TEXT_XML + Constants.CHARSET_UTF8 })
    @org.codehaus.enunciate.jaxrs.StatusCodes({
            @org.codehaus.enunciate.jaxrs.ResponseCode(code = 200, condition = "Everything is OK."),
            @org.codehaus.enunciate.jaxrs.ResponseCode(code = 404, condition = "If no pet is found with the given ID."),
            @org.codehaus.enunciate.jaxrs.ResponseCode(code = 500, condition = "If any unexpected error happens.") })
    public Response getPet(@PathParam("id") final String id,
            @QueryParam("debug") final boolean debug) {
        try {
            monitor.start(AvailableMonitors.Load_Pet_Core);
            Pet pet = manager.loadById(id);
            monitor.stop(AvailableMonitors.Load_Pet_Core);

            monitor.start(AvailableMonitors.Marshal_Pet);
            JSONObject json = petMarshaller.marshall(pet);
            monitor.stop(AvailableMonitors.Marshal_Pet);

            ResponseBuilder builder = Response.status(Status.OK).entity(json);
            if (debug) {
                builder.header(PetsResource.DEBUG_PREFIX + AvailableMonitors.Load_Pet_Core,
                        monitor.getShortData(AvailableMonitors.Load_Pet_Core));
                builder.header(PetsResource.DEBUG_PREFIX + AvailableMonitors.Marshal_Pet,
                        monitor.getShortData(AvailableMonitors.Marshal_Pet));
            }

            return builder.build();
        } catch (NoSuchPetException e) {
            throw new NotFoundException(e);
        } catch (Exception e) {
            LOGGER.error(e);
            throw new GenericErrorException(e);
        }
    }

    /**
     * Update an existing pet object.
     * 
     * @return Response containing the pet object updated.
     */
    @POST
    @Path("/pet")
    @Produces({ MediaType.APPLICATION_JSON + Constants.CHARSET_UTF8,
            MediaType.TEXT_XML + Constants.CHARSET_UTF8 })
    @org.codehaus.enunciate.jaxrs.StatusCodes({
            @org.codehaus.enunciate.jaxrs.ResponseCode(code = 200, condition = "Everything is OK."),
            @org.codehaus.enunciate.jaxrs.ResponseCode(code = 400, condition = "If there is a problem validating the request (missing parameters, invalid types, etc.)."),
            @org.codehaus.enunciate.jaxrs.ResponseCode(code = 500, condition = "If any unexpected error happens.") })
    public Response updatePet(JSONObject jsonObject, @QueryParam("debug") final boolean debug) {
        try {

            monitor.start(AvailableMonitors.Unmarshal_Pet);
            Pet pet = marshallHandler.unmarshal(jsonObject);
            monitor.stop(AvailableMonitors.Unmarshal_Pet);

            monitor.start(AvailableMonitors.Update_Pet_Core);
            Pet newPet = manager.update(pet);
            monitor.stop(AvailableMonitors.Update_Pet_Core);

            monitor.start(AvailableMonitors.Marshal_Pet);
            JSONObject json = petMarshaller.marshall(newPet);
            monitor.stop(AvailableMonitors.Marshal_Pet);

            ResponseBuilder builder = Response.status(Status.OK).entity(json);
            if (debug) {
                builder.header(PetsResource.DEBUG_PREFIX + AvailableMonitors.Unmarshal_Pet,
                        monitor.getShortData(AvailableMonitors.Unmarshal_Pet));
                builder.header(PetsResource.DEBUG_PREFIX + AvailableMonitors.Update_Pet_Core,
                        monitor.getShortData(AvailableMonitors.Update_Pet_Core));
                builder.header(PetsResource.DEBUG_PREFIX + AvailableMonitors.Marshal_Pet,
                        monitor.getShortData(AvailableMonitors.Marshal_Pet));
            }

            return builder.build();
        } catch (PetsValidationException e) {
            throw new DataValidationException(e);
        } catch (Exception e) {
            LOGGER.error(e);
            throw new GenericErrorException(e);
        }
    }

    /**
     * Get pets by their <code>id<code>s.
     * 
     * <p><code>id</code> parameter can be specified multiple times.</p>
     * 
     * @param ids
     *            pets IDs. Optional parameter, the default value is null.
     * 
     * @return Response containing a list of pet objects.
     */
    @GET
    @Path("/byid")
    @Produces({ MediaType.APPLICATION_JSON + Constants.CHARSET_UTF8,
            MediaType.TEXT_XML + Constants.CHARSET_UTF8 })
    @org.codehaus.enunciate.jaxrs.StatusCodes({
            @org.codehaus.enunciate.jaxrs.ResponseCode(code = 200, condition = "Everything is OK."),
            @org.codehaus.enunciate.jaxrs.ResponseCode(code = 500, condition = "If any unexpected error happens.") })
    public Response getPetsById(@QueryParam("id") @DefaultValue("") final List<String> ids,
            @QueryParam("debug") final boolean debug) {
        try {
            monitor.start(AvailableMonitors.Get_Pets_Core);
            Pets result = manager.getPetsById(ids);
            monitor.stop(AvailableMonitors.Get_Pets_Core);

            monitor.start(AvailableMonitors.Marshal_List);
            JSONObject json = petsMarshaller.marshall(result);
            monitor.stop(AvailableMonitors.Marshal_List);

            ResponseBuilder builder = Response.status(Status.OK).entity(json);
            if (debug) {
                builder.header(PetsResource.DEBUG_PREFIX + AvailableMonitors.Get_Pets_Core,
                        monitor.getShortData(AvailableMonitors.Get_Pets_Core));
                builder.header(PetsResource.DEBUG_PREFIX + AvailableMonitors.Marshal_List,
                        monitor.getShortData(AvailableMonitors.Marshal_List));
            }
            return builder.build();
        } catch (Exception e) {
            LOGGER.error(e);
            throw new GenericErrorException(e);
        }
    }

    /**
     * Search pets by bounds
     * 
     * @param nwLatitude
     *            Northwest latitude. Mandatory parameter.
     * @param nwLongitude
     *            Northwest longitude. Mandatory parameter.
     * @param seLatitude
     *            Southeast latitude. Mandatory parameter.
     * @param seLongitude
     *            Southeast longitude. Mandatory parameter.
     * @param group
     *            pet group. Optional parameter, the default value is null.
     * @param type
     *            pet type. Optional parameter, the default value is null.
     * @param page
     *            Pagination start page. Default value is 0
     * @param size
     *            Num of rows to retrieve. Default value is 10
     * 
     * @return Response containing a list of pet objects.
     */
    @GET
    @Path("/search/within")
    @Produces({ MediaType.APPLICATION_JSON + Constants.CHARSET_UTF8,
            MediaType.TEXT_XML + Constants.CHARSET_UTF8 })
    @org.codehaus.enunciate.jaxrs.StatusCodes({
            @org.codehaus.enunciate.jaxrs.ResponseCode(code = 200, condition = "Everything is OK."),
            @org.codehaus.enunciate.jaxrs.ResponseCode(code = 500, condition = "If any unexpected error happens.") })
    public Response searchWithin(
            @QueryParam("nwlat") @DefaultValue(Constants.INVALID_COORDINATE_VALUE) final double nwLatitude,
            @QueryParam("nwlon") @DefaultValue(Constants.INVALID_COORDINATE_VALUE) final double nwLongitude,
            @QueryParam("selat") @DefaultValue(Constants.INVALID_COORDINATE_VALUE) final double seLatitude,
            @QueryParam("selon") @DefaultValue(Constants.INVALID_COORDINATE_VALUE) final double seLongitude,
            @QueryParam("group") final List<String> groups,
            @QueryParam("type") final List<String> types,
            @QueryParam("page") @DefaultValue(Constants.DEFAULT_START_PAGE) final int page,
            @QueryParam("size") @DefaultValue(Constants.DEFAULT_PAGE_NUMROWS) final int size,
            @QueryParam("debug") final boolean debug) {

        Box bounds = new Box(new Location(nwLatitude, nwLongitude), new Location(seLatitude,
                seLongitude));
        if (!boxValidator.isValid(bounds)) {
            throw new DataValidationException(MSG_INVALID_BOX);
        }

        PageRequest pagination = new PageRequest(page, size);
        if (!pageRequestValidator.isValid(pagination)) {
            throw new DataValidationException(getInvalidPaginationErrorMessage());
        }

        try {
            monitor.start(AvailableMonitors.Within_Core);
            Pets result = manager.searchWithin(groups, types, bounds, pagination);
            monitor.stop(AvailableMonitors.Within_Core);

            monitor.start(AvailableMonitors.Marshal_List);
            JSONObject json = petsMarshaller.marshall(result);
            monitor.stop(AvailableMonitors.Marshal_List);

            ResponseBuilder builder = Response.status(Status.OK).entity(json);
            if (debug) {
                builder.header(PetsResource.DEBUG_PREFIX + AvailableMonitors.Within_Core,
                        monitor.getShortData(AvailableMonitors.Within_Core));
                builder.header(PetsResource.DEBUG_PREFIX + AvailableMonitors.Marshal_List,
                        monitor.getShortData(AvailableMonitors.Marshal_List));
            }
            return builder.build();
        } catch (Exception e) {
            LOGGER.error(e);
            throw new GenericErrorException(e);
        }
    }

    /**
     * @return The error message to be shown if a pagination request is not
     *         valid.
     */
    private String getInvalidPaginationErrorMessage() {
        return MSG_MAX_PAGE + pageRequestValidator.getMaxPage() + ", " + MAX_PAGE_SIZE
                + pageRequestValidator.getMaxSize();
    }

}
