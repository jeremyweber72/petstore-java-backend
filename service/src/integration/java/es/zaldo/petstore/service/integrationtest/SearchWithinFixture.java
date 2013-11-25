package es.zaldo.petstore.service.integrationtest;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import es.zaldo.integration.utils.ConcordionSpringJunit4ClassRunner;
import es.zaldo.petstore.core.Location;
import es.zaldo.petstore.core.Pet;

/**
 * Integration test for search near functionallity.
 */
@RunWith(ConcordionSpringJunit4ClassRunner.class)
@ContextConfiguration(locations={"/mongo-integration-test-context.xml"})
public class SearchWithinFixture extends MongoDbIntegrationTest {

    private static final Logger LOGGER =
            Logger.getLogger(SearchWithinFixture.class);

    /**
     *
     */
    public String searchWithin(String nw, String se) {
        LOGGER.debug("=== searchWithin (" + nw + "), (" +se + ")");

        Location nwLocation = Location.parseLocation(nw);
        Location seLocation = Location.parseLocation(se);

        Response response = RestAssured.given().port(TomcatConfiguration.PORT)
            .contentType(ContentType.JSON)
            .param("nwlat", nwLocation.getLatitude())
            .param("nwlon", nwLocation.getLongitude())
            .param("selat", seLocation.getLatitude())
            .param("selon", seLocation.getLongitude())
                .get(ServiceConfiguration.SEARCH_WITHIN_URL);

        if (response.getStatusCode() != 200) {
            return "Response status code: " + response.getStatusCode();
        }

        String result = getIdsList(response.asString(), 3, true);

        LOGGER.debug("=== searchWithin END");
        return result.toString();
    }

    /**
     * {@inheritDoc}
     */
    protected List<Pet> getTestData() {
        return DatasetUtils.getWithinPets();
    }

}
