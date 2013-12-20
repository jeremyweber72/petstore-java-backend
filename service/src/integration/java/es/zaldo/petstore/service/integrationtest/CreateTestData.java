package es.zaldo.petstore.service.integrationtest;

import java.util.ArrayList;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;

import es.zaldo.petstore.core.Location;
import es.zaldo.petstore.core.Pet;

/**
 * Creates some test data.
 */
public class CreateTestData {

    private static final String OWNER_1 = "owner1";
    private static final String OWNER_2 = "owner2";
    private static final String GROUP_HOTELS = "hotels";
    private static final String GROUP_RESTAURANTS = "restaurants";
    private static final String FREE_TYPE = "free";
    private static final String PAID_TYPE = "paid";

    private MongoTemplate template;

    /**
     * Constructor of the class.
     *
     * @param template Mongo template to use
     */
    public CreateTestData(MongoTemplate template) {
        this.template = template;
    }

    /**
     * Create test pets.
     *
     * @param pets pets to create.
     */
    public void createTestPets(Pet... pets) {
        template.dropCollection(Pet.class);
        template.save(pets);
    }

    /**
     * Creates pets around center (10, 10) ordered by proximity (nearest ones
     * first).
     */
    public void createTestPetsForNearAndProximity() throws Exception {
        template.dropCollection(Pet.class);

        // Create index
        template.indexOps(Pet.class).ensureIndex(new GeospatialIndex("coords"));

        ArrayList<Pet> pets = new ArrayList<Pet>(5);
        Pet pet0 = new Pet("0", "0", new Location(10.5555, 10.5555), OWNER_1, GROUP_RESTAURANTS, FREE_TYPE);
        pets.add(pet0);
        template.save(pet0);
        Pet pet1 = new Pet("1", "1", new Location(10.555510, 10.555510), OWNER_2, GROUP_HOTELS, PAID_TYPE);
        pets.add(pet1);
        template.save(pet1);
        Pet pet2 = new Pet("2", "2", new Location(10.555520, 10.555520), OWNER_2, GROUP_RESTAURANTS, FREE_TYPE);
        pets.add(pet2);
        template.save(pet2);
        Pet pet3 = new Pet("3", "3", new Location(10.555530, 10.555530), OWNER_1, GROUP_HOTELS, PAID_TYPE);
        pets.add(pet3);
        template.save(pet3);
        Pet pet4 = new Pet("4", "4", new Location(10.555540, 10.555540), OWNER_1, GROUP_RESTAURANTS, PAID_TYPE);
        pets.add(pet4);
        template.save(pet4);
    }
}
