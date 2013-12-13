package es.zaldo.petstore.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import es.zaldo.petstore.core.Pet;
import es.zaldo.petstore.core.exceptions.PetsValidationException;

/**
 * Class used to marshal and unmarshal the Pet Service entities
 *
 * <p>This class will be used to marshal and unmarshal the Pet and Pets objects.</p>
 */
public class MarshalHandler {

    private static final String MSG_WRONG_JSON_ONJECT = "Wrong JSON object: ";
    private final String[] mainFields = { "id", "name", "owner", "coords", "group", "type" };

    /**
     * The method receive a JSONObject from pet service and return a pet object with the information obtained from Json
     *
     * @param jsonObject Json object to process
     * @return Pet A Pet object
     */
    public Pet unmarshal(JSONObject jsonObject) throws PetsValidationException {
        Pet pet = null;
        JSONObject coords = null;

        try {
            // Setting the petId
            if (jsonObject.isNull("id") || jsonObject.getString("id").trim().isEmpty()) {
                pet = new Pet();
            } else {
                pet = new Pet(jsonObject.getString("id"));
            }

            pet.setOwner(getMandatoryValueFromJson(jsonObject, "owner"));
            pet.setName(getMandatoryValueFromJson(jsonObject, "name"));

            pet.setType(getMandatoryValueFromJson(jsonObject, "type"));
            pet.setGroup(getMandatoryValueFromJson(jsonObject, "group"));

            coords = jsonObject.getJSONObject("coords");
            if (coords.isNull("latitude") || coords.isNull("longitude")) {
                throw new PetsValidationException(MSG_WRONG_JSON_ONJECT
                        + "Coords element with latitude and longitude is mandatory.");
            }

            // Validate the latitude and longitude
            double latitude = coords.getDouble("latitude");
            double longitude = coords.getDouble("longitude");

            pet.setLocation(latitude, longitude);

            // At this petnt the request is valid
            // Now its necessary to map the parameters to the Pet object

            pet.setAttributes(getAttributes(jsonObject, mainFields));

        } catch (NullPointerException ex) {
            throw new RuntimeException("Unable read mandatory attributes: " + ex.getMessage(),
                    ex.getCause());
        } catch (JSONException ex) {
            throw new PetsValidationException(MSG_WRONG_JSON_ONJECT
                    + "Name, Owner, Group, Type and Coords attributes are mandatory.");
        }

        return pet;
    }

    /**
     * The method receive a JSONObject from pet service and return a HashMap with the extra fields found in the Json document
     *
     * @param jsonObject Json object to process
     * @param skipKeys Json attributes to skip
     * @return HashMap A hashmap with the attributes found
     */
    @SuppressWarnings("unchecked")
    private static HashMap<String, Object> getAttributes(JSONObject jsonObject, String[] skipKeys) {
        HashMap<String, Object> attr = new HashMap<String, Object>();
        try {

            // Reading the Json object elements
            for (Iterator<String> itr = jsonObject.keys(); itr.hasNext();) {
                String key = itr.next();

                // if the node is one of the mandatory fields, skip
                if (!Arrays.asList(skipKeys).contains(key)) {
                    Object element = jsonObject.get(key);

                    // if the element is a JSONObject, we have to add the
                    // attribute fields to the attr var
                    if (JSONObject.class.isAssignableFrom(element.getClass())) {
                        HashMap<String, Object> node = new HashMap<String, Object>();
                        Iterator<String> childKeys = ((JSONObject) element).keys();

                        // Reading the child attributes
                        while (childKeys.hasNext()) {
                            String childKey = childKeys.next();
                            Object child = ((JSONObject) element).get(childKey);

                            // If the attribute of the child is a string, we add
                            // it to the attr var
                            if (String.class.isAssignableFrom(child.getClass())
                                    || Double.class.isAssignableFrom(child.getClass())
                                    || Integer.class.isAssignableFrom(child.getClass()))
                                node.put(childKey, child);
                        }
                        attr.put(key, node);
                    } else if (String.class.isAssignableFrom(element.getClass())) {
                        attr.put(key, jsonObject.get(key));
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException("Server error: " + e.getMessage(), e.getCause());
        }
        return attr;
    }

    /**
     * Retrieves a mandatory value from a JSON object's attribute.
     *
     * @param json JSON
     *
     * @return The value of the JSON attribute.
     *
     * @throws JSONException If the attribute is not found
     * @throws PetsValidationException If the value is empty
     */
    private String getMandatoryValueFromJson(JSONObject json, String attribute)
            throws JSONException, PetsValidationException {
        String result = json.getString(attribute).trim();
        if (result.isEmpty()) {
            throw new PetsValidationException(MSG_WRONG_JSON_ONJECT
                    + "Name, Owner, Group, Type and Coords attributes are mandatory.");
        }
        return result;
    }

}
