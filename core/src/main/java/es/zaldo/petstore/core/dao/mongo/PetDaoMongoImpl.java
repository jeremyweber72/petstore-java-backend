package es.zaldo.petstore.core.dao.mongo;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import es.zaldo.petstore.core.Box;
import es.zaldo.petstore.core.PageRequest;
import es.zaldo.petstore.core.Pet;
import es.zaldo.petstore.core.Pets;
import es.zaldo.petstore.core.dao.NoSuchPetException;
import es.zaldo.petstore.core.dao.PetDao;

/**
 * Implementation of <code>PetDao</code> for Mongo DB.
 */
public class PetDaoMongoImpl implements PetDao {

    private static final Logger LOGGER =
            Logger.getLogger(PetDaoMongoImpl.class);

    public static final String ID_FIELD_NAME = "_id";
    public static final String TYPE_FIELD_NAME = "type";
    public static final String OWNER_FIELD_NAME = "owner";
    public static final String GROUP_FIELD_NAME = "group";
    public static final String COORDS_FIELD_NAME = "coords";

    private MongoTemplate template;

    /**
     * Constructor of the class.
     *
     * @param repository Repository
     * @param metric Metric that can be applied to a base scale. Allowed values
     * are MILES or KILOMETERS.
     */
    public PetDaoMongoImpl(MongoTemplate template) {
        this.template = template;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pet loadById(String id) throws NoSuchPetException {
        Pet pet = template.findById(id, Pet.class);
        if (pet == null) {
            throw new NoSuchPetException(id);
        }
        return pet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pet upsert(Pet newpet) {
        template.save(newpet);
        return newpet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pets getPetsById(List<String> ids) {
        return executeInQuery(ID_FIELD_NAME, ids);
    }

    /**
     * Executes a query to retrieve documents matching different data.
     *
     * @param field Field name
     * @param data Data
     *
     * @return A list of pets
     */
    private Pets executeInQuery(String field, List<String> data) {

        // Do some data checks
        if (data == null || data.isEmpty()) {
            return new Pets(new ArrayList<Pet>(0), 0, 0, 0);
        }

        // Create criteria
        Criteria criteria = Criteria.where(field).in(data);

        // Execute query
        Query query = Query.query(criteria);
        LOGGER.debug("Executing query: " + query.toString());
        List<Pet> results = template.find(query, Pet.class);

        return new Pets(results, 0, results.size(), results.size());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pets searchWithin(List<String> groups, List<String> types,
            Box bounds, PageRequest pagination) {

        // Transform coordinates to Mongo interface
        double[] lowerLeft = new double[] {
                bounds.getUpperLeft().getLongitude(),
                bounds.getLowerRight().getLatitude()
        };
        double[] upperRight = new double[] {
                bounds.getLowerRight().getLongitude(),
                bounds.getUpperLeft().getLatitude()
        };

        Criteria criteria = Criteria.where(COORDS_FIELD_NAME).within(
                new org.springframework.data.mongodb.core.geo.Box(lowerLeft, upperRight));
        return executeQueryWithPagination(groups, types, criteria, pagination);
    }

    /**
     * Executes a query with pagination.
     *
     * @param group Group parameter
     * @param pagination Pagination info
     * @param criteria Query's criteria
     *
     * @return The results of the query.
     */
    private Pets executeQueryWithPagination(List<String> groups,
            List<String> types, Criteria criteria, PageRequest pagination) {
        if (groups != null  && !groups.isEmpty()) {
            criteria.and(GROUP_FIELD_NAME).in(groups);
        }
        if (types != null && !types.isEmpty()) {
            criteria.and(TYPE_FIELD_NAME).in(types);
        }
        Query query = getQueryWithPagination(criteria, pagination);
        LOGGER.debug("Executing query: " + query.toString());
        List<Pet> results = template.find(query, Pet.class);
        long totalNumberOfResults = template.count(query, Pet.class);

        return new Pets(results, pagination.getPage(), results.size(),
                new Long(totalNumberOfResults).intValue());
    }

    /**
     * Creates a new query object with the given criteria and pagination values.
     *
     * @param pagination
     * @param criteria
     *
     * @return A new query object ready to use
     */
    private Query getQueryWithPagination(
            Criteria criteria, PageRequest pagination) {
        Query query = Query.query(criteria);
        query.with(new org.springframework.data.domain.PageRequest(
                pagination.getPage(), pagination.getSize()));
        return query;
    }

}
