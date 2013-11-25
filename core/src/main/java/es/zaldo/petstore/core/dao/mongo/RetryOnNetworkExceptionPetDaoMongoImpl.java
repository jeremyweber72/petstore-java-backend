package es.zaldo.petstore.core.dao.mongo;

import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.MongoException.Network;

import es.zaldo.petstore.core.Box;
import es.zaldo.petstore.core.PageRequest;
import es.zaldo.petstore.core.Pet;
import es.zaldo.petstore.core.Pets;
import es.zaldo.petstore.core.dao.NoSuchPetException;
import es.zaldo.petstore.core.dao.PetDao;

/**
 * Dao implementation that will retry the operation if an MongoDB network error
 * happens.
 *
 * <p>This kind of error usually happen when MongoDB is restarted while the
 * pet service is up.</p>
 */
public class RetryOnNetworkExceptionPetDaoMongoImpl implements PetDao {

    /*
     * This class just decorates the standard PetDaoMongoImpl class.
     */

    private static final Logger LOGGER =
            Logger.getLogger(RetryOnNetworkExceptionPetDaoMongoImpl.class);

    private PetDao parentDao;

    /**
     * Number of retries
     */
    private int retries;

    /**
     * Milliseconds to wait until calling the method again.
     */
    private long delay;

    /**
     * Defines an operation to be retried if an error happens.
     *
     * @param <T> Return type
     * @param <U> Exception to be thrown
     */
    private interface Operation<T, U extends Throwable> {

        /**
         * Common implementation for all operations.
         */
        T execute() throws U;

    }

    /**
     * Abstract class that defines the common behavior for all the operations.
     *
     * @param <T> Return type
     * @param <U> Exception to be thrown
     */
    private abstract class MongoDbOperation<T, U extends Exception>
            implements Operation<T, U> {

        /**
         * {@inheritDoc}
         */
        @Override
		public T execute() throws U {
            try {
                return executeOp();
            } catch (RuntimeException e) {
                if (e instanceof Network || e.getCause() instanceof Network) {
                    for (int i = 0; i < retries; i++) {
                        try {

                            try {
                                Thread.sleep(delay);
                            } catch (InterruptedException e1) {

                                // Nothing to do here
                            }

                            LOGGER.warn(e.getCause().toString() +
                                " exception thrown, retry " +
                                    (i + 1) + " out of " + retries + ".", e);

                            return executeOp();
                        } catch (Throwable t) {

                            // Nothing to do here, just retry
                        }
                    }
                }

                // No Network exception or number of retries reached
                throw e;
            }
        }

        /**
         * Method were the decorated operation is executed.
         *
         * @return T
         *
         * @throws U
         */
        protected abstract T executeOp() throws U;

    }

    /**
     * Constructor of the class.
     *
     * @param parentDao Dao to decorate
     * @param retries Number of retries
     * @param delay Delay between retries (in milliseconds)
     */
    public RetryOnNetworkExceptionPetDaoMongoImpl(
            PetDao parentDao, int retries, long delay) {
        this.parentDao = parentDao;
        this.retries = retries;
        this.delay = delay;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pet loadById(final String id) throws NoSuchPetException {
        return new MongoDbOperation<Pet, NoSuchPetException>() {

            @Override
            protected Pet executeOp()
                    throws NoSuchPetException {
                return parentDao.loadById(id);
            }
        }.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pet upsert(final Pet newpet) {
        return new MongoDbOperation<Pet, RuntimeException>() {

            @Override
            protected Pet executeOp() {
                return parentDao.upsert(newpet);
            }
        }.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pets getPetsById(final List<String> ids) {
        return new MongoDbOperation<Pets, RuntimeException>() {

            @Override
            protected Pets executeOp() {
                return parentDao.getPetsById(ids);
            }
        }.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pets searchWithin(final List<String> groups,
        final List<String> types, final Box bounds,
            final PageRequest pagination) {
        return new MongoDbOperation<Pets, RuntimeException>() {

            @Override
            protected Pets executeOp() {
                return parentDao.searchWithin(
                        groups, types, bounds, pagination);
            }
        }.execute();
    }

}
