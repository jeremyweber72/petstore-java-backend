package es.zaldo.petstore.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import es.zaldo.petstore.core.Pet;
import es.zaldo.petstore.core.Pets;
import es.zaldo.petstore.core.exceptions.PetsValidationException;
import es.zaldo.petstore.core.utils.PetUtils;
import es.zaldo.petstore.service.MarshalHandler;

/**
 * Tests for the {@link MarshalHandler} class.
 */
public class MarshalHandlerTest {

    private static final String EMPTY_STRING = "       ";

    private static final String ID = "id";

    private static final String URL = "http://localhost:8080/service/api/v1/pets/pet";

    private static final String OWNER = "name";

    private static final String NAME = "owner";

    private static final String GROUP = "group";

    private static final String TYPE = "type";

    private static final double LATITUDE = 40;

    private static final double LONGITUDE = -3;

    private static final String ADDRESS_STREET = "test street";

    private static final String ADDRESS_CITY = "test city";

    private static final String ADDRESS_POBOX = "test pobox";

    private static final String ADDRESS_PHONE = "913390000";

    private static final double POV_LATITUDE = 41.3;

    private static final double POV_LONGITUDE = -4.1;

    private static final int POV_VIEW_TYPE = 3;

    private static final String POV_HEADING = "test pov heading";

    private static final String POV_FOV = "test pov fov";

    private static final String POV_PITCH = "test pov pitch";

    private static final String ADDITIONAL_FIELD = "additionalField";

    private static final String ADDITIONAL_NODE = "additionalNode";

    private static final int CURRENT_PAGE = 0;

    private static final int NUMBER_OF_RESULTS = 2;

    private static final int TOTAL_NUMBER_OF_RESULTS = 3;

    private static final double MAX_LATITUDE = 44;

    private static final double MIN_LATITUDE = 27;

    private static final double MAX_LONGITUDE = 5;

    private static final double MIN_LONGITUDE = -18.5;

    @Autowired
    private static MarshalHandler marshalUnderTest;

    private static PetUtils petUtils;

    @BeforeClass
    public static void initializeObjects()    {
        petUtils= new PetUtils(URL, MAX_LATITUDE, MIN_LATITUDE, MAX_LONGITUDE, MIN_LONGITUDE);
        marshalUnderTest= new MarshalHandler(petUtils);
    }

    @Test
    public void testUnmarshal() throws Exception {

        JSONObject json= getJSONObjectWithMandatoryFields();

        Pet pet= marshalUnderTest.unmarshal(json);

        Assert.assertEquals(ID, pet.getId());
        Assert.assertEquals(OWNER, pet.getOwner());
        Assert.assertEquals(NAME, pet.getName());

        Assert.assertEquals( String.valueOf(LATITUDE),
                String.valueOf(pet.getLocation().getLatitude()));
        Assert.assertEquals( String.valueOf(LONGITUDE),
                String.valueOf(pet.getLocation().getLongitude()));
    }

    @Test
    public void testUnmarshalWithoutId() throws Exception {

        JSONObject json= getJSONObjectWithMandatoryFields();
        json.remove("id");

        Pet pet= marshalUnderTest.unmarshal(json);

        Assert.assertEquals(NAME, pet.getName());
    }

