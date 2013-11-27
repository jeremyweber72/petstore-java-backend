package es.zaldo.petstore.service;

/**
 * Define operations on how to serialize and deserialize objects.
 *
 * @param <S> Source type
 * @param <D> Destination type
 */
public interface Marshaller <S, D> {

	// TODO Implement a JAXB Marshaller

	/**
	 * Transforms a source type into another type.
	 *
	 * @param source Source type
	 *
	 * @return An instance of the destination type.
	 */
	D marshall(S source) throws MarshallerException;

}
