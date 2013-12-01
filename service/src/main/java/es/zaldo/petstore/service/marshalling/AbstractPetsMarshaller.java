package es.zaldo.petstore.service.marshalling;

import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import es.zaldo.petstore.core.Pet;
import es.zaldo.petstore.core.Pets;

/**
 * Contains all the common code to marshall {@link Pets}.
 */
public abstract class AbstractPetsMarshaller implements Marshaller<Pets, JSONObject> {

    protected static final String FIELD_PETS = "pets";

    private static final String FIELD_TOTAL_NUMBER_OF_RESULTS = "totalNumberOfResults";
    private static final String FIELD_NUMBER_OF_RESULTS = "numberOfResults";
    private static final String FIELD_CURRENT_PAGE = "currentPage";

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject marshall(Pets pets) throws MarshallerException {
        JSONObject json = new JSONObject();
        try {
            json.put(FIELD_CURRENT_PAGE, pets.getCurrentPage());
            json.put(FIELD_NUMBER_OF_RESULTS, pets.getNumberOfResults());
            json.put(FIELD_TOTAL_NUMBER_OF_RESULTS, pets.getTotalNumberOfResults());

            return marshallPets(json, pets.getPets());
        } catch (NullPointerException ex) {
            throw new MarshallerException("Unable read mandatory attributes: " + ex.getMessage(),
                    ex.getCause());
        } catch (Exception ex) {
            throw new MarshallerException("Server error: " + ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Marshall a list of pets.
     * 
     * @param json JSON object with common data
     * @param pets Pets to marshall
     * 
     * @return A JSON object containing the marshalled list of pets.
     * 
     * @throws MarshallerException If any error happens while marshalling
     */
    protected abstract JSONObject marshallPets(JSONObject json, List<Pet> pets)
            throws JSONException, MarshallerException;

}
