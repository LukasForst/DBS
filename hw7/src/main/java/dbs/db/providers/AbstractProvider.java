package dbs.db.providers;

import javax.persistence.EntityManager;

abstract class AbstractProvider {
    final EntityManager entityManager;

    AbstractProvider(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
