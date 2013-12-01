package es.zaldo.petstore.service.marshalling;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import es.zaldo.petstore.core.Pet;
import es.zaldo.petstore.core.Pets;

/**
 * Marshall {@link Pets} into a JSON object in parallel.
 * 
 * <p>This implementation uses futures to compute Pet marshalls in parallel.</p>
 * 
 * <p>This class is usefull when a lot of Pets have to be marshalled. For little
 * volumes of Pets use {@link SerialPetsMarshaller} class.</p>
 */
public class ParallelPetsMarshaller extends AbstractPetsMarshaller {

    private final int numberOfThreads;
    private final PetMarshaller petMarshaller;

    private class PetMarshallerWorker implements Callable<JSONObject> {

        private final Pet pet;
        private final PetMarshaller petMarshaller;

        /**
         * Constructor of the class.
         * 
         * @param pet Pet to marshall
         */
        public PetMarshallerWorker(final PetMarshaller petMarshaller, final Pet pet) {
            this.pet = pet;
            this.petMarshaller = petMarshaller;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JSONObject call() throws Exception {
            return petMarshaller.marshall(pet);
        }

    }

    /**
     * Constructor of the class.
     * 
     * @param numberOfThreads Number of threads to launch in parallel
     */
    public ParallelPetsMarshaller(final PetMarshaller petMarshaller, final int numberOfThreads) {
        this.petMarshaller = petMarshaller;
        this.numberOfThreads = numberOfThreads;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JSONObject marshallPets(JSONObject json, List<Pet> pets) throws JSONException,
            MarshallerException {
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        List<Future<JSONObject>> jsonObjects = new ArrayList<Future<JSONObject>>(pets.size());
        for (Pet pet : pets) {
            jsonObjects.add(executor.submit(new PetMarshallerWorker(petMarshaller, pet)));
        }

        for (Future<JSONObject> jsonObject : jsonObjects) {
            try {
                json.accumulate(FIELD_PETS, jsonObject.get());
            } catch (InterruptedException e) {
                new MarshallerException(e.getMessage(), e);
            } catch (ExecutionException e) {
                new MarshallerException(e.getMessage(), e);
            }
        }

        return json;
    }
}
