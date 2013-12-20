package es.zaldo.petstore.core.dao.mixed;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.solr.core.geo.GeoLocation;

import es.zaldo.petstore.core.Location;
import es.zaldo.petstore.core.Pet;

/**
 * Tests for the {@link PetMixed} class.
 */
public class PetMixedTest {

    private static final String ID = "ID";
    private static final String OWNER = "owner";
    private static final String GROUP = "group";
    private static final String TYPE = "type";
    private static final double LATITUDE = 40.5;
    private static final double LONGITUDE = -3.9;

    /**
     *
     */
    @Test
    public void testParsePet() {
        PetMixed expectedPet = new PetMixed(
                ID, new GeoLocation(LATITUDE, LONGITUDE), OWNER, GROUP, TYPE);

        Pet pet = new Pet(ID, "1", new Location(LATITUDE, LONGITUDE), OWNER, GROUP, TYPE);

        Assert.assertEquals("PetMixed is different.",
                expectedPet, PetMixed.parsePet(pet));
    }

}
