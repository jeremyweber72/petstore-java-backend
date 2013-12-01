package es.zaldo.petstore.service.marshalling;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import es.zaldo.petstore.core.Pet;
import es.zaldo.petstore.core.Pets;
import es.zaldo.petstore.core.utils.PetUtils;

/**
 * Tests for {@link PetsMarshaller} class.
 */
public class PetsMarshallerTest {

    private PetUtils petUtils = new PetUtils(MarshallingDataGenerator.URL,
            MarshallingDataGenerator.MAX_LATITUDE, MarshallingDataGenerator.MIN_LATITUDE,
            MarshallingDataGenerator.MAX_LONGITUDE, MarshallingDataGenerator.MIN_LONGITUDE);
    private PetMarshaller petMarshaller = new PetMarshaller(petUtils);

    /**
     * 
     */
    @Test
    public void testPetsMarshal() throws Exception {
        PetsMarshaller sut = new PetsMarshaller(petMarshaller);

        List<Pet> listPets = new ArrayList<Pet>();

        Pet pet1 = MarshallingDataGenerator.getPetWithMandatoryFields();
        Pet pet2 = MarshallingDataGenerator.getPetWithMandatoryFields();
        Pet pet3 = MarshallingDataGenerator.getPetWithMandatoryFields();
        Pet pet4 = MarshallingDataGenerator.getPetWithMandatoryFields();

        listPets.add(pet1);
        listPets.add(pet2);
        listPets.add(pet3);
        listPets.add(pet4);

        Pets pets = new Pets(listPets, MarshallingDataGenerator.CURRENT_PAGE,
                MarshallingDataGenerator.NUMBER_OF_RESULTS,
                MarshallingDataGenerator.TOTAL_NUMBER_OF_RESULTS);

        JSONObject json = sut.marshall(pets);

        JSONArray jsonPets = json.getJSONArray("pets");
        Assert.assertEquals(MarshallingDataGenerator.NAME,
                ((JSONObject) jsonPets.get(0)).getString("name"));

        // System.out.println(json.toString());
    }

    /**
     * 
     */
    @Test
    public void testPetsAttributesMarshal() throws Exception {
        PetsMarshaller sut = new PetsMarshaller(petMarshaller);

        List<Pet> listPets = new ArrayList<Pet>();

        Pet pet1 = MarshallingDataGenerator.getPetWithMandatoryFields();
        Pet pet2 = MarshallingDataGenerator.getPetWithMandatoryFields();
        Pet pet3 = MarshallingDataGenerator.getPetWithMandatoryFields();

        listPets.add(pet1);
        listPets.add(pet2);
        listPets.add(pet3);

        Pets pets = new Pets(listPets, MarshallingDataGenerator.CURRENT_PAGE,
                MarshallingDataGenerator.NUMBER_OF_RESULTS,
                MarshallingDataGenerator.TOTAL_NUMBER_OF_RESULTS);

        JSONObject json = sut.marshall(pets);

        Assert.assertEquals(MarshallingDataGenerator.CURRENT_PAGE, json.getInt("currentPage"));
        Assert.assertEquals(MarshallingDataGenerator.NUMBER_OF_RESULTS,
                json.getInt("numberOfResults"));
        Assert.assertEquals(MarshallingDataGenerator.TOTAL_NUMBER_OF_RESULTS,
                json.getInt("totalNumberOfResults"));
    }

}
