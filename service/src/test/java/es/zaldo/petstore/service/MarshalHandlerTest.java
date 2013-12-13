package es.zaldo.petstore.service;

import java.util.HashMap;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import es.zaldo.petstore.core.Pet;
import es.zaldo.petstore.core.exceptions.PetsValidationException;

/**
 * Tests for the {@link MarshalHandler} class.
 */
public class MarshalHandlerTest {

    private static final String EMPTY_STRING = "       ";

    private static final String ID = "id";

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

    @Autowired
    private static MarshalHandler marshalUnderTest;

    @BeforeClass
    public static void initializeObjects() {
        marshalUnderTest = new MarshalHandler();
    }

    @Test
    public void testUnmarshal() throws Exception {

        JSONObject json = getJSONObjectWithMandatoryFields();

        Pet pet = marshalUnderTest.unmarshal(json);

        Assert.assertEquals(ID, pet.getId());
        Assert.assertEquals(OWNER, pet.getOwner());
        Assert.assertEquals(NAME, pet.getName());

        Assert.assertEquals(String.valueOf(LATITUDE),
                String.valueOf(pet.getLocation().getLatitude()));
        Assert.assertEquals(String.valueOf(LONGITUDE),
                String.valueOf(pet.getLocation().getLongitude()));
    }

    @Test
    public void testUnmarshalWithoutId() throws Exception {

        JSONObject json = getJSONObjectWithMandatoryFields();
        json.remove("id");

        Pet pet = marshalUnderTest.unmarshal(json);

        Assert.assertEquals(NAME, pet.getName());
    }

    @Test
    public void testUnmarshalWithEmptyId() throws Exception {

        JSONObject json = buildTestJSONObject(EMPTY_STRING, NAME, OWNER, GROUP, TYPE, LATITUDE,
                LONGITUDE);
        Pet pet = marshalUnderTest.unmarshal(json);
        Assert.assertFalse("No ID found!", pet.getId().isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUnmarshalWithAttributes() throws Exception {
        JSONObject json = getJSONObjectWithMandatoryFields();

        json.put("address", getJSONObjectAddressAttributes());
        json.put("pov", getJSONObjectPovAttributes());

        Pet pet = marshalUnderTest.unmarshal(json);

        HashMap<String, Object> attr = pet.getAttributes();
        HashMap<String, String> address = (HashMap<String, String>) attr.get("address");

        Assert.assertEquals(ADDRESS_STREET, address.get("street"));
        Assert.assertEquals(ADDRESS_CITY, address.get("city"));
        Assert.assertEquals(ADDRESS_POBOX, address.get("poBox"));
        Assert.assertEquals(ADDRESS_PHONE, address.get("phoneNumber"));

        HashMap<String, String> pov = (HashMap<String, String>) attr.get("pov");

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
        JSONObject json = getJSONObjectWithMandatoryFields();

        json.put(ADDITIONAL_FIELD, ADDITIONAL_FIELD);

        JSONObject additionalJson = new JSONObject();
        additionalJson.put(ADDITIONAL_FIELD, ADDITIONAL_FIELD);
        json.put(ADDITIONAL_NODE, additionalJson);

        Pet pet = marshalUnderTest.unmarshal(json);

        HashMap<String, Object> attr = pet.getAttributes();
        Assert.assertEquals(ADDITIONAL_FIELD, attr.get(ADDITIONAL_FIELD));

        HashMap<String, String> node = (HashMap<String, String>) attr.get(ADDITIONAL_NODE);
        Assert.assertEquals(ADDITIONAL_FIELD, node.get(ADDITIONAL_FIELD));
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithoutName() throws Exception {
        JSONObject json = getJSONObjectWithMandatoryFields();
        json.remove("name");

        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithoutOwner() throws Exception {
        JSONObject json = getJSONObjectWithMandatoryFields();
        json.remove("owner");

        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithEmptyOwner() throws Exception {
        JSONObject json = buildTestJSONObject(ID, NAME, EMPTY_STRING, GROUP, TYPE, LATITUDE,
                LONGITUDE);
        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithoutGroup() throws Exception {
        JSONObject json = getJSONObjectWithMandatoryFields();
        json.remove("group");

        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithEmptyGroup() throws Exception {
        JSONObject json = buildTestJSONObject(ID, NAME, OWNER, EMPTY_STRING, TYPE, LATITUDE,
                LONGITUDE);
        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithoutType() throws Exception {
        JSONObject json = getJSONObjectWithMandatoryFields();
        json.remove("type");

        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithEmptyType() throws Exception {
        JSONObject json = buildTestJSONObject(ID, NAME, OWNER, GROUP, EMPTY_STRING, LATITUDE,
                LONGITUDE);
        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithoutOneCoord() throws Exception {
        JSONObject json = getJSONObjectWithMandatoryFields();
        JSONObject coords = json.getJSONObject("coords");
        coords.remove("latitude");
        json.put("coords", coords);

        marshalUnderTest.unmarshal(json);
    }

    @Test(expected = PetsValidationException.class)
    public void testUnmarshalExceptionWithoutCoords() throws Exception {
        JSONObject json = getJSONObjectWithMandatoryFields();
        json.remove("coords");

        marshalUnderTest.unmarshal(json);
    }

    private JSONObject getJSONObjectWithMandatoryFields() {
        return buildTestJSONObject(ID, NAME, OWNER, GROUP, TYPE, LATITUDE, LONGITUDE);
    }

    private JSONObject buildTestJSONObject(String id, String name, String owner, String group,
            String type, double latitude, double longitude) {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("owner", owner);
            json.put("name", name);
            json.put("group", group);
            json.put("type", type);

            JSONObject jsLocation = new JSONObject();
            jsLocation.put("latitude", latitude);
            jsLocation.put("longitude", longitude);
            json.put("coords", jsLocation);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private JSONObject getJSONObjectAddressAttributes() {
        JSONObject address = new JSONObject();
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

    private JSONObject getJSONObjectPovAttributes() {
        JSONObject pov = new JSONObject();
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

}
