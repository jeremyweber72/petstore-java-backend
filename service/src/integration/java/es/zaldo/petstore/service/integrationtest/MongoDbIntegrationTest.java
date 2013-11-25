package es.zaldo.petstore.service.integrationtest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.api.extension.Extension;
import org.concordion.ext.EmbedExtension;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;

import com.jayway.restassured.path.json.JsonPath;

import es.zaldo.integration.utils.EmbeddedMongoDbHandler;
import es.zaldo.petstore.core.Pet;

/**
 * Base class for all MongoDB integration tests.
 *
 */
public abstract class MongoDbIntegrationTest {

    private static final Logger LOGGER =
            Logger.getLogger(MongoDbIntegrationTest.class);

    /**
     * DO NOT CHANGE VISIBILITY! Concordion needs this field to be public.
     */
    @Extension
    public ConcordionExtension extension = new EmbedExtension().withNamespace(
            "embed", "urn:concordion-extensions:2010");

    @Autowired
    private DatasetUtils testDataUtils;

    @BeforeClass
    public static void startMongo() throws Exception {
        LOGGER.debug("=== startMongo");
        EmbeddedMongoDbHandler.start();
        LOGGER.debug("=== startMongo END ");
    }

    @AfterClass
    public static void stopMongo() throws Exception {
        LOGGER.debug("=== stopMongo ");
        EmbeddedMongoDbHandler.stop();
        LOGGER.debug("=== stopMongo END");
    }

    /**
     * Set ups the database.
     *
     * @throws Exception
     */
    public void setUpDatabase() throws Exception {
        LOGGER.debug("=== setUpDatabase ");
        testDataUtils.createTestPets(getTestData());
        LOGGER.debug("=== setUpDatabase END ");
    }

    /**
     * @return A String representation of the whole dataset.
     */
    public String printDataset() {
        return DatasetUtils.printPetList(getTestData());
    }

    protected String getIdsList(String json, int size) {
        return getIdsList(json, size, false);
    }

    /**
    *
    * @param json
    * @return
    */
   protected String getIdsList(String json, int size, boolean sort) {
       ArrayList<String> result = new ArrayList<String>(size);
       for (int i = 0 ; i < size; i++) {
           String id = JsonPath.with(json).get("pets[" + i + "].id").toString().trim();
           if (!id.isEmpty()) {
               result.add(id);
           }
       }

       if (sort) {
           LOGGER.debug("========================================== Sorting " + result.toString());
           Collections.sort(result, new Comparator<String>() {

               @Override
               public int compare(String s1, String s2) {
                   return s1.compareTo(s2);
               }
           });
           LOGGER.debug("========================================== Sorting END " + result.toString());
       }

       return result.toString();
   }

    /**
     * @return The list of pets used in the test.
     */
    protected abstract List<Pet> getTestData();

    /**
     * @return the extension
     */
    public ConcordionExtension getExtension() {
        return extension;
    }

}
