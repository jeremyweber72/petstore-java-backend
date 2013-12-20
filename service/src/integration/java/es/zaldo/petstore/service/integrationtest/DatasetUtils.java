package es.zaldo.petstore.service.integrationtest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;

import com.mongodb.Mongo;

import es.zaldo.petstore.core.Location;
import es.zaldo.petstore.core.Pet;

/**
 * Creates some test data.
 */
public class DatasetUtils {

    private static final String OWNER_1 = "owner1";
    private static final String OWNER_2 = "owner2";
    private static final String GROUP_HOTELS = "hotels";
    private static final String GROUP_RESTAURANTS = "restaurants";
    private static final String FREE_TYPE = "free";
    private static final String PAID_TYPE = "paid";

    private int port;
    private String host;
    private String databaseName;

    /**
     * Constructor of the class.
     *
     * @param template Mongo template to use
     */
    public DatasetUtils(String host, int port, String database) {
        this.host = host;
        this.port = port;
        this.databaseName = database;
    }

    /**
     * Create test pets.
     *
     * @param pets pets to create.
     */
    public void createTestPets(List<Pet> pets) throws Exception {

        // IT IS MANDATORY TO CREATE A NEW MONGO INSTANCE EACH TIME, they
        // cannot be reused. If you see in the logs something like "WARNING:
        // emptying DBPortPool to localhost/127.0.0.1:27777 b/c of error
        // java.io.EOFException" you are reusing the Mongo object!
        MongoTemplate template = new MongoTemplate(
                new Mongo(host, port), databaseName);

        template.dropCollection(Pet.class);

        // Create index
        template.indexOps(Pet.class).ensureIndex(new GeospatialIndex("coords"));

        // Create pets
        for (Pet pet : pets) {
            template.save(pet);
        }
    }

    /**
     *
     */
    public static List<Pet> getProximityPets() {
        ArrayList<Pet> pets = new ArrayList<Pet>(5);
        Pet pet0 = new Pet("0", "0", new Location(10.5555, 10.5555), OWNER_1, GROUP_RESTAURANTS, FREE_TYPE);
        pets.add(pet0);
        Pet pet1 = new Pet("1", "1", new Location(10.555510, 10.555510), OWNER_2, GROUP_HOTELS, PAID_TYPE);
        pets.add(pet1);
        Pet pet2 = new Pet("2", "2", new Location(10.555520, 10.555520), OWNER_2, GROUP_RESTAURANTS, FREE_TYPE);
        pets.add(pet2);
        Pet pet3 = new Pet("3", "3", new Location(10.555530, 10.555530), OWNER_1, GROUP_HOTELS, PAID_TYPE);
        pets.add(pet3);
        Pet pet4 = new Pet("4", "4", new Location(10.555540, 10.555540), OWNER_1, GROUP_RESTAURANTS, PAID_TYPE);
        pets.add(pet4);
        return pets;
    }

    /**
     *
     */
    public static List<Pet> getNearPets() {
        ArrayList<Pet> pets = new ArrayList<Pet>(5);
        Pet pet0 = new Pet("0", "0", new Location(10.5555, 10.5555), OWNER_1, GROUP_RESTAURANTS, FREE_TYPE);
        pets.add(pet0);
        Pet pet1 = new Pet("1", "1", new Location(10.555530, 10.555530), OWNER_1, GROUP_HOTELS, PAID_TYPE);
        pets.add(pet1);
        Pet pet2 = new Pet("2", "2", new Location(10.555510, 10.555510), OWNER_2, GROUP_HOTELS, PAID_TYPE);
        pets.add(pet2);
        Pet pet3 = new Pet("3", "3", new Location(10.555540, 10.555540), OWNER_1, GROUP_RESTAURANTS, PAID_TYPE);
        pets.add(pet3);
        Pet pet4 = new Pet("4", "4", new Location(10.555520, 10.555520), OWNER_2, GROUP_RESTAURANTS, FREE_TYPE);
        pets.add(pet4);
        return pets;
    }

    public static List<Pet> getWithinPets() {
        ArrayList<Pet> pets = new ArrayList<Pet>(5);
        Pet pet0 = new Pet("0", "0", new Location(13, 1), OWNER_1, GROUP_RESTAURANTS, FREE_TYPE);
        pets.add(pet0);
        Pet pet1 = new Pet("1", "1", new Location(12, 0.5), OWNER_1, GROUP_HOTELS, PAID_TYPE);
        pets.add(pet1);
        Pet pet2 = new Pet("2", "2", new Location(11, 0.2), OWNER_2, GROUP_HOTELS, PAID_TYPE);
        pets.add(pet2);
        Pet pet3 = new Pet("3", "3", new Location(9, 0.2), OWNER_1, GROUP_RESTAURANTS, PAID_TYPE);
        pets.add(pet3);
        Pet pet4 = new Pet("4", "4", new Location(11, 2.1), OWNER_2, GROUP_RESTAURANTS, FREE_TYPE);
        pets.add(pet4);
        return pets;
    }

    /**
     *
     */
    public static String printPetList(List<Pet> pets) {
        StringBuffer result = new StringBuffer();
        result.append("<pre>");
        for (Pet pet : pets) {
            result.append(pet.toString());
            result.append("<br />");
        }
        result.append("</pre>");
        return result.toString();
    }
}
