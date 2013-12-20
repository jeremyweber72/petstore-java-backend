package es.zaldo.petstore.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import es.zaldo.petstore.core.dao.PetDao;

/**
 * Abstract class that contains different utility methods for {@link PetDao}
 * tests.
 */
public abstract class AbstractPetDaoTest {

    protected static final String ID_NOTFOUND = "99999999-3fe6-400e-9a0e-a42846809e38";
    protected static final String ID = "00000000-3fe6-400e-9a0e-a42846809e38";
    protected static final String ID_1 = "00000001-3fe6-400e-9a0e-a42846809e38";
    protected static final String ID_2 = "00000002-3fe6-400e-9a0e-a42846809e38";
    protected static final String ID_3 = "00000003-3fe6-400e-9a0e-a42846809e38";
    protected static final String ID_4 = "00000004-3fe6-400e-9a0e-a42846809e38";
    protected static final String ID_5 = "00000005-3fe6-400e-9a0e-a42846809e38";
    protected static final String ID_6 = "00000006-3fe6-400e-9a0e-a42846809e38";
    protected static final double DEFAULT_LON = 10.555;
    protected static final double DEFAULT_LAT = 10.555;
    protected static final int DEFAULT_PAGE_SIZE = 10;

    protected static final String OWNER_1 = "owner1";
    protected static final String OWNER_2 = "owner2";
    protected static final String OWNER_3 = "owner3";
    protected static final String GROUP_HOTELS = "hotels";
    protected static final String GROUP_RESTAURANTS = "restaurants";
    protected static final String FREE_TYPE = "free";
    protected static final String PAID_TYPE = "paid";

    protected List<Pet> testPets = null;
    protected List<Pet> proxPets = null;
    protected List<Pet> withinPets = null;

    /**
     * {@inheritDoc}
     */
    @Test
    public void testCreateAndLoad() throws Exception {
        Pet petToCreate = new Pet(ID_1, ID_1, new Location(11d, 1d), "me", "group", "paid");
        getDaoUnderTest().upsert(petToCreate);
        Pet petLoaded = getDaoUnderTest().loadById(petToCreate.getId());
        Assert.assertEquals(petToCreate, petLoaded);
    }

    @Test
    public void updatePet() throws Exception {
        Pet pet = new Pet(ID_1, ID_1, new Location(10.5, 10.5), OWNER_1, GROUP_RESTAURANTS, FREE_TYPE);
        getDaoUnderTest().upsert(pet);

        Pet newPet = getDaoUnderTest().loadById(pet.getId());
        newPet.setGroup(GROUP_HOTELS).setType(PAID_TYPE).setOwner(OWNER_2);
        getDaoUnderTest().upsert(newPet);

        Pet retrievedPet = getDaoUnderTest().loadById(pet.getId());
        Assert.assertEquals("Wrong group", GROUP_HOTELS, retrievedPet.getGroup());
        Assert.assertEquals("Wrong type", PAID_TYPE, retrievedPet.getType());
    }

    @Test
    public void testSearchWithin() throws Exception {

        // Data setup
        createTestPetsForWithin();

        // Execution
        Pets pets = getDaoUnderTest().searchWithin(new ArrayList<String>(),
            new ArrayList<String>(), new Box(new Location(15, 0), new Location(10, 2)),
                new PageRequest(0, DEFAULT_PAGE_SIZE));

        Assert.assertEquals("Wrong number of results.", 3, pets.getNumberOfResults());
        Assert.assertEquals("Wrong page.", 0, pets.getCurrentPage());
        Assert.assertEquals("Wrong total number of results.", 3, pets.getTotalNumberOfResults());

        Assert.assertTrue("Pet1In not found!", contains(pets, withinPets.get(0)));
        Assert.assertTrue("Pet2In not found!", contains(pets, withinPets.get(1)));
        Assert.assertTrue("Pet3In not found!", contains(pets, withinPets.get(2)));

        Assert.assertFalse("Pet1Out found!", contains(pets, withinPets.get(3)));
        Assert.assertFalse("Pet2Out found!", contains(pets, withinPets.get(4)));
        Assert.assertFalse("Pet3Out found!", contains(pets, withinPets.get(5)));
    }

