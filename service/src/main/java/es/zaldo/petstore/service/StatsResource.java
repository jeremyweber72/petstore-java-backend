package es.zaldo.petstore.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import es.zaldo.petstore.core.utils.MonitorData;
import es.zaldo.petstore.core.utils.PerformanceMonitor;
import es.zaldo.petstore.core.utils.PerformanceMonitor.AvailableMonitors;

/**
 * Resource used to return some usage stats.
 */
@Path("/api/v1/stats")
@org.codehaus.enunciate.modules.jersey.ExternallyManagedLifecycle
public class StatsResource {

    private PerformanceMonitor performanceMonitor;

    /**
     * Constructor of the class.
     *
     * @param performanceMonitor Performance monitor
     */
    public StatsResource(PerformanceMonitor performanceMonitor) {
        this.performanceMonitor = performanceMonitor;
    }

    /**l
     * @return Some usage stats.
     */
    @org.codehaus.enunciate.jaxrs.StatusCodes({
        @org.codehaus.enunciate.jaxrs.ResponseCode (code = 200, condition = "Everything is OK.")
    })
    @GET
    @Produces({
        MediaType.APPLICATION_JSON
    })
    public Response getStats(@QueryParam("monitor") @DefaultValue("") final List<String> monitor) {
        List<MonitorData> result = new ArrayList<MonitorData>(monitor.size());
        for (String monitorId : monitor) {
            result.add(performanceMonitor.getData(
                    AvailableMonitors.valueOf(monitorId)));
        }
        final GenericEntity<List<MonitorData>> entity =
                new GenericEntity<List<MonitorData>>(result) { };
        return Response.status(Status.OK).entity(entity).build();
    }

}
