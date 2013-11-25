package es.zaldo.petstore.core.dao.mongo;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import es.zaldo.petstore.core.AbstractPetDaoTest;
import es.zaldo.petstore.core.Pet;
import es.zaldo.petstore.core.Pets;
import es.zaldo.petstore.core.dao.NoSuchPetException;
import es.zaldo.petstore.core.dao.PetDao;

/**
 * Tests for the {@link PetDaoMongoImpl} class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/es/zaldo/petstore/core/dao/mongo/mongo-test-context.xml"})
public class PetDaoMongoImplTest extends AbstractPetDaoTest {

    private static final int MONGO_PORT = 27777;

    private static MongodExecutable mongodExe;
    private static MongodProcess mongod;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PetDaoMongoImpl daoUnderTest;

    @BeforeClass
    public static void startMongo() throws Exception {
        MongodStarter runtime = MongodStarter.getDefaultInstance();
        mongodExe = runtime.prepare(new MongodConfig(
                Version.V2_3_0, MONGO_PORT, Network.localhostIsIPv6()));
        mongod = mongodExe.start();
    }

    @After
    public void dropCollection() {
        mongoTemplate.dropCollection(Pet.class);

        // Create index
        mongoTemplate.indexOps(Pet.class).ensureIndex(
                new GeospatialIndex("coords"));
    }

    @AfterClass
    public static void stopMongo() throws Exception {
        if (mongod != null) {
            mongod.stop();
            mongodExe.stop();
        }
    }

    @Test(expected = NoSuchPetException.class)
    public void testLoadNonExistentPet() throws Exception {
        daoUnderTest.loadById("none");
    }

    /**
     * {@inheritDoc}
     */
    @Test
    public void testCreateComplexObjectAndLoad() throws Exception {
        Pet petToCreate = new Pet(ID_1).setName(ID_1).setLocation(11d, 1d)
                .setOwner("me").setGroup("group").setType("paid");

        HashMap<String, Object> nested = new HashMap<String, Object>();
        nested.put("fov", 12.23);
        nested.put("provider", "StreetView");

        HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("pov", nested);
        petToCreate.setAttributes(attributes);

        getDaoUnderTest().upsert(petToCreate);
        Pet petLoaded = getDaoUnderTest().loadById(petToCreate.getId());
        Assert.assertEquals(petToCreate, petLoaded);
    }

    @Test
    public void loadPetsByIdNullListsAsParameter() throws Exception {
        Pets pets = daoUnderTest.getPetsById(null);
        Assert.assertTrue("Empty pet list", pets.getPets().isEmpty());
        Assert.assertEquals("Wrong total number of results.",
                0, pets.getTotalNumberOfResults());
        Assert.assertEquals("Wrong number of results.",
                0, pets.getNumberOfResults());
        Assert.assertEquals("Wrong page", 0, pets.getCurrentPage());
    }

    @Test
    public void loadPetsByIdEmptyListAsParameter() throws Exception {
        Pets pets = daoUnderTest.getPetsById(new ArrayList<String>(0));
        Assert.assertTrue("Empty pet list", pets.getPets().isEmpty());
        Assert.assertEquals("Wrong total number of results.",
                0, pets.getTotalNumberOfResults());
        Assert.assertEquals("Wrong number of results.",
                0, pets.getNumberOfResults());
        Assert.assertEquals("Wrong page", 0, pets.getCurrentPage());
    }

    @Override
    protected PetDao getDaoUnderTest() {
        return daoUnderTest;
    }

    @Override
    protected double getRadiusForProximitySearch() {
        return 4.75;
    }

}
