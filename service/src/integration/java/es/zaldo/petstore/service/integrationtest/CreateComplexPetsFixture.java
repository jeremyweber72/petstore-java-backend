package es.zaldo.petstore.service.integrationtest;

import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import es.zaldo.integration.utils.ConcordionSpringJunit4ClassRunner;
import es.zaldo.integration.utils.EmbeddedMongoDbHandler;

/**
 * Integration test for the status page.
 */
@RunWith(ConcordionSpringJunit4ClassRunner.class)
@ContextConfiguration(locations={"/mongo-integration-test-context.xml"})
public class CreateComplexPetsFixture {

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeClass
    public static void startMongo() throws Exception {
        EmbeddedMongoDbHandler.start();
    }

    @AfterClass
    public static void stopMongo() throws Exception {
        EmbeddedMongoDbHandler.stop();
    }

    public String createPet(String pet) {
        int statusCode = RestAssured.given()
            .port(TomcatConfiguration.PORT)
            .contentType(ContentType.JSON)
            .body(pet)
            .post(ServiceConfiguration.pet_URL)
                .statusCode();
        if (statusCode == 200) {
            return "created";
        } else {
            return "not created (response status code: " + statusCode + ")";
        }
    }

    @SuppressWarnings("unchecked")
    public String loadById(String id) {
        Response response = RestAssured.given()
            .port(TomcatConfiguration.PORT)
            .contentType(ContentType.JSON)
                .get(ServiceConfiguration.pet_URL + "/" + id);

        if (response.getStatusCode() != 200) {
            return "Response status code: " + response.getStatusCode();
        }

        HashMap<String, Object> pov = (HashMap<String, Object>) JsonPath.from(
                response.asString()).get("info");
        return pov.toString();
    }

}