    /**
     * WARNING! This test will rely on the default sorting strategy, so it may
     * fail in the future or in different environments!!!
     */
    @Test
    public void testSearchWithinPagination() throws Exception {

        // Data setup
        createTestPetsForWithin();

        // Execution
        Pets pets = getDaoUnderTest().searchWithin(null,
            null, new Box(new Location(15, 0), new Location(10, 2)),
                new PageRequest(0, 2));
        Assert.assertEquals(2, pets.getPets().size());
        Assert.assertEquals("Wrong number of results.", 2, pets.getNumberOfResults());
        Assert.assertEquals("Wrong total number of results.",
                3, pets.getTotalNumberOfResults());

        pets = getDaoUnderTest().searchWithin(null,
            null, new Box(new Location(15, 0), new Location(10, 2)),
                new PageRequest(2, 1));
        Assert.assertEquals(1, pets.getPets().size());
        Assert.assertEquals("Wrong page.", 2, pets.getCurrentPage());
        Assert.assertEquals("Wrong number of results.", 1, pets.getNumberOfResults());
        Assert.assertEquals("Wrong total number of results.",
                3, pets.getTotalNumberOfResults());
    }

    @Test
    public void testSearchWithinByGroup() throws Exception {

        // Data setup
        Pet pet1 = new Pet(ID_1, "1", new Location(11, 1), OWNER_1, GROUP_RESTAURANTS, FREE_TYPE);
        getDaoUnderTest().upsert(pet1);
        Pet pet2 = new Pet(ID_2, "2", new Location(12, 0.5), OWNER_2, GROUP_HOTELS, FREE_TYPE);
        getDaoUnderTest().upsert(pet2);
        Pet pet3 = new Pet(ID_3, "3", new Location(11, 0.2), OWNER_2, GROUP_RESTAURANTS, FREE_TYPE);
        getDaoUnderTest().upsert(pet3);
        Pet pet4 = new Pet(ID_4, "4", new Location(10.5, 0.2), OWNER_1, GROUP_HOTELS, FREE_TYPE);
        getDaoUnderTest().upsert(pet4);
        Pet pet5 = new Pet(ID_5, "5", new Location(11, 0.8), OWNER_1, GROUP_RESTAURANTS, FREE_TYPE);
        getDaoUnderTest().upsert(pet5);
        Pet pet6 = new Pet(ID_6, "6", new Location(10.2, 0), OWNER_1, GROUP_HOTELS, FREE_TYPE);
        getDaoUnderTest().upsert(pet6);

        // Execution
        Pets pets = getDaoUnderTest().searchWithin(Arrays.asList(GROUP_RESTAURANTS),
            null, new Box(new Location(15, 0), new Location(10, 2)),
                new PageRequest(0, DEFAULT_PAGE_SIZE));

        Assert.assertTrue("Pet1 not found!", pets.getPets().contains(pet1));
        Assert.assertTrue("Pet3 not found!", pets.getPets().contains(pet3));
        Assert.assertTrue("Pet5 not found!", pets.getPets().contains(pet5));

        Assert.assertFalse("Pet2 found!", pets.getPets().contains(pet2));
        Assert.assertFalse("Pet4 found!", pets.getPets().contains(pet4));
        Assert.assertFalse("Pet6 found!", pets.getPets().contains(pet6));
    }

    @Test
    public void loadPetsById() throws Exception {

        // Data setup
        List<Pet> testPets = createTestPets();

        // Test Execution
        List<String> ids = new ArrayList<String>(3);
        ids.add(testPets.get(1).getId());
        ids.add(testPets.get(3).getId());
        ids.add(testPets.get(4).getId());

        Pets pets = getDaoUnderTest().getPetsById(ids);

        Assert.assertFalse("Empty pet list", pets.getPets().isEmpty());
        Assert.assertEquals("Wrong total number of results.",
                3, pets.getTotalNumberOfResults());
        Assert.assertEquals("Wrong number of results.",
                3, pets.getNumberOfResults());
        Assert.assertEquals("Wrong page", 0, pets.getCurrentPage());

        Assert.assertTrue("Pet1 not found!", contains(pets, testPets.get(1)));
        Assert.assertTrue("Pet3 not found!", contains(pets, testPets.get(3)));
        Assert.assertTrue("Pet4 not found!", contains(pets, testPets.get(4)));
        Assert.assertFalse("Pet0 found!", contains(pets, testPets.get(0)));
        Assert.assertFalse("Pet2 found!", contains(pets, testPets.get(2)));
        Assert.assertFalse("Pet5 found!", contains(pets, testPets.get(5)));
    }

