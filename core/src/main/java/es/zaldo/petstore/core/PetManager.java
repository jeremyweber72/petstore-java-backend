package es.zaldo.petstore.core;

import java.util.List;

import es.zaldo.petstore.core.dao.NoSuchPetException;

/**
 * Defines all operation that can be done with a pet.
 */
public interface PetManager {

    /**
     * Loads a pet with a given ID.
     *
     * @param id The ID of the pet
     *
     * @return The pet with the given ID.
     *
     * @throws NoSuchPetException If no pet is found with the given ID
     */
    Pet loadById(String id) throws NoSuchPetException;

    /**
     * Creates or updates a certain pet.
     *
     * @param newpet New pet
     *
     * @return The modified pet.
     */
    Pet update(Pet newpet);

    /**
     * Gets a list of pets given their IDs.
     *
     * @param ids List of IDs
     *
     * @return A list of pets with the given IDs. An empty list will be
     * returned if no pet match the given criteria.
     */
    Pets getPetsById(List<String> ids);

    /**
     * Search pets inside an area.
     *
     * @param groups pet groups to use as filters
     * @param types pet types to use as filters
     * @param bounds Area boundary
     * @param pagination Pagination information
     *
     * @return All the pets within the given area. An empty list will be
     * returned if no pet match the given criteria.
     */
    Pets searchWithin(List<String> groups, List<String> types, Box bounds,
            PageRequest pagination);

}
