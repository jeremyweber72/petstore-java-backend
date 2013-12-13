package es.zaldo.petstore.service.marshalling;


/**
 * Tests for {@link ParallelPetsMarshaller} class.
 */
public class ParallelPetsMarshallerTest extends AbstractPetsMarshallerTest {

    private PetMarshaller petMarshaller = new PetMarshaller(MarshallingDataGenerator.URL);

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected Marshaller getMarshallerClassToTest() {
        return new ParallelPetsMarshaller(petMarshaller, 8);
    }

}
