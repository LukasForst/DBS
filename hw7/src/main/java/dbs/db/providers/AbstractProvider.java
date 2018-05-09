package dbs.db.providers;

import javax.persistence.EntityManager;

/**
 * Abstract db provider defines what has every provider.
 * <p>
 * Note that all providers can throw exception while performing query to db.
 */
abstract class AbstractProvider {
    final EntityManager entityManager;

    AbstractProvider(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
