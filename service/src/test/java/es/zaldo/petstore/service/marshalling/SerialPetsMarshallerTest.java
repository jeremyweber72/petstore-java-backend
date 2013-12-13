package es.zaldo.petstore.service.marshalling;


/**
 * Tests for {@link SerialPetsMarshaller} class.
 */
public class SerialPetsMarshallerTest extends AbstractPetsMarshallerTest {

    private PetMarshaller petMarshaller = new PetMarshaller(MarshallingDataGenerator.URL);

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected Marshaller getMarshallerClassToTest() {
        return new SerialPetsMarshaller(petMarshaller);
    }

}
