package es.zaldo.petstore.service.marshalling;

import java.util.Iterator;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import es.zaldo.petstore.core.Pet;
import es.zaldo.petstore.core.Pets;

/**
 * Knows how to marshall {@link Pets} into a JSON object.
 */
public class PetsMarshaller implements Marshaller<Pets, JSONObject> {

    private static final String FIELD_PETS = "pets";
    private static final String FIELD_TOTAL_NUMBER_OF_RESULTS = "totalNumberOfResults";
    private static final String FIELD_NUMBER_OF_RESULTS = "numberOfResults";
    private static final String FIELD_CURRENT_PAGE = "currentPage";

    private final PetMarshaller petMarshaller;

    /**
     * Constructor of the class.
     * 
     * @param petMarshaller Class to marshall a single et
     */
    public PetsMarshaller(final PetMarshaller petMarshaller) {
        this.petMarshaller = petMarshaller;
    }

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

            List<Pet> listPets = pets.getPets();
            Iterator<Pet> itr = listPets.iterator();

            while (itr.hasNext()) {
                json.accumulate(FIELD_PETS, petMarshaller.marshall(itr.next()));
            }

            return json;
        } catch (NullPointerException ex) {
            throw new RuntimeException("Unable read mandatory attributes: " + ex.getMessage(),
                    ex.getCause());
        } catch (Exception ex) {
            throw new RuntimeException("Server error: " + ex.getMessage(), ex.getCause());
        }
    }

}