    @Test
    public void testUnmarshalWithEmptyId() throws Exception {

        JSONObject json = buildTestJSONObject(
                EMPTY_STRING, NAME, OWNER, GROUP, TYPE, LATITUDE, LONGITUDE);
        Pet pet = marshalUnderTest.unmarshal(json);
        Assert.assertFalse("No ID found!", pet.getId().isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUnmarshalWithAttributes() throws Exception {
        JSONObject json= getJSONObjectWithMandatoryFields();

        json.put("address", getJSONObjectAddressAttributes());
        json.put("pov", getJSONObjectPovAttributes());

        Pet pet= marshalUnderTest.unmarshal(json);

        HashMap<String, Object> attr = pet.getAttributes();
        HashMap<String, String> address= (HashMap<String, String>) attr.get("address");

        Assert.assertEquals(ADDRESS_STREET, address.get("street"));
        Assert.assertEquals(ADDRESS_CITY, address.get("city"));
        Assert.assertEquals(ADDRESS_POBOX, address.get("poBox"));
        Assert.assertEquals(ADDRESS_PHONE, address.get("phoneNumber"));

        HashMap<String, String> pov= (HashMap<String, String>) attr.get("pov");

        Assert.assertEquals(POV_LATITUDE, pov.get("latitude"));
        Assert.assertEquals(POV_LONGITUDE, pov.get("longitude"));
        Assert.assertEquals(POV_HEADING, pov.get("heading"));
        Assert.assertEquals(POV_FOV, pov.get("fov"));
        Assert.assertEquals(POV_PITCH, pov.get("pitch"));
        Assert.assertEquals(POV_VIEW_TYPE, pov.get("viewType"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUnmarshalWithAdditionalElements() throws Exception {
        JSONObject json= getJSONObjectWithMandatoryFields();

        json.put(ADDITIONAL_FIELD, ADDITIONAL_FIELD);

        JSONObject additionalJson= new JSONObject();
        additionalJson.put(ADDITIONAL_FIELD, ADDITIONAL_FIELD);
        json.put(ADDITIONAL_NODE, additionalJson);

        Pet pet= marshalUnderTest.unmarshal(json);

        HashMap<String, Object> attr = pet.getAttributes();
        Assert.assertEquals(ADDITIONAL_FIELD, attr.get(ADDITIONAL_FIELD));

        HashMap<String, String> node= (HashMap<String, String>) attr.get(ADDITIONAL_NODE);
        Assert.assertEquals(ADDITIONAL_FIELD, node.get(ADDITIONAL_FIELD));
    }

    @Test
    public void testMarshal() throws Exception {

        Pet pet= getPetWithMandatoryFields();
        JSONObject json= marshalUnderTest.marshal(pet);

        Assert.assertEquals(ID, json.getString("id"));
        Assert.assertEquals(URL + "/" + ID, json.getString("url"));
        Assert.assertEquals(OWNER, json.getString("owner"));
        Assert.assertEquals(NAME, json.getString("name"));

        Assert.assertEquals( String.valueOf(LATITUDE),
                String.valueOf(json.getJSONObject("coords").getString("latitude")));
        Assert.assertEquals( String.valueOf(LONGITUDE),
                String.valueOf(json.getJSONObject("coords").getString("longitude")));
    }

    @Test
    public void testMarshalWithAttributes() throws Exception {

        Pet pet= getPetWithMandatoryFields();
        HashMap<String, Object> mapAttr= new HashMap<String, Object>();
        mapAttr.put("address", getAddressAttributes());
        mapAttr.put(ADDITIONAL_FIELD, ADDITIONAL_FIELD);
        pet.setAttributes(mapAttr);

        JSONObject json= marshalUnderTest.marshal(pet);
        //System.out.println(json.toString());

        Assert.assertEquals( ADDITIONAL_FIELD, json.getString(ADDITIONAL_FIELD));
        Assert.assertEquals( ADDRESS_STREET, json.getJSONObject("address").getString("street"));
        Assert.assertEquals( ADDRESS_CITY, json.getJSONObject("address").getString("city"));
        Assert.assertEquals( ADDRESS_POBOX, json.getJSONObject("address").getString("poBox"));
        Assert.assertEquals( ADDRESS_PHONE, json.getJSONObject("address").getString("phoneNumber"));
    }

    @Test
    public void testPetsMarshal() throws Exception {
        List<Pet> listPets= new ArrayList<Pet>();

        Pet pet1= getPetWithMandatoryFields();
        Pet pet2= getPetWithMandatoryFields();
        Pet pet3= getPetWithMandatoryFields();
        Pet pet4= getPetWithMandatoryFields();

        listPets.add(pet1);
        listPets.add(pet2);
        listPets.add(pet3);
        listPets.add(pet4);

        Pets pets= new Pets(listPets, CURRENT_PAGE, NUMBER_OF_RESULTS, TOTAL_NUMBER_OF_RESULTS);

        JSONObject json= marshalUnderTest.marshal(pets);

        JSONArray jsonPets= json.getJSONArray("pets");
        Assert.assertEquals(NAME, ((JSONObject) jsonPets.get(0)).getString("name"));


        //System.out.println(json.toString());
    }


    @Test
    public void testPetsAttributesMarshal() throws Exception {
        List<Pet> listPets= new ArrayList<Pet>();

        Pet pet1= getPetWithMandatoryFields();
        Pet pet2= getPetWithMandatoryFields();
        Pet pet3= getPetWithMandatoryFields();

        listPets.add(pet1);
        listPets.add(pet2);
        listPets.add(pet3);

        Pets pets= new Pets(listPets, CURRENT_PAGE, NUMBER_OF_RESULTS, TOTAL_NUMBER_OF_RESULTS);

        JSONObject json= marshalUnderTest.marshal(pets);

        Assert.assertEquals(CURRENT_PAGE, json.getInt("currentPage"));
        Assert.assertEquals(NUMBER_OF_RESULTS, json.getInt("numberOfResults"));
        Assert.assertEquals(TOTAL_NUMBER_OF_RESULTS, json.getInt("totalNumberOfResults"));
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithoutName() throws Exception {
        JSONObject json= getJSONObjectWithMandatoryFields();
        json.remove("name");

        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithEmptyName() throws Exception {
        JSONObject json = buildTestJSONObject(
                ID, EMPTY_STRING, OWNER, GROUP, TYPE, LATITUDE, LONGITUDE);
        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithoutOwner() throws Exception {
        JSONObject json= getJSONObjectWithMandatoryFields();
        json.remove("owner");

        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithEmptyOwner() throws Exception {
        JSONObject json = buildTestJSONObject(
                ID, NAME, EMPTY_STRING, GROUP, TYPE, LATITUDE, LONGITUDE);
        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithoutGroup() throws Exception {
        JSONObject json= getJSONObjectWithMandatoryFields();
        json.remove("group");

        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithEmptyGroup() throws Exception {
        JSONObject json = buildTestJSONObject(
                ID, NAME, OWNER, EMPTY_STRING, TYPE, LATITUDE, LONGITUDE);
        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithoutType() throws Exception {
        JSONObject json= getJSONObjectWithMandatoryFields();
        json.remove("type");

        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithEmptyType() throws Exception {
        JSONObject json = buildTestJSONObject(
                ID, NAME, OWNER, GROUP, EMPTY_STRING, LATITUDE, LONGITUDE);
        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithoutOneCoord() throws Exception {
        JSONObject json= getJSONObjectWithMandatoryFields();
        JSONObject coords= json.getJSONObject("coords");
        coords.remove("latitude");
        json.put("coords", coords);

        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionCoordsOutofBounds() throws Exception {
        JSONObject json= getJSONObjectWithMandatoryFields();
        JSONObject coords= json.getJSONObject("coords");
        coords.put("latitude", 200);
        coords.put("longitude", -200);
        json.put("coords", coords);

        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithoutCoords() throws Exception {
        JSONObject json= getJSONObjectWithMandatoryFields();
        json.remove("coords");

        marshalUnderTest.unmarshal(json);
    }

    private JSONObject getJSONObjectWithMandatoryFields() {
        return buildTestJSONObject(ID, NAME, OWNER, GROUP, TYPE, LATITUDE, LONGITUDE);
    }

    private JSONObject buildTestJSONObject(String id, String name, String owner,
            String group, String type, double latitude, double longitude) {
        JSONObject json= new JSONObject();
        try {
            json.put("id", id);
            json.put("owner", owner);
            json.put("name", name);
            json.put("group", group);
            json.put("type", type);

            JSONObject jsLocation= new JSONObject();
            jsLocation.put("latitude", latitude);
            jsLocation.put("longitude", longitude);
            json.put("coords", jsLocation);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    private JSONObject getJSONObjectAddressAttributes() {
        JSONObject address= new JSONObject();
        try {
            address.put("street", ADDRESS_STREET);
            address.put("city", ADDRESS_CITY);
            address.put("poBox", ADDRESS_POBOX);
            address.put("phoneNumber", ADDRESS_PHONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return address;
    }

    private JSONObject getJSONObjectPovAttributes()    {
        JSONObject pov= new JSONObject();
        try {
            pov.put("latitude", POV_LATITUDE);
            pov.put("longitude", POV_LONGITUDE);
            pov.put("heading", POV_HEADING);
            pov.put("fov", POV_FOV);
            pov.put("viewType", POV_VIEW_TYPE);
            pov.put("pitch", POV_PITCH);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pov;
    }

    private Pet getPetWithMandatoryFields()    {
        Pet pet= new Pet(ID);
        pet.setOwner(OWNER);
        pet.setName(NAME);
        pet.setGroup(GROUP);
        pet.setLocation(LATITUDE, LONGITUDE);
        pet.setLastUpdate();
        return pet;
    }


    private HashMap<String, String> getAddressAttributes() {
        HashMap<String,String> attr= new HashMap<String, String>();
        attr.put("street", ADDRESS_STREET);
        attr.put("city", ADDRESS_CITY);
        attr.put("poBox", ADDRESS_POBOX);
        attr.put("phoneNumber", ADDRESS_PHONE);
        return attr;
    }

}
