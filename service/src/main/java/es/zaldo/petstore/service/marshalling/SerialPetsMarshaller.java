package es.zaldo.petstore.service.marshalling;

import java.util.Iterator;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import es.zaldo.petstore.core.Pet;
import es.zaldo.petstore.core.Pets;

/**
 * Marshall {@link Pets} into a JSON object one by one.
 */
public class SerialPetsMarshaller extends AbstractPetsMarshaller {

    private final PetMarshaller petMarshaller;

    /**
     * Constructor of the class.
     * 
     * @param petMarshaller Class to marshall a single et
     */
    public SerialPetsMarshaller(final PetMarshaller petMarshaller) {
        this.petMarshaller = petMarshaller;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JSONObject marshallPets(JSONObject json, List<Pet> pets) throws JSONException,
            MarshallerException {
        Iterator<Pet> itr = pets.iterator();

        while (itr.hasNext()) {
            json.accumulate(FIELD_PETS, petMarshaller.marshall(itr.next()));
        }

        return json;
    }
}
