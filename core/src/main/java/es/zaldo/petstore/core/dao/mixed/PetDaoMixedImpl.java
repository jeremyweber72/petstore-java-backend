package es.zaldo.petstore.core.dao.mixed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;

import es.zaldo.petstore.core.Box;
import es.zaldo.petstore.core.PageRequest;
import es.zaldo.petstore.core.Pet;
import es.zaldo.petstore.core.Pets;
import es.zaldo.petstore.core.dao.NoSuchPetException;
import es.zaldo.petstore.core.dao.PetDao;

/**
 * Implementation of <code>PetDao</code> using MongoDB and Solr as repositories.
 *
 * <p>Searcheable information will be stored in Solr, but the whole pet
 * information will be stored in MongoDB.</p>
 */
public class PetDaoMixedImpl implements PetDao {

    private static final Logger LOGGER =
            Logger.getLogger(PetDaoMixedImpl.class);

    private SolrTemplate solrTemplate;
    private PetDao mongoDao;

    /**
     * Constructor of the class.
     *
     * @param mongoDao DAO class to connect to MongoDB
     * @param repository Repository
     * @param metric Metric that can be applied to a base scale. Allowed values
     * are MILES or KILOMETERS.
     */
    public PetDaoMixedImpl(PetDao mongoDao, SolrTemplate solrTemplate) {
        this.solrTemplate = solrTemplate;
        this.mongoDao = mongoDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pet loadById(String id) throws NoSuchPetException {
        return mongoDao.loadById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pet upsert(Pet newpet) {
        Pet oldpet = null;
        try {
            oldpet = loadById(newpet.getId());
        } catch (NoSuchPetException e) {

            // TODO Throw an exception with more semantics!
            throw new RuntimeException("Could not update MongoDB pet with ID: "
                    + newpet.getId());
        }

        LOGGER.debug("Updating pet in MongoDB: " + newpet);
        Date date = new Date();
        newpet.setLastUpdate(date);
        newpet = mongoDao.upsert(newpet);
        LOGGER.debug("Updated pet in MongoDB: " + newpet);

        PetMixed solrpet = PetMixed.parsePet(newpet);
        try {
            LOGGER.debug("Updating pet in Solr: " + solrpet);
            solrTemplate.saveBean(solrpet);
            solrTemplate.commit();
            LOGGER.debug("Updated pet in Solr: " + solrpet);

            return newpet;
        } catch (Throwable e) {

            // Restore pet in MongoDB
            LOGGER.debug("Updating pet in MongoDB: " + oldpet);
            mongoDao.upsert(oldpet);
            LOGGER.debug("Updated pet in MongoDB: " + oldpet);

            // TODO Throw an exception with more semantics!
            throw new RuntimeException(
                    "Could not update pet: " + e.getMessage(), e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Pets getPetsById(List<String> ids) {
        return mongoDao.getPetsById(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pets searchWithin(List<String> groups, List<String> types,
            Box bounds, PageRequest pagination) {
        Query query = SolrUtils.buildWithinQuery(
                groups, types, bounds, pagination);

        LOGGER.debug("Within - Getting data from Solr." + query);
        Page<PetMixed> pets =
                solrTemplate.queryForPage(query, PetMixed.class);

        LOGGER.debug("Within - Getting data from Mongo.");
        Pets petsFromMongo = getPetsById(getIds(pets));
        return new Pets(petsFromMongo.getPets(), pets.getNumber(),
            pets.getNumberOfElements(),
                Long.valueOf(pets.getTotalElements()).intValue());
    }

    /**
     * Retrieves the IDs of the pets of a list.
     *
     * @param pets List of pets
     *
     * @return The list of IDs
     */
    private List<String> getIds(Page<PetMixed> pets) {
        ArrayList<String> result = new ArrayList<String>(pets.getSize());
        for (PetMixed pet : pets) {
            result.add(pet.getId());
        }
        return result;
    }

}
