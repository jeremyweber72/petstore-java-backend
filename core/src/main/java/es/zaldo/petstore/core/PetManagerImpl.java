package es.zaldo.petstore.core;

import java.util.List;

import es.zaldo.petstore.core.dao.NoSuchPetException;
import es.zaldo.petstore.core.dao.PetDao;

/**
 * Implementation of the <code>PetManager</code> interface.
 */
public class PetManagerImpl implements PetManager {

    private PetDao dao;

    /**
     * Constructor of the class.
     *
     * @param dao Dao to use to connect with the underlying repository
     */
    public PetManagerImpl(PetDao dao) {
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pet loadById(String id) throws NoSuchPetException {
        return dao.loadById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pet update(Pet newPet) {
    	newPet.setLastUpdate();
        return dao.upsert(newPet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pets getPetsById(List<String> ids) {
        return dao.getPetsById(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pets searchWithin(List<String> groups, List<String> types,
            Box bounds, PageRequest pagination) {
        return dao.searchWithin(groups, types, bounds, pagination);
    }

}