    /**
     * Check if a pet with a given ID is in a list of pets.
     *
     * <p>This is needed because calls to SolrDao will return PetSolr instances
     * that will be compared against Pet instances, so equals will always
     * fail because PetSolr class is not the same as Pet class.</p>
     *
     * @param pets List
     * @param petToSearch pet we are looking for
     *
     * @return <code>true</code> if the pet is in the list.
     */
    private boolean contains(Pets pets, Pet petToSearch) {
        for (Pet pet : pets.getPets()) {
            if (pet.getId().equals(petToSearch.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates pets around center (10, 10) ordered by proximity (nearest ones
     * first).
     */
    public List<Pet> createTestPetsForProximity() throws Exception {
        proxPets = new ArrayList<Pet>(5);

        Pet pet0 = new Pet(ID_3, "2", new Location(10.5555, 10.5555), OWNER_1, GROUP_RESTAURANTS, FREE_TYPE);
        getDaoUnderTest().upsert(pet0);
        proxPets.add(pet0);

        Pet pet1 = new Pet(ID_1, "0", new Location(10.555510, 10.555510), OWNER_2, GROUP_HOTELS, PAID_TYPE);
        getDaoUnderTest().upsert(pet1);
        proxPets.add(pet1);

        Pet pet2 = new Pet(ID_2, "1", new Location(10.555520, 10.555520), OWNER_2, GROUP_RESTAURANTS, FREE_TYPE);
        getDaoUnderTest().upsert(pet2);
        proxPets.add(pet2);

        Pet pet3 = new Pet(ID_4, "3", new Location(10.555530, 10.555530), OWNER_1, GROUP_HOTELS, PAID_TYPE);
        getDaoUnderTest().upsert(pet3);
        proxPets.add(pet3);

        Pet pet4 = new Pet(ID_5, "4", new Location(10.555540, 10.555540), OWNER_1, GROUP_RESTAURANTS, FREE_TYPE);
        getDaoUnderTest().upsert(pet4);
        proxPets.add(pet4);

        return proxPets;
    }

    private void createTestPetsForWithin() {
        withinPets = new ArrayList<Pet>(5);

        Pet pet1 = new Pet(ID_1, "1", new Location(13, 1), OWNER_1, GROUP_RESTAURANTS, FREE_TYPE);
        getDaoUnderTest().upsert(pet1);
        withinPets.add(pet1);

        Pet pet2 = new Pet(ID_2, "2", new Location(12, 0.5), OWNER_1, GROUP_HOTELS, PAID_TYPE);
        getDaoUnderTest().upsert(pet2);
        withinPets.add(pet2);

        Pet pet3 = new Pet(ID_3, "3", new Location(11, 0.2), OWNER_1, GROUP_RESTAURANTS, PAID_TYPE);
        getDaoUnderTest().upsert(pet3);
        withinPets.add(pet3);

        Pet pet4 = new Pet(ID_4, "4", new Location(9, 0.2), OWNER_1, GROUP_HOTELS, FREE_TYPE);
        getDaoUnderTest().upsert(pet4);
        withinPets.add(pet4);

        Pet pet5 = new Pet(ID_5, "5", new Location(11, 2.1), OWNER_1, GROUP_RESTAURANTS, FREE_TYPE);
        getDaoUnderTest().upsert(pet5);
        withinPets.add(pet5);

        Pet pet6 = new Pet(ID_6, "6", new Location(9, 2.1), OWNER_1, GROUP_HOTELS, PAID_TYPE);
        getDaoUnderTest().upsert(pet6);
        withinPets.add(pet6);
    }

    /**
     * Creates some pets to do testing.
     */
    protected List<Pet> createTestPets() throws Exception {
        testPets = new ArrayList<Pet>(5);
        Pet pet0 = new Pet(ID_1, "0", new Location(13, 11), OWNER_1, GROUP_RESTAURANTS, FREE_TYPE);
        getDaoUnderTest().upsert(pet0);
        testPets.add(pet0);
        Pet pet1 = new Pet(ID_2, "1", new Location(12, 10.5), OWNER_2, GROUP_HOTELS, PAID_TYPE);
        getDaoUnderTest().upsert(pet1);
        testPets.add(pet1);
        Pet pet2 = new Pet(ID_3, "2", new Location(11, 10.2), OWNER_1, GROUP_HOTELS, FREE_TYPE);
        getDaoUnderTest().upsert(pet2);
        testPets.add(pet2);
        Pet pet3 = new Pet(ID_4, "3", new Location(9, 10.2), OWNER_2, GROUP_RESTAURANTS, PAID_TYPE);
        getDaoUnderTest().upsert(pet3);
        testPets.add(pet3);
        Pet pet4 = new Pet(ID_5, "4", new Location(11, 12.1), OWNER_1, GROUP_RESTAURANTS, FREE_TYPE);
        getDaoUnderTest().upsert(pet4);
        testPets.add(pet4);
        Pet pet5 = new Pet(ID_6, "5", new Location(9, 12.1), OWNER_2, GROUP_RESTAURANTS, PAID_TYPE);
        getDaoUnderTest().upsert(pet5);
        testPets.add(pet5);
        return testPets;
    }

    protected abstract PetDao getDaoUnderTest();

    protected abstract double getRadiusForProximitySearch();

}
