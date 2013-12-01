package es.zaldo.petstore.service.marshalling;

import java.util.HashMap;

import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import es.zaldo.petstore.core.Pet;
import es.zaldo.petstore.core.utils.PetUtils;

/**
 * Tests for {@link PetMarshaller} class.
 */
public class PetMarshallerTest {

    private PetUtils petUtils = new PetUtils(MarshallingDataGenerator.URL,
            MarshallingDataGenerator.MAX_LATITUDE, MarshallingDataGenerator.MIN_LATITUDE,
            MarshallingDataGenerator.MAX_LONGITUDE, MarshallingDataGenerator.MIN_LONGITUDE);

    /**
     * 
     */
    @Test
    public void testMarshal() throws Exception {
        PetMarshaller sut = new PetMarshaller(petUtils);

        Pet pet = MarshallingDataGenerator.getPetWithMandatoryFields();
        JSONObject json = sut.marshall(pet);

        Assert.assertEquals(MarshallingDataGenerator.ID, json.getString("id"));
        Assert.assertEquals(petUtils.getUrlBase() + MarshallingDataGenerator.SEPARATOR
                + MarshallingDataGenerator.ID, json.getString("url"));
        Assert.assertEquals(MarshallingDataGenerator.OWNER, json.getString("owner"));
        Assert.assertEquals(MarshallingDataGenerator.NAME, json.getString("name"));

        Assert.assertEquals(String.valueOf(MarshallingDataGenerator.LATITUDE),
                String.valueOf(json.getJSONObject("coords").getString("latitude")));
        Assert.assertEquals(String.valueOf(MarshallingDataGenerator.LONGITUDE),
                String.valueOf(json.getJSONObject("coords").getString("longitude")));
    }

    /**
     * 
     */
    @Test
    public void testMarshalWithAttributes() throws Exception {
        PetMarshaller sut = new PetMarshaller(petUtils);

        Pet pet = MarshallingDataGenerator.getPetWithMandatoryFields();
        HashMap<String, Object> mapAttr = new HashMap<String, Object>();
        mapAttr.put("address", MarshallingDataGenerator.getAddressAttributes());
        mapAttr.put(MarshallingDataGenerator.ADDITIONAL_FIELD,
                MarshallingDataGenerator.ADDITIONAL_FIELD);
        pet.setAttributes(mapAttr);

        JSONObject json = sut.marshall(pet);

        Assert.assertEquals(MarshallingDataGenerator.ADDITIONAL_FIELD,
                json.getString(MarshallingDataGenerator.ADDITIONAL_FIELD));
        Assert.assertEquals(MarshallingDataGenerator.ADDRESS_STREET, json.getJSONObject("address")
                .getString("street"));
        Assert.assertEquals(MarshallingDataGenerator.ADDRESS_CITY, json.getJSONObject("address")
                .getString("city"));
        Assert.assertEquals(MarshallingDataGenerator.ADDRESS_POBOX, json.getJSONObject("address")
                .getString("poBox"));
        Assert.assertEquals(MarshallingDataGenerator.ADDRESS_PHONE, json.getJSONObject("address")
                .getString("phoneNumber"));
    }

}
