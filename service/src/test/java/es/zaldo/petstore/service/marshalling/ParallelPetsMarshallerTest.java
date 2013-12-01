package es.zaldo.petstore.service.marshalling;

import es.zaldo.petstore.core.utils.PetUtils;

/**
 * Tests for {@link ParallelPetsMarshaller} class.
 */
public class ParallelPetsMarshallerTest extends AbstractPetsMarshallerTest {

    private PetUtils petUtils = new PetUtils(MarshallingDataGenerator.URL,
            MarshallingDataGenerator.MAX_LATITUDE, MarshallingDataGenerator.MIN_LATITUDE,
            MarshallingDataGenerator.MAX_LONGITUDE, MarshallingDataGenerator.MIN_LONGITUDE);

    private PetMarshaller petMarshaller = new PetMarshaller(petUtils);

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected Marshaller getMarshallerClassToTest() {
        return new ParallelPetsMarshaller(petMarshaller, 8);
    }

}
