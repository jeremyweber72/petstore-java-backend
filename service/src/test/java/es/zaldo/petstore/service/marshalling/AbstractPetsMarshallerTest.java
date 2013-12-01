package es.zaldo.petstore.service.marshalling;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import es.zaldo.petstore.core.Pet;
import es.zaldo.petstore.core.Pets;

/**
 * Test definition for all the implementations of the Pets marshallers.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class AbstractPetsMarshallerTest {

    /**
     * @return An instance of a marshaller to execute the tests.
     */
    protected abstract Marshaller getMarshallerClassToTest();

    /**
     * 
     */
    @Test
    public void testPetsMarshal() throws Exception {
        Marshaller sut = getMarshallerClassToTest();

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

        JSONObject json = (JSONObject) sut.marshall(pets);

        JSONArray jsonPets = json.getJSONArray("pets");
        Assert.assertEquals(MarshallingDataGenerator.NAME,
                ((JSONObject) jsonPets.get(0)).getString("name"));
    }

    /**
     * 
     */
    @Test
    public void testPetsAttributesMarshal() throws Exception {
        Marshaller sut = getMarshallerClassToTest();

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

        JSONObject json = (JSONObject) sut.marshall(pets);

        Assert.assertEquals(MarshallingDataGenerator.CURRENT_PAGE, json.getInt("currentPage"));
        Assert.assertEquals(MarshallingDataGenerator.NUMBER_OF_RESULTS,
                json.getInt("numberOfResults"));
        Assert.assertEquals(MarshallingDataGenerator.TOTAL_NUMBER_OF_RESULTS,
                json.getInt("totalNumberOfResults"));
    }

    /**
     * Test to see if there is any different between serial and parallel
     * marshalling.
     * 
     * TODO Move this to a performance test on its own.
     */
    @Test
    public void testPetsMarshalALotOfPets() throws Exception {
        Marshaller sut = getMarshallerClassToTest();

        List<Pet> listPets = new ArrayList<Pet>();

        for (int i = 0; i < 10000; i++) {
            listPets.add(MarshallingDataGenerator.getPetWithMandatoryFields());
        }

        Pets pets = new Pets(listPets, MarshallingDataGenerator.CURRENT_PAGE,
                MarshallingDataGenerator.NUMBER_OF_RESULTS,
                MarshallingDataGenerator.TOTAL_NUMBER_OF_RESULTS);

        long start = new Date().getTime();
        sut.marshall(pets);
        long end = new Date().getTime();

        System.out.println("Execution time (ms): " + (end - start));
    }

}
