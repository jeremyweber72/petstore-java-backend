package es.zaldo.petstore.service.integrationtest;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;

/**
 * Integration test for the status page.
 */
@RunWith(ConcordionRunner.class)
public class StatusPageFixture {

    public String getStatusInfo(String attribute) {
        String json =
            RestAssured.given().port(TomcatConfiguration.PORT).get(
                "/service/status.jsp").asString();
        return JsonPath.from(json).get(attribute);
    }

}
