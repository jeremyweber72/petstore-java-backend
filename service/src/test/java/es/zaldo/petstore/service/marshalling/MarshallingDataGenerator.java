package es.zaldo.petstore.service.marshalling;

import java.util.HashMap;

import es.zaldo.petstore.core.Location;
import es.zaldo.petstore.core.Pet;

/**
 * Utility methods to create data for marshalling tests.
 */
public abstract class MarshallingDataGenerator {

    public static final String ID = "id";
    public static final String SEPARATOR = "/";
    public static final String URL = "http://localhost:8080/service/api/v1/pets/pet";
    public static final String OWNER = "name";
    public static final String NAME = "owner";
    public static final String GROUP = "group";
    public static final double LATITUDE = 40;
    public static final double LONGITUDE = -3;
    public static final String ADDRESS_STREET = "test street";
    public static final String ADDRESS_CITY = "test city";
    public static final String ADDRESS_POBOX = "test pobox";
    public static final String ADDRESS_PHONE = "913390000";
    public static final String ADDITIONAL_FIELD = "additionalField";
    public static final String ADDITIONAL_NODE = "additionalNode";
    public static final int CURRENT_PAGE = 0;
    public static final int NUMBER_OF_RESULTS = 2;
    public static final int TOTAL_NUMBER_OF_RESULTS = 3;
    public static final double MAX_LATITUDE = 44;
    public static final double MIN_LATITUDE = 27;
    public static final double MAX_LONGITUDE = 5;
    public static final double MIN_LONGITUDE = -18.5;

    public static Pet getPetWithMandatoryFields() {
        return new Pet(ID, NAME, new Location(LATITUDE, LONGITUDE), 
                OWNER, GROUP, "free");
    }

    public static HashMap<String, String> getAddressAttributes() {
        HashMap<String, String> attr = new HashMap<String, String>();
        attr.put("street", ADDRESS_STREET);
        attr.put("city", ADDRESS_CITY);
        attr.put("poBox", ADDRESS_POBOX);
        attr.put("phoneNumber", ADDRESS_PHONE);
        return attr;
    }

}
