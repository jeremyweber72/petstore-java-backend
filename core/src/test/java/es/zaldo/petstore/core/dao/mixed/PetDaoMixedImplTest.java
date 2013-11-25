package es.zaldo.petstore.core.dao.mixed;

import java.io.File;

import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.jboss.byteman.contrib.bmunit.BMRule;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import es.zaldo.petstore.core.Pet;
import es.zaldo.petstore.core.dao.NoSuchPetException;
import es.zaldo.petstore.core.dao.mongo.PetDaoMongoImpl;

/**
 * Tests for {@link PetDaoMixedImpl} class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/es/zaldo/petstore/core/dao/mixed/mixed-test-context.xml"})
public class PetDaoMixedImplTest {

	// TODO Do proper tests for this class!

    private static final int MONGO_PORT = 27777;

    private static MongodExecutable mongodExe;
    private static MongodProcess mongod;

    private static EmbeddedSolrServer server;
    private static CoreContainer container;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PetDaoMongoImpl mongoDao;

    private PetDaoMixedImpl daoUnderTest;

    @Rule
    public BytemanRule byteman = BytemanRule.create(PetDaoMixedImplTest.class);

    /**
     * {@inheritDoc}
     */
    @BeforeClass
    public static void startRepos() throws Exception {

        // Start Mongo
        MongodStarter runtime = MongodStarter.getDefaultInstance();
        mongodExe = runtime.prepare(new MongodConfig(
                Version.V2_3_0, MONGO_PORT, Network.localhostIsIPv6()));
        mongod = mongodExe.start();

        container = new CoreContainer("./src/test/resources/petstore-solr-mixed",
                new File("./src/test/resources/petstore-solr-mixed/solr.xml"));

        // Start Solr
        server = new EmbeddedSolrServer(container, "core");
        server.deleteByQuery("*:*");
        server.commit();
    }

    /**
     * {@inheritDoc}
     */
    @AfterClass
    public static void stopRepos() {

        // Stop Mongo
        if (mongod != null) {
            mongod.stop();
            mongodExe.stop();
        }

        // Stop Solr
        if (server != null) {
            server.shutdown();
        }
    }

    @Before
    public void initDao() {
        this.daoUnderTest = new PetDaoMixedImpl(
                mongoDao, new SolrTemplate(server));
    }

    @After
    public void dropCollection() {
        mongoTemplate.dropCollection(Pet.class);

        // Create index
        mongoTemplate.indexOps(Pet.class).ensureIndex(
                new GeospatialIndex("coords"));
    }

    @Test(expected = RuntimeException.class)
    @BMRule(
            name = "introduce error while storing pet in Solr",
            targetClass = "org.springframework.data.solr.core.SolrTemplate",
            targetMethod = "saveBean",
            action = "throw new java.lang.RuntimeException();"
        )
    public void testCreateInMongoButErrorInSolr() throws Exception {
        Pet petToCreate = new Pet().setName("1").setLocation(11d, 1d)
                .setOwner("me").setGroup("group").setType("paid");
        daoUnderTest.upsert(petToCreate);
        daoUnderTest.loadById(petToCreate.getId());
    }

    @Test(expected = NoSuchPetException.class)
    public void testLoadNonExistentPet() throws Exception {
        daoUnderTest.loadById("d94ce45d-3fe6-400e-9a0e-a42846809e38");
    }

}
